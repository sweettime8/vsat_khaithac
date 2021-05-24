package com.elcom.rule.repository.rule;

import com.elcom.rule.model.dto.VesselDTO;
import com.elcom.rule.model.dto.request.AddVessellToGroup;
import com.elcom.gateway.message.MessageContent;
import com.elcom.rule.constant.Constant;
import com.elcom.rule.model.dto.AreaWithObjectId;
import com.elcom.rule.model.dto.RuleNotifyInsertDTO;
import com.elcom.rule.repository.BaseRepository;
import com.elcom.rule.utils.StringUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.query.NativeQuery;
import com.elcom.rule.model.vessel.VesselGroup;
import com.elcom.rule.utils.DateUtil;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Repository
public class RuleRepository extends BaseRepository {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleRepository.class);

    @Autowired
    @Qualifier("clickHouseSession")
    SessionFactory clickHouseSessionFactory;
    
    @Autowired
    public RuleRepository(@Qualifier("vsatEntityManagerFactory") EntityManagerFactory factory
                        , @Qualifier("vsatChDataSource") DataSource clickHouseDataSource) {
        super(factory, clickHouseDataSource);
    }
    
    // Lấy danh sách id vùng của đối tượng
    // e.g: startTime: 2021-02-27 00:00:00
    // e.g: endTime:   2021-03-01 00:00:00
    public List<List<Integer>> findAreaIdsByObjectId222(String startTime, String endTime, String objId) {
        Session session = this.clickHouseSessionFactory.openSession();
        try {
            String timeKeyStart = startTime.substring(0, startTime.length()-2)
                                    .replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
            String timeKeyEnd = endTime.substring(0, endTime.length()-2)
                                    .replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
            
            String sql = " SELECT areaIds FROM vsat_ais final "
                        + " WHERE objId = :objId AND partName IN (:partitionName) AND (timeKey BETWEEN :startTime AND :endTime) "
                        + " ORDER BY ingestTime DESC ";
                    
            NativeQuery query = session.createSQLQuery(sql);
            query.setParameter("objId", objId)
                 .setParameterList("partitionName", DateUtil.getPartitionNameByTwoDate(startTime, endTime, Constant.PARTITION_TYPE_DAY))
                 .setParameter("startTime", timeKeyStart)
                 .setParameter("endTime", timeKeyEnd);
            Object obj = query.getResultList();
            List<List<Integer>> lst = (List<List<Integer>>) obj;
            return lst;
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
    
    // Lấy danh sách id vùng của đối tượng
    // e.g: startTime: 2021-02-27 00:00:00
    // e.g: endTime:   2021-03-01 00:00:00
    public Set<Integer> findAreaIdsByObjectId(String startTime, String endTime, String objId) {
        Set<Integer> set = new HashSet<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            connection = this.clickHouseDataSource.getConnection();
            String timeKeyStart = startTime.substring(0, startTime.length()-2)
                                    .replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
            String timeKeyEnd = endTime.substring(0, endTime.length()-2)
                                    .replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
            List<Long> partitionLst = DateUtil.getPartitionNameByTwoDate(startTime, endTime, Constant.PARTITION_TYPE_DAY);
            
            String sql = " SELECT areaIds FROM vsat_ais final "
                        + " WHERE objId = ? AND partName IN (?) AND (timeKey BETWEEN ? AND ?) "
                        + " ORDER BY ingestTime DESC ";
            
            statement = connection.prepareStatement(sql);
            statement.setString(1, objId);
            statement.setArray(2, connection.createArrayOf("Long", partitionLst.toArray(new Long[partitionLst.size()])));
            statement.setString(3, timeKeyStart);
            statement.setString(4, timeKeyEnd);
            rs = statement.executeQuery();
            Array arr;
            int[] intArr;
            while( rs.next() ) {
                arr = rs.getArray("areaIds");
                if( arr != null ) {
                    intArr = (int[]) arr.getArray();
                    if( intArr != null && intArr.length > 0 )
                        for( int i : intArr )
                            set.add(i);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            try {
                if( connection!=null && !connection.isClosed() )
                    connection.close();
                if( statement!=null && !statement.isClosed() )
                    statement.close();
                if( rs!=null && !rs.isClosed() )
                    rs.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return set;
    }
    
    /** ruleActionId = 1: luật vào vùng,    ruleActionId = 2: luật ra vùng
     * @param ruleActionId
     * @return List */
    public List<AreaWithObjectId> findListRuleArea(Integer ruleActionId) {
        if( ruleActionId == null )
            return null;
        
        Session session = openSession();
        try {
            String sql = " select r.id as ruleId, r.area_id as areaId, r.from_date as fromDate, r.to_date as toDate, ro.object_id as objId from vsat02.rule r " +
                            " inner join vsat02.rule_object ro on r.id = ro.rule_id " +
                            " where r.rule_action_id = :ruleActionId and r.status = 1 and (r.from_date <= now() and r.to_date >= now()) " +
                            "	union " +
                            " select r.id as ruleId, r.area_id as areaId, r.from_date as fromDate, r.to_date as toDate, CAST(vgm.mmsi AS VARCHAR) as objId from vsat02.rule r " +
                            " inner join vsat02.rule_group_object rgo on r.id = rgo.rule_id  " +
                            " inner join vsat02.vessel_group_mapping vgm on rgo.group_object_id = vgm.vessel_group_id " +
                            " inner join vsat02.vessel_group vg on vg.id = vgm.vessel_group_id  " +
                            " where r.rule_action_id = :ruleActionId and r.status = 1 and vg.is_active = 1 and (r.from_date <= now() and r.to_date >= now()) " +
                            "	order by areaId ";
            NativeQuery query = session.createNativeQuery(sql)
                                    .setParameter("ruleActionId", ruleActionId)
                                    .addScalar("ruleId", StringType.INSTANCE)
                                    .addScalar("areaId", StringType.INSTANCE)
                                    .addScalar("fromDate", StringType.INSTANCE)
                                    .addScalar("toDate", StringType.INSTANCE)
                                    .addScalar("objId", StringType.INSTANCE);
            query.setResultTransformer(Transformers.aliasToBean(AreaWithObjectId.class));
            return query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            closeSession(session);
        }
        return null;
    }

    private String findRuleIdAndAreaName(Integer ruleActionId, Long areaId, String objId) {
        Session session = openSession();
        try {
            String sql = " select r.id || '###' || a.name as result from vsat02.rule r " +
                        " inner join vsat02.areas a on a.id = r.area_id " + 
                        " inner join vsat02.rule_object ro on r.id = ro.rule_id " + 
                        " left join vsat02.rule_group_object rgo on r.id = rgo.rule_id " +
                        " left join vsat02.vessel_group_mapping vgm on rgo.group_object_id = vgm.vessel_group_id " +
                        " left join vsat02.vessel_group vg on (vg.is_active = 1 and vg.id = vgm.vessel_group_id) " + 
                        " where r.rule_action_id = :ruleActionId and r.status = 1 and r.area_id = :areaId " +
                        " and ( ro.object_id = :objId or CAST(vgm.mmsi AS VARCHAR) = :objId ) limit 1 ";
            NativeQuery query = session.createSQLQuery(sql);
            query.setParameter("ruleActionId", ruleActionId);
            query.setParameter("areaId", areaId);
            query.setParameter("objId", objId);
            
            Object objResult = query.uniqueResult();
            if( objResult != null )
                return (String) objResult;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            closeSession(session);
        }
        return null;
    }
    
    public String insertRuleNotify(RuleNotifyInsertDTO ruleNotify) {
        String result = null;
        Session session = openSession();
        try {
            String ruleIdAndAreaName = this.findRuleIdAndAreaName(ruleNotify.getRuleActionId(), ruleNotify.getAreaId(), ruleNotify.getObjId());
            
            if( !StringUtil.isNullOrEmpty(ruleIdAndAreaName) ) {
                String[] ruleIdAndAreaNameArr = ruleIdAndAreaName.split("###");
                Transaction tx = session.beginTransaction();
                
                String sql = " insert into vsat02.rule_notify ( rule_id, object_id, description, event_time, area_id, rule_action_id, longitude, latitude ) " +
                            " values ( :ruleId, :objId, :description, :eventTime, :areaId, :ruleActionId, :longitude, :latitude )";
                NativeQuery query = session.createNativeQuery(sql);

                query.setParameter("ruleId", Integer.parseInt(ruleIdAndAreaNameArr[0]));
                query.setParameter("objId", ruleNotify.getObjId());
                query.setParameter("description", ruleNotify.getDescription() + "\"" +ruleIdAndAreaNameArr[1]+ "\"");
                query.setParameter("eventTime", ruleNotify.getEventTime());
                query.setParameter("areaId", ruleNotify.getAreaId());
                query.setParameter("ruleActionId", ruleNotify.getRuleActionId());
                query.setParameter("longitude", ruleNotify.getLongitude());
                query.setParameter("latitude", ruleNotify.getLatitude());

                int resultInsert = query.executeUpdate();
                tx.commit();
                if( resultInsert > 0 )
                    result = ruleIdAndAreaNameArr[1];
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            closeSession(session);
        }
        return result;
    }
    
    public Page<VesselGroup> searchVesselGroupList(String name, Integer isActive, Pageable pageable, String sortBy) {
        Session session = openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<VesselGroup> criteria = builder.createQuery(VesselGroup.class);
            Root<VesselGroup> booksRoot = criteria.from(VesselGroup.class);
            
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
            
            List<VesselGroup> lst = session.createQuery(criteria)
                                        .setFirstResult((int) pageable.getOffset())
                                        .setMaxResults(pageable.getPageSize())
                                        .getResultList();

            CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
            Root<VesselGroup> booksRootCount = countQuery.from(VesselGroup.class);
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
        }catch(Exception ex) {
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            closeSession(session);
        }
        return null;
    }
    
    public boolean removeVesselGroup(Long id) {
        Session session = openSession();
        try {
            Transaction tx = session.beginTransaction();
            String sql = " DELETE FROM vsat02.vessel_group where id = :id AND id not in " +
                         " ( SELECT vessel_group_id FROM vsat02.vessel_group_mapping where vessel_group_id = :id ) ";
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
    
    public Page<VesselDTO> searchVesselList(String mmsi, String vesselName, Integer countryId, Integer vesselTypeId
                                          , String ip, String phone, Pageable pageable) {
        Session session = openSession();
        try {
            String condition = "";
            if( !StringUtil.isNullOrEmpty(mmsi) && !"0".equals(mmsi) )
                condition += " AND ai.mmsi = :mmsi ";
            if( countryId!=null && countryId > 0 )
                condition += " AND ai.country_id = :countryId ";
            if( vesselTypeId!=null && vesselTypeId > 0 )
                condition += " AND ai.type_id = :vesselTypeId ";
            if( !StringUtil.isNullOrEmpty(vesselName) )
                condition += " AND ai.name LIKE :vesselName ";
            if( !StringUtil.isNullOrEmpty(ip) && StringUtil.validIpV4(ip) )
                condition += " AND ai.mmsi in (SELECT mmsi FROM vsat02.mmsi_ip WHERE ip_address LIKE :ip) ";
            if( !StringUtil.isNullOrEmpty(phone) )
                condition += " AND ai.mmsi in (SELECT mmsi FROM vsat02.mmsi_phone WHERE phone_number LIKE :phone) ";
            
            String sqlBody = " FROM vsat02.ais_info ai " +
                             " LEFT JOIN vsat02.countries c ON c.id = ai.country_id " +
                             " LEFT JOIN vsat02.vessel_types vt ON vt.type_id = ai.type_id " +
                             " WHERE 1 = 1 " + condition;
            String sqlFindRows = " SELECT ai.mmsi, ai.name AS vesselName, c.name AS countryName, vt.type_name AS vesselTypeName, ai.callsign AS callSign, ai.imo, ai.dim_c AS width "
                                 + sqlBody
                                 + " ORDER BY ai.mmsi ";
            String sqlCountRows = " SELECT count(ai.mmsi) " + sqlBody;
            
            NativeQuery query = session.createNativeQuery(sqlFindRows);
            if( !StringUtil.isNullOrEmpty(mmsi) && !"0".equals(mmsi) )
                query.setParameter("mmsi", new Long(mmsi));
            if( countryId!=null && countryId > 0 )
                query.setParameter("countryId", countryId);
            if( vesselTypeId!=null && vesselTypeId > 0 )
                query.setParameter("vesselTypeId", vesselTypeId);
            if( !StringUtil.isNullOrEmpty(vesselName) )
                query.setParameter("vesselName", "%" + vesselName.trim() + "%");
            if( !StringUtil.isNullOrEmpty(ip) && StringUtil.validIpV4(ip) )
                query.setParameter("ip", "%" + ip.trim() + "%");
            if( !StringUtil.isNullOrEmpty(phone) )
                query.setParameter("phone", "%" + phone.trim() + "%");
            
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
            if( result!=null && !result.isEmpty() ) {
                query = session.createNativeQuery(sqlCountRows);
                if( !StringUtil.isNullOrEmpty(mmsi) && !"0".equals(mmsi) )
                    query.setParameter("mmsi", new Long(mmsi));
                if( countryId!=null && countryId > 0 )
                    query.setParameter("countryId", countryId);
                if( vesselTypeId!=null && vesselTypeId > 0 )
                    query.setParameter("vesselTypeId", vesselTypeId);
                if( !StringUtil.isNullOrEmpty(vesselName) )
                    query.setParameter("vesselName", vesselName);
                if( !StringUtil.isNullOrEmpty(ip) && StringUtil.validIpV4(ip) )
                    query.setParameter("ip", "%" + ip + "%");
                if( !StringUtil.isNullOrEmpty(phone) )
                    query.setParameter("phone", "%" + phone + "%");
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
    
    private boolean checkVesselInGroup(AddVessellToGroup data){
        Boolean isExistVesselInGroup=false;
        Session session = openSession();
        try {
            Transaction tx = session.beginTransaction();
            String sql = "SELECT count(*) FROM vsat02.vessel_group_mapping  " +
                    "where mmsi= :mmsi and vessel_group_id=:vessel_group_id";
            NativeQuery query = session.createNativeQuery(sql);
            query.setParameter("mmsi", data.getMmsi());
            query.setParameter("vessel_group_id", data.getVesselGroupId());
            Long count = ((BigInteger)query.uniqueResult()).longValue();
            tx.commit();
            if(count >0){
                isExistVesselInGroup=true;
            }
            else{
                isExistVesselInGroup=false;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            LOGGER.error(ex.toString());
        }
        finally {
            closeSession(session);
        }
        return isExistVesselInGroup;
    }

    public MessageContent addVesselToGroup(AddVessellToGroup data){
        MessageContent messageContent=new MessageContent();
        Session session = openSession();
        try {

            if(this.checkVesselInGroup(data)==true){
                messageContent.setStatus(HttpStatus.BAD_REQUEST.value());
                messageContent.setMessage("Tàu đã năm trong nhóm này rồi.");
                return messageContent;
            }

            Transaction tx = session.beginTransaction();
            String sql = " INSERT INTO vsat02.vessel_group_mapping ( mmsi, vessel_group_id, updated_time ) " +
                        " VALUES( :mmsi, :vessel_group_id, CURRENT_TIMESTAMP ) ";
            NativeQuery query = session.createNativeQuery(sql);
            query.setParameter("mmsi", data.getMmsi());
            query.setParameter("vessel_group_id", data.getVesselGroupId());
            int record= query.executeUpdate();
            tx.commit();
            if(record >0){
                messageContent.setStatus(HttpStatus.OK.value());
            }
            else{
                messageContent.setStatus(HttpStatus.BAD_REQUEST.value());
                messageContent.setMessage("addVesselToGroup fail");
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            LOGGER.error(ex.toString());
        }
        finally {
            closeSession(session);
        }
        return messageContent;
    }

    public boolean existsVesselGroup(String vesselGroupName, String actionType, Long idForUpdate) {
        Session session = openSession();
        try {
            String sql = " SELECT id FROM vsat02.vessel_group WHERE name = :vesselGroupName ";
            if( "UPDATE".equals(actionType) )
                sql += " and id <> :idForUpdate ";
            NativeQuery query = session.createNativeQuery(sql)
                                       .setParameter("vesselGroupName", vesselGroupName.trim());
            if( "UPDATE".equals(actionType) )
                query.setParameter("idForUpdate", idForUpdate);
            
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
