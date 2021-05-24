package com.elcom.media.model.dto.request.media;

import java.io.Serializable;

public class MediaRelationDTO implements Serializable {
    
    private String uuidKey;
    private Long srcId;
    private String srcName;
    private String sourcePort;
    private String sourcePhone;
    private Long destId;
    private String destName;
    private String destPort;
    private String destPhone;
    private String fileName;
    private String filePathLocal;

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
     * @return the uuidKey
     */
    public String getUuidKey() {
        return uuidKey;
    }

    /**
     * @param uuidKey the uuidKey to set
     */
    public void setUuidKey(String uuidKey) {
        this.uuidKey = uuidKey;
    }
}
