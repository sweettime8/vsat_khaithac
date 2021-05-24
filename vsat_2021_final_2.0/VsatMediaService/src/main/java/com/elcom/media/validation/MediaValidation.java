package com.elcom.media.validation;

import com.elcom.media.model.dto.request.media.AddCommentRequest;
import com.elcom.media.model.dto.request.media.GetTotalMediaByObjectRequest;
import com.elcom.media.model.dto.request.media.SearchListMediaRequest;
import com.elcom.media.utils.DateUtil;
import com.elcom.media.utils.StringUtil;

public class MediaValidation extends AbstractValidation {
    
    public String validateSearchList(SearchListMediaRequest req) {

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
        
        if( req.getCurrentPage() == null || req.getCurrentPage() == 0 )
            getMessageDes().add("currentPage không được để trống");
        if( req.getRowsPerPage() == null || req.getRowsPerPage() == 0 )
            getMessageDes().add("rowsPerPage không được để trống");
        
        return !isValid() ? this.buildValidationMessage() : null;
    }
    
    public String validateGetTotalByObjectId(GetTotalMediaByObjectRequest req) {

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
        
        if ( StringUtil.isNullOrEmpty(req.getObjId()) )
            getMessageDes().add("objId không được để trống");
        
        return !isValid() ? this.buildValidationMessage() : null;
    }
    
    public String validateAddComment(AddCommentRequest request) {

        if ( request.getCommentTypeId()==null || request.getCommentTypeId()== 0 )
            getMessageDes().add("commentTypeId không được để trống");
        if ( StringUtil.isNullOrEmpty(request.getRefId()) )
            getMessageDes().add("refId không được để trống");
        if ( StringUtil.isNullOrEmpty(request.getContent()) )
            getMessageDes().add("content không được để trống");
        if ( StringUtil.isNullOrEmpty(request.getCreateUser()) )
            getMessageDes().add("createUser không được để trống");
        
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
