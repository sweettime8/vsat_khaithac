/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.rbac.model.dto;

import java.util.List;

/**
 *
 * @author Admin
 */
public class RoleWithPathsPermissionDTO {
    
    private String roleCode;
    
    private String roleName;
    
    List<PathPerMissionDTO> lstApiAddRole;

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<PathPerMissionDTO> getLstApiAddRole() {
        return lstApiAddRole;
    }

    public void setLstApiAddRole(List<PathPerMissionDTO> lstApiAddRole) {
        this.lstApiAddRole = lstApiAddRole;
    }
    
    
}
