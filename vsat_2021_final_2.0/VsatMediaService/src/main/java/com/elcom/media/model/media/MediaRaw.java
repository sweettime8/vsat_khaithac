package com.elcom.media.model.media;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.stream.Stream;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author anhdv
 */
@Entity
@Table(name="vsat_media")
public class MediaRaw implements Serializable {
    
    @Id
    @Column(name="uuidKey")
    private String uuid;

    @Column(name="eventTime")
    private Timestamp eventTime;
    
    @Column(name="procTime")
    private Timestamp procTime;
    
    @Column(name="mediaTypeId")
    private Long mediaTypeId;

    @Column(name="mediaTypeName")
    private String mediaTypeName;
    
    @Column(name="sourceIp")
    private String sourceIp;
    
    @Column(name="srcId")
    private Long srcId;
    
    @Column(name="srcObjId")
    private String srcObjId;
    
    @Column(name="srcIsHq")
    private Integer srcIsHq;
    
    @Column(name="srcCountryId")
    private Long srcCountryId;
    
    @Column(name="srcTypeId")
    private Long srcTypeId;
    
    @Column(name="srcName")
    private String srcName;
    
    @Column(name="srcExtra")
    private String srcExtra;
    
    @Column(name="sourcePort")
    private String sourcePort;
    
    @Column(name="sourcePhone")
    private String sourcePhone;
    
    @Column(name="destIp")
    private String destIp;
    
    @Column(name="destId")
    private Long destId;
    
    @Column(name="destObjId")
    private String destObjId;
    
    @Column(name="destIsHq")
    private Integer destIsHq;
    
    @Column(name="destCountryId")
    private Long destCountryId;
    
    @Column(name="destTypeId")
    private Long destTypeId;
    
    @Column(name="destName")
    private String destName;
    
    @Column(name="destExtra")
    private String destExtra;
    
    @Column(name="destPort")
    private String destPort;
    
    @Column(name="destPhone")
    private String destPhone;
    
    @Column(name="filePath")
    private String filePath;
    
    @Column(name="fileSize")
    private BigDecimal fileSize;
    
    @Column(name="fileName")
    private String fileName;
    
    @Column(name="dataSource")
    private Long dataSource;
    
    @Column(name="dataSourceName")
    private String dataSourceName;
    
    @Column(name="direction")
    private Integer direction;
    
    @Column(name="relId")
    private String relId;
    
    @Column(name="partName")
    private Long partName;
    
    @Column(name="updatedTime")
    private Timestamp updatedTime;
    
    @Column(name="ingestTime")
    private Timestamp ingestTime;

    @Column(name="status")
    private int status;
    
    @Transient
    private String filePathLocal;

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

    public MediaRaw(String uuid, Long mediaTypeId, String mediaTypeName, String srcObjId, Integer srcIsHq, Long srcCountryId, Long srcTypeId
            , String srcName, String srcExtra, String sourcePhone, String destObjId, Integer destIsHq, Long destCountryId, Long destTypeId
            , String destName, String destExtra, String destPhone, String filePath, BigDecimal fileSize, Timestamp eventTime, Timestamp procTime, Long dataSource, Long partName, BigDecimal dateKey, String sourcePort, String destPort, String sourceIp, String destIp, Timestamp ingestTime, Integer direction, int status) {
        this.uuid = uuid;
        this.mediaTypeId = mediaTypeId;
        this.mediaTypeName = mediaTypeName;
        this.srcObjId = srcObjId;
        this.srcIsHq = srcIsHq;
        this.srcCountryId = srcCountryId;
        this.srcTypeId = srcTypeId;
        this.srcName = srcName;
        this.srcExtra = srcExtra;
        this.sourcePhone = sourcePhone;
        this.destObjId = destObjId;
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
        this.sourcePort = sourcePort;
        this.destPort = destPort;
        this.sourceIp = sourceIp;
        this.destIp = destIp;
        this.ingestTime = ingestTime;
//        this.direction = direction;
        this.status = status;
    }

    public MediaRaw(Long mediaTypeId, String mediaTypeName, String sourceIp, String destIp, String srcObjId, String destObjId
            , String filePath, BigDecimal fileSize, Long dataSource, Long partName, BigDecimal dateKey) {
        this.mediaTypeId = mediaTypeId;
        this.mediaTypeName = mediaTypeName;
        this.sourceIp = sourceIp;
        this.destIp = destIp;
        this.srcObjId = srcObjId;
        this.destObjId = destObjId;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.dataSource = dataSource;
        this.partName = partName;
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
    public Integer getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    /**
     * @return the srcId
     */
    public Long getSrcId() {
        return srcId;
    }

    /**
     * @param srcId the srcId to set
     */
    public void setSrcId(Long srcId) {
        this.srcId = srcId;
    }

    /**
     * @return the destId
     */
    public Long getDestId() {
        return destId;
    }

    /**
     * @param destId the destId to set
     */
    public void setDestId(Long destId) {
        this.destId = destId;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the dataSourceName
     */
    public String getDataSourceName() {
        return dataSourceName;
    }

    /**
     * @param dataSourceName the dataSourceName to set
     */
    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    /**
     * @return the relId
     */
    public String getRelId() {
        return relId;
    }

    /**
     * @param relId the relId to set
     */
    public void setRelId(String relId) {
        this.relId = relId;
    }

    /**
     * @return the updatedTime
     */
    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime the updatedTime to set
     */
    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Stream<Object> getFilePath(String aas) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the filePathLocal
     */
    public String getFilePathLocal() {
        return filePathLocal;
    }

    /**
     * @param filePathLocal the filePathLocal to set
     */
    public void setFilePathLocal(String filePathLocal) {
        this.filePathLocal = filePathLocal;
    }

    /**
     * @return the srcObjId
     */
    public String getSrcObjId() {
        return srcObjId;
    }

    /**
     * @param srcObjId the srcObjId to set
     */
    public void setSrcObjId(String srcObjId) {
        this.srcObjId = srcObjId;
    }

    /**
     * @return the destObjId
     */
    public String getDestObjId() {
        return destObjId;
    }

    /**
     * @param destObjId the destObjId to set
     */
    public void setDestObjId(String destObjId) {
        this.destObjId = destObjId;
    }

}
