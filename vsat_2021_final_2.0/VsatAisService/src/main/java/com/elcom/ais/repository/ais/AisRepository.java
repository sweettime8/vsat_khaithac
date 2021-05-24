package com.elcom.ais.repository.ais;

import com.elcom.ais.config.ApplicationConfig;
import com.elcom.ais.constant.Constant;
import com.elcom.ais.model.dto.AisDTO;
import com.elcom.ais.model.dto.AisInfoDetailDTO;
import com.elcom.ais.model.dto.MmsiIpDTO;
import com.elcom.ais.model.dto.MmsiPhoneDTO;
import com.elcom.ais.model.dto.VesselDTO;
import com.elcom.ais.model.dto.request.AddObjectToGroup;
import com.elcom.ais.model.dto.request.AdvanceSearch;
import com.elcom.ais.model.vessel.ObjectGroup;
import com.elcom.gateway.message.MessageContent;
import com.elcom.ais.repository.BaseRepository;
import com.elcom.ais.utils.StringUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import com.elcom.ais.model.dto.request.media.SearchListAisRequest;
import com.elcom.ais.model.vessel.VesselGroup;
import com.elcom.ais.utils.DateUtil;
import com.vladmihalcea.hibernate.type.array.IntArrayType;
import java.math.BigInteger;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Transaction;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Repository
public class AisRepository extends BaseRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(AisRepository.class);

    @Autowired
    @Qualifier("clickHouseSession")
    SessionFactory clickHouseSessionFactory;

    @Autowired
    public AisRepository(@Qualifier("vsatEntityManagerFactory") EntityManagerFactory factory, @Qualifier("vsatChDataSource") DataSource dataSource) {
        super(factory, dataSource);
    }

    // Lấy danh sách TẤT CẢ vị trí của tất cả các đối tượng
    public MessageContent searchAisList(SearchListAisRequest input) {
        Session session = this.clickHouseSessionFactory.openSession();
        try {
            String timeKeyStart = "";
            String timeKeyEnd = "";
            if (input.isSearchForList()) {
                String startTime = input.getStartTime().trim();
                String endTime = input.getEndTime().trim();
                timeKeyStart = startTime.substring(0, startTime.length() - 2)
                        .replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
                timeKeyEnd = endTime.substring(0, endTime.length() - 2)
                        .replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
            }

            String condition = "";
            String orderBy = "ingestTime";
            if (!StringUtil.isNullOrEmpty(input.getSourceIps())) {
                condition += " AND sourceIp IN (:sourceIps) ";
            }
            if (!StringUtil.isNullOrEmpty(input.getDestIps())) {
                condition += " AND destIp IN (:destIps) ";
            }
            if (!StringUtil.isNullOrEmpty(input.getMmsi())) {
                condition += " AND objId = :mmsi ";
                orderBy = "eventTime"; // Nếu tìm theo 1 mssi cụ thể, thì sort theo EVENT_TIME (thời gian thực của bản tin AIS)
            }
            if (input.getDataSource() != null && !input.getDataSource().equals(0)) {
                condition += " AND dataSource = :dataSource ";
            }

            String timeCondition = " AND partName IN (:partitionName) AND (timeKey BETWEEN :startTime AND :endTime) ";

            String sql = " SELECT objId, draugth, destination, name, eta, sourceId, rot, sog, cog "
                    + " , longitude, latitude, eventTime, toString(ingestTime) AS ingestTime, mmsiMaster, sourceIp, destIp, direction, isUfo, typeId "
                    //+ " FROM vsat_ais WHERE isUfo = " + Constant.AIS_MSG_CLEAR_TYPE;
                    + " FROM vsat_ais WHERE 1 = 1 ";
            if (input.isSearchForList()) {
                sql += timeCondition;
            }
            sql += condition + " ORDER BY " + orderBy + " DESC LIMIT :limit ";

            NativeQuery query = session.createSQLQuery(sql);
            if (input.isSearchForList()) {
                query.setParameterList("partitionName", DateUtil.getPartitionNameByTwoDate(input.getStartTime(), input.getEndTime(), Constant.PARTITION_TYPE_DAY))
                        .setParameter("startTime", timeKeyStart)
                        .setParameter("endTime", timeKeyEnd);
            }

            query.setParameter("limit", input.getLimit());

            if (!StringUtil.isNullOrEmpty(input.getSourceIps())) {
                String[] sourceIpLst = input.getSourceIps().trim().split(",");
                query.setParameterList("sourceIps", sourceIpLst);
            }
            if (!StringUtil.isNullOrEmpty(input.getDestIps())) {
                String[] destIpLst = input.getDestIps().trim().split(",");
                query.setParameterList("destIps", destIpLst);
            }

            if (!StringUtil.isNullOrEmpty(input.getMmsi())) {
                query.setParameter("mmsi", input.getMmsi().trim());
            }
            if (input.getDataSource() != null && !input.getDataSource().equals(0)) {
                query.setParameter("dataSource", input.getDataSource());
            }

            query.addScalar("objId", StringType.INSTANCE)
                    .addScalar("draugth", FloatType.INSTANCE)
                    .addScalar("destination", StringType.INSTANCE)
                    .addScalar("name", StringType.INSTANCE)
                    .addScalar("eta", StringType.INSTANCE)
                    .addScalar("sourceId", LongType.INSTANCE)
                    .addScalar("rot", FloatType.INSTANCE)
                    .addScalar("sog", FloatType.INSTANCE)
                    .addScalar("cog", FloatType.INSTANCE)
                    .addScalar("longitude", BigDecimalType.INSTANCE)
                    .addScalar("latitude", BigDecimalType.INSTANCE)
                    .addScalar("eventTime", TimestampType.INSTANCE)
                    .addScalar("ingestTime", StringType.INSTANCE)
                    .addScalar("mmsiMaster", StringType.INSTANCE)
                    .addScalar("sourceIp", StringType.INSTANCE)
                    .addScalar("destIp", StringType.INSTANCE)
                    .addScalar("direction", IntegerType.INSTANCE)
                    .addScalar("isUfo", IntegerType.INSTANCE)
                    .addScalar("typeId", LongType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(AisDTO.class));
            List<AisDTO> lstAisDto = query.getResultList();
            return new MessageContent(lstAisDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            if (session != null && session.isOpen()) {
                session.disconnect();
                session.close();
            }
        }
        return null;
    }

    // Lấy danh sách vị trí HIỆN TẠI (mmsi unique) của tất cả các đối tượng
    public MessageContent searchAisListGeneral(SearchListAisRequest input) {
        Session session = this.clickHouseSessionFactory.openSession();
        try {
            String startTime = input.getStartTime().trim();
            String endTime = input.getEndTime().trim();
            String timeKeyStart = startTime.substring(0, startTime.length() - 2)
                    .replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
            String timeKeyEnd = endTime.substring(0, endTime.length() - 2)
                    .replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");

            String condition = "";
            String orderBy = "ingestTime";
            if (!StringUtil.isNullOrEmpty(input.getSourceIps())) {
                condition += " AND sourceIp IN (:sourceIps) ";
            }
            if (!StringUtil.isNullOrEmpty(input.getDestIps())) {
                condition += " AND destIp IN (:destIps) ";
            }
            if (!StringUtil.isNullOrEmpty(input.getMmsi())) {
                condition += " AND objId = :mmsi ";
                orderBy = "eventTime"; // Nếu tìm theo 1 mssi cụ thể, thì sort theo EVENT_TIME (thời gian thực của bản tin AIS)
            }
            if (input.getDataSource() != null && !input.getDataSource().equals(0)) {
                condition += " AND dataSource = :dataSource ";
            }
            if (input.getTypeId() != null && !input.getTypeId().equals(0)) {
                condition += " AND typeId = :typeId ";
            }
            if (input.getCountryId() != null && !input.getCountryId().equals(0)) {
                condition += " AND country = :countryId ";
            }
            if (input.getIsUfo() != null) {
                condition += " AND isUfo = :isUfo ";
            }
            if (input.getGroupIds() != null && !input.getGroupIds().isEmpty()) //condition += " AND hasAll(groupIds, :groupIds) ";
            {
                condition += " AND arrayExists(x -> x IN :groupIds, groupIds) ";
            }
            if (input.getAreaIds() != null && !input.getAreaIds().isEmpty()) //condition += " AND hasAll(areaIds, :areaIds) ";
            {
                condition += " AND arrayExists(x -> x IN :areaIds, areaIds) ";
            }

            String sql = " SELECT objId, draugth, destination, name, eta, sourceId, rot, sog, cog "
                    + " , longitude, latitude, eventTime, toString(ingestTime) AS ingestTime, mmsiMaster, sourceIp, destIp, direction, isUfo, typeId "
                    //+ " FROM vsat_ais_current final WHERE isUfo = " + Constant.AIS_MSG_CLEAR_TYPE + " AND (timeKey BETWEEN :startTime AND :endTime) ";
                    + " FROM vsat_ais_current final WHERE timeKey BETWEEN :startTime AND :endTime ";
            sql += condition + " ORDER BY " + orderBy + " DESC ";

            NativeQuery query = session.createSQLQuery(sql)
                    .setParameter("startTime", timeKeyStart)
                    .setParameter("endTime", timeKeyEnd);

            if (!StringUtil.isNullOrEmpty(input.getSourceIps())) {
                String[] sourceIpLst = input.getSourceIps().trim().split(",");
                query.setParameterList("sourceIps", sourceIpLst);
            }
            if (!StringUtil.isNullOrEmpty(input.getDestIps())) {
                String[] destIpLst = input.getDestIps().trim().split(",");
                query.setParameterList("destIps", destIpLst);
            }

            if (!StringUtil.isNullOrEmpty(input.getMmsi())) {
                query.setParameter("mmsi", input.getMmsi().trim());
            }
            if (input.getDataSource() != null && !input.getDataSource().equals(0)) {
                query.setParameter("dataSource", input.getDataSource());
            }
            if (input.getTypeId() != null && !input.getTypeId().equals(0)) {
                query.setParameter("typeId", input.getTypeId());
            }
            if (input.getCountryId() != null && !input.getCountryId().equals(0)) {
                query.setParameter("countryId", input.getCountryId());
            }
            if (input.getIsUfo() != null) {
                query.setParameter("isUfo", input.getIsUfo());
            }
//            if( !StringUtil.isNullOrEmpty(input.getVesselGroupIds()) ) {
//                List<Integer> vesselGroupIds = Arrays.stream(input.getVesselGroupIds().trim()
//                                                .split(",")).map(x-> Integer.parseInt(x)).collect(Collectors.toList());
//                query.setParameter("vesselGroupIds", new TypedParameterValue(IntArrayType.INSTANCE, vesselGroupIds));
//            }
            if (input.getGroupIds() != null && !input.getGroupIds().isEmpty()) {
                query.setParameter("groupIds", new TypedParameterValue(IntArrayType.INSTANCE, input.getGroupIds()));
            }
            if (input.getAreaIds() != null && !input.getAreaIds().isEmpty()) {
                query.setParameter("areaIds", new TypedParameterValue(IntArrayType.INSTANCE, input.getAreaIds()));
            }

            query.addScalar("objId", StringType.INSTANCE)
                    .addScalar("draugth", FloatType.INSTANCE)
                    .addScalar("destination", StringType.INSTANCE)
                    .addScalar("name", StringType.INSTANCE)
                    .addScalar("eta", StringType.INSTANCE)
                    .addScalar("sourceId", LongType.INSTANCE)
                    .addScalar("rot", FloatType.INSTANCE)
                    .addScalar("sog", FloatType.INSTANCE)
                    .addScalar("cog", FloatType.INSTANCE)
                    .addScalar("longitude", BigDecimalType.INSTANCE)
                    .addScalar("latitude", BigDecimalType.INSTANCE)
                    .addScalar("eventTime", TimestampType.INSTANCE)
                    .addScalar("ingestTime", StringType.INSTANCE)
                    .addScalar("mmsiMaster", StringType.INSTANCE)
                    .addScalar("sourceIp", StringType.INSTANCE)
                    .addScalar("destIp", StringType.INSTANCE)
                    .addScalar("direction", IntegerType.INSTANCE)
                    .addScalar("isUfo", IntegerType.INSTANCE)
                    .addScalar("typeId", LongType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(AisDTO.class));
            return new MessageContent(query.getResultList());
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            if (session != null && session.isOpen()) {
                session.disconnect();
                session.close();
            }
        }
        return null;
    }

    public Page<ObjectGroup> searchObjectGroupList(String name, Integer isActive, Pageable pageable, String sortBy) {
        Session session = openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<ObjectGroup> criteria = builder.createQuery(ObjectGroup.class);
            Root<ObjectGroup> booksRoot = criteria.from(ObjectGroup.class);

            List<Predicate> predicates = new ArrayList<>();
            if( !StringUtil.isNullOrEmpty(name) )
                predicates.add(builder.like(builder.lower(booksRoot.get("name")), "%" + name.toLowerCase().trim() + "%"));
            if( isActive!=null )
                predicates.add(builder.equal(booksRoot.get("isActive"), isActive));
            if( !predicates.isEmpty() )
                criteria.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));

            if( StringUtil.isNullOrEmpty(sortBy) )
                sortBy = "createdTime";
            criteria.orderBy(builder.desc(booksRoot.get(sortBy)));

            List<ObjectGroup> lst = session.createQuery(criteria)
                                        .setFirstResult((int) pageable.getOffset())
                                        .setMaxResults(pageable.getPageSize())
                                        .getResultList();

            CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
            Root<ObjectGroup> booksRootCount = countQuery.from(ObjectGroup.class);
            countQuery.select(builder.count(booksRootCount));
            if( !predicates.isEmpty() )
                countQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
            Long count = session.createQuery(countQuery).getSingleResult();

            return new PageImpl<>(lst, pageable, count);
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            closeSession(session);
        }
        return null;
    }
    
    public VesselGroup findVesselGroupById(Long id) {
        Session session = openSession();
        try {
            return session.find(VesselGroup.class, id);
        } catch (Exception ex) {
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            closeSession(session);
        }
        return null;
    }

    public boolean removeObjectGroup(Long id) {
        Session session = openSession();
        try {
            Transaction tx = session.beginTransaction();
            String sql = " DELETE FROM object_group where id = :id AND id not in "
                    + " ( SELECT object_group_id FROM object_group_mapping where object_group_id = :id ) ";
            int result = session.createNativeQuery(sql).setParameter("id", id).executeUpdate();
            tx.commit();
            return result >= 1;
        } catch (EntityNotFoundException ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return false;
    }

    //advance Search
    public Page<VesselDTO> searchVesselListAdvanced(String mmsi, String vesselName, Integer countryId, Integer vesselTypeId,
            String ip, String phone, Pageable pageable, AdvanceSearch advanceSearch) {
        Session session = openSession();
        try {
            String condition = "";
            if (!StringUtil.isNullOrEmpty(mmsi) && !"0".equals(mmsi)) {
                condition += " AND ai.mmsi = :mmsi ";
            }
            if (countryId != null && countryId > 0) {
                condition += " AND ai.country_id = :countryId ";
            }
            if (vesselTypeId != null && vesselTypeId > 0) {
                condition += " AND ai.type_id = :vesselTypeId ";
            }
            if (!StringUtil.isNullOrEmpty(vesselName)) {
                condition += " AND ai.name LIKE :vesselName ";
            }
            if (!StringUtil.isNullOrEmpty(ip) && StringUtil.validIpV4(ip)) {
                condition += " AND ai.mmsi in (SELECT mmsi FROM vsat02.mmsi_ip WHERE ip_address LIKE :ip) ";
            }
            if (!StringUtil.isNullOrEmpty(advanceSearch.getAdvancePhone())) {
                condition += " AND ai.mmsi in (SELECT mmsi FROM vsat02.mmsi_phone WHERE phone_number LIKE :phone) ";
            }
            //advance Search
            if (!StringUtil.isNullOrEmpty(advanceSearch.getAdvancecallSign())) {
                condition += " AND ai.callsign LIKE :callsign ";
            }
            if (advanceSearch.getAdvanceMmsiMax() != null && advanceSearch.getAdvanceMmsiMin() != null) {
                condition += " AND ai.mmsi >= :mmsiMin And ai.mmsi <= :mmsiMax";
            }
            if (advanceSearch.getAdvanceIMOMax() != null && advanceSearch.getAdvanceIMOMin() != null) {
                condition += " AND ai.imo >= :imoMin And ai.imo <= :imoMax";
            }
            if (advanceSearch.getAdvanceHeightMax() != null && advanceSearch.getAdvanceHeightMin() != null) {
                condition += " AND (ai.dim_c + ai.dim_d) >= :heightMin And (ai.dim_c + ai.dim_d) <= :heightMax";
            }
            if (advanceSearch.getAdvanceWidthMin() != null && advanceSearch.getAdvanceWidthMax() != null) {
                condition += " AND (ai.dim_a + ai.dim_b) >= :widthMin And (ai.dim_a + ai.dim_b) <= :widthMax";
            }

            String sqlBody = " FROM vsat02.ais_info ai "
                    + " LEFT JOIN vsat02.countries c ON c.id = ai.country_id "
                    + " LEFT JOIN vsat02.vessel_types vt ON vt.type_id = ai.type_id "
                    + " WHERE 1 = 1 " + condition;
            String sqlFindRows = " SELECT ai.mmsi, ai.name AS vesselName, c.name AS countryName, vt.type_name AS vesselTypeName, ai.callsign AS callSign, ai.imo, ai.dim_c AS width "
                    + sqlBody
                    + " ORDER BY ai.mmsi ";
            String sqlCountRows = " SELECT count(ai.mmsi) " + sqlBody;

            NativeQuery query = session.createNativeQuery(sqlFindRows);
            if (!StringUtil.isNullOrEmpty(mmsi) && !"0".equals(mmsi)) {
                query.setParameter("mmsi", new Long(mmsi));
            }
            if (countryId != null && countryId > 0) {
                query.setParameter("countryId", countryId);
            }
            if (vesselTypeId != null && vesselTypeId > 0) {
                query.setParameter("vesselTypeId", vesselTypeId);
            }
            if (!StringUtil.isNullOrEmpty(vesselName)) {
                query.setParameter("vesselName", "%" + vesselName.trim() + "%");
            }
            if (!StringUtil.isNullOrEmpty(ip) && StringUtil.validIpV4(ip)) {
                query.setParameter("ip", "%" + ip.trim() + "%");
            }
            if (!StringUtil.isNullOrEmpty(advanceSearch.getAdvancePhone())) {
                query.setParameter("phone", "%" + advanceSearch.getAdvancePhone().trim() + "%");
            }
            if (!StringUtil.isNullOrEmpty(advanceSearch.getAdvancecallSign()) && !"0".equals(advanceSearch.getAdvancecallSign())) {
                query.setParameter("callsign", advanceSearch.getAdvancecallSign());
            }
            if (advanceSearch.getAdvanceMmsiMax() != null && advanceSearch.getAdvanceMmsiMin() != null) {
                query.setParameter("mmsiMin", advanceSearch.getAdvanceMmsiMin());
                query.setParameter("mmsiMax", advanceSearch.getAdvanceMmsiMax());
            }
            if (advanceSearch.getAdvanceIMOMax() != null && advanceSearch.getAdvanceIMOMin() != null) {
                query.setParameter("imoMax", advanceSearch.getAdvanceIMOMax());
                query.setParameter("imoMin", advanceSearch.getAdvanceIMOMin());
            }
            if (advanceSearch.getAdvanceHeightMax() != null && advanceSearch.getAdvanceHeightMin() != null) {
                query.setParameter("heightMin", advanceSearch.getAdvanceHeightMin());
                query.setParameter("heightMax", advanceSearch.getAdvanceHeightMax());
            }
            if (advanceSearch.getAdvanceWidthMin() != null && advanceSearch.getAdvanceWidthMax() != null) {
                query.setParameter("widthMin", advanceSearch.getAdvanceWidthMin());
                query.setParameter("widthMax", advanceSearch.getAdvanceWidthMax());
            }

            query.addScalar("mmsi", LongType.INSTANCE)
                    .addScalar("vesselName", StringType.INSTANCE)
                    .addScalar("countryName", StringType.INSTANCE)
                    .addScalar("vesselTypeName", StringType.INSTANCE)
                    .addScalar("callSign", StringType.INSTANCE)
                    .addScalar("imo", LongType.INSTANCE)
                    .addScalar("width", BigDecimalType.INSTANCE)
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .setResultTransformer(Transformers.aliasToBean(VesselDTO.class));
            List<VesselDTO> result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                query = session.createNativeQuery(sqlCountRows);
                if (!StringUtil.isNullOrEmpty(mmsi) && !"0".equals(mmsi)) {
                    query.setParameter("mmsi", new Long(mmsi));
                }
                if (countryId != null && countryId > 0) {
                    query.setParameter("countryId", countryId);
                }
                if (vesselTypeId != null && vesselTypeId > 0) {
                    query.setParameter("vesselTypeId", vesselTypeId);
                }
                if (!StringUtil.isNullOrEmpty(vesselName)) {
                    query.setParameter("vesselName", vesselName);
                }
                if (!StringUtil.isNullOrEmpty(ip) && StringUtil.validIpV4(ip)) {
                    query.setParameter("ip", "%" + ip + "%");
                }
                if (!StringUtil.isNullOrEmpty(advanceSearch.getAdvancePhone())) {
                    query.setParameter("phone", "%" + advanceSearch.getAdvancePhone() + "%");
                }
                if (!StringUtil.isNullOrEmpty(advanceSearch.getAdvancecallSign()) && !"0".equals(advanceSearch.getAdvancecallSign())) {
                    query.setParameter("callsign", advanceSearch.getAdvancecallSign());
                }
                if (advanceSearch.getAdvanceMmsiMax() != null && advanceSearch.getAdvanceMmsiMin() != null) {
                    query.setParameter("mmsiMin", advanceSearch.getAdvanceMmsiMin());
                    query.setParameter("mmsiMax", advanceSearch.getAdvanceMmsiMax());
                }
                if (advanceSearch.getAdvanceIMOMax() != null && advanceSearch.getAdvanceIMOMin() != null) {
                    query.setParameter("imoMax", advanceSearch.getAdvanceIMOMax());
                    query.setParameter("imoMin", advanceSearch.getAdvanceIMOMin());
                }
                if (advanceSearch.getAdvanceHeightMax() != null && advanceSearch.getAdvanceHeightMin() != null) {
                    query.setParameter("heightMin", advanceSearch.getAdvanceHeightMin());
                    query.setParameter("heightMax", advanceSearch.getAdvanceHeightMax());
                }
                if (advanceSearch.getAdvanceWidthMin() != null && advanceSearch.getAdvanceWidthMax() != null) {
                    query.setParameter("widthMin", advanceSearch.getAdvanceWidthMin());
                    query.setParameter("widthMax", advanceSearch.getAdvanceWidthMax());
                }
                BigInteger count = (BigInteger) query.getSingleResult();
                return new PageImpl<>(result, pageable, count != null ? count.longValue() : 0L);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            closeSession(session);
        }
        return null;
    }

    public Page<VesselDTO> searchVesselList(String mmsi, String vesselName, Integer countryId, Integer vesselTypeId,
            String ip, String phone, Pageable pageable) {
        Session session = openSession();
        try {
            String condition = "";
            if (!StringUtil.isNullOrEmpty(mmsi) && !"0".equals(mmsi)) {
                condition += " AND ai.mmsi = :mmsi ";
            }
            if (countryId != null && countryId > 0) {
                condition += " AND ai.country_id = :countryId ";
            }
            if (vesselTypeId != null && vesselTypeId > 0) {
                condition += " AND ai.type_id = :vesselTypeId ";
            }
            if (!StringUtil.isNullOrEmpty(vesselName)) {
                condition += " AND ai.name LIKE :vesselName ";
            }
            if (!StringUtil.isNullOrEmpty(ip) && StringUtil.validIpV4(ip)) {
                condition += " AND ai.mmsi in (SELECT mmsi FROM vsat02.mmsi_ip WHERE ip_address LIKE :ip) ";
            }
            if (!StringUtil.isNullOrEmpty(phone)) {
                condition += " AND ai.mmsi in (SELECT mmsi FROM vsat02.mmsi_phone WHERE phone_number LIKE :phone) ";
            }

            String sqlBody = " FROM vsat02.ais_info ai "
                    + " LEFT JOIN vsat02.countries c ON c.id = ai.country_id "
                    + " LEFT JOIN vsat02.vessel_types vt ON vt.type_id = ai.type_id "
                    + " WHERE 1 = 1 " + condition;
            String sqlFindRows = " SELECT ai.mmsi, ai.name AS vesselName, c.name AS countryName, vt.type_name AS vesselTypeName, ai.callsign AS callSign, ai.imo, ai.dim_c AS width "
                    + sqlBody
                    + " ORDER BY ai.mmsi ";
            String sqlCountRows = " SELECT count(ai.mmsi) " + sqlBody;

            NativeQuery query = session.createNativeQuery(sqlFindRows);
            if (!StringUtil.isNullOrEmpty(mmsi) && !"0".equals(mmsi)) {
                query.setParameter("mmsi", new Long(mmsi));
            }
            if (countryId != null && countryId > 0) {
                query.setParameter("countryId", countryId);
            }
            if (vesselTypeId != null && vesselTypeId > 0) {
                query.setParameter("vesselTypeId", vesselTypeId);
            }
            if (!StringUtil.isNullOrEmpty(vesselName)) {
                query.setParameter("vesselName", "%" + vesselName.trim() + "%");
            }
            if (!StringUtil.isNullOrEmpty(ip) && StringUtil.validIpV4(ip)) {
                query.setParameter("ip", "%" + ip.trim() + "%");
            }
            if (!StringUtil.isNullOrEmpty(phone)) {
                query.setParameter("phone", "%" + phone.trim() + "%");
            }

            query.addScalar("mmsi", LongType.INSTANCE)
                    .addScalar("vesselName", StringType.INSTANCE)
                    .addScalar("countryName", StringType.INSTANCE)
                    .addScalar("vesselTypeName", StringType.INSTANCE)
                    .addScalar("callSign", StringType.INSTANCE)
                    .addScalar("imo", LongType.INSTANCE)
                    .addScalar("width", BigDecimalType.INSTANCE)
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .setResultTransformer(Transformers.aliasToBean(VesselDTO.class));
            List<VesselDTO> result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                query = session.createNativeQuery(sqlCountRows);
                if (!StringUtil.isNullOrEmpty(mmsi) && !"0".equals(mmsi)) {
                    query.setParameter("mmsi", new Long(mmsi));
                }
                if (countryId != null && countryId > 0) {
                    query.setParameter("countryId", countryId);
                }
                if (vesselTypeId != null && vesselTypeId > 0) {
                    query.setParameter("vesselTypeId", vesselTypeId);
                }
                if (!StringUtil.isNullOrEmpty(vesselName)) {
                    query.setParameter("vesselName", vesselName);
                }
                if (!StringUtil.isNullOrEmpty(ip) && StringUtil.validIpV4(ip)) {
                    query.setParameter("ip", "%" + ip + "%");
                }
                if (!StringUtil.isNullOrEmpty(phone)) {
                    query.setParameter("phone", "%" + phone + "%");
                }
                BigInteger count = (BigInteger) query.getSingleResult();
                return new PageImpl<>(result, pageable, count != null ? count.longValue() : 0L);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            closeSession(session);
        }
        return null;
    }

    public MessageContent findDetailVessel(Long mmsi) {
        Session session = openSession();
        try {
            String sql = " SELECT ai.mmsi, ai.imo, ai.name AS vesselName, (ai.dim_a + ai.dim_b) AS length, (ai.dim_c + ai.dim_d) AS width, ai.created_time AS createdTime "
                    + " , ai.updated_time AS updatedTime, ai.image_path AS imagePath, ai.year_of_build AS yearOfBuild, ai.draugth, ai.engine_type AS engineType "
                    + " , ai.gross_tonnage AS grossTonnage, ai.dead_weight AS deadWeight, ai.speed_avg AS speedAvg, ai.speed_max AS speedMax, ai.displacement, ai.crew "
                    + " , ai.callsign AS callSign, c.name as countryName, vt.type_name as vesselTypeName, vai.operation_unit AS operationUnit, vai.other_info as otherInfo, vai.owner "
                    + " , vai.unit, vai.status "
                    + " ,(SELECT STRING_AGG(CONCAT(ip_address, CONCAT('---', note)), '###') FROM vsat02.mmsi_ip WHERE mmsi = ai.mmsi) AS ipLstStr "
                    + " ,(SELECT STRING_AGG(CONCAT(phone_number, CONCAT('---', note)), '###') FROM vsat02.mmsi_phone WHERE mmsi = ai.mmsi) AS phoneLstStr "
                    + " ,(SELECT STRING_AGG(image_path, '###') FROM vsat02.mmsi_image WHERE mmsi = ai.mmsi) AS imageLst "
                    + " FROM vsat02.ais_info ai "
                    + " LEFT JOIN vsat02.vessel_alt_info vai ON ai.mmsi = vai.mmsi "
                    + " LEFT JOIN vsat02.countries c ON c.id = ai.country_id  "
                    + " LEFT JOIN vsat02.vessel_types vt ON vt.type_id = ai.type_id "
                    + " WHERE ai.mmsi = :mmsi ";
            NativeQuery query = session.createSQLQuery(sql).setParameter("mmsi", mmsi);
            query.addScalar("mmsi", LongType.INSTANCE)
                    .addScalar("imo", LongType.INSTANCE)
                    .addScalar("vesselName", StringType.INSTANCE)
                    .addScalar("length", LongType.INSTANCE)
                    .addScalar("width", LongType.INSTANCE)
                    .addScalar("imagePath", StringType.INSTANCE)
                    .addScalar("yearOfBuild", IntegerType.INSTANCE)
                    .addScalar("draugth", IntegerType.INSTANCE)
                    .addScalar("engineType", StringType.INSTANCE)
                    .addScalar("grossTonnage", IntegerType.INSTANCE)
                    .addScalar("deadWeight", IntegerType.INSTANCE)
                    .addScalar("speedAvg", FloatType.INSTANCE)
                    .addScalar("speedMax", FloatType.INSTANCE)
                    .addScalar("displacement", StringType.INSTANCE)
                    .addScalar("crew", StringType.INSTANCE)
                    .addScalar("callSign", StringType.INSTANCE)
                    .addScalar("createdTime", TimestampType.INSTANCE)
                    .addScalar("updatedTime", TimestampType.INSTANCE)
                    .addScalar("countryName", StringType.INSTANCE)
                    .addScalar("vesselTypeName", StringType.INSTANCE)
                    .addScalar("operationUnit", StringType.INSTANCE)
                    .addScalar("otherInfo", StringType.INSTANCE)
                    .addScalar("owner", StringType.INSTANCE)
                    .addScalar("unit", StringType.INSTANCE)
                    .addScalar("status", StringType.INSTANCE)
                    .addScalar("ipLstStr", StringType.INSTANCE)
                    .addScalar("phoneLstStr", StringType.INSTANCE)
                    .addScalar("imageLst", StringType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(AisInfoDetailDTO.class));

            AisInfoDetailDTO result = (AisInfoDetailDTO) query.uniqueResult();
            if (result != null) {
                // Danh sách hải trình
                result.setAisLst(
                        (List<AisDTO>) searchAisList(new SearchListAisRequest(mmsi.toString(), ApplicationConfig.LIMIT_AIS_MSG_FOR_DETAIL_VESSEL)).getData()
                );

                //Danh sách IP tàu
                if (result.getIpLstStr() != null) {
                    List<MmsiIpDTO> ipLst = new ArrayList<>();
                    String[] ipArr = result.getIpLstStr().split("###");
                    for (String ipRows : ipArr) {
                        String[] ipRow = ipRows.split("---");
                        ipLst.add(new MmsiIpDTO(ipRow[0], ipRow[1]));
                    }
                    result.setIpLst(ipLst);
                    result.setIpLstStr(null);
                }

                //Danh sách SĐT tàu
                if (result.getPhoneLstStr() != null) {
                    List<MmsiPhoneDTO> phoneLst = new ArrayList<>();
                    String[] phoneArr = result.getPhoneLstStr().split("###");
                    for (String phoneRows : phoneArr) {
                        String[] phoneRow = phoneRows.split("---");
                        phoneLst.add(new MmsiPhoneDTO(phoneRow[0], phoneRow[1]));
                    }
                    result.setPhoneLst(phoneLst);
                    result.setPhoneLstStr(null);
                }
            }
            return new MessageContent(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            closeSession(session);
        }
        return null;
    }

    private boolean checkVesselInGroup(AddObjectToGroup data) {
        Boolean isExistVesselInGroup = false;
        Session session = openSession();
        try {
            Transaction tx = session.beginTransaction();
            String sql = "SELECT count(*) FROM object_group_mapping \n"
                    + "where objid= :objId and object_group_id=:object_group_id";
            NativeQuery query = session.createNativeQuery(sql);
            query.setParameter("objId", data.getObjId());
            query.setParameter("object_group_id", data.getObjectGroupId());
            Long count = ((BigInteger) query.uniqueResult()).longValue();
            tx.commit();
            if (count > 0) {
                isExistVesselInGroup = true;
            } else {
                isExistVesselInGroup = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return isExistVesselInGroup;
    }

    public MessageContent addVesselToGroup(AddObjectToGroup data) {
        MessageContent messageContent = new MessageContent();
        Session session = openSession();
        try {

            if (this.checkVesselInGroup(data) == true) {
                messageContent.setStatus(HttpStatus.BAD_REQUEST.value());
                messageContent.setMessage("Đối tượng đã năm trong nhóm này rồi.");
                return messageContent;
            }

            Transaction tx = session.beginTransaction();
            String sql = "INSERT INTO \n"
                    + "object_group_mapping \n"
                    + "(objid, object_group_id, updated_time) \n"
                    + "VALUES(:objid, :object_group_id, CURRENT_TIMESTAMP);";
            NativeQuery query = session.createNativeQuery(sql);
            query.setParameter("objid", data.getObjId());
            query.setParameter("object_group_id", data.getObjectGroupId());
            int record = query.executeUpdate();
            tx.commit();
            if (record > 0) {
                messageContent.setStatus(HttpStatus.OK.value());
            } else {
                messageContent.setStatus(HttpStatus.BAD_REQUEST.value());
                messageContent.setMessage("addVesselToGroup fail");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return messageContent;
    }

    public boolean existsObjectGroup(String objectGroupName, String actionType, Long idForUpdate) {
        Session session = openSession();
        try {
            String sql = " SELECT id FROM object_group WHERE name = :objectGroupName ";
            if ("UPDATE".equals(actionType)) {
                sql += " and id <> :idForUpdate ";
            }
            NativeQuery query = session.createNativeQuery(sql)
                    .setParameter("objectGroupName", objectGroupName.trim());
            if ("UPDATE".equals(actionType)) {
                query.setParameter("idForUpdate", idForUpdate);
            }

            return query.uniqueResult() != null;
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return true;
    }
}
