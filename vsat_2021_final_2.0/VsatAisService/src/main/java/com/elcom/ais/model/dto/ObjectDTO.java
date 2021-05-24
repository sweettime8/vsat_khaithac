package com.elcom.ais.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author anhdv
 */
public class ObjectDTO implements Serializable {

    private Long mmsi;
    private String vesselName;
    private String countryName;
    private String vesselTypeName;
    private String callSign;
    private Long imo;
    private BigDecimal width;

    public ObjectDTO() {
    }

    /**
     * @return the mmsi
     */
    public Long getMmsi() {
        return mmsi;
    }

    /**
     * @param mmsi the mmsi to set
     */
    public void setMmsi(Long mmsi) {
        this.mmsi = mmsi;
    }

    /**
     * @return the vesselName
     */
    public String getVesselName() {
        return vesselName;
    }

    /**
     * @param vesselName the vesselName to set
     */
    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    /**
     * @return the countryName
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * @param countryName the countryName to set
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * @return the vesselTypeName
     */
    public String getVesselTypeName() {
        return vesselTypeName;
    }

    /**
     * @param vesselTypeName the vesselTypeName to set
     */
    public void setVesselTypeName(String vesselTypeName) {
        this.vesselTypeName = vesselTypeName;
    }

    /**
     * @return the callSign
     */
    public String getCallSign() {
        return callSign;
    }

    /**
     * @param callSign the callSign to set
     */
    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    /**
     * @return the imo
     */
    public Long getImo() {
        return imo;
    }

    /**
     * @param imo the imo to set
     */
    public void setImo(Long imo) {
        this.imo = imo;
    }

    /**
     * @return the width
     */
    public BigDecimal getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(BigDecimal width) {
        this.width = width;
    }
}
