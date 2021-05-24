/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.rbac.service;

import com.elcom.rbac.model.Role;
import com.elcom.rbac.model.RoleUser;
import com.elcom.rbac.model.dto.RoleDTO;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Admin
 */
public interface RoleService {

    void save(Role role);

    boolean update(Role role);

    boolean updateRoleCode(String newRoleCode, String newRoleDetail, String roleCodeOld);

    boolean remove(Role role);

    Role findByRoleCode(String roleCode);

    Optional<Role> findById(Long id);

    Role findAdminRole();

    List<Role> findAdminRoleList();

    List<Role> findActive();

    List<RoleDTO> findRoleCreateByUserName(String username);
}
