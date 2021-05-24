package com.elcom.contact.repository.media;

import com.elcom.gateway.message.MessageContent;

import com.elcom.contact.model.DataRawJDBC;
import com.elcom.contact.model.dto.request.media.AddCommentRequest;
import com.elcom.contact.model.dto.request.media.DeleteFileMediaRequest;
import com.elcom.contact.model.dto.request.media.GetInfoByMediaIdRequest;
import com.elcom.contact.model.dto.request.media.UpdateStatusByMediaIdRequest;
import com.elcom.contact.model.media.MediaRaw;
import com.elcom.contact.repository.BaseRepository;
import com.elcom.contact.utils.StringUtil;

import com.elcom.vsat.model.MediaType;


import com.elcom.vsat.model.dto.controller.request.media.SearchListMediaRequest;

import com.elcom.vsat.repository.Utils.NamedParameterStatement;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;

import static com.elcom.contact.constant.Constant.PARTITION_TYPE_MONTH;
import static com.elcom.contact.utils.DateUtil.getSListPartitionBetweenTwoDates;


@Repository
public class MediaRepository extends BaseRepository {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaRepository.class);

    @Autowired
    @Qualifier("clickHouseSession")
    SessionFactory sessionFactory;
    
    @Autowired
    public MediaRepository(@Qualifier("vsatEntityManagerFactory") EntityManagerFactory factory, @Qualifier("vsatChDataSource") DataSource dataSource) {
        super(factory,dataSource);
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

    public List<MediaType> getListMediaTypes(){
        Session session = openSession();
        List<MediaType> lstMediaType=null;
        try {
            Query query = session.createNativeQuery("SELECT \"ID\", \"NAME\" FROM public.\"MEDIA_TYPE\"");
            lstMediaType = (List<MediaType>)query.getResultList();
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return lstMediaType != null ? lstMediaType : null;
    }

    public MessageContent searchMedia(SearchListMediaRequest data){
        List<DataRawJDBC> lstDataRawJDBC =new ArrayList<>();
        try (
                Connection connection = this.clDatasource.getConnection();
                ){
            String sLstPartition=getSListPartitionBetweenTwoDates(data.getStart_time(),data.getEnd_time(),PARTITION_TYPE_MONTH);
            String sql="select * from VSAT_MEDIA vm where 1=1 ";
            if(data.getStart_time()!=null&&data.getEnd_time()!=null){
               sql+=" and EVENT_TIME >= :start_time";
               sql+=" and EVENT_TIME <= :end_time";
               if(!sLstPartition.isEmpty()){
                   sql+=" and PART_NAME IN (:listPartition)";
               }
            }
            if(data.getMedia_type()!=null){
                sql+=" and MEDIA_TYPE_ID = :media_type_id";
            }
            if(data.getSource_ip()!=null){
                sql+=" and SOURCE_IP= :source_ip";
            }
            if(data.getDest_ip()!=null){
                sql+=" and DEST_IP= :dest_ip";
            }
            if(data.getRows_end()!=null&&data.getRows_start()!=null) {
                sql += " LIMIT :start_row,:end_row";
            }else{
                sql += " LIMIT 0,100";
            }

            NamedParameterStatement statement = new NamedParameterStatement(connection,sql);
            if(data.getStart_time()!=null && data.getEnd_time()!=null){
                statement.setDate("start_time",new java.sql.Date(data.getStart_time().getTime()));
                statement.setDate("end_time",new java.sql.Date(data.getEnd_time().getTime()));
                if(!sLstPartition.isEmpty()){
                    statement.setObject("listPartition",sLstPartition.split(","));
                }
            }
            if(data.getMedia_type()!=null){
                statement.setString("media_type_id",data.getMedia_type());
            }
            if(data.getSource_ip()!=null){
                statement.setString("source_ip",data.getSource_ip());
            }
            if(data.getDest_ip()!=null){
                statement.setString("dest_ip",data.getDest_ip());
            }
            if(data.getRows_end()!=null&&data.getRows_start()!=null) {
                statement.setInt("start_row",Integer.parseInt(data.getRows_start()));
                statement.setInt("end_row",Integer.parseInt(data.getRows_end()));
            }
//            PreparedStatement statement= connection.prepareStatement("select * from VSAT_MEDIA vm where vm.PROC_TIME <now() and 1=:a LIMIT 0,1000000");

//            statement.setString(1,"1");
            ResultSet rs = statement.executeQuery();
            LOGGER.error(new Date().toString());

            while (rs.next()) {
                DataRawJDBC dataRawJDBC =new DataRawJDBC();
                dataRawJDBC.setUuid(rs.getString("UUID_KEY"));
                dataRawJDBC.setEvent_time(rs.getDate("EVENT_TIME"));
                dataRawJDBC.setProc_time(rs.getDate("PROC_TIME"));
                dataRawJDBC.setMedia_type(rs.getInt("MEDIA_TYPE_ID"));
                dataRawJDBC.setMedia_type_name(rs.getString("MEDIA_TYPE_NAME"));

                dataRawJDBC.setSource_ip(rs.getString("SOURCE_IP"));
                dataRawJDBC.setSrc_mmsi(rs.getString("SRC_MMSI"));
                dataRawJDBC.setSrc_is_hq(rs.getString("SRC_IS_HQ"));
                dataRawJDBC.setSrc_country_id(rs.getInt("SRC_COUNTRY_ID"));
                dataRawJDBC.setSrc_type_id(rs.getInt("SRC_TYPE_ID"));
                dataRawJDBC.setSrc_name(rs.getString("SRC_NAME"));
                dataRawJDBC.setSrc_extra(rs.getString("SRC_EXTRA"));
                dataRawJDBC.setSource_port(rs.getString("SOURCE_PORT"));
                dataRawJDBC.setSource_phone(rs.getString("SOURCE_PHONE"));
                dataRawJDBC.setDest_ip(rs.getString("DEST_IP"));
                dataRawJDBC.setDest_mmsi(rs.getString("DEST_MMSI"));
                dataRawJDBC.setDest_is_hq(rs.getInt("DEST_IS_HQ"));

                dataRawJDBC.setDest_is_hq(rs.getInt("DEST_IS_HQ"));
                dataRawJDBC.setDest_country_id(rs.getInt("DEST_COUNTRY_ID"));
                dataRawJDBC.setDest_type_id(rs.getInt("DEST_TYPE_ID"));
                dataRawJDBC.setDest_name(rs.getString("DEST_NAME"));
                dataRawJDBC.setDest_extra(rs.getString("DEST_EXTRA"));
                dataRawJDBC.setDest_port(rs.getString("DEST_PORT"));
                dataRawJDBC.setDest_phone(rs.getString("DEST_PHONE"));
                dataRawJDBC.setFile_path(rs.getString("FILE_PATH"));
                dataRawJDBC.setFileSize(rs.getLong("FILESIZE"));
                dataRawJDBC.setData_source(rs.getInt("DATA_SOURCE"));
                dataRawJDBC.setPart_name(rs.getInt("PART_NAME"));
                dataRawJDBC.setDate_key(rs.getInt("DATE_KEY"));
                dataRawJDBC.setIngest_time(rs.getDate("INGEST_TIME"));
                dataRawJDBC.setStatus(rs.getInt("STATUS"));
//                dataRaw.setDirection(rs.getInt("DIRECTION"));

                lstDataRawJDBC.add(dataRawJDBC);
            }
            LOGGER.error(new Date().toString());
            LOGGER.error("ex.toString()");
        }
        catch(Exception ex){
            LOGGER.error(ex.toString());
        }
        MessageContent messageContent=new MessageContent();
        messageContent.setData(lstDataRawJDBC);
        messageContent.setTotal(lstDataRawJDBC.stream().count());
        return messageContent;
    }

    public MediaRaw getInfoByMediaId(GetInfoByMediaIdRequest data){
        Session session = this.sessionFactory.openSession();
        try {
            NativeQuery query = session.createNativeQuery("SELECT * FROM vsat.VSAT_MEDIA where UUID_KEY=:id", MediaRaw.class);
            query.setParameter("id",data.getId());
            MediaRaw mediaRaw= (MediaRaw)query.getSingleResult();
            return mediaRaw;
        } catch (EntityNotFoundException ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return null;
    }

    public Boolean updatestatus(UpdateStatusByMediaIdRequest data){
        Session session = this.sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            NativeQuery query = session.createNativeQuery("ALTER TABLE vsat.VSAT_MEDIA UPDATE STATUS=1 where UUID_KEY=:id");
            query.setParameter("id",data.getId());
            int i=query.executeUpdate();
            session.getTransaction().commit();
            if(i>0){
                return true;
            }
        } catch (EntityNotFoundException ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {

            closeSession(session);
        }
        return false;
    }

    public Boolean addCommentCommon(AddCommentRequest data){
        Session session = openSession();
        Session clSession=this.sessionFactory.openSession();
        Boolean bAddcomment=false;
        try {
            session.getTransaction().begin();
            NativeQuery query = session.createNativeQuery("INSERT INTO vsat02.comments\n" +
                    "    ( comment_type_id, ref_id, v_comment, v_hagtag, create_user, update_user, created_time, updated_time) VALUES\n" +
                    "    ( :comment_type_id, :ref_id, :v_comment, :v_hagtag, :create_user, :update_user, :created_time, :updated_time);");
            query.setParameter("v_comment",data.getComment());
            query.setParameter("comment_type_id",data.getCmt_type());
            query.setParameter("v_hagtag",data.getHagtag());
            query.setParameter("ref_id",data.getCategory_id());
            query.setParameter("create_user","");
            query.setParameter("update_user",null);
            query.setParameter("created_time",new Date());
            query.setParameter("updated_time",null);

            int i=query.executeUpdate();

            if(i>0){
                clSession.getTransaction().begin();
                String clSql=null;
                if(data.getCmt_type()==3||data.getCmt_type()==5){
                    clSql+="ALTER TABLE vsat.VSAT_MEDIA UPDATE IS_COMMENT=1 where UUID_KEY=:uuid_key;";
                }

//                else if(data.getCmt_type()==4){
//                    clSql+="";
//                }

                NativeQuery clQuery = clSession.createNativeQuery(clSql);
                clQuery.setParameter("uuid_key",data.getCategory_id());
                int resCh=clQuery.executeUpdate();
                clSession.getTransaction().commit();
            }
            session.getTransaction().commit();
            bAddcomment=true;
        } catch (EntityNotFoundException ex) {
            session.getTransaction().rollback();
            clSession.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(clSession);
            closeSession(session);
        }
        return bAddcomment;
    }

    public Boolean deleteFileMedia(DeleteFileMediaRequest data){
        Session clSession=this.sessionFactory.openSession();
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
        Session session = this.sessionFactory.openSession();
//        Session session = obtainSession(this.sessionFactory);
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
//        Session session = this.sessionFactory.openSession();
////        Session session = obtainSession(this.sessionFactory);
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
    
    private void generateRandomIp(int numOfIp, BlockingQueue<String> queue) {
        Random r = new Random();
        String ip = "";
        for( int i=0; i<numOfIp; i++ ) {
            try {
                ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
                if( !queue.contains(ip) )
                    queue.put(ip);
                else {
//                    System.out.println("DUPLICATED Level_1");
                    ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
                    queue.put(ip);
                    if( !queue.contains(ip) )
                        queue.put(ip);
                    else {
//                        System.out.println("DUPLICATED Level_2");
                        ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
                        queue.put(ip);
                        if( !queue.contains(ip) )
                            queue.put(ip);
                        else {
//                            System.out.println("DUPLICATED Level_3");
                            ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
                            queue.put(ip);
                            if( !queue.contains(ip) )
                                queue.put(ip);
                            else {
//                                System.out.println("DUPLICATED Level_4");
                                ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
                                queue.put(ip);
                                if( !queue.contains(ip) )
                                    queue.put(ip);
                                else {
//                                    System.out.println("DUPLICATED Level_5");
                                    ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
                                    queue.put(ip);
                                    if( !queue.contains(ip) )
                                        queue.put(ip);
                                    else {
//                                        System.out.println("DUPLICATED Level_6");
                                        ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
                                        queue.put(ip);
                                        if( !queue.contains(ip) )
                                            queue.put(ip);
                                        else {
//                                            System.out.println("DUPLICATED Level_7");
                                            ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
                                            queue.put(ip);
                                            if( !queue.contains(ip) )
                                                queue.put(ip);
                                            else {
//                                                System.out.println("DUPLICATED Level_8");
                                                ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
                                                queue.put(ip);
                                                if( !queue.contains(ip) )
                                                    queue.put(ip);
                                                else {
//                                                    System.out.println("DUPLICATED Level_9");
                                                    ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
                                                    queue.put(ip);
                                                    if( !queue.contains(ip) )
                                                        queue.put(ip);
                                                    else {
//                                                        System.out.println("DUPLICATED Level_10");
                                                        queue.put(r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.getStackTrace();
            }
        }
    }
    
//    public void testInsertAis(BlockingQueue<String> ipQueue, Map<String, String> mapMssiWithIp
//                            , Map<String, String> mapMssiWithLongLat) {
//        Session session = this.sessionFactory.openSession();
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
//        Session session = this.sessionFactory.openSession();
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
