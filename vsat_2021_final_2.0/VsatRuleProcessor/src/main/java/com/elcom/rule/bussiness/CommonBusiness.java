/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.rule.bussiness;

import com.elcom.rule.constant.Constant;
import com.elcom.rule.controller.BaseController;
import com.elcom.rule.model.vessel.Countries;
import com.elcom.rule.model.dto.AuthorizationResponseDTO;
import com.elcom.rule.model.vessel.VesselTypes;
import com.elcom.gateway.message.MessageContent;
import com.elcom.gateway.message.ResponseMessage;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import com.elcom.rule.service.RuleService;

/**
 *
 * @author ducnh
 */
@Controller
public class CommonBusiness extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonBusiness.class);

    @Autowired
    private RuleService aisService;

    public ResponseMessage getListCountry(Map<String, String> headerMap) {
        ResponseMessage response = null;
//        AuthorizationResponseDTO dto = authenToken(headerMap);
//        if (dto == null) {
//            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
//                    new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
//                            "Bạn chưa đăng nhập"));
//        }
        List<Countries> lstCountry = aisService.getListCountry();
        if (lstCountry != null && !lstCountry.isEmpty()) {
            response = new ResponseMessage(new MessageContent(lstCountry));
        } else {
            response = new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                    new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                            "Not found country"));
        }

        return response;
    }

    public ResponseMessage getListVesselType(Map<String, String> headerMap) {
        ResponseMessage response = null;
//        AuthorizationResponseDTO dto = authenToken(headerMap);
//        if (dto == null) {
//            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
//                    new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
//                            "Bạn chưa đăng nhập"));
//        }
//        
        List<VesselTypes> lstVesselType = aisService.getListVesselType();
        if (lstVesselType != null && !lstVesselType.isEmpty()) {
            response = new ResponseMessage(new MessageContent(lstVesselType));
        } else {
            response = new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                    new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                            "Not found vessel type"));
        }

        return response;
    }
}
