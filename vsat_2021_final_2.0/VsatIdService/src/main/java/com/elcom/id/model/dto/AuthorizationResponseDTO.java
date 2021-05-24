/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.id.model.dto;

import com.elcom.id.auth.CustomUserDetails;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class AuthorizationResponseDTO {

    private String accessToken;
    private String refreshToken;
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
    private Timestamp createdAt;
    private Timestamp lastUpdate;
    private Integer isDelete;

    public AuthorizationResponseDTO() {
    }

    public AuthorizationResponseDTO(String uuid) {
        this.uuid = uuid;
    }

    public AuthorizationResponseDTO(CustomUserDetails userDetails, String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.uuid = userDetails.getUser().getUuid();
        this.userName = userDetails.getUser().getUserName();
        this.email = userDetails.getUser().getEmail();
        this.mobile = userDetails.getUser().getMobile();
        this.fullName = userDetails.getUser().getFullName();
        this.status = userDetails.getUser().getStatus();
        this.avatar = userDetails.getUser().getAvatar();
        this.address = userDetails.getUser().getAddress();
        this.birthDay = userDetails.getUser().getBirthDay();
        this.gender = userDetails.getUser().getGender();
        this.unitName = userDetails.getUser().getUnitName();
        this.departmentName = userDetails.getUser().getDepartmentName();
        this.positionName = userDetails.getUser().getPositionName();
        this.description = userDetails.getUser().getDescription();
        this.createdBy = userDetails.getUser().getCreatedBy();
        this.modifyBy = userDetails.getUser().getModifyBy();
        this.createdAt = userDetails.getUser().getCreatedAt();
        this.lastUpdate = userDetails.getUser().getLastUpdate();
        this.isDelete = userDetails.getUser().getIsDelete();

    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

}
