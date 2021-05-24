package com.elcom.rule.model.dto;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author anhdv
 */
public class RuleNotifyInsertDTO implements Serializable {
    
    private String objId;
    private String description;
    private Timestamp eventTime;
    private Long areaId;
    private Float longitude;
    private Float latitude;
    private Integer ruleActionId;

    public RuleNotifyInsertDTO() {
    }

    public RuleNotifyInsertDTO(String objId, String description, Timestamp eventTime, Long areaId, Float longitude, Float latitude, Integer ruleActionId) {
        this.objId = objId;
        this.description = description;
        this.eventTime = eventTime;
        this.areaId = areaId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.ruleActionId = ruleActionId;
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
     * @return the areaId
     */
    public Long getAreaId() {
        return areaId;
    }

    /**
     * @param areaId the areaId to set
     */
    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    /**
     * @return the longitude
     */
    public Float getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the latitude
     */
    public Float getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(Float latitude) {
        this.latitude = latitude;
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

    
}
