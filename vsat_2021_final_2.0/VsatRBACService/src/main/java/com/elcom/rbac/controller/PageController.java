/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.rbac.controller;

import com.elcom.gateway.message.MessageContent;
import com.elcom.gateway.message.ResponseMessage;
import com.elcom.rbac.constant.Constant;
import com.elcom.rbac.model.RolePagePermission;
import com.elcom.rbac.model.dto.AuthorizationResponseDTO;
import com.elcom.rbac.model.dto.RolePathPermissionDTO;
import com.elcom.rbac.service.PathService;
import com.elcom.rbac.service.RoleModulePermissionService;
import com.elcom.rbac.service.RolePagePermissionService;
import com.elcom.rbac.utils.StringUtil;
import com.elcom.rbac.validation.PathValidation;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Admin
 */
@Controller
public class PageController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PathController.class);


    @Autowired
    private PathService pageService;
    
    @Autowired
    private RolePagePermissionService rolePagePermissionService;

    public ResponseMessage getListPageOfRole(Map<String, String> headerMap, String urlParam) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                    new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                            "Bạn chưa đăng nhập"));
        } else {
            Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
            String roleCode = params.get("roleCode");

            List<RolePagePermission> pageList = rolePagePermissionService.findByRoleCode(roleCode);
            if (pageList != null && !pageList.isEmpty()) {
                   response = new ResponseMessage(new MessageContent(pageList));
            }else{
                   response = new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                                new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                                        "Không tìm thấy Page ứng với roleCode " + roleCode));
            }      
        }
        return response;
    }
}
