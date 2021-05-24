package com.elcom.media.model.media;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name="vsat_media_relation")
public class MediaRelation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "uuid")
    private UUID uuid;

    @Column(name="uuidKeyFrom")
    private String uuidKeyFrom;

    @Column(name="eventTimeFrom")
    private Timestamp eventTimeFrom;

    @Column(name="mediaTypeIdFrom")
    private String mediaTypeIdFrom;

    @Column(name="mediaTypeNameFrom")
    private String mediaTypeNameFrom;

    @Column(name="directionFrom")
    private Integer directionFrom;

    @Column(name="statusFrom")
    private Integer statusFrom;

    @Column(name="fileSizeFrom")
    private BigDecimal fileSizeFrom;

    @Column(name="dataSourceFrom")
    private String dataSourceFrom;
    
    @Column(name="sourceIpFrom")
    private String sourceIpFrom;

    @Column(name="destIpFrom")
    private String destIpFrom;

    @Column(name="partNameFrom")
    private Long partNameFrom;

    @Column(name="uuidKeyTo")
    private String uuidKeyTo;

    @Column(name="eventTimeTo")
    private Timestamp eventTimeTo;

    @Column(name="mediaTypeIdTo")
    private String mediaTypeIdTo;

    @Column(name="mediaTypeNameTo")
    private String mediaTypeNameTo;

    @Column(name="directionTo")
    private Integer directionTo;

    @Column(name="statusTo")
    private Integer statusTo;

    @Column(name="fileSizeTo")
    private BigDecimal fileSizeTo;
    
    @Column(name="dataSourceTo")
    private String dataSourceTo;

    @Column(name="sourceIpTo")
    private String sourceIpTo;

    @Column(name="destIpTo")
    private String destIpTo;

    @Column(name="partNameTo")
    private Long partNameTo;

    @Column(name="partName")
    private Long partName;

    /**
     * @return the uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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
     * @return the eventTimeFrom
     */
    public Timestamp getEventTimeFrom() {
        return eventTimeFrom;
    }

    /**
     * @param eventTimeFrom the eventTimeFrom to set
     */
    public void setEventTimeFrom(Timestamp eventTimeFrom) {
        this.eventTimeFrom = eventTimeFrom;
    }

    /**
     * @return the mediaTypeIdFrom
     */
    public String getMediaTypeIdFrom() {
        return mediaTypeIdFrom;
    }

    /**
     * @param mediaTypeIdFrom the mediaTypeIdFrom to set
     */
    public void setMediaTypeIdFrom(String mediaTypeIdFrom) {
        this.mediaTypeIdFrom = mediaTypeIdFrom;
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
     * @return the directionFrom
     */
    public Integer getDirectionFrom() {
        return directionFrom;
    }

    /**
     * @param directionFrom the directionFrom to set
     */
    public void setDirectionFrom(Integer directionFrom) {
        this.directionFrom = directionFrom;
    }

    /**
     * @return the statusFrom
     */
    public Integer getStatusFrom() {
        return statusFrom;
    }

    /**
     * @param statusFrom the statusFrom to set
     */
    public void setStatusFrom(Integer statusFrom) {
        this.statusFrom = statusFrom;
    }

    /**
     * @return the fileSizeFrom
     */
    public BigDecimal getFileSizeFrom() {
        return fileSizeFrom;
    }

    /**
     * @param fileSizeFrom the fileSizeFrom to set
     */
    public void setFileSizeFrom(BigDecimal fileSizeFrom) {
        this.fileSizeFrom = fileSizeFrom;
    }

    /**
     * @return the dataSourceFrom
     */
    public String getDataSourceFrom() {
        return dataSourceFrom;
    }

    /**
     * @param dataSourceFrom the dataSourceFrom to set
     */
    public void setDataSourceFrom(String dataSourceFrom) {
        this.dataSourceFrom = dataSourceFrom;
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
     * @return the partNameFrom
     */
    public long getPartNameFrom() {
        return partNameFrom;
    }

    /**
     * @param partNameFrom the partNameFrom to set
     */
    public void setPartNameFrom(long partNameFrom) {
        this.partNameFrom = partNameFrom;
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
     * @return the eventTimeTo
     */
    public Timestamp getEventTimeTo() {
        return eventTimeTo;
    }

    /**
     * @param eventTimeTo the eventTimeTo to set
     */
    public void setEventTimeTo(Timestamp eventTimeTo) {
        this.eventTimeTo = eventTimeTo;
    }

    /**
     * @return the mediaTypeIdTo
     */
    public String getMediaTypeIdTo() {
        return mediaTypeIdTo;
    }

    /**
     * @param mediaTypeIdTo the mediaTypeIdTo to set
     */
    public void setMediaTypeIdTo(String mediaTypeIdTo) {
        this.mediaTypeIdTo = mediaTypeIdTo;
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
     * @return the directionTo
     */
    public Integer getDirectionTo() {
        return directionTo;
    }

    /**
     * @param directionTo the directionTo to set
     */
    public void setDirectionTo(Integer directionTo) {
        this.directionTo = directionTo;
    }

    /**
     * @return the statusTo
     */
    public Integer getStatusTo() {
        return statusTo;
    }

    /**
     * @param statusTo the statusTo to set
     */
    public void setStatusTo(Integer statusTo) {
        this.statusTo = statusTo;
    }

    /**
     * @return the fileSizeTo
     */
    public BigDecimal getFileSizeTo() {
        return fileSizeTo;
    }

    /**
     * @param fileSizeTo the fileSizeTo to set
     */
    public void setFileSizeTo(BigDecimal fileSizeTo) {
        this.fileSizeTo = fileSizeTo;
    }

    /**
     * @return the dataSourceTo
     */
    public String getDataSourceTo() {
        return dataSourceTo;
    }

    /**
     * @param dataSourceTo the dataSourceTo to set
     */
    public void setDataSourceTo(String dataSourceTo) {
        this.dataSourceTo = dataSourceTo;
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
     * @return the partNameTo
     */
    public Long getPartNameTo() {
        return partNameTo;
    }

    /**
     * @param partNameTo the partNameTo to set
     */
    public void setPartNameTo(Long partNameTo) {
        this.partNameTo = partNameTo;
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

    
}
