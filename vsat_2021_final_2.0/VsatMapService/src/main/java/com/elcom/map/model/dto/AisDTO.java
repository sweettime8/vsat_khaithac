package com.elcom.map.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 *
 * @author anhdv
 */
public class AisDTO implements Serializable {
    
    private String mmsi;
//    private Long dimA;
//    private Long dimB;
//    private Long dimC;
//    private Long dimD;
    private Float draugth;
//    private Long heading;
    private String destination;
    private String eta;
    private Long sourceId;
    private Float rot;
    private Float sog;
    private Float cog;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private Timestamp eventTime;
    private String ingestTime;
    private String mmsiMaster;
    //private Long areaId;
    private String sourceIp;
    private String destIp;
    //private Integer direction;

    public AisDTO() {
    }

    /**
     * @return the mmsi
     */
    public String getMmsi() {
        return mmsi;
    }

    /**
     * @param mmsi the mmsi to set
     */
    public void setMmsi(String mmsi) {
        this.mmsi = mmsi;
    }

    /**
     * @return the draugth
     */
    public Float getDraugth() {
        return draugth;
    }

    /**
     * @param draugth the draugth to set
     */
    public void setDraugth(Float draugth) {
        this.draugth = draugth;
    }

    /**
     * @return the rot
     */
    public Float getRot() {
        return rot;
    }

    /**
     * @param rot the rot to set
     */
    public void setRot(Float rot) {
        this.rot = rot;
    }

    /**
     * @return the sog
     */
    public Float getSog() {
        return sog;
    }

    /**
     * @param sog the sog to set
     */
    public void setSog(Float sog) {
        this.sog = sog;
    }

    /**
     * @return the cog
     */
    public Float getCog() {
        return cog;
    }

    /**
     * @param cog the cog to set
     */
    public void setCog(Float cog) {
        this.cog = cog;
    }

    /**
     * @return the longitude
     */
    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the latitude
     */
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
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
     * @return the mmsiMaster
     */
    public String getMmsiMaster() {
        return mmsiMaster;
    }

    /**
     * @param mmsiMaster the mmsiMaster to set
     */
    public void setMmsiMaster(String mmsiMaster) {
        this.mmsiMaster = mmsiMaster;
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
    public String getIngestTime() {
        return ingestTime;
    }

    /**
     * @param ingestTime the ingestTime to set
     */
    public void setIngestTime(String ingestTime) {
        this.ingestTime = ingestTime;
    }

    /**
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * @return the eta
     */
    public String getEta() {
        return eta;
    }

    /**
     * @param eta the eta to set
     */
    public void setEta(String eta) {
        this.eta = eta;
    }

    /**
     * @return the sourceId
     */
    public Long getSourceId() {
        return sourceId;
    }

    /**
     * @param sourceId the sourceId to set
     */
    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }
}
