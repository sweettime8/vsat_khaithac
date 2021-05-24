/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.rbac.model;

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Proxy;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "vsat02.role_page_permission")
@XmlRootElement
@Proxy(lazy = false)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.cache.annotation.Cacheable
public class RolePagePermission {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "role_code")
    private String roleCode;
    
    @Column(name = "page_url")
    private String pageUrl;
    
    @Column(name = "page_name")
    private String pageName;

    @Column(name = "can_create")
    private Integer canCreate;

    @Column(name = "can_read")
    private Integer canRead;

    @Column(name = "can_update")
    private Integer canUpdate;

    @Column(name = "can_delete")
    private Integer canDelete;

    @Column(name = "is_delete")
    private Integer isDelete;

    @Column(name = "created_at")
    private Timestamp createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public Integer getCanCreate() {
        return canCreate;
    }

    public void setCanCreate(Integer canCreate) {
        this.canCreate = canCreate;
    }

    public Integer getCanRead() {
        return canRead;
    }

    public void setCanRead(Integer canRead) {
        this.canRead = canRead;
    }

    public Integer getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(Integer canUpdate) {
        this.canUpdate = canUpdate;
    }

    public Integer getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Integer canDelete) {
        this.canDelete = canDelete;
    }
    
    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    
    
}
