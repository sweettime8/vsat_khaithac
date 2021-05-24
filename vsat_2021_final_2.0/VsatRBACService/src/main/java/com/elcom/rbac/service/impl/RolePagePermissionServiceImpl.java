/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.rbac.service.impl;

import com.elcom.rbac.model.RolePagePermission;
import com.elcom.rbac.repository.RolePagePermissionCustomRepository;
import com.elcom.rbac.repository.RolePagePermissionRepository;
import com.elcom.rbac.service.RolePagePermissionService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class RolePagePermissionServiceImpl implements RolePagePermissionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleModulePermissionServiceImpl.class);

    @Autowired
    private RolePagePermissionRepository rolePagePermissionRepository;

    @Autowired
    private RolePagePermissionCustomRepository rolePagePermissionCustomRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<RolePagePermission> findByRoleCode(String roleCode) {
        return rolePagePermissionCustomRepository.findByRoleCodeAndIsDeleteNotOrderByRoleCodeAsc(roleCode, 0);
    }

    @Override
    public void removeRolePagePermission(Long id) {
        rolePagePermissionRepository.deleteById(id);
    }

    @Override
    public void save(RolePagePermission rolePagePermission) {
        rolePagePermissionRepository.save(rolePagePermission); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeRolePagePermissionByRoleCode(String roleCode) {
        rolePagePermissionCustomRepository.removeRolePagePermissionByRoleCode(roleCode);
    }
    
}
