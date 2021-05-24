/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.contact.validation;

import com.elcom.contact.model.dto.request.contactObject.AddContactObjectIpRequest;
import com.elcom.contact.model.dto.request.contactObject.AddObjectUnInfoRequest;
import com.elcom.contact.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Admin
 */
public class UfoValidation extends AbstractValidation {

    private static final Logger LOGGER = LoggerFactory.getLogger(UfoValidation.class);

    public String validateInsertUfo(AddObjectUnInfoRequest data) {
        if (data == null) {
            return "payLoad không hợp lệ";
        } else if (StringUtil.isNullOrEmpty(data.getName())) {
            getMessageDes().add("Tên không được để trống ");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String validateUpdateUfo(AddObjectUnInfoRequest data) {
        if (data == null) {
            return "payLoad không hợp lệ";
        } else if (StringUtil.isNullOrEmpty(data.getName())) {
            getMessageDes().add("Tên không được để trống ");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
    
    public String validateIP(AddContactObjectIpRequest data){
        if (data == null) {
            return "payLoad không hợp lệ";
        } else if (data.getNote().length() > 500) {
            getMessageDes().add("Ghi chú không được vượt quá 500 ký tự");
        }
        return !isValid() ? this.buildValidationMessage() : null;        
    }
}
