/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.rbac.controller;

import com.elcom.gateway.message.MessageContent;
import com.elcom.gateway.message.ResponseMessage;
import com.elcom.rbac.constant.Constant;
import com.elcom.rbac.model.Path;
import com.elcom.rbac.model.Role;
import com.elcom.rbac.model.RolePagePermission;
import com.elcom.rbac.model.RoleUser;
import com.elcom.rbac.model.Service;
import com.elcom.rbac.model.dto.AuthorizationResponseDTO;
import com.elcom.rbac.model.dto.RolePathPermissionDTO;
import com.elcom.rbac.service.RolePagePermissionService;
import com.elcom.rbac.service.RoleService;
import com.elcom.rbac.service.RoleUserService;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

/**
 *
 * @author ducnh
 */
@Controller
public class RolePagePermissionController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RolePagePermissionController.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    private RolePagePermissionService rolePagePermissionService;

    public ResponseMessage deleteUserRoleAndPermission(String pathParam, Map<String, String> headerMap) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                    new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                            "Bạn chưa đăng nhập"));

        }
        if (pathParam == null || pathParam.isEmpty()) {
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
        } else {
            Long id = new Long(pathParam);
            Optional<Role> optional = roleService.findById(id);
            Role role = optional != null && optional.isPresent() ? optional.get() : null;

            if (role == null) {
                response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                        new MessageContent(HttpStatus.NOT_FOUND.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                                "Không tìm thấy Role ứng với id " + id));
            } else {
                //check quyền đã được gán cho ai chưa
                List<RoleUser> roleUserList = roleUserService.findByRoleCode(role);
                if (roleUserList != null && !roleUserList.isEmpty()) {
                    return new ResponseMessage(HttpStatus.OK.value(), "Không thể xóa quyền đã được gán cho user",
                            new MessageContent(HttpStatus.OK.value(), "Không thể xóa quyền đã được gán cho user",
                                    null));
                } else {
                    //delete role_page_permission                               
                    List<RolePagePermission> lstRolePagePermission = rolePagePermissionService.findByRoleCode(role.getRoleCode());
                    for (RolePagePermission existPermissionByRole : lstRolePagePermission) {
                        rolePagePermissionService.removeRolePagePermission(existPermissionByRole.getId());
                    }

                    //delete role
                    boolean result = roleService.remove(role);
                    if (result) {
                        return new ResponseMessage(new MessageContent(role.getRoleCode()));
                    } else {
                        response = new ResponseMessage(HttpStatus.NOT_MODIFIED.value(), HttpStatus.NOT_MODIFIED.getReasonPhrase(),
                                new MessageContent(HttpStatus.NOT_MODIFIED.value(), HttpStatus.NOT_MODIFIED.getReasonPhrase(),
                                        id));
                    }
                }

            }

        }
        return response;
    }

    //Create RoleModulePermission
    public ResponseMessage createUserRoleAndPermission(Map<String, Object> bodyParam, Map<String, String> headerMap) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                    new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                            "Bạn chưa đăng nhập"));
        } else {
            if (bodyParam == null || bodyParam.isEmpty()) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            } else {
                String roleName = (String) bodyParam.get("roleCode");
                String roleCode = (String) bodyParam.get("roleCode");
                String roleCodeOld = (String) bodyParam.get("roleCodeOld");
                String roleDetail = (String) bodyParam.get("roleDetail");
                String action = (String) bodyParam.get("action");
                String createdBy = dto.getUserName();

                Role newRole = new Role();
                newRole.setRoleName(roleName);
                newRole.setRoleCode(roleCode);
                newRole.setRoleDetail(roleDetail);
                newRole.setCreatedBy(createdBy);
                newRole.setCreatedBy(createdBy);
                newRole.setIsAdmin(0);
                newRole.setIsDelete(0);
                newRole.setCreatedAt(new Date());

                //remove role page permission
                rolePagePermissionService.removeRolePagePermissionByRoleCode(roleCode);
                
                try {
                    //check tên quyền đã tồn tại chưa  
                    if (action.equals("INSERT_RULE")) {
                        Role existRole = roleService.findByRoleCode(roleCode);
                        if (existRole == null) {
                            roleService.save(newRole);
                        }else{
                            return new ResponseMessage(HttpStatus.OK.value(), "Đã tồn tại quyền  " + roleCode, 
                                    new MessageContent(HttpStatus.OK.value(), "Đã tồn tại quyền  " + roleCode, null));
                        }
                    }else if (action.equals("UPDATE_RULE")){
                        Role roleOld = roleService.findByRoleCode(roleCodeOld);
                        //update role
                        boolean result = roleService.updateRoleCode(roleCode,roleDetail,roleCodeOld);
                        //update role_user
                        if(result && !roleCode.equals(roleCodeOld)){                           
                            List<RoleUser> lstRoleUser =  roleUserService.findByRoleCode(roleOld);
                            if(lstRoleUser != null && lstRoleUser.size() > 0){
                                for(int i =0;i<=lstRoleUser.size();i++){
                                    boolean resultSecond = roleUserService.update(lstRoleUser.get(i),roleCode);
                                }                               
                            }
                            
                        }
                    }

                    ArrayList<HashMap<String, String>> pagePerMissionDTOs = (ArrayList<HashMap<String, String>>) bodyParam.get("lstApiAddRole");
                    //insert role_path_permission
                    for (int i = 0; i < pagePerMissionDTOs.size(); i++) {
                        RolePagePermission permission = new RolePagePermission();

                        int createVal = Integer.parseInt(String.valueOf(pagePerMissionDTOs.get(i).get("canCreate")));
                        int readVal = Integer.parseInt(String.valueOf(pagePerMissionDTOs.get(i).get("canRead")));
                        int updateVal = Integer.parseInt(String.valueOf(pagePerMissionDTOs.get(i).get("canUpdate")));
                        int deleteVal = Integer.parseInt(String.valueOf(pagePerMissionDTOs.get(i).get("canDelete")));
                        String pageUrl = pagePerMissionDTOs.get(i).get("pageUrl");
                        String pageName = pagePerMissionDTOs.get(i).get("pageName");

                        permission.setPageUrl(pageUrl);
                        permission.setPageName(pageName);
                        permission.setRoleCode(roleCode);
                        permission.setCanCreate(createVal);
                        permission.setCanRead(readVal);
                        permission.setCanUpdate(updateVal);
                        permission.setCanDelete(deleteVal);

                        //mrd bo sung
                        //String invalidData = permissionValidation.validateUpsertPermissionDTO(permission, "INSERT");
                        String invalidData = null;
                        if (invalidData != null) {
                            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), invalidData,
                                    new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
                        } else {
                            //Insert Rolepath_permission

                            RolePagePermission newPermission = new RolePagePermission();
                            newPermission.setPageUrl(pageUrl);
                            newPermission.setPageName(pageName);
                            newPermission.setRoleCode(roleCode);
                            newPermission.setIsDelete(0);
                            newPermission.setCanCreate(createVal);
                            newPermission.setCanRead(readVal);
                            newPermission.setCanUpdate(updateVal);
                            newPermission.setCanDelete(deleteVal);
                            newPermission.setIsDelete(0);
                            newPermission.setCreatedAt(new Timestamp(System.currentTimeMillis()));

                            try {
                                rolePagePermissionService.save(newPermission);
                                LOGGER.info("fun[createUserRoleAndPermission] save");
                            } catch (Exception ex) {
                                LOGGER.error("Error to save role path permission >>> " + ex.toString());
                                ex.printStackTrace();
                            }

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                response = new ResponseMessage(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), new MessageContent("success"));

            }
        }
        return response;
    }
}
