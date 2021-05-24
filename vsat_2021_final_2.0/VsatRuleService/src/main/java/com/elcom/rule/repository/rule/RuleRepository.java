package com.elcom.rule.repository.rule;

import com.elcom.rule.model.dto.VesselDTO;
import com.elcom.rule.model.dto.request.AddVessellToGroup;
import com.elcom.gateway.message.MessageContent;
import com.elcom.rule.model.dto.DetailRuleEventDTO;
import com.elcom.rule.model.dto.RuleEventNotifiDTO;
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
import java.math.BigInteger;
import java.util.UUID;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
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
    
    public Page<RuleEventNotifiDTO> findRuleEvents(String startTime, String endTime, Long ruleId, Long groupObjectId
                                                , String objId, String objName, Pageable pageable) {
        Session session = openSession();
        try {
//            String condition = " ( rn.event_time >= :startTime AND rn.event_time <= :endTime ) ";
//            String condition = " ( rn.event_time BETWEEN :startTime AND :endTime ) ";
            String condition = " ( rn.event_time BETWEEN '"+startTime+"' AND '"+endTime+"' ) ";
            if( !StringUtil.isNullOrEmpty(objId) && !"0".equals(objId) )
                condition += " AND ( CAST(ai.mmsi AS VARCHAR) = :objId OR CAST(oui.uuid AS VARCHAR) = :objId ) ";
            if( ruleId != null && !ruleId.equals(0L) )
                condition += " AND rn.rule_id = :ruleId ";
            if( groupObjectId != null && !groupObjectId.equals(0L) )
                condition += " AND rgo.group_object_id = :groupObjectId ";
            if( !StringUtil.isNullOrEmpty(objName) )
                condition += " AND ( ai.name LIKE :objName OR oui.name LIKE :objName ) ";
            
            String sqlBody = " FROM vsat02.rule_notify rn " +
                             " LEFT JOIN vsat02.rule_group_object rgo ON rn.rule_id = rgo.rule_id " +
                             " LEFT JOIN vsat02.ais_info ai ON CAST(ai.mmsi AS VARCHAR) = rn.object_id " +
                             " LEFT JOIN vsat02.object_undefined_info oui ON CAST(oui.uuid AS VARCHAR) = rn.object_id " +
                             " WHERE " + condition;
            String sqlFindRows = " SELECT rn.uuid, rn.object_id AS objId, description, rn.event_time AS eventTime " +
                                " , rn.rule_action_id AS ruleActionId, rn.read_status AS readStatus " +
                                " , ai.mmsi AS vesselId, oui.uuid AS ufoId "
                                 + sqlBody
                                 + " ORDER BY rn.created_time DESC ";
            String sqlCountRows = " SELECT COUNT(rn.object_id) " + sqlBody;
            
            NativeQuery query = session.createNativeQuery(sqlFindRows);
            
//            query.setParameter("startTime", startTime);
//            query.setParameter("endTime", endTime);
            if( !StringUtil.isNullOrEmpty(objId) && !"0".equals(objId) )
                query.setParameter("objId", objId);
            if( ruleId != null && !ruleId.equals(0L) )
                query.setParameter("ruleId", ruleId);
            if( groupObjectId != null && !groupObjectId.equals(0L) )
                query.setParameter("groupObjectId", groupObjectId);
            if( !StringUtil.isNullOrEmpty(objName) )
                query.setParameter("objName", "%" + objName.trim() + "%");
            
            query.addScalar("uuid", StringType.INSTANCE)
                 .addScalar("objId", StringType.INSTANCE)
                 .addScalar("description", StringType.INSTANCE)
                 .addScalar("eventTime", StringType.INSTANCE)
                 .addScalar("ruleActionId", IntegerType.INSTANCE)
                 .addScalar("readStatus", IntegerType.INSTANCE)
                 .addScalar("vesselId", LongType.INSTANCE)
                 .addScalar("ufoId", StringType.INSTANCE)
                 .setFirstResult((int) pageable.getOffset())
                 .setMaxResults(pageable.getPageSize())
                 .setResultTransformer(Transformers.aliasToBean(RuleEventNotifiDTO.class));
            List<RuleEventNotifiDTO> result = query.getResultList();
            
            if( result!=null && !result.isEmpty() ) {
                query = session.createNativeQuery(sqlCountRows);
                
//                query.setParameter("startTime", startTime);
//                query.setParameter("endTime", endTime);
                if( !StringUtil.isNullOrEmpty(objId) && !"0".equals(objId) )
                    query.setParameter("objId", objId);
                if( ruleId != null && !ruleId.equals(0L) )
                    query.setParameter("ruleId", ruleId);
                if( groupObjectId != null && !groupObjectId.equals(0L) )
                    query.setParameter("groupObjectId", groupObjectId);
                if( !StringUtil.isNullOrEmpty(objName) )
                    query.setParameter("objName", "%" + objName.trim() + "%");
                
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
    
    public Long countRuleEventByObjId(String startTime, String endTime, String objId) {
        Long result = 0L;
        Session session = openSession();
        try {
            String condition = " ( rn.event_time BETWEEN '"+startTime+"' AND '"+endTime+"' ) " +
                               " AND ( CAST(ai.mmsi AS VARCHAR) = :objId OR CAST(oui.uuid AS VARCHAR) = :objId ) ";
            
            String sqlBody = " FROM vsat02.rule_notify rn " +
                             " LEFT JOIN vsat02.rule_group_object rgo ON rn.rule_id = rgo.rule_id " +
                             " LEFT JOIN vsat02.ais_info ai ON CAST(ai.mmsi AS VARCHAR) = rn.object_id " +
                             " LEFT JOIN vsat02.object_undefined_info oui ON CAST(oui.uuid AS VARCHAR) = rn.object_id " +
                             " WHERE " + condition;
            String sqlCountRows = " SELECT COUNT(rn.uuid) " + sqlBody;
            NativeQuery query = session.createNativeQuery(sqlCountRows)
                                       .setParameter("objId", objId);
            BigInteger count = (BigInteger) query.getSingleResult();
            if( count != null )
                result = count.longValue();
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            closeSession(session);
        }
        return result;
    }
    
    public boolean updateRuleEventReadStatus(String ruleNotifyUuid) {
        Session session = null;
        try {
            session = openSession();
            Transaction tx = session.beginTransaction();
            
            String sql = " UPDATE vsat02.rule_notify SET read_status = 1 WHERE uuid = :ruleNotifyUuid ";
            NativeQuery query = session.createNativeQuery(sql)
                                       .setParameter("ruleNotifyUuid", UUID.fromString(ruleNotifyUuid));
            int updateStatus = query.executeUpdate();
            tx.commit();
            return updateStatus > 0;
        } catch (Exception ex) {
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            closeSession(session);
        }
        return false;
    }
    
    public DetailRuleEventDTO findDetailRuleEventWithVessel(String ruleNotifyUuid) {
        Session session = openSession();
        try {
            String sqlBody = " FROM vsat02.rule_notify rn " +
                             " INNER JOIN vsat02.ais_info ai ON CAST(ai.mmsi AS VARCHAR) = rn.object_id " +
                             " LEFT JOIN vsat02.countries c ON c.id = ai.country_id " +
                             " WHERE rn.uuid = :ruleNotifyUuid ";
            String sqlFindRows = " SELECT rn.object_id AS objId, rn.rule_action_id AS ruleActionId, rn.description, rn.event_time AS eventTime, rn.longitude, rn.latitude " +
                                 " , ai.name AS objName, ai.imo, ai.callsign AS callSign, c.name AS countryName " +
                                 " , (SELECT STRING_AGG(ip_address, '###') FROM vsat02.mmsi_ip WHERE mmsi = ai.mmsi) AS ipLst "
                                 + sqlBody;
            
            NativeQuery query = session.createNativeQuery(sqlFindRows);
            query.setParameter("ruleNotifyUuid", UUID.fromString(ruleNotifyUuid));
            
            query.addScalar("objId", StringType.INSTANCE)
                    .addScalar("ruleActionId", IntegerType.INSTANCE)
                    .addScalar("description", StringType.INSTANCE)
                    .addScalar("eventTime", StringType.INSTANCE)
                    .addScalar("longitude", DoubleType.INSTANCE)
                    .addScalar("latitude", DoubleType.INSTANCE)
                    .addScalar("objName", StringType.INSTANCE)
                    .addScalar("imo", LongType.INSTANCE)
                    .addScalar("callSign", StringType.INSTANCE)
                    .addScalar("countryName", StringType.INSTANCE)
                    .addScalar("ipLst", StringType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(DetailRuleEventDTO.class));
            return (DetailRuleEventDTO) query.uniqueResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            closeSession(session);
        }
        return null;
    }
    
    public DetailRuleEventDTO findDetailRuleEventWithObjectUndefined(String ruleNotifyUuid) {
        Session session = openSession();
        try {
            String sqlBody = " FROM vsat02.rule_notify rn " +
                             " INNER JOIN vsat02.object_undefined_info oui on CAST(oui.uuid AS VARCHAR) = rn.object_id " +
                             " LEFT JOIN vsat02.countries c ON c.id = oui.country_id " +
                             " WHERE rn.uuid = :ruleNotifyUuid ";
            String sqlFindRows = " SELECT rn.object_id AS objId, rn.rule_action_id AS ruleActionId, rn.description, rn.event_time AS eventTime, rn.longitude, rn.latitude " +
                                 " , oui.name AS objName, c.name AS countryName " +
                                 " , (SELECT STRING_AGG(ip_address, '###') FROM vsat02.ufo_ip WHERE ufo_id = oui.uuid) AS ipLst "
                                 + sqlBody;
            
            NativeQuery query = session.createNativeQuery(sqlFindRows);
            query.setParameter("ruleNotifyUuid", UUID.fromString(ruleNotifyUuid));
            
            query.addScalar("objId", StringType.INSTANCE)
                    .addScalar("ruleActionId", IntegerType.INSTANCE)
                    .addScalar("description", StringType.INSTANCE)
                    .addScalar("eventTime", StringType.INSTANCE)
                    .addScalar("longitude", DoubleType.INSTANCE)
                    .addScalar("latitude", DoubleType.INSTANCE)
                    .addScalar("objName", StringType.INSTANCE)
                    .addScalar("countryName", StringType.INSTANCE)
                    .addScalar("ipLst", StringType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(DetailRuleEventDTO.class));
            return (DetailRuleEventDTO) query.uniqueResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            closeSession(session);
        }
        return null;
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
