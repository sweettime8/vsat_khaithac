package com.elcom.contact.model.media;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author anhdv
 */
@Entity
@Table(name="VSAT_MEDIA")
public class MediaRaw implements Serializable {
    @Id
    @Column(name="UUID_KEY")
    private String uuid;

    @Column(name="MEDIA_TYPE_ID")
    private Long mediaTypeId;

    @Column(name="MEDIA_TYPE_NAME")
    private String mediaTypeName;
    
    @Id
    @Column(name="SRC_MMSI")
    private String srcMmsi;
    
    @Column(name="SRC_IS_HQ")
    private Integer srcIsHq;
    
    @Column(name="SRC_COUNTRY_ID")
    private Long srcCountryId;
    
    @Column(name="SRC_TYPE_ID")
    private Long srcTypeId;
    
    @Column(name="SRC_NAME")
    private String srcName;
    
    @Column(name="SRC_EXTRA")
    private String srcExtra;
    
    @Column(name="SOURCE_PHONE")
    private String sourcePhone;
    
    @Id
    @Column(name="DEST_MMSI")
    private String destMmsi;
    
    @Column(name="DEST_IS_HQ")
    private Integer destIsHq;
    
    @Column(name="DEST_COUNTRY_ID")
    private Long destCountryId;
    
    @Column(name="DEST_TYPE_ID")
    private Long destTypeId;
    
    @Column(name="DEST_NAME")
    private String destName;
    
    @Column(name="DEST_EXTRA")
    private String destExtra;
    
    @Column(name="DEST_PHONE")
    private String destPhone;
    
    @Column(name="FILE_PATH")
    private String filePath;
    
    @Column(name="FILESIZE")
    private BigDecimal fileSize;
    
    @Column(name="EVENT_TIME")
    private Timestamp eventTime;
    
    @Column(name="PROC_TIME")
    private Timestamp procTime;
    
    @Column(name="DATA_SOURCE")
    private Long dataSource;
    
    @Column(name="PART_NAME")
    private Long partName;
    
    @Id
    @Column(name="DATE_KEY")
    private BigDecimal dateKey;
    
    @Column(name="SOURCE_PORT")
    private String sourcePort;
    
    @Column(name="DEST_PORT")
    private String destPort;
    
    @Column(name="SOURCE_IP")
    private String sourceIp;
    
    @Column(name="DEST_IP")
    private String destIp;
    
    @Column(name="INGEST_TIME")
    private Timestamp ingestTime;

//    @Column(name="DIRECTION")
//    private Integer direction;
    @Column(name="STATUS")
    private int status;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MediaRaw() {
    }

    public MediaRaw(String uuid, Long mediaTypeId, String mediaTypeName, String srcMmsi, Integer srcIsHq, Long srcCountryId, Long srcTypeId, String srcName, String srcExtra, String sourcePhone, String destMmsi, Integer destIsHq, Long destCountryId, Long destTypeId, String destName, String destExtra, String destPhone, String filePath, BigDecimal fileSize, Timestamp eventTime, Timestamp procTime, Long dataSource, Long partName, BigDecimal dateKey, String sourcePort, String destPort, String sourceIp, String destIp, Timestamp ingestTime, Integer direction, int status) {
        this.uuid = uuid;
        this.mediaTypeId = mediaTypeId;
        this.mediaTypeName = mediaTypeName;
        this.srcMmsi = srcMmsi;
        this.srcIsHq = srcIsHq;
        this.srcCountryId = srcCountryId;
        this.srcTypeId = srcTypeId;
        this.srcName = srcName;
        this.srcExtra = srcExtra;
        this.sourcePhone = sourcePhone;
        this.destMmsi = destMmsi;
        this.destIsHq = destIsHq;
        this.destCountryId = destCountryId;
        this.destTypeId = destTypeId;
        this.destName = destName;
        this.destExtra = destExtra;
        this.destPhone = destPhone;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.eventTime = eventTime;
        this.procTime = procTime;
        this.dataSource = dataSource;
        this.partName = partName;
        this.dateKey = dateKey;
        this.sourcePort = sourcePort;
        this.destPort = destPort;
        this.sourceIp = sourceIp;
        this.destIp = destIp;
        this.ingestTime = ingestTime;
//        this.direction = direction;
        this.status = status;
    }

    public MediaRaw(Long mediaTypeId, String mediaTypeName, String sourceIp, String destIp, String srcMmsi, String destMmsi
            , String filePath, BigDecimal fileSize, Long dataSource, Long partName, BigDecimal dateKey) {
        this.mediaTypeId = mediaTypeId;
        this.mediaTypeName = mediaTypeName;
        this.sourceIp = sourceIp;
        this.destIp = destIp;
        this.srcMmsi = srcMmsi;
        this.destMmsi = destMmsi;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.dataSource = dataSource;
        this.partName = partName;
        this.dateKey = dateKey;
    }

    /**
     * @return the mediaTypeId
     */
    public Long getMediaTypeId() {
        return mediaTypeId;
    }

    /**
     * @param mediaTypeId the mediaTypeId to set
     */
    public void setMediaTypeId(Long mediaTypeId) {
        this.mediaTypeId = mediaTypeId;
    }

    /**
     * @return the mediaTypeName
     */
    public String getMediaTypeName() {
        return mediaTypeName;
    }

    /**
     * @param mediaTypeName the mediaTypeName to set
     */
    public void setMediaTypeName(String mediaTypeName) {
        this.mediaTypeName = mediaTypeName;
    }

    /**
     * @return the srcMmsi
     */
    public String getSrcMmsi() {
        return srcMmsi;
    }

    /**
     * @param srcMmsi the srcMmsi to set
     */
    public void setSrcMmsi(String srcMmsi) {
        this.srcMmsi = srcMmsi;
    }

    /**
     * @return the srcIsHq
     */
    public Integer getSrcIsHq() {
        return srcIsHq;
    }

    /**
     * @param srcIsHq the srcIsHq to set
     */
    public void setSrcIsHq(Integer srcIsHq) {
        this.srcIsHq = srcIsHq;
    }

    /**
     * @return the srcCountryId
     */
    public Long getSrcCountryId() {
        return srcCountryId;
    }

    /**
     * @param srcCountryId the srcCountryId to set
     */
    public void setSrcCountryId(Long srcCountryId) {
        this.srcCountryId = srcCountryId;
    }

    /**
     * @return the srcTypeId
     */
    public Long getSrcTypeId() {
        return srcTypeId;
    }

    /**
     * @param srcTypeId the srcTypeId to set
     */
    public void setSrcTypeId(Long srcTypeId) {
        this.srcTypeId = srcTypeId;
    }

    /**
     * @return the srcName
     */
    public String getSrcName() {
        return srcName;
    }

    /**
     * @param srcName the srcName to set
     */
    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    /**
     * @return the srcExtra
     */
    public String getSrcExtra() {
        return srcExtra;
    }

    /**
     * @param srcExtra the srcExtra to set
     */
    public void setSrcExtra(String srcExtra) {
        this.srcExtra = srcExtra;
    }

    /**
     * @return the sourcePhone
     */
    public String getSourcePhone() {
        return sourcePhone;
    }

    /**
     * @param sourcePhone the sourcePhone to set
     */
    public void setSourcePhone(String sourcePhone) {
        this.sourcePhone = sourcePhone;
    }

    /**
     * @return the destMmsi
     */
    public String getDestMmsi() {
        return destMmsi;
    }

    /**
     * @param destMmsi the destMmsi to set
     */
    public void setDestMmsi(String destMmsi) {
        this.destMmsi = destMmsi;
    }

    /**
     * @return the destIsHq
     */
    public Integer getDestIsHq() {
        return destIsHq;
    }

    /**
     * @param destIsHq the destIsHq to set
     */
    public void setDestIsHq(Integer destIsHq) {
        this.destIsHq = destIsHq;
    }

    /**
     * @return the destCountryId
     */
    public Long getDestCountryId() {
        return destCountryId;
    }

    /**
     * @param destCountryId the destCountryId to set
     */
    public void setDestCountryId(Long destCountryId) {
        this.destCountryId = destCountryId;
    }

    /**
     * @return the destTypeId
     */
    public Long getDestTypeId() {
        return destTypeId;
    }

    /**
     * @param destTypeId the destTypeId to set
     */
    public void setDestTypeId(Long destTypeId) {
        this.destTypeId = destTypeId;
    }

    /**
     * @return the destName
     */
    public String getDestName() {
        return destName;
    }

    /**
     * @param destName the destName to set
     */
    public void setDestName(String destName) {
        this.destName = destName;
    }

    /**
     * @return the destExtra
     */
    public String getDestExtra() {
        return destExtra;
    }

    /**
     * @param destExtra the destExtra to set
     */
    public void setDestExtra(String destExtra) {
        this.destExtra = destExtra;
    }

    /**
     * @return the destPhone
     */
    public String getDestPhone() {
        return destPhone;
    }

    /**
     * @param destPhone the destPhone to set
     */
    public void setDestPhone(String destPhone) {
        this.destPhone = destPhone;
    }

    /**
     * @return the filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @param filePath the filePath to set
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * @return the fileSize
     */
    public BigDecimal getFileSize() {
        return fileSize;
    }

    /**
     * @param fileSize the fileSize to set
     */
    public void setFileSize(BigDecimal fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return the eventTime
     */
    public Timestamp getEventTime() {
        return eventTime;
    }

    /**
     * @param eventTime the eventTime to set
     */
    public void setEventTime(Timestamp eventTime) {
        this.eventTime = eventTime;
    }

    /**
     * @return the procTime
     */
    public Timestamp getProcTime() {
        return procTime;
    }

    /**
     * @param procTime the procTime to set
     */
    public void setProcTime(Timestamp procTime) {
        this.procTime = procTime;
    }

    /**
     * @return the dataSource
     */
    public Long getDataSource() {
        return dataSource;
    }

    /**
     * @param dataSource the dataSource to set
     */
    public void setDataSource(Long dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @return the partName
     */
    public Long getPartName() {
        return partName;
    }

    /**
     * @param partName the partName to set
     */
    public void setPartName(Long partName) {
        this.partName = partName;
    }

    /**
     * @return the dateKey
     */
    public BigDecimal getDateKey() {
        return dateKey;
    }

    /**
     * @param dateKey the dateKey to set
     */
    public void setDateKey(BigDecimal dateKey) {
        this.dateKey = dateKey;
    }

    /**
     * @return the sourcePort
     */
    public String getSourcePort() {
        return sourcePort;
    }

    /**
     * @param sourcePort the sourcePort to set
     */
    public void setSourcePort(String sourcePort) {
        this.sourcePort = sourcePort;
    }

    /**
     * @return the destPort
     */
    public String getDestPort() {
        return destPort;
    }

    /**
     * @param destPort the destPort to set
     */
    public void setDestPort(String destPort) {
        this.destPort = destPort;
    }

    /**
     * @return the sourceIp
     */
    public String getSourceIp() {
        return sourceIp;
    }

    /**
     * @param sourceIp the sourceIp to set
     */
    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    /**
     * @return the destIp
     */
    public String getDestIp() {
        return destIp;
    }

    /**
     * @param destIp the destIp to set
     */
    public void setDestIp(String destIp) {
        this.destIp = destIp;
    }

    /**
     * @return the ingestTime
     */
    public Timestamp getIngestTime() {
        return ingestTime;
    }

    /**
     * @param ingestTime the ingestTime to set
     */
    public void setIngestTime(Timestamp ingestTime) {
        this.ingestTime = ingestTime;
    }

    /**
     * @return the direction
     */
//    public Integer getDirection() {
//        return direction;
//    }
//
//    /**
//     * @param direction the direction to set
//     */
//    public void setDirection(Integer direction) {
//        this.direction = direction;
//    }

}
