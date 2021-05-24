package com.elcom.rule.model.dto;

import java.io.Serializable;

/**
 *
 * @author anhdv
 */
public class RuleEventNotifiDTO implements Serializable {
    
    private String uuid;
    private String objId;
    private String description;
    private String eventTime;
    private Integer ruleActionId;
    private Integer readStatus;
    private Long vesselId;
    private String ufoId;

    public RuleEventNotifiDTO() {
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
     * @return the readStatus
     */
    public Integer getReadStatus() {
        return readStatus;
    }

    /**
     * @param readStatus the readStatus to set
     */
    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    /**
     * @return the vesselId
     */
    public Long getVesselId() {
        return vesselId;
    }

    /**
     * @param vesselId the vesselId to set
     */
    public void setVesselId(Long vesselId) {
        this.vesselId = vesselId;
    }

    /**
     * @return the ufoId
     */
    public String getUfoId() {
        return ufoId;
    }

    /**
     * @param ufoId the ufoId to set
     */
    public void setUfoId(String ufoId) {
        this.ufoId = ufoId;
    }

    /**
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
