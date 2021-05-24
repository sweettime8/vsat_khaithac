package com.elcom.media.repository.media;

import com.elcom.gateway.message.MessageContent;
import com.elcom.media.config.ApplicationConfig;
import com.elcom.media.constant.Constant;
import com.elcom.media.model.dto.request.media.AddCommentRequest;
import com.elcom.media.model.dto.request.media.DeleteFileMediaRequest;
import com.elcom.media.model.dto.request.media.GetDetailMediaRelationRequest;
import com.elcom.media.model.dto.request.media.GetListlMediaByIdRelationRequest;
import com.elcom.media.model.dto.request.media.GetTotalMediaByObjectRequest;
import com.elcom.media.model.dto.request.media.MediaRelationDTO;
import com.elcom.media.model.media.MediaRaw;
import com.elcom.media.repository.BaseRepository;
import com.elcom.media.utils.StringUtil;
import com.elcom.vsat.model.MediaType;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import com.elcom.media.model.dto.request.media.SearchListMediaRequest;
import com.elcom.media.model.media.Comment;
import com.elcom.media.utils.DateUtil;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.http.HttpStatus;

@Repository
public class MediaRepository extends BaseRepository {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaRepository.class);

    @Autowired
    @Qualifier("clickHouseSession")
    SessionFactory clickHouseSessionFactory;
    
    @Autowired
    public MediaRepository(@Qualifier("vsatEntityManagerFactory") EntityManagerFactory factory, @Qualifier("vsatChDataSource") DataSource dataSource) {
        super(factory, dataSource);
        //ex ket noi ch query
//        try (
//            Connection connection = this.clDatasource.getConnection();
//            ResultSet rs = connection.createStatement().executeQuery("SELECT *\n" +
//                    "FROM VSAT_AIS");){
//            while (rs.next()) {
//                String lastName = rs.getString("MMSI");
//                System.out.println(lastName + "\n");
//            }
//        }
//        catch(Exception ex){
//
//        }

    }

    public List<MediaType> getListMediaTypes() {
        Session session = null;
        try {
            session = openSession();
            Query query = session.createNativeQuery(" SELECT * FROM vsat02.media_type ", MediaType.class);
            return (List<MediaType>) query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            closeSession(session);
        }
        return null;
    }
    
    public List<Comment> getListComment(String refId, Integer commentTypeId) {
        Session session = null;
        try {
            session = openSession();
            String sql = " SELECT * FROM vsat02.comments WHERE ref_id = :refId AND comment_type_id = :commentTypeId ORDER BY id ";
            Query query = session.createNativeQuery(sql, Comment.class)
                                .setParameter("refId", refId)
                                .setParameter("commentTypeId", commentTypeId);
            return (List<Comment>) query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            closeSession(session);
        }
        return null;
    }
//    public static void main(String[] args) {
//        String s=" OR fileName like '%a'";
//        System.out.println("s:"+s.replaceFirst("OR", ""));
//    }
    public MessageContent searchMedia(SearchListMediaRequest data) {
        List<MediaRaw> lstMediaRaw = new ArrayList<>();
        Session session = this.clickHouseSessionFactory.openSession();
        long count = 0;
        
        Pageable pageable = PageRequest.of(data.getCurrentPage(), data.getRowsPerPage());
        
        try {
            String sqlWhere = "";
            String sql = " SELECT * FROM vsat_media WHERE (eventTime >= toDateTime(:start_time) AND eventTime <= toDateTime(:end_time)) AND partName IN (:listPartition) ";
            String sqlCount = " SELECT COUNT(*) from vsat_media WHERE (eventTime >= toDateTime(:start_time) AND eventTime <= toDateTime(:end_time)) AND partName IN (:listPartition) ";
            
            if( data.getDataSource()!=null && !data.getDataSource().isEmpty() )
                sqlWhere+=" AND dataSource IN :data_source ";
            
            if( data.getMediaType()!=null && !data.getMediaType().isEmpty() )
                sqlWhere+=" AND mediaTypeId IN :media_type ";
            
            if( data.getFileType()!=null && !data.getFileType().isEmpty() ) {
                StringBuilder fileTypeCondition = new StringBuilder();
                data.getFileType().forEach((s) -> {
                    fileTypeCondition.append(" OR fileName LIKE '%").append(s).append("'");
                });
                sqlWhere += " AND (" + fileTypeCondition.toString().replaceFirst(" OR", "") + ")";
            }
            
            if( !StringUtil.isNullOrEmpty(data.getSourceIps()) )
                sqlWhere+=" AND sourceIp IN (:source_ips) ";
            
            if( !StringUtil.isNullOrEmpty(data.getDestIps()) )
                sqlWhere+=" AND destIp IN (:dest_ips) ";
            
            if( !StringUtil.isNullOrEmpty(data.getObjId()) )
                sqlWhere+=" AND (srcObjId = :objId OR destObjId = :objId) ";
            
            sqlCount = sqlCount + sqlWhere;
            sql = sql + sqlWhere + " ORDER BY ingestTime DESC ";
            
            NativeQuery query = session.createNativeQuery(sql, MediaRaw.class);
            NativeQuery queryCount = session.createNativeQuery(sqlCount);
            
            query.setParameter("start_time",data.getStartTime(),TemporalType.TIMESTAMP);
            query.setParameter("end_time",data.getEndTime(),TemporalType.TIMESTAMP);

            queryCount.setParameter("start_time",data.getStartTime(),TemporalType.TIMESTAMP);
            queryCount.setParameter("end_time",data.getEndTime(),TemporalType.TIMESTAMP);

            List<Long> sLstPartition = DateUtil.getPartitionNameByTwoDate(data.getStartTime(), data.getEndTime(), Constant.PARTITION_TYPE_DAY);
            query.setParameterList("listPartition", sLstPartition);
            queryCount.setParameterList("listPartition", sLstPartition);
            
            if( data.getDataSource()!=null && !data.getDataSource().isEmpty() ) {
                query.setParameterList("data_source", data.getDataSource());
                queryCount.setParameterList("data_source", data.getDataSource());
            }
            if( data.getMediaType()!=null && !data.getMediaType().isEmpty() ) {
                query.setParameterList("media_type", data.getMediaType());
                queryCount.setParameterList("media_type", data.getMediaType());
            }
            if( !StringUtil.isNullOrEmpty(data.getSourceIps()) ) {
                String[] sourceIpLst = data.getSourceIps().trim().split(",");
                query.setParameterList("source_ips", sourceIpLst);
                queryCount.setParameterList("source_ips", sourceIpLst);
            }
            if( !StringUtil.isNullOrEmpty(data.getDestIps()) ) {
                String[] destIpLst = data.getDestIps().trim().split(",");
                query.setParameterList("dest_ips", destIpLst);
                queryCount.setParameterList("dest_ips", destIpLst);
            }
            if( !StringUtil.isNullOrEmpty(data.getObjId()) ) {
                query.setParameter("objId", data.getObjId().trim());
                queryCount.setParameter("objId", data.getObjId().trim());
            }
            
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            lstMediaRaw = query.getResultList();
            count = ((Number) queryCount.getSingleResult()).intValue();
            
            if( lstMediaRaw!=null && !lstMediaRaw.isEmpty() )
                lstMediaRaw.stream().forEach(media -> {
                        media.setFilePathLocal(media.getFilePath());
                        media.setFilePath(media.getFilePath().replace(ApplicationConfig.ROOT_FOLDER_FILE_PATH_INTERNAL, ApplicationConfig.MEDIA_LINK_ROOT_API));
//                        media.setFilePath(media.getFilePath().replace(ApplicationConfig.ROOT_FOLDER_FILE_PATH_INTERNAL, ""));
                    }
                );
        }
        catch(Exception e) {
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
        }finally {
            if (session != null && session.isOpen()) {
                session.disconnect();
                session.close();
            }
        }
        
        MessageContent messageContent=new MessageContent();
        messageContent.setData(lstMediaRaw);
        messageContent.setTotal(count);
        messageContent.setStatus(HttpStatus.OK.value());
        messageContent.setMessage(HttpStatus.OK.toString());
        
        return messageContent;
    }
    
    public MessageContent findTotalMediaByObjectId(GetTotalMediaByObjectRequest data) {
        Session session = this.clickHouseSessionFactory.openSession();
        Long count = 0L;
        try {
            String sql = " SELECT COUNT(*) from vsat_media WHERE (eventTime >= toDateTime(:start_time) AND eventTime <= toDateTime(:end_time)) AND partName IN (:listPartition) ";
            
            if( data.getDataSource()!=null && !data.getDataSource().isEmpty() )
                sql += " AND dataSource IN :data_source ";
            
            if( data.getMediaType()!=null && !data.getMediaType().isEmpty() )
                sql += " AND mediaTypeId IN :media_type ";
            
            if( !StringUtil.isNullOrEmpty(data.getSourceIps()) )
                sql += " AND sourceIp IN (:source_ips) ";
            
            if( !StringUtil.isNullOrEmpty(data.getDestIps()) )
                sql += " AND destIp IN (:dest_ips) ";
            
            if( !StringUtil.isNullOrEmpty(data.getObjId()) )
                sql += " AND (srcObjId = :objId OR destObjId = :objId) ";
            
            NativeQuery query = session.createNativeQuery(sql);
            
            query.setParameter("start_time",data.getStartTime(),TemporalType.TIMESTAMP);
            query.setParameter("end_time",data.getEndTime(),TemporalType.TIMESTAMP);

            List<Long> sLstPartition = DateUtil.getPartitionNameByTwoDate(data.getStartTime(), data.getEndTime(), Constant.PARTITION_TYPE_DAY);
            query.setParameterList("listPartition", sLstPartition);
            
            if( data.getDataSource()!=null && !data.getDataSource().isEmpty() )
                query.setParameterList("data_source", data.getDataSource());
            if( data.getMediaType()!=null && !data.getMediaType().isEmpty() )
                query.setParameterList("media_type", data.getMediaType());
            if( !StringUtil.isNullOrEmpty(data.getSourceIps()) ) {
                String[] sourceIpLst = data.getSourceIps().trim().split(",");
                query.setParameterList("source_ips", sourceIpLst);
            }
            if( !StringUtil.isNullOrEmpty(data.getDestIps()) ) {
                String[] destIpLst = data.getDestIps().trim().split(",");
                query.setParameterList("dest_ips", destIpLst);
            }
            if( !StringUtil.isNullOrEmpty(data.getObjId()) )
                query.setParameter("objId", data.getObjId().trim());
            
            count = ((Number) query.getSingleResult()).longValue();
        }
        catch(Exception e) {
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
        }finally {
            if (session != null && session.isOpen()) {
                session.disconnect();
                session.close();
            }
        }
        
        MessageContent messageContent = new MessageContent();
        messageContent.setData(count);
        messageContent.setTotal(count);
        messageContent.setStatus(HttpStatus.OK.value());
        messageContent.setMessage(HttpStatus.OK.toString());
        
        return messageContent;
    }
    
    public MessageContent getDetailMediaRelation(GetDetailMediaRelationRequest inp) {
        List<MediaRaw> lstMediaRaw = new ArrayList<>();
        Session session = this.clickHouseSessionFactory.openSession();
        try {
            String sql = " SELECT * FROM vsat_media WHERE partName IN (:partName) AND uuidKey IN (:uuidKeyFrom, :uuidKeyTo) ORDER BY eventTime ";
            NativeQuery query = session.createNativeQuery(sql, MediaRaw.class);
            query.setParameter("partName", inp.getPartName());
            query.setParameter("uuidKeyFrom", inp.getUuidKeyFrom());
            query.setParameter("uuidKeyTo", inp.getUuidKeyTo());
            
            lstMediaRaw = query.getResultList();
            if( lstMediaRaw!=null && !lstMediaRaw.isEmpty() )
                lstMediaRaw.stream().forEach(media -> media.setFilePath(
                        media.getFilePath().replace(ApplicationConfig.ROOT_FOLDER_FILE_PATH_INTERNAL, ApplicationConfig.MEDIA_LINK_ROOT_API))
                );
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            if (session != null && session.isOpen()) {
                session.disconnect();
                session.close();
            }
        }
        
        MessageContent messageContent = new MessageContent();
        messageContent.setData(lstMediaRaw);
        messageContent.setStatus(HttpStatus.OK.value());
        messageContent.setMessage(HttpStatus.OK.toString());
        
        return messageContent;
    }
    
    public MessageContent getListMediaByRelationId(GetListlMediaByIdRelationRequest inp) {
        List<MediaRelationDTO> lstMediaRaw = new ArrayList<>();
        Session session = this.clickHouseSessionFactory.openSession();
        try {
            String sql = " SELECT uuidKey, srcId, srcName, sourcePort, sourcePhone, destId, destName, destPort, destPhone, fileName, filePath AS filePathLocal " +
                         " FROM vsat_media WHERE partName IN :partNameLst AND uuidKey IN :uuidLst ";
            NativeQuery query = session.createSQLQuery(sql);
            query.setParameterList("partNameLst", inp.getPartNameLst());
            query.setParameterList("uuidLst", inp.getUuidLst());
            query.addScalar("uuidKey", StringType.INSTANCE)
                .addScalar("srcId", LongType.INSTANCE)
                .addScalar("srcName", StringType.INSTANCE)
                .addScalar("sourcePort", StringType.INSTANCE)
                .addScalar("sourcePhone", StringType.INSTANCE)
                .addScalar("destId", LongType.INSTANCE)
                .addScalar("destName", StringType.INSTANCE)
                .addScalar("destPort", StringType.INSTANCE)
                .addScalar("destPhone", StringType.INSTANCE)
                .addScalar("fileName", StringType.INSTANCE)
                .addScalar("filePathLocal", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(MediaRelationDTO.class));
            
            lstMediaRaw = query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            if (session != null && session.isOpen()) {
                session.disconnect();
                session.close();
            }
        }
        
        MessageContent messageContent = new MessageContent();
        messageContent.setData(lstMediaRaw);
        messageContent.setStatus(HttpStatus.OK.value());
        messageContent.setMessage(HttpStatus.OK.toString());
        
        return messageContent;
    }

    public boolean updateReadStatus(List<String> uuidKeys, String updateType) {
        Session session = null;
        try {
            session = this.clickHouseSessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            
            String sql = " ALTER TABLE vsat_media UPDATE status = 1 WHERE uuidKey IN (:uuidKeys) ";
            Query query = session.createNativeQuery(sql)
                                       .setParameterList("uuidKeys", uuidKeys);
            int updateStatus = query.executeUpdate();
            
            // Xử lý update cột statusFrom, statusTo bên bảng vsat_media_relation
            if( "MEDIA_RELATION".equals(updateType) && uuidKeys.size() == 2 ) {
                sql = " ALTER TABLE vsat_media_relation UPDATE statusFrom = 1, statusTo = 1 WHERE uuidKeyFrom = :uuidKeyFrom AND uuidKeyTo = :uuidKeyTo ";
                query = session.createNativeQuery(sql)
                                .setParameter("uuidKeyFrom", uuidKeys.get(0))
                                .setParameter("uuidKeyTo", uuidKeys.get(1));
                query.executeUpdate();
            }
            
            tx.commit();
            return updateStatus > 0;
        } catch (Exception ex) {
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            if (session != null && session.isOpen()) {
                session.disconnect();
                session.close();
            }
        }
        return false;
    }

    public boolean addCommentCommon(AddCommentRequest data){
        Session session = null;
        try {
            session = openSession();
            Transaction tx = session.beginTransaction();
            
            String sql = " INSERT INTO vsat02.comments " +
                        " (comment_type_id, v_comment, v_hagtag, create_user, ref_id) VALUES " +
                        " (:commentType, :content, :hagtag, :userAction, :refId) ";
            NativeQuery query = session.createNativeQuery(sql);
            query.setParameter("content", data.getContent());
            query.setParameter("commentType", data.getCommentTypeId());
            query.setParameter("hagtag", data.getHagtag());
            query.setParameter("refId", data.getRefId());
            query.setParameter("userAction", data.getCreateUser());
            //query.setParameter("created_time",new Date(), TemporalType.TIMESTAMP);
            //query.setParameter("updated_time",new Date(), TemporalType.TIMESTAMP);
            int insertStatus = query.executeUpdate();

            // Update bảng media bên Clickhouse
            /*if( insertStatus > 0 && (data.getCmt_type()==3 || data.getCmt_type()==5) ) {
                sql = " ALTER TABLE vsat.vsat_media UPDATE isComment = 1 where uuidKey = :uuidKey ";
                NativeQuery clQuery = session.createNativeQuery(sql);
                clQuery.setParameter("uuidKey",data.getCategory_id());
                clQuery.executeUpdate();
            }*/
            
            tx.commit();
            return insertStatus > 0;
        } catch (Exception ex) {
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            closeSession(session);
        }
        return false;
    }
    
    public Boolean deleteFileMedia(DeleteFileMediaRequest data){
        Session clSession=this.clickHouseSessionFactory.openSession();
        Boolean bAddcomment=false;
        try {
//                clSession.getTransaction().begin();
//                String clSql=null;
//                if(data.getCmt_type()==3||data.getCmt_type()==5){
//                    clSql+="ALTER TABLE vsat.VSAT_MEDIA UPDATE IS_COMMENT=1 where UUID_KEY=:uuid_key;";
//                }
//
////                else if(data.getCmt_type()==4){
////                    clSql+="";
////                }
//
//                NativeQuery clQuery = clSession.createNativeQuery(clSql);
//                clQuery.setParameter("uuid_key",data.getCategory_id());
//                int resCh=clQuery.executeUpdate();
//                clSession.getTransaction().commit();
            bAddcomment=true;
        } catch (EntityNotFoundException ex) {
            clSession.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(clSession);
        }
        return bAddcomment;
    }
    public void testFindMedia(String startDate, String endDate) {
        String sql = " select * from VSAT_MEDIA " +
                    " where PART_NAME = 202102 and INGEST_TIME >= ? and INGEST_TIME <= ? " +
                    " order by INGEST_TIME desc ";
        try {
            Connection connection = this.clDatasource.getConnection();
            
            long startTime = System.currentTimeMillis();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, startDate);
            statement.setString(2, endDate);
            ResultSet rs = statement.executeQuery();
            System.out.println("elapseTimeFetchFromDB: "+getElapsedTime(System.currentTimeMillis() - startTime)+" ms");
            
            List<String> lst = new ArrayList<>();
            long startTime2 = System.currentTimeMillis();
            while( rs.next() ) {
                //lst.add(rs.getString("MEDIA_TYPE_NAME"));
            }
            System.out.println("lst.size: "+lst.size()+", elapseTimeConvertToList: "
                    + getElapsedTime(System.currentTimeMillis() - startTime2)+" ms");
            
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        }
    }
    
    public void testFindMedia2(String startDate, String endDate) {
        Session session = this.clickHouseSessionFactory.openSession();
//        Session session = obtainSession(this.clickHouseSessionFactory);
        try {
            String sql = " select * from VSAT_MEDIA " +
                        " where PART_NAME = 202102 and INGEST_TIME >= ? and INGEST_TIME <= ? " +
                        " order by INGEST_TIME desc ";

                long startTime2 = System.currentTimeMillis();
                NativeQuery query = session.createSQLQuery(sql)
                                           .setParameter(1, startDate)
                                           .setParameter(2, endDate);
                List lst = query.getResultList();
                System.out.println("lst.size: "+lst.size()+", elapseTimeConvertToList: "
                        + getElapsedTime(System.currentTimeMillis() - startTime2)+" ms");
            
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            if (session != null && session.isOpen()) {
                session.disconnect();
                session.close();
            }
        }
    }
    
//    public void testFindAis(String startDate, String endDate) {
//        Session session = this.clickHouseSessionFactory.openSession();
////        Session session = obtainSession(this.clickHouseSessionFactory);
//        try {
//            String sql = " select dai.NAME as vesselName, va.COUNTRY_ID as countryId, va.TIMEKEY as timeKey, va.INGEST_TIME as ingestTime " +
//                         " from VSAT_AIS va inner join DIM_AIS_INFO dai on va.MMSI = dai.MMSI " +
//                         " where va.PARTNAME = 20210208 and dai.NAME <> '' and va.INGEST_TIME >= ? and va.INGEST_TIME <= ? " +
//                         " order by va.EVENT_TIME desc ";
//
//                long startTime2 = System.currentTimeMillis();
//                NativeQuery query = session.createSQLQuery(sql);
//                query.setParameter(1, startDate)
//                     .setParameter(2, endDate)
//                     .addScalar("vesselName", StringType.INSTANCE)
//                     .addScalar("countryId", IntegerType.INSTANCE)
//                     .addScalar("timeKey", BigDecimalType.INSTANCE)
//                     .addScalar("ingestTime", org.hibernate.type.TimestampType.INSTANCE)
//                     .setFirstResult(1)
//                     .setMaxResults(14)
//                     .setResultTransformer(Transformers.aliasToBean(AisJoinTest.class));
//                List lst = query.getResultList();
//                System.out.println("lst.size: "+lst.size()+", elapseTimeConvertToList: "
//                        + getElapsedTime(System.currentTimeMillis() - startTime2)+" ms");
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            LOGGER.error(StringUtil.printException(ex));
//        } finally {
//            if (session != null && session.isOpen()) {
//                session.disconnect();
//                session.close();
//            }
//        }
//    }
    
//    public static void main(String[] args) {
//        try {
//            BlockingQueue<String> queue = new LinkedBlockingDeque<>();
//            generateRandomIp(50000, queue);
//            System.out.println("BLockingQueue: " + queue);
//        } catch (Exception e) {
//            e.getStackTrace();
//        }
//    }
    
//    public void testInsertAis(BlockingQueue<String> ipQueue, Map<String, String> mapMssiWithIp
//                            , Map<String, String> mapMssiWithLongLat) {
//        Session session = this.clickHouseSessionFactory.openSession();
//        Transaction tx = session.beginTransaction();
//        try {
//            int numOfMmsi = 100000;
//            long currentTime = 0;
//            DateFormat dateFormatPartName = new SimpleDateFormat("yyyyMMdd");
//            DateFormat dateFormatTimeKey = new SimpleDateFormat("yyyyMMddHHmm");
//            AisMessage item;
//            Date date;
//            for( int i=1; i<=numOfMmsi; i++ ) {
//                currentTime = System.currentTimeMillis() - (86400000 * 2);
//                date = new Date(currentTime);
//                item = new AisMessage(i+"", new Long(412), new Float(121.654), new Float(38.94803)
//                        , i+"", new Long(1), new Long(6), 0L, null
//                        , new Long(1), "", "", System.nanoTime()+"");
//
//                if( !mapMssiWithIp.containsKey(i+"") ) {
//                    String ipVal = ipQueue.take() + "_" + ipQueue.take();
//                    mapMssiWithIp.put(i+"", ipVal);
//                    item.setSourceIp(ipVal.split("_")[0]);
//                    item.setDestIp(ipVal.split("_")[1]);
//                }else {
//                    String ipVal = mapMssiWithIp.get(i+"");
//                    item.setSourceIp(ipVal.split("_")[0]);
//                    item.setDestIp(ipVal.split("_")[1]);
//                }
//
//                if( !mapMssiWithLongLat.containsKey(i+"") ) {
//                    Float longitude = new Float(121.654);
//                    Float latitude = new Float(38.94803);
//                    String longLat =  longitude + "_" + latitude;
//                    mapMssiWithLongLat.put(i+"", longLat);
//                    item.setLongitude(longitude);
//                    item.setLatitude(latitude);
//                }else {
//                    String longLatOld = mapMssiWithLongLat.get(i+"");
//                    Float longitude = new Float(longLatOld.split("_")[0]) + new Float(0.05);
//                    Float latitude = new Float(longLatOld.split("_")[1]) + new Float(0.0005);
//                    item.setLongitude(longitude);
//                    item.setLatitude(latitude);
//                    String longLat =  longitude + "_" + latitude;
//                    mapMssiWithLongLat.put(i+"", longLat);
//                }
//
//                item.setDataSource(ThreadLocalRandom.current().nextLong(1, 9));
//
//                item.setEventTime(new Timestamp(currentTime-20000));
//                item.setProcTime(new Timestamp(currentTime-10000));
//                item.setIngestTime(new Timestamp(currentTime));
//                item.setPartName(new Long(dateFormatPartName.format(date)));
//                item.setTimeKey(new BigDecimal(dateFormatTimeKey.format(date)));
//
//                session.save(item);
//                if (i % 500 == 0) {
//                    session.flush();
//                    session.clear();
//                }
//            }
//            tx.commit();
//            LOGGER.info("insert OK!");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            LOGGER.error(StringUtil.printException(ex));
//        } finally {
//            if ( session.isOpen() ) {
//                session.disconnect();
//                session.close();
//            }
//        }
//    }
    
//    public void testInsertMedia(BlockingQueue<String> ipQueue) {
//        Session session = this.clickHouseSessionFactory.openSession();
//        Transaction tx = session.beginTransaction();
//        try {
//            int numOfMedia = 500000;
//            long currentTime = 0;
//            DateFormat dateFormatPartName = new SimpleDateFormat("yyyyMM");
//            DateFormat dateFormatDateKey = new SimpleDateFormat("yyyyMMdd");
//            MediaRaw item;
//            Date date;
//
//            List<Integer> mmsiLst = NumberUtils.generateIntArray(500000, 1, 500000);
//
//            LinkedHashMap<Long, String> mediaTypeMaps = new LinkedHashMap<>();
//            mediaTypeMaps.put(1L, "AUDIO");
//            mediaTypeMaps.put(2L, "VIDEO");
//            mediaTypeMaps.put(3L, "WEB");
//            mediaTypeMaps.put(4L, "EMAIL");
//            mediaTypeMaps.put(5L, "TRANFER FILE");
//            mediaTypeMaps.put(8L, "UNDEFINED");
//
//            for( int i=1; i<=numOfMedia; i++ ) {
//                currentTime = System.currentTimeMillis() - 86400000;
//                date = new Date(currentTime);
//                String mediaTypeName = (new ArrayList<>(mediaTypeMaps.values())).get(new Random().nextInt(6));
//
//                String sourceMmsi = NumberUtils.getRandomIntFromArray(mmsiLst)+"";
//                String destMmsi = NumberUtils.getRandomIntFromArray(mmsiLst)+"";
//                item = new MediaRaw((Long) NumberUtils.getKeysFromValue(mediaTypeMaps, mediaTypeName).get(0), mediaTypeName, ipQueue.take(), ipQueue.take()
//                        , sourceMmsi, destMmsi
//                        , "/ttttbien2/vsat/media_files/undefined/20210301/15/44/" + StringUtil.getRandomSaltString() + ".mp4"
//                        , new BigDecimal(12720), 0L, 0L, null);
//
//                item.setDataSource(ThreadLocalRandom.current().nextLong(1, 9));
//
//                item.setEventTime(new Timestamp(currentTime-20000));
//                item.setProcTime(new Timestamp(currentTime-10000));
//                item.setIngestTime(new Timestamp(currentTime));
//                item.setPartName(new Long(dateFormatPartName.format(date)));
//                item.setDateKey(new BigDecimal(dateFormatDateKey.format(date)));
//
//                session.save(item);
//                if (i % 500 == 0) {
//                    session.flush();
//                    session.clear();
//                }
//            }
//            tx.commit();
//            LOGGER.info("insert OK!");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            LOGGER.error(StringUtil.printException(ex));
//        } finally {
//            if ( session.isOpen() ) {
//                session.disconnect();
//                session.close();
//            }
//        }
//    }
    
//    private Session obtainSession(SessionFactory sessionFactory) {
//        Session session;
//        try {
//            session = sessionFactory.getCurrentSession();
//            LOGGER.info("getSessionFromCurrentSession");
//        } catch (Exception e) {
//            session = sessionFactory.openSession();
//            LOGGER.info("getSessionFromOpenSession");
//        }
//        return session;
//    }
    
    private String getElapsedTime(long miliseconds) {
        return miliseconds + " (ms)";
    }
}
