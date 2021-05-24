package com.elcom.ais.validation;

import com.elcom.ais.model.dto.request.media.SearchListAisRequest;
import com.elcom.ais.model.vessel.ObjectGroup;
import com.elcom.ais.model.vessel.VesselGroup;
import com.elcom.ais.utils.DateUtil;
import com.elcom.ais.utils.StringUtil;
import java.util.Arrays;

public class AisValidation extends AbstractValidation {
    
    public String validateSearchAisList(SearchListAisRequest req) {

        String timeFormat = "yyyy-MM-dd HH:mm:ss";
        
        if ( StringUtil.isNullOrEmpty(req.getStartTime()) )
            getMessageDes().add("startTime không được để trống");
        else if ( !DateUtil.isValidTimeByFormat(req.getStartTime(), timeFormat) )
            getMessageDes().add("startTime không đúng định dạng [" + timeFormat + "]");
        
        if ( StringUtil.isNullOrEmpty(req.getEndTime()) )
            getMessageDes().add("endTime không được để trống");
        else if ( !DateUtil.isValidTimeByFormat(req.getEndTime(), timeFormat) )
            getMessageDes().add("endTime không đúng định dạng [" + timeFormat + "]");
        
        if( !StringUtil.isNullOrEmpty(req.getSourceIps()) && !StringUtil.validIpV4(req.getSourceIps()) )
            getMessageDes().add("sourceIps không hợp lệ");
        
        if( !StringUtil.isNullOrEmpty(req.getDestIps()) && !StringUtil.validIpV4(req.getDestIps()) )
            getMessageDes().add("destIps không hợp lệ");
        
//        if( !StringUtil.isNullOrEmpty(req.getVesselGroupIds()) ) {
//            String[] lst = req.getVesselGroupIds().split(",");
//            for( String item : lst ) {
//                if( !StringUtil.isDigit(item) ) {
//                    getMessageDes().add("vesselGroupIds không hợp lệ [" + item + "]");
//                    break;
//                }
//            }
//        }
        
        if( !StringUtil.isNullOrEmpty(req.getMmsi()) && "0".equals(req.getMmsi()) )
            getMessageDes().add("mmsi phải lớn hơn 0");
        
        if( req.getDataSource()!=null && req.getDataSource().equals(0) )
            getMessageDes().add("dataSource phải lớn hơn 0");
        
        if( req.getTypeId()!=null && req.getTypeId().equals(0) )
            getMessageDes().add("typeId phải lớn hơn 0");
        
        if( req.getCountryId()!=null && req.getCountryId().equals(0) )
            getMessageDes().add("countryId phải lớn hơn 0");
        
        return !isValid() ? this.buildValidationMessage() : null;
    }
    
    public String validateSearchVesselGroupList(String name, Integer isActive, Integer currentPage, Integer rowsPerPage, String sortBy) {

        if( isActive!=null && isActive.toString().length() > 1 )
            getMessageDes().add("active không hợp lệ: ["+isActive+"]");

        if ( currentPage==null || currentPage == 0 )
            getMessageDes().add("currentPage không được để trống");
        if ( rowsPerPage==null || rowsPerPage == 0 )
            getMessageDes().add("rowsPerPage không được để trống");
        if ( !StringUtil.isNullOrEmpty(sortBy) ) {
            String[] validValues = { "id", "name", "groupType", "isActive", "createdTime", "updatedTime" };
            if ( !Arrays.asList(validValues).contains(sortBy.trim()) )
                getMessageDes().add("sortBy không hợp lệ: ["+sortBy+"]");
        }
        
        return !isValid() ? this.buildValidationMessage() : null;
    }
    
    public String validateSaveVesselGroup(ObjectGroup vesselGroup, String saveType) {

        if( "UPDATE".equals(saveType) && (vesselGroup.getId()==null || vesselGroup.getId().equals(0L)) )
            getMessageDes().add("id không được để trống");
        if ( StringUtil.isNullOrEmpty(vesselGroup.getName()) )
            getMessageDes().add("name không được để trống");
//        if ( vesselGroup.getGroupType()==null )
//            getMessageDes().add("groupType không được để trống");
        if( "UPDATE".equals(saveType) && (vesselGroup.getIsActive()==null) )
            getMessageDes().add("isActive không được để trống");
        if( vesselGroup.getIsActive()!=null && vesselGroup.getIsActive().toString().length() > 1 )
            getMessageDes().add("isActive không hợp lệ: ["+vesselGroup.getIsActive()+"]");

        return !isValid() ? this.buildValidationMessage() : null;
    }
    
    public String validateSearchVesselList(String mmsi, String vesselName, Integer countryId, Integer vesselTypeId, String ip, String phone, Integer currentPage, Integer rowsPerPage) {

        if ( !StringUtil.isNullOrEmpty(mmsi) && "0".equals(mmsi) )
            getMessageDes().add("mmsi không hợp lệ");
        
        if( countryId!=null && countryId == 0 )
            getMessageDes().add("countryId không hợp lệ");
        
        if( vesselTypeId!=null && vesselTypeId == 0 )
            getMessageDes().add("vesselTypeId không hợp lệ");

        if ( currentPage==null || currentPage == 0 )
            getMessageDes().add("currentPage không được để trống");
        
        if ( rowsPerPage==null || rowsPerPage == 0 )
            getMessageDes().add("rowsPerPage không được để trống");
        
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
