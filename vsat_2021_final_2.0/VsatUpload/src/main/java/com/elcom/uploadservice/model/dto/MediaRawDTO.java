package com.elcom.uploadservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

/**
 *
 * @author anhdv
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaRawDTO implements Serializable {
    
    private Long eventTime;
    private String mediaTypeName;
    private String sourceIp;
    private Long srcId;
    private Integer srcIsHq;
    private String srcName;
    private String sourcePort;
    private String sourcePhone;
    private String destIp;
    private Long destId;
    private Integer destIsHq;
    private String destName;
    private String destPort;
    private String destPhone;
    private String fileName;
    private String fileSize;
    private String filePathLocal;
    private String dataSourceName;
    private Integer direction;

    /**
     * @return the eventTime
     */
    public Long getEventTime() {
        return eventTime;
    }

    /**
     * @param eventTime the eventTime to set
     */
    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
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
     * @return the fileSize
     */
    public String getFileSize() {
        return fileSize;
    }

    /**
     * @param fileSize the fileSize to set
     */
    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
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
}
