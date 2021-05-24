package com.elcom.ais.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddObjectToGroup implements Serializable {
    public String objId;
    public Long objectGroupId;
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

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public Long getObjectGroupId() {
        return objectGroupId;
    }

    public void setObjectGroupId(Long objectGroupId) {
        this.objectGroupId = objectGroupId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
