package com.elcom.rule.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Proxy;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "\"USER\"")
@Proxy(lazy = false)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.cache.annotation.Cacheable
@ApiModel(value = "User entity")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "\"UUID\"")
    private String uuid;

    @Column(name = "\"USERNAME\"")
    private String userName;

    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Column(name = "\"EMAIL\"")
    private String email;

    @Size(max = 20)
    @Column(name = "\"MOBILE\"")
    private String mobile;

    @Column(name = "\"FULLNAME\"")
    private String fullName;

    @Column(name = "\"PASSWORD\"")
    @JsonIgnore
    private String password;

    @Column(name = "\"STATUS\"")
    private Integer status;

    @Column(name = "\"AVATAR\"")
    private String avatar;

    @Column(name = "\"ADDRESS\"")
    private String address;

    @Column(name = "\"BIRTHDAY\"")
    private String birthDay;

    @Column(name = "\"GENDER\"")
    private Integer gender;

    @Column(name = "\"UNIT_NAME\"")
    private String unitName;

    @Column(name = "\"DEPARTMENT_NAME\"")
    private String departmentName;

    @Column(name = "\"POSITION_NAME\"")
    private String positionName;

    @Column(name = "\"DESCRIPTION\"")
    private String description;
        
    @Column(name = "\"CREATED_BY\"")
    private String createdBy;
    
    @Column(name = "\"MODIFY_BY\"")
    private String modifyBy;
    
    @Column(name = "\"CREATED_AT\"")
    private Timestamp createdAt;

    @Column(name = "\"LAST_UPDATE\"")
    private Timestamp lastUpdate;

    @Column(name = "\"IS_DELETE\"")
    private Integer isDelete;

    @JsonIgnore
    public static final Integer STATUS_ACTIVE = 1;
    @JsonIgnore
    public static final Integer STATUS_LOCK = -1;

    public User() {
    }

    @PrePersist
    void preInsert() {
        if (this.getCreatedAt() == null) {
            this.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }
    }

    public User(String uuid) {
        this.uuid = uuid;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
    
    

  

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uuid != null ? uuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        return !((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid)));
    }

    @Override
    public String toString() {
        return "com.elcom.id.model.User[ uuid=" + uuid + " ]";
    }

    public String toJsonString() {
        return new Gson().toJson(this);
    }
}
