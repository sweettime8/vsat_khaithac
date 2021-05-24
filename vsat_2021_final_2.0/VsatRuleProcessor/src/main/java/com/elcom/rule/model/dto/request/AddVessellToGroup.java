package com.elcom.rule.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddVessellToGroup implements Serializable {
    public Integer mmsi;
    public Long vesselGroupId;
    public String groupName;
    public Integer type;
    public Integer groupType;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    public Integer getMmsi() {
        return mmsi;
    }

    public void setMmsi(Integer mmsi) {
        this.mmsi = mmsi;
    }

    public Long getVesselGroupId() {
        return vesselGroupId;
    }

    public void setVesselGroupId(Long vesselGroupId) {
        this.vesselGroupId = vesselGroupId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
