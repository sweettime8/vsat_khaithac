package com.elcom.contact.model.dto;

import java.util.Map;

/**
 *
 * @author Admin
 */
public class AuthorizationResponseDTO {
    private String uuid;
    private String userName;
    private String email;
    private String mobile;
    private String fullName;
    private Integer status;
    private String avatar;
    private String address;
    private String birthDay;
    private Integer gender;
    private String unitName;
    private String departmentName;
    private String positionName;
    private String description;
    private String createdBy;
    private String modifyBy;
    private Long createdAt;
    private Long lastUpdate;
    private Integer isDelete;
    
    
    public AuthorizationResponseDTO(){}
    
    public AuthorizationResponseDTO(Map<String, Object> map){
        if(map != null && map.size() > 0){
            if(map.containsKey("uuid")) this.uuid = (String) map.get("uuid");
            if(map.containsKey("userName")) this.userName = (String) map.get("userName");
            if(map.containsKey("email")) this.email = (String) map.get("email");
            if(map.containsKey("mobile")) this.mobile = (String) map.get("mobile");
            if(map.containsKey("fullName")) this.fullName = (String) map.get("fullName");
            if(map.containsKey("avatar")) this.avatar = (String) map.get("avatar");
            if(map.containsKey("status")) this.status = (Integer) map.get("status");
            if(map.containsKey("address")) this.address = (String) map.get("address");
            if(map.containsKey("description")) this.address = (String) map.get("description");
            if(map.containsKey("createdBy")) this.address = (String) map.get("createdBy");
            if(map.containsKey("createdAt")) this.createdAt = (Long) map.get("createdAt");
            if(map.containsKey("lastUpdate")) this.lastUpdate = (Long) map.get("lastUpdate");
            if(map.containsKey("isDelete")) this.isDelete = (Integer) map.get("isDelete");
            if(map.containsKey("birthDay")) this.birthDay = (String) map.get("birthDay");
            if(map.containsKey("gender")) this.gender = (Integer) map.get("gender");
            if(map.containsKey("unitName")) this.avatar = (String) map.get("unitName");
            if(map.containsKey("departmentName")) this.avatar = (String) map.get("departmentName");
            if(map.containsKey("positionName")) this.avatar = (String) map.get("positionName");
            if(map.containsKey("modifyBy")) this.avatar = (String) map.get("modifyBy");
        }
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }
    
    
    
}
