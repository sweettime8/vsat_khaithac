/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.contact.validation;

import com.elcom.contact.model.contact.AisInfo;
import com.elcom.contact.model.dto.request.contact.AddContactIpRequest;
import com.elcom.contact.model.dto.request.contact.AddVesselRequest;
import com.elcom.contact.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Admin
 */
public class VesselValidation extends AbstractValidation {

    private static final Logger LOGGER = LoggerFactory.getLogger(VesselValidation.class);

    public String validateInsertVessel(AddVesselRequest ais) {
        if (ais.getMmsi() == 0 || ais.getMmsi() == null) {
            return "MMSI không được để trống hoặc bằng 0";
        }
        if (String.valueOf(ais.getMmsi()).length() > 10 ) {
            return "MMSI không được quá 10 ký tự";
        }
        if (StringUtil.isNullOrEmpty(ais.getVesselName())) {
            return "Tên tàu không được để trống";
        }
        if (ais.getVesselName().length() > 100) {
            return "Tên tàu không được vượt quá 100 ký tự";
        }        
        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String validateIP(AddContactIpRequest data) {
        if (data == null) {
            return "payLoad không hợp lệ";
        } else if (data.getNote().length() > 500) {
            getMessageDes().add("Ghi chú không được vượt quá 500 ký tự");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
