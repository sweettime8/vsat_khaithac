package com.elcom.media.repository.media;

import com.elcom.gateway.message.MessageContent;
import com.elcom.media.constant.Constant;
import com.elcom.media.model.dto.request.media.SearchListMediaRequest;
import com.elcom.media.model.media.MediaRelation;
import com.elcom.media.model.dto.media.MediaRelationDto;
import com.elcom.media.repository.BaseRepository;
import com.elcom.media.utils.DateUtil;
import com.elcom.media.utils.MediaUtils;
import com.elcom.media.utils.StringUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TemporalType;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

@Repository
public class MediaRelationRepository extends BaseRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MediaRelationRepository.class);

    @Autowired
    @Qualifier("clickHouseSession")
    SessionFactory clickHouseSessionFactory;

    @Autowired
    public MediaRelationRepository(@Qualifier("vsatEntityManagerFactory") EntityManagerFactory factory, @Qualifier("vsatChDataSource") DataSource dataSource) {
        super(factory, dataSource);
    }

    public MessageContent searchMediaRelation(SearchListMediaRequest data) {
        List<MediaRelation> lstMediaRaw = new ArrayList<>();
        Session session = this.clickHouseSessionFactory.openSession();
        long count = 0;
        
        Pageable pageable = PageRequest.of(data.getCurrentPage(), data.getRowsPerPage());
        
        try {
            String sqlWhere = "";
            String sql = " SELECT * FROM vsat_media_relation WHERE ((eventTimeFrom >= toDateTime(:start_time) AND eventTimeFrom <= toDateTime(:end_time)) OR (eventTimeTo >= toDateTime(:start_time) AND eventTimeTo <= toDateTime(:end_time))) AND (partNameFrom IN (:listPartitionFrom) OR partNameTo IN (:listPartitionTo)) ";
            String sqlCount=" SELECT COUNT(*) FROM vsat_media_relation WHERE ((eventTimeFrom >= toDateTime(:start_time) AND eventTimeFrom <= toDateTime(:end_time)) OR (eventTimeTo >= toDateTime(:start_time) AND eventTimeTo <= toDateTime(:end_time))) AND (partNameFrom IN (:listPartitionFrom) OR partNameTo IN (:listPartitionTo)) ";
            
            if( data.getDataSource()!=null && !data.getDataSource().isEmpty() )
                sqlWhere+=" AND (dataSourceFrom IN :data_source OR dataSourceTo IN :data_source) ";
            
            if( data.getMediaType()!=null && !data.getMediaType().isEmpty() )
                sqlWhere+=" AND (mediaTypeIdFrom IN :media_type OR mediaTypeIdTo IN :media_type) ";
            
            if( !StringUtil.isNullOrEmpty(data.getSourceIps()) )
                sqlWhere+=" AND (sourceIpFrom IN (:source_ips) OR sourceIpTo IN (:source_ips)) ";
            
            if( !StringUtil.isNullOrEmpty(data.getDestIps()) )
                sqlWhere+=" AND (destIpFrom IN (:dest_ips) OR destIpTo IN (:dest_ips)) ";
            
            sqlCount = sqlCount + sqlWhere;
            sql = sql + sqlWhere + " ORDER BY ingestTime DESC ";
            
            NativeQuery query = session.createNativeQuery(sql, MediaRelation.class);
            NativeQuery queryCount = session.createNativeQuery(sqlCount);
            
            query.setParameter("start_time",data.getStartTime(),TemporalType.TIMESTAMP);
            query.setParameter("end_time",data.getEndTime(),TemporalType.TIMESTAMP);

            queryCount.setParameter("start_time",data.getStartTime(),TemporalType.TIMESTAMP);
            queryCount.setParameter("end_time",data.getEndTime(),TemporalType.TIMESTAMP);

            List<Long> sLstPartition = DateUtil.getPartitionNameByTwoDate(data.getStartTime(), data.getEndTime(), Constant.PARTITION_TYPE_DAY);
            query.setParameterList("listPartitionFrom", sLstPartition);
            queryCount.setParameterList("listPartitionFrom", sLstPartition);
            query.setParameterList("listPartitionTo", sLstPartition);
            queryCount.setParameterList("listPartitionTo", sLstPartition);
            
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
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            lstMediaRaw = query.getResultList();
            count = ((Number) queryCount.getSingleResult()).intValue();
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
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
    
    public Object setMediaRelation(){
        Session session=this.clickHouseSessionFactory.openSession();
        int diffFortwoMessage=10;
        try {
            session.getTransaction().begin();

            int partition = DateUtil.getPartitionByDate(new Date());
            NativeQuery query = session.createNativeQuery("SELECT * from (SELECT \n" +
                    "toUUID(vmFrom.uuidKey) as uuidKeyFrom, \n" +
                    "vmFrom.eventTime as eventTimeFrom, \n" +
                    "vmFrom.procTime as procTimeFrom, \n" +
                    "vmFrom.mediaTypeId as mediaTypeIdFrom, \n" +
                    "vmFrom.mediaTypeName as mediaTypeNameFrom, \n" +
                    "vmFrom.direction as directionFrom, \n" +
                    "vmFrom.status as statusFrom, \n" +
                    "vmFrom.fileSize as fileSizeFrom, \n" +
                    "vmFrom.filePath as filePathFrom, \n" +
                    "vmFrom.dataSource as dataSourceFrom, \n" +
                    "vmFrom.sourceIp as sourceIpFrom, \n" +
                    "vmFrom.destIp as destIpFrom, \n" +
                    "vmFrom.partName as partNameFrom, \n" +
                    "\n" +
                    "toUUID(vmTo.uuidKey) as uuidKeyTo, \n" +
                    "vmTo.eventTime as eventTimeTo, \n" +
                    "vmTo.procTime as procTimeTo, \n" +
                    "vmTo.mediaTypeId as mediaTypeIdTo, \n" +
                    "vmTo.mediaTypeName as mediaTypeNameTo, \n" +
                    "vmTo.direction as directionTo, \n" +
                    "vmTo.status as statusTo, \n" +
                    "vmTo.fileSize as fileSizeTo, \n" +
                    "vmTo.filePath as filePathTo, \n" +
                    "vmTo.dataSource as dataSourceTo, \n" +
                    "vmTo.sourceIp as sourceIpTo, \n" +
                    "vmTo.destIp as destIpTo, \n" +
                    "vmTo.partName as partNameTo \n" +
                    "\n" +
                    "FROM vsat.vsat_media as vmFrom\n" +
                    "INNER JOIN vsat.vsat_media as vmTo \n" +
                    "on vmFrom.sourceIp =vmTo.destIp \n" +
                    "and vmFrom.destIp =vmTo.sourceIp \n" +
                    "Where (vmFrom.eventTime - vmTo.eventTime) <= :diffFortwoMessage AND (vmFrom.eventTime - vmTo.eventTime) >=0 " +
                    "and (vmFrom.mediaTypeId =1 or vmFrom.mediaTypeId =2) \n" +
                    "and (vmTo.mediaTypeId =1 or vmTo.mediaTypeId =2)" +
                    "and vmFrom.eventTime >=:eventTime and vmTo.eventTime >=:eventTime) as fulla \n" +
                    "WHERE fulla.uuidKeyFrom NOT IN \n" +
                    "(\n" +
                    "select uuidKeyFrom from \n" +
                    "(SELECT \n" +
                    "toUUID(vmFrom.uuidKey) as uuidKeyFrom, \n" +
                    "vmFrom.eventTime as eventTimeFrom, \n" +
                    "vmFrom.procTime as procTimeFrom, \n" +
                    "vmFrom.mediaTypeId as mediaTypeIdFrom, \n" +
                    "vmFrom.mediaTypeName as mediaTypeNameFrom, \n" +
                    "vmFrom.direction as directionFrom, \n" +
                    "vmFrom.status as statusFrom, \n" +
                    "vmFrom.fileSize as fileSizeFrom, \n" +
                    "vmFrom.filePath as filePathFrom, \n" +
                    "vmFrom.dataSource as dataSourceFrom, \n" +
                    "vmFrom.sourceIp as sourceIpFrom, \n" +
                    "vmFrom.destIp as destIpFrom, \n" +
                    "vmFrom.partName as partNameFrom, \n" +
                    "\n" +
                    "toUUID(vmTo.uuidKey) as uuidKeyTo, \n" +
                    "vmTo.eventTime as eventTimeTo, \n" +
                    "vmTo.procTime as procTimeTo, \n" +
                    "vmTo.mediaTypeId as mediaTypeIdTo, \n" +
                    "vmTo.mediaTypeName as mediaTypeNameTo, \n" +
                    "vmTo.direction as directionTo, \n" +
                    "vmTo.status as statusTo, \n" +
                    "vmTo.fileSize as fileSizeTo, \n" +
                    "vmTo.filePath as filePathTo, \n" +
                    "vmTo.dataSource as dataSourceTo, \n" +
                    "vmTo.sourceIp as sourceIpTo, \n" +
                    "vmTo.destIp as destIpTo, \n" +
                    "vmTo.partName as partNameTo \n" +
                    "\n" +
                    "FROM vsat.vsat_media as vmFrom\n" +
                    "INNER JOIN vsat.vsat_media as vmTo \n" +
                    "on vmFrom.sourceIp =vmTo.destIp \n" +
                    "Where (vmFrom.eventTime - vmTo.eventTime) <= :diffFortwoMessage AND (vmFrom.eventTime - vmTo.eventTime) >=0 " +
                    "and (vmFrom.mediaTypeId =1 or vmFrom.mediaTypeId =2) \n" +
                    "and (vmTo.mediaTypeId =1 or vmTo.mediaTypeId =2)" +
                    "and vmFrom.eventTime >=:eventTime and vmTo.eventTime >=:eventTime ) \n" +
                    "as rs\n" +
                    "INNER JOIN vsat.vsat_media_relation vmr \n" +
                    "on vmr.uuidKeyFrom = rs.uuidKeyFrom AND vmr.uuidKeyTo =rs.uuidKeyTo\n" +
                    "AND vmr.eventTimeTo =rs.eventTimeTo \n" +
                    "AND vmr.destIpFrom =rs.destIpFrom AND vmr.destIpTo =rs.destIpTo\n" +
                    "AND vmr.eventTimeTo =rs.eventTimeTo \n" +
                    ")\n",MediaRelationDto.class);
            Date dateQuery=DateUtil.getDateByHourAgo(new Date(),8);
            query.setParameter("eventTime", dateQuery,TemporalType.TIMESTAMP);
            query.setParameter("diffFortwoMessage",diffFortwoMessage);
//            query.setParameter("partName", DateUtil.getPartitionByDate(dateQuery));
            List<MediaRelationDto> lstMediaRelationDto=query.getResultList();
            List<MediaRelationDto> lstMediaRelationDtoProcess=new ArrayList<>();
            List<MediaRelationDto> lstMediaRelationDtoProcessed=new ArrayList<>();
            List<String> lstUUIDAdd=new ArrayList<String>();
            for (MediaRelationDto media:lstMediaRelationDto) {
                if(lstUUIDAdd.indexOf(media.getUuidKeyFrom()+media.getUuidKeyTo())<0){
                    lstUUIDAdd.add(media.getUuidKeyFrom()+media.getUuidKeyTo());
                    lstUUIDAdd.add(media.getUuidKeyTo()+media.getUuidKeyFrom());
                    lstMediaRelationDtoProcess.add(media);
                }
            }
            for(MediaRelationDto media:lstMediaRelationDtoProcess) {
                try {
                    NativeQuery _query = session.createNativeQuery("SELECT * FROM vsat.vsat_media_relation where \n" +
                            "(uuidKeyFrom = :uuidKeyFrom AND uuidKeyTo=:uuidKeyTo)\n" +
                            "OR\n" +
                            "(uuidKeyTo = :uuidKeyFrom AND uuidKeyFrom=:uuidKeyTo)", MediaRelation.class);
                    _query.setParameter("uuidKeyFrom",media.getUuidKeyFrom());
                    _query.setParameter("uuidKeyTo",media.getUuidKeyTo());
                    List<MediaRelation> lstMediaRelation = _query.getResultList();
                    if(lstMediaRelation.size()==0){
                        lstMediaRelationDtoProcessed.add(media);
                    }
                }
                catch (Exception e){
                    LOGGER.error(e.getMessage());
                }
            }
            if(lstMediaRelationDtoProcessed.size()>0) {
                session.doWork(new Work() {
                    @Override
                    public void execute(Connection conn) throws SQLException {
                        PreparedStatement pstmt = null;
                        try {
                            String sqlInsert = "INSERT INTO vsat.vsat_media_relation " +
                                    " (uuid, uuidKeyFrom, eventTimeFrom, mediaTypeIdFrom, mediaTypeNameFrom, directionFrom, " +
                                    " statusFrom, fileSizeFrom, sourceIpFrom, destIpFrom, partNameFrom, uuidKeyTo, eventTimeTo, " +
                                    " mediaTypeIdTo, mediaTypeNameTo, directionTo, statusTo, fileSizeTo, sourceIpTo, destIpTo, " +
                                    " partNameTo,partName,dataSourceFrom,dataSourceTo, ingestTime) " +
                                    " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                                    " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?, ?) ";
                            pstmt = conn.prepareStatement(sqlInsert);
                            int i = 0;
                            Timestamp currentTs = new Timestamp(System.currentTimeMillis());
                            for (MediaRelationDto mediaRealion : lstMediaRelationDtoProcessed) {
                                pstmt.setString(1, UUID.randomUUID().toString());
                                pstmt.setString(2, mediaRealion.getUuidKeyFrom());
                                pstmt.setTimestamp(3, mediaRealion.getEventTimeFrom());
                                pstmt.setInt(4, mediaRealion.getMediaTypeIdFrom());
                                pstmt.setString(5, mediaRealion.getMediaTypeNameFrom());
                                pstmt.setInt(6, mediaRealion.getDirectionFrom());
                                pstmt.setInt(7, mediaRealion.getStatusFrom());
                                pstmt.setInt(8, mediaRealion.getFileSizeFrom());
                                pstmt.setString(9, mediaRealion.getSourceIpFrom());
                                pstmt.setString(10, mediaRealion.getDestIpFrom());
                                pstmt.setLong(11, mediaRealion.getPartNameFrom());


                                pstmt.setString(12, mediaRealion.getUuidKeyTo().toString());
                                pstmt.setTimestamp(13, mediaRealion.getEventTimeTo());
                                pstmt.setInt(14, mediaRealion.getMediaTypeIdTo());
                                pstmt.setString(15, mediaRealion.getMediaTypeNameTo());
                                pstmt.setInt(16, mediaRealion.getDirectionTo());
                                pstmt.setInt(17, mediaRealion.getStatusTo());
                                pstmt.setInt(18, mediaRealion.getFileSizeTo());
                                pstmt.setString(19, mediaRealion.getSourceIpTo());
                                pstmt.setString(20, mediaRealion.getDestIpTo());
                                pstmt.setLong(21, mediaRealion.getPartNameTo());

                                //partition
                                pstmt.setInt(22, partition);

                                //datasourFrom
                                pstmt.setInt(23, mediaRealion.getDataSourceFrom());
                                //data src to
                                pstmt.setInt(24, mediaRealion.getDataSourceTo());
                                
                                pstmt.setTimestamp(25, currentTs);

                                if( mediaRealion.getFilePathFrom().trim().endsWith(".ts") )
                                    if( StringUtil.isNullOrEmpty(MediaUtils.generateM3u8ByTs(mediaRealion.getFilePathFrom())) )
                                        LOGGER.error("generateM3u8ByTs error, file: [{}]", mediaRealion.getFilePathFrom());
                                
                                if( mediaRealion.getFilePathTo().trim().endsWith(".ts") )
                                    if( StringUtil.isNullOrEmpty(MediaUtils.generateM3u8ByTs(mediaRealion.getFilePathTo())) )
                                        LOGGER.error("generateM3u8ByTs error, file: [{}]", mediaRealion.getFilePathTo());

                                pstmt.addBatch();

                                //20 : JDBC batch size
                                if (i % 20 == 0)
                                    pstmt.executeBatch();
                                
                                i++;
                            }
                            pstmt.executeBatch();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            LOGGER.error(StringUtil.printException(ex));
                        } finally {
                            pstmt.close();
                        }
                    }
                });
            }
            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            closeSession(session);
        }
        return null;
    }
}
