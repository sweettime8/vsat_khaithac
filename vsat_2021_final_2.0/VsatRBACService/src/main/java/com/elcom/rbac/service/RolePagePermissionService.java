/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.rbac.service;

import com.elcom.rbac.model.RolePagePermission;
import com.elcom.rbac.model.dto.RolePathPermissionDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public interface RolePagePermissionService {
    List<RolePagePermission> findByRoleCode(String roleCode);
    
    void removeRolePagePermission(Long id);
    
    void removeRolePagePermissionByRoleCode(String roleCode);
    
    void save(RolePagePermission rolePagePermission);
}
