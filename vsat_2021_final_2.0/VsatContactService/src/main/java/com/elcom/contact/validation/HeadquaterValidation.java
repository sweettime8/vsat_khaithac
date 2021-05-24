/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.contact.validation;

import com.elcom.contact.model.dto.request.contacLand.AddContactLandIpRequest;
import com.elcom.contact.model.dto.request.contacLand.AddHeadquaterRequest;
import com.elcom.contact.model.dto.request.contacLand.ContactLandPhoneRequest;
import com.elcom.contact.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Admin
 */
public class HeadquaterValidation extends AbstractValidation {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeadquaterValidation.class);

    public String validateInsertHeadquater(AddHeadquaterRequest data) {
        if (data == null) {
            return "payLoad không hợp lệ";
        } else if (StringUtil.isNullOrEmpty(data.getName())) {
            getMessageDes().add("Tên không được để trống ");
        } else if (data.getNote().length() > 500) {
            getMessageDes().add("Ghi chú không được vượt quá 500 ký tự");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String validateUpdateHeadquater(AddHeadquaterRequest data) {
        if (data == null) {
            return "payLoad không hợp lệ";
        } else if (StringUtil.isNullOrEmpty(data.getName())) {
            getMessageDes().add("Tên không được để trống ");
        } else if (data.getNote().length() > 500) {
            getMessageDes().add("Ghi chú không được vượt quá 500 ký tự");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
    
    public String validateIP(AddContactLandIpRequest data) {
        if (data == null) {
            return "payLoad không hợp lệ";
        } else if (data.getNote().length() > 500) {
            getMessageDes().add("Ghi chú không được vượt quá 500 ký tự");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
    
    public String validatePhone(ContactLandPhoneRequest data) {
        if (data == null) {
            return "payLoad không hợp lệ";
        } else if (data.getNote().length() > 500) {
            getMessageDes().add("Ghi chú không được vượt quá 500 ký tự");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
