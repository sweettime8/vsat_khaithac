package com.elcom.uploadservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

/**
 *
 * @author anhdv
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaRawRelationDTO implements Serializable {
    
    private String uuidKeyFrom;//
    private String uuidKeyTo;//
    private Long partNameFrom;//
    private Long eventTimeFrom;//
    private Long eventTimeTo;//
    private String mediaTypeNameFrom;//
    private String mediaTypeNameTo;//
    private String sourceIpFrom;//
    private String sourceIpTo;//
    private Long srcIdFrom;
    private Long srcIdTo;
    private String srcNameFrom;
    private String srcNameTo;
    private String sourcePortFrom;
    private String sourcePortTo;
    private String sourcePhoneFrom;
    private String sourcePhoneTo;
    private String destIpFrom;//
    private String destIpTo;//
    private Long destIdFrom;
    private Long destIdTo;
    private String destNameFrom;
    private String destNameTo;
    private String destPortFrom;
    private String destPortTo;
    private String destPhoneFrom;
    private String destPhoneTo;
    private String fileNameFrom;
    private String fileNameTo;
    private String fileSizeFrom;//
    private String fileSizeTo;//
    private String filePathLocalFrom;
    private String filePathLocalTo;
    private String dataSourceNameFrom;//
    private String dataSourceNameTo;//

    /**
     * @return the eventTimeFrom
     */
    public Long getEventTimeFrom() {
        return eventTimeFrom;
    }

    /**
     * @param eventTimeFrom the eventTimeFrom to set
     */
    public void setEventTimeFrom(Long eventTimeFrom) {
        this.eventTimeFrom = eventTimeFrom;
    }

    /**
     * @return the eventTimeTo
     */
    public Long getEventTimeTo() {
        return eventTimeTo;
    }

    /**
     * @param eventTimeTo the eventTimeTo to set
     */
    public void setEventTimeTo(Long eventTimeTo) {
        this.eventTimeTo = eventTimeTo;
    }

    /**
     * @return the mediaTypeNameFrom
     */
    public String getMediaTypeNameFrom() {
        return mediaTypeNameFrom;
    }

    /**
     * @param mediaTypeNameFrom the mediaTypeNameFrom to set
     */
    public void setMediaTypeNameFrom(String mediaTypeNameFrom) {
        this.mediaTypeNameFrom = mediaTypeNameFrom;
    }

    /**
     * @return the mediaTypeNameTo
     */
    public String getMediaTypeNameTo() {
        return mediaTypeNameTo;
    }

    /**
     * @param mediaTypeNameTo the mediaTypeNameTo to set
     */
    public void setMediaTypeNameTo(String mediaTypeNameTo) {
        this.mediaTypeNameTo = mediaTypeNameTo;
    }

    /**
     * @return the sourceIpFrom
     */
    public String getSourceIpFrom() {
        return sourceIpFrom;
    }

    /**
     * @param sourceIpFrom the sourceIpFrom to set
     */
    public void setSourceIpFrom(String sourceIpFrom) {
        this.sourceIpFrom = sourceIpFrom;
    }

    /**
     * @return the sourceIpTo
     */
    public String getSourceIpTo() {
        return sourceIpTo;
    }

    /**
     * @param sourceIpTo the sourceIpTo to set
     */
    public void setSourceIpTo(String sourceIpTo) {
        this.sourceIpTo = sourceIpTo;
    }

    /**
     * @return the srcIdFrom
     */
    public Long getSrcIdFrom() {
        return srcIdFrom;
    }

    /**
     * @param srcIdFrom the srcIdFrom to set
     */
    public void setSrcIdFrom(Long srcIdFrom) {
        this.srcIdFrom = srcIdFrom;
    }

    /**
     * @return the srcIdTo
     */
    public Long getSrcIdTo() {
        return srcIdTo;
    }

    /**
     * @param srcIdTo the srcIdTo to set
     */
    public void setSrcIdTo(Long srcIdTo) {
        this.srcIdTo = srcIdTo;
    }

    /**
     * @return the srcNameFrom
     */
    public String getSrcNameFrom() {
        return srcNameFrom;
    }

    /**
     * @param srcNameFrom the srcNameFrom to set
     */
    public void setSrcNameFrom(String srcNameFrom) {
        this.srcNameFrom = srcNameFrom;
    }

    /**
     * @return the srcNameTo
     */
    public String getSrcNameTo() {
        return srcNameTo;
    }

    /**
     * @param srcNameTo the srcNameTo to set
     */
    public void setSrcNameTo(String srcNameTo) {
        this.srcNameTo = srcNameTo;
    }

    /**
     * @return the sourcePortFrom
     */
    public String getSourcePortFrom() {
        return sourcePortFrom;
    }

    /**
     * @param sourcePortFrom the sourcePortFrom to set
     */
    public void setSourcePortFrom(String sourcePortFrom) {
        this.sourcePortFrom = sourcePortFrom;
    }

    /**
     * @return the sourcePortTo
     */
    public String getSourcePortTo() {
        return sourcePortTo;
    }

    /**
     * @param sourcePortTo the sourcePortTo to set
     */
    public void setSourcePortTo(String sourcePortTo) {
        this.sourcePortTo = sourcePortTo;
    }

    /**
     * @return the sourcePhoneFrom
     */
    public String getSourcePhoneFrom() {
        return sourcePhoneFrom;
    }

    /**
     * @param sourcePhoneFrom the sourcePhoneFrom to set
     */
    public void setSourcePhoneFrom(String sourcePhoneFrom) {
        this.sourcePhoneFrom = sourcePhoneFrom;
    }

    /**
     * @return the sourcePhoneTo
     */
    public String getSourcePhoneTo() {
        return sourcePhoneTo;
    }

    /**
     * @param sourcePhoneTo the sourcePhoneTo to set
     */
    public void setSourcePhoneTo(String sourcePhoneTo) {
        this.sourcePhoneTo = sourcePhoneTo;
    }

    /**
     * @return the destIpFrom
     */
    public String getDestIpFrom() {
        return destIpFrom;
    }

    /**
     * @param destIpFrom the destIpFrom to set
     */
    public void setDestIpFrom(String destIpFrom) {
        this.destIpFrom = destIpFrom;
    }

    /**
     * @return the destIpTo
     */
    public String getDestIpTo() {
        return destIpTo;
    }

    /**
     * @param destIpTo the destIpTo to set
     */
    public void setDestIpTo(String destIpTo) {
        this.destIpTo = destIpTo;
    }

    /**
     * @return the destIdFrom
     */
    public Long getDestIdFrom() {
        return destIdFrom;
    }

    /**
     * @param destIdFrom the destIdFrom to set
     */
    public void setDestIdFrom(Long destIdFrom) {
        this.destIdFrom = destIdFrom;
    }

    /**
     * @return the destIdTo
     */
    public Long getDestIdTo() {
        return destIdTo;
    }

    /**
     * @param destIdTo the destIdTo to set
     */
    public void setDestIdTo(Long destIdTo) {
        this.destIdTo = destIdTo;
    }

    /**
     * @return the destNameFrom
     */
    public String getDestNameFrom() {
        return destNameFrom;
    }

    /**
     * @param destNameFrom the destNameFrom to set
     */
    public void setDestNameFrom(String destNameFrom) {
        this.destNameFrom = destNameFrom;
    }

    /**
     * @return the destNameTo
     */
    public String getDestNameTo() {
        return destNameTo;
    }

    /**
     * @param destNameTo the destNameTo to set
     */
    public void setDestNameTo(String destNameTo) {
        this.destNameTo = destNameTo;
    }

    /**
     * @return the destPortFrom
     */
    public String getDestPortFrom() {
        return destPortFrom;
    }

    /**
     * @param destPortFrom the destPortFrom to set
     */
    public void setDestPortFrom(String destPortFrom) {
        this.destPortFrom = destPortFrom;
    }

    /**
     * @return the destPortTo
     */
    public String getDestPortTo() {
        return destPortTo;
    }

    /**
     * @param destPortTo the destPortTo to set
     */
    public void setDestPortTo(String destPortTo) {
        this.destPortTo = destPortTo;
    }

    /**
     * @return the destPhoneFrom
     */
    public String getDestPhoneFrom() {
        return destPhoneFrom;
    }

    /**
     * @param destPhoneFrom the destPhoneFrom to set
     */
    public void setDestPhoneFrom(String destPhoneFrom) {
        this.destPhoneFrom = destPhoneFrom;
    }

    /**
     * @return the destPhoneTo
     */
    public String getDestPhoneTo() {
        return destPhoneTo;
    }

    /**
     * @param destPhoneTo the destPhoneTo to set
     */
    public void setDestPhoneTo(String destPhoneTo) {
        this.destPhoneTo = destPhoneTo;
    }

    /**
     * @return the fileNameFrom
     */
    public String getFileNameFrom() {
        return fileNameFrom;
    }

    /**
     * @param fileNameFrom the fileNameFrom to set
     */
    public void setFileNameFrom(String fileNameFrom) {
        this.fileNameFrom = fileNameFrom;
    }

    /**
     * @return the fileNameTo
     */
    public String getFileNameTo() {
        return fileNameTo;
    }

    /**
     * @param fileNameTo the fileNameTo to set
     */
    public void setFileNameTo(String fileNameTo) {
        this.fileNameTo = fileNameTo;
    }

    /**
     * @return the fileSizeFrom
     */
    public String getFileSizeFrom() {
        return fileSizeFrom;
    }

    /**
     * @param fileSizeFrom the fileSizeFrom to set
     */
    public void setFileSizeFrom(String fileSizeFrom) {
        this.fileSizeFrom = fileSizeFrom;
    }

    /**
     * @return the fileSizeTo
     */
    public String getFileSizeTo() {
        return fileSizeTo;
    }

    /**
     * @param fileSizeTo the fileSizeTo to set
     */
    public void setFileSizeTo(String fileSizeTo) {
        this.fileSizeTo = fileSizeTo;
    }

    /**
     * @return the filePathLocalFrom
     */
    public String getFilePathLocalFrom() {
        return filePathLocalFrom;
    }

    /**
     * @param filePathLocalFrom the filePathLocalFrom to set
     */
    public void setFilePathLocalFrom(String filePathLocalFrom) {
        this.filePathLocalFrom = filePathLocalFrom;
    }

    /**
     * @return the filePathLocalTo
     */
    public String getFilePathLocalTo() {
        return filePathLocalTo;
    }

    /**
     * @param filePathLocalTo the filePathLocalTo to set
     */
    public void setFilePathLocalTo(String filePathLocalTo) {
        this.filePathLocalTo = filePathLocalTo;
    }

    /**
     * @return the dataSourceNameFrom
     */
    public String getDataSourceNameFrom() {
        return dataSourceNameFrom;
    }

    /**
     * @param dataSourceNameFrom the dataSourceNameFrom to set
     */
    public void setDataSourceNameFrom(String dataSourceNameFrom) {
        this.dataSourceNameFrom = dataSourceNameFrom;
    }

    /**
     * @return the dataSourceNameTo
     */
    public String getDataSourceNameTo() {
        return dataSourceNameTo;
    }

    /**
     * @param dataSourceNameTo the dataSourceNameTo to set
     */
    public void setDataSourceNameTo(String dataSourceNameTo) {
        this.dataSourceNameTo = dataSourceNameTo;
    }

    /**
     * @return the uuidKeyFrom
     */
    public String getUuidKeyFrom() {
        return uuidKeyFrom;
    }

    /**
     * @param uuidKeyFrom the uuidKeyFrom to set
     */
    public void setUuidKeyFrom(String uuidKeyFrom) {
        this.uuidKeyFrom = uuidKeyFrom;
    }

    /**
     * @return the uuidKeyTo
     */
    public String getUuidKeyTo() {
        return uuidKeyTo;
    }

    /**
     * @param uuidKeyTo the uuidKeyTo to set
     */
    public void setUuidKeyTo(String uuidKeyTo) {
        this.uuidKeyTo = uuidKeyTo;
    }

    /**
     * @return the partNameFrom
     */
    public Long getPartNameFrom() {
        return partNameFrom;
    }

    /**
     * @param partNameFrom the partNameFrom to set
     */
    public void setPartNameFrom(Long partNameFrom) {
        this.partNameFrom = partNameFrom;
    }
}
