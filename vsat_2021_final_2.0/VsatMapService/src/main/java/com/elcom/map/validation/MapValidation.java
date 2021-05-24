package com.elcom.map.validation;

import com.elcom.map.model.dto.request.map.AddAreaRequest;
import com.elcom.map.model.dto.request.media.SearchListAisRequest;
import com.elcom.map.model.vessel.VesselGroup;
import com.elcom.map.utils.DateUtil;
import com.elcom.map.utils.StringUtil;
import java.util.Arrays;

public class MapValidation extends AbstractValidation {
    
    public String validateAddAreaRequest(AddAreaRequest req) {

        String timeFormat = "yyyy-MM-dd HH:mm:ss";
        
        if ( StringUtil.isNullOrEmpty(req.getName()) )
            getMessageDes().add("Name không được để trống");
        else if ( StringUtil.isNullOrEmpty(req.getValue()) )
            getMessageDes().add("Dữ liệu bản đồ không được để trống");
        return !isValid() ? this.buildValidationMessage() : null;
    }

}
