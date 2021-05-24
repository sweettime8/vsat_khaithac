package com.elcom.media.model.dto.media;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
public class MediaRelationDto implements Serializable {
    @Id
    @Column(name="uuidKeyFrom")
    private String uuidKeyFrom;

    @Column(name="eventTimeFrom")
    private Timestamp eventTimeFrom;

    @Column(name="procTimeFrom")
    private Timestamp procTimeFrom;

    @Column(name="mediaTypeIdFrom")
    private int mediaTypeIdFrom;

    @Column(name="mediaTypeNameFrom")
    private String mediaTypeNameFrom;

    @Column(name="directionFrom")
    private int directionFrom;

    @Column(name="statusFrom")
    private int statusFrom;

    @Column(name="fileSizeFrom")
    private int fileSizeFrom;

    @Column(name="filePathFrom")
    private String filePathFrom;

    @Column(name="dataSourceFrom")
    private int dataSourceFrom;

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

    @Column(name="procTimeTo")
    private Timestamp procTimeTo;

    @Column(name="mediaTypeIdTo")
    private int mediaTypeIdTo;

    @Column(name="mediaTypeNameTo")
    private String mediaTypeNameTo;

    @Column(name="directionTo")
    private int directionTo;

    @Column(name="statusTo")
    private int statusTo;

    @Column(name="fileSizeTo")
    private int fileSizeTo;

    @Column(name="filePathTo")
    private String filePathTo;

    @Column(name="dataSourceTo")
    private int dataSourceTo;

    @Column(name="sourceIpTo")
    private String sourceIpTo;

    @Column(name="destIpTo")
    private String destIpTo;

    @Column(name="partNameTo")
    private Long partNameTo;

    public String getUuidKeyFrom() {
        return uuidKeyFrom;
    }

    public void setUuidKeyFrom(String uuidKeyFrom) {
        this.uuidKeyFrom = uuidKeyFrom;
    }

    public Timestamp getEventTimeFrom() {
        return eventTimeFrom;
    }

    public void setEventTimeFrom(Timestamp eventTimeFrom) {
        this.eventTimeFrom = eventTimeFrom;
    }

    public Timestamp getProcTimeFrom() {
        return procTimeFrom;
    }

    public void setProcTimeFrom(Timestamp procTimeFrom) {
        this.procTimeFrom = procTimeFrom;
    }

    public int getMediaTypeIdFrom() {
        return mediaTypeIdFrom;
    }

    public void setMediaTypeIdFrom(int mediaTypeIdFrom) {
        this.mediaTypeIdFrom = mediaTypeIdFrom;
    }

    public String getSourceIpFrom() {
        return sourceIpFrom;
    }

    public void setSourceIpFrom(String sourceIpFrom) {
        this.sourceIpFrom = sourceIpFrom;
    }

    public String getDestIpFrom() {
        return destIpFrom;
    }

    public void setDestIpFrom(String destIpFrom) {
        this.destIpFrom = destIpFrom;
    }

    public Long getPartNameFrom() {
        return partNameFrom;
    }

    public void setPartNameFrom(Long partNameFrom) {
        this.partNameFrom = partNameFrom;
    }

    public String getUuidKeyTo() {
        return uuidKeyTo;
    }

    public void setUuidKeyTo(String uuidKeyTo) {
        this.uuidKeyTo = uuidKeyTo;
    }

    public Timestamp getEventTimeTo() {
        return eventTimeTo;
    }

    public void setEventTimeTo(Timestamp eventTimeTo) {
        this.eventTimeTo = eventTimeTo;
    }

    public Timestamp getProcTimeTo() {
        return procTimeTo;
    }

    public void setProcTimeTo(Timestamp procTimeTo) {
        this.procTimeTo = procTimeTo;
    }

    public int getMediaTypeIdTo() {
        return mediaTypeIdTo;
    }

    public void setMediaTypeIdTo(int mediaTypeIdTo) {
        this.mediaTypeIdTo = mediaTypeIdTo;
    }

    public String getSourceIpTo() {
        return sourceIpTo;
    }

    public void setSourceIpTo(String sourceIpTo) {
        this.sourceIpTo = sourceIpTo;
    }

    public String getDestIpTo() {
        return destIpTo;
    }

    public void setDestIpTo(String destIpTo) {
        this.destIpTo = destIpTo;
    }

    public Long getPartNameTo() {
        return partNameTo;
    }

    public void setPartNameTo(Long partNameTo) {
        this.partNameTo = partNameTo;
    }

    public String getMediaTypeNameFrom() {
        return mediaTypeNameFrom;
    }

    public void setMediaTypeNameFrom(String mediaTypeNameFrom) {
        this.mediaTypeNameFrom = mediaTypeNameFrom;
    }

    public int getDirectionFrom() {
        return directionFrom;
    }

    public void setDirectionFrom(int directionFrom) {
        this.directionFrom = directionFrom;
    }

    public int getStatusFrom() {
        return statusFrom;
    }

    public void setStatusFrom(int statusFrom) {
        this.statusFrom = statusFrom;
    }

    public int getFileSizeFrom() {
        return fileSizeFrom;
    }

    public void setFileSizeFrom(int fileSizeFrom) {
        this.fileSizeFrom = fileSizeFrom;
    }

    public String getMediaTypeNameTo() {
        return mediaTypeNameTo;
    }

    public void setMediaTypeNameTo(String mediaTypeNameTo) {
        this.mediaTypeNameTo = mediaTypeNameTo;
    }

    public int getDirectionTo() {
        return directionTo;
    }

    public void setDirectionTo(int directionTo) {
        this.directionTo = directionTo;
    }

    public int getStatusTo() {
        return statusTo;
    }

    public void setStatusTo(int statusTo) {
        this.statusTo = statusTo;
    }

    public int getFileSizeTo() {
        return fileSizeTo;
    }

    public void setFileSizeTo(int fileSizeTo) {
        this.fileSizeTo = fileSizeTo;
    }

    public int getDataSourceFrom() {
        return dataSourceFrom;
    }

    public void setDataSourceFrom(int dataSourceFrom) {
        this.dataSourceFrom = dataSourceFrom;
    }

    public int getDataSourceTo() {
        return dataSourceTo;
    }

    public void setDataSourceTo(int dataSourceTo) {
        this.dataSourceTo = dataSourceTo;
    }

    public String getFilePathFrom() {
        return filePathFrom;
    }

    public void setFilePathFrom(String filePathFrom) {
        this.filePathFrom = filePathFrom;
    }

    public String getFilePathTo() {
        return filePathTo;
    }

    public void setFilePathTo(String filePathTo) {
        this.filePathTo = filePathTo;
    }
}
