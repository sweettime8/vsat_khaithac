package com.elcom.rule.model.dto;

import java.io.Serializable;

/**
 *
 * @author anhdv
 */
public class AreaWithObjectId implements Serializable {
    
    private String ruleId;
    private String areaId;
    private String fromDate;
    private String toDate;
    private String objId;

    public AreaWithObjectId() {
    }

    /**
     * @return the areaId
     */
    public String getAreaId() {
        return areaId;
    }

    /**
     * @param areaId the areaId to set
     */
    public void setAreaId(String areaId) {
        this.areaId = areaId;
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
     * @return the ruleId
     */
    public String getRuleId() {
        return ruleId;
    }

    /**
     * @param ruleId the ruleId to set
     */
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    /**
     * @return the fromDate
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public String getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
