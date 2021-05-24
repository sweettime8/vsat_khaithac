package com.elcom.rule.validation;

import com.elcom.rule.model.vessel.VesselGroup;
import com.elcom.rule.utils.DateUtil;
import com.elcom.rule.utils.StringUtil;
import java.util.Arrays;

public class RuleValidation extends AbstractValidation {
    
    public String validateSearchRuleEventList(String startTime, String endTime, Integer currentPage, Integer rowsPerPage) {

        String timeFormat = "yyyy-MM-dd HH:mm:ss";
        
        if ( StringUtil.isNullOrEmpty(startTime) )
            getMessageDes().add("startTime không được để trống");
        else if ( !DateUtil.isValidTimeByFormat(startTime, timeFormat) )
            getMessageDes().add("startTime không đúng định dạng [" + timeFormat + "]");
        
        if ( StringUtil.isNullOrEmpty(endTime) )
            getMessageDes().add("endTime không được để trống");
        else if ( !DateUtil.isValidTimeByFormat(endTime, timeFormat) )
            getMessageDes().add("endTime không đúng định dạng [" + timeFormat + "]");
        
        if ( currentPage==null || currentPage == 0 )
            getMessageDes().add("currentPage không được để trống");
        
        if ( rowsPerPage==null || rowsPerPage == 0 )
            getMessageDes().add("rowsPerPage không được để trống");
        
        return !isValid() ? this.buildValidationMessage() : null;
    }
    
    public String validateFindDetailRuleEvent(String ruleNotifyUuid, String objId) {

        if ( !StringUtil.validUuid(ruleNotifyUuid) )
            getMessageDes().add("ruleNotifyUuid không đúng định dạng");
        if ( !StringUtil.validUuid(objId) && !StringUtil.isNumeric(objId) )
            getMessageDes().add("objId không đúng định dạng");
        
        return !isValid() ? this.buildValidationMessage() : null;
    }
    
    public String validateCountRuleEventByObjId(String startTime, String endTime, String objId) {

        String timeFormat = "yyyy-MM-dd HH:mm:ss";
        
        if ( StringUtil.isNullOrEmpty(startTime) )
            getMessageDes().add("startTime không được để trống");
        else if ( !DateUtil.isValidTimeByFormat(startTime, timeFormat) )
            getMessageDes().add("startTime không đúng định dạng [" + timeFormat + "]");
        
        if ( StringUtil.isNullOrEmpty(endTime) )
            getMessageDes().add("endTime không được để trống");
        else if ( !DateUtil.isValidTimeByFormat(endTime, timeFormat) )
            getMessageDes().add("endTime không đúng định dạng [" + timeFormat + "]");
        
        if ( StringUtil.isNullOrEmpty(objId) )
            getMessageDes().add("objId không được để trống");
        
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
    
    public String validateSaveVesselGroup(VesselGroup vesselGroup, String saveType) {

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
