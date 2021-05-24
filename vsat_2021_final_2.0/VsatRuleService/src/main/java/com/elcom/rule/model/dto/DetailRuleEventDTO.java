package com.elcom.rule.model.dto;

import java.io.Serializable;

/**
 *
 * @author anhdv
 */
public class DetailRuleEventDTO implements Serializable {
    
    private String objId;
    private Integer ruleActionId;
    private String description;
    private String eventTime;
    private Double longitude;
    private Double latitude;
    private String objName;
    private Long imo;
    private String callSign;
    private String countryName;
    private String ipLst;

    public DetailRuleEventDTO() {
    }

    /**
     * @return the objId
     */
    public String getObjId() {
        return objId;
    }

    /**
     * @param objId the objId to set
     */
    public void setObjId(String objId) {
        this.objId = objId;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the eventTime
     */
    public String getEventTime() {
        return eventTime;
    }

    /**
     * @param eventTime the eventTime to set
     */
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    /**
     * @return the ruleActionId
     */
    public Integer getRuleActionId() {
        return ruleActionId;
    }

    /**
     * @param ruleActionId the ruleActionId to set
     */
    public void setRuleActionId(Integer ruleActionId) {
        this.ruleActionId = ruleActionId;
    }

    /**
     * @return the longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the objName
     */
    public String getObjName() {
        return objName;
    }

    /**
     * @param objName the objName to set
     */
    public void setObjName(String objName) {
        this.objName = objName;
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
     * @return the ipLst
     */
    public String getIpLst() {
        return ipLst;
    }

    /**
     * @param ipLst the ipLst to set
     */
    public void setIpLst(String ipLst) {
        this.ipLst = ipLst;
    }
}
