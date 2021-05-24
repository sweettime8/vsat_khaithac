/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.rbac.model.dto;

/**
 *
 * @author Admin
 */
public class RoleUserDTO {

    private Long id;
    private String uuidUser;
    private String roleCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuidUser() {
        return uuidUser;
    }

    public void setUuidUser(String uuidUser) {
        this.uuidUser = uuidUser;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
    
    
}
