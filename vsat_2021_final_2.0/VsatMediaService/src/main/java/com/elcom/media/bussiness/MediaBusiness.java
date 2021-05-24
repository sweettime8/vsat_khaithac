package com.elcom.media.bussiness;

import com.elcom.gateway.message.MessageContent;
import com.elcom.gateway.message.ResponseMessage;
import com.elcom.media.constant.Constant;
import com.elcom.media.controller.BaseController;
import com.elcom.media.model.dto.request.media.AddCommentRequest;
import com.elcom.media.model.dto.request.media.DeleteFileMediaRequest;
import com.elcom.media.model.dto.request.media.GetDetailMediaRelationRequest;
import com.elcom.media.model.dto.request.media.GetDetailMediaRequest;
import com.elcom.media.model.dto.request.media.GetListlMediaByIdRelationRequest;
import com.elcom.media.model.dto.request.media.GetTotalMediaByObjectRequest;
import com.elcom.media.model.dto.request.media.SearchListMediaRequest;
import com.elcom.media.model.media.Comment;
import com.elcom.media.service.MediaService;
import com.elcom.media.utils.StringUtil;
import com.elcom.media.validation.MediaValidation;
import com.elcom.vsat.model.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;

@Controller
public class MediaBusiness extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaBusiness.class);
    
    @Autowired
    private MediaService mediaService;
    
    public ResponseMessage getListMediaTypes(Map<String, String> headerParam) {
        ResponseMessage responseMessage=new ResponseMessage();
        responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            List<MediaType> lst = mediaService.getListMediaTypes();
            if ( lst==null || lst.isEmpty() )
                return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                        new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
            else
                return new ResponseMessage(new MessageContent(lst));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
        }
        return responseMessage;
    }
    
    /** Get list Media
     * Service public để RestClient gọi qua Gateway, có check Auth
     * @param bodyParam
     * @param headerMap
     * @return list */
    public ResponseMessage search(Map<String, Object> bodyParam, Map<String, String> headerMap) {
        /*AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null)
            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                    new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else {*/
            if (bodyParam == null || bodyParam.isEmpty())
                return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                           new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            else {
                String startTime = (String) bodyParam.get("startTime");
                String endTime = (String) bodyParam.get("endTime");
                
                List<Integer> dataSources = null;
                Object dataSourceObj = bodyParam.get("dataSource");
                if( dataSourceObj!=null )
                    dataSources = (List<Integer>) dataSourceObj;
                
                List<Integer> mediaTypes = null;
                Object mediaTypeObj = bodyParam.get("mediaType");
                if( mediaTypeObj!=null )
                    mediaTypes = (List<Integer>) mediaTypeObj;
                
                List<String> fileTypes = null;
                Object fileTypesObj = bodyParam.get("fileType");
                if( fileTypesObj!=null )
                    fileTypes = (List<String>) fileTypesObj;
                
                String sourceIps = (String) bodyParam.get("sourceIps");
                String destIps = (String) bodyParam.get("destIps");
                String objId = (String) bodyParam.get("objId");
                Integer currentPage = StringUtil.objectToInteger(bodyParam.get("currentPage"));
                Integer rowsPerPage = StringUtil.objectToInteger(bodyParam.get("rowsPerPage"));
                
                SearchListMediaRequest request = new SearchListMediaRequest(startTime, endTime, dataSources
                        , mediaTypes, fileTypes, sourceIps, destIps, objId, currentPage, rowsPerPage);
                
                String validationMsg = new MediaValidation().validateSearchList(request);
                if( validationMsg != null )
                    return new ResponseMessage(HttpStatus.OK.value(), validationMsg,
                               new MessageContent(HttpStatus.OK.value(), validationMsg, null));
                else {
                    MessageContent messageContent = this.mediaService.search(request);
                    if ( messageContent==null || messageContent.getData()==null )
                        return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                                   new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
                    else
                        return new ResponseMessage(messageContent);
                }
            }
        //}
    }
    
    /** Get list Media đã xử lý
     * Service public để RestClient gọi qua Gateway, có check Auth
     * @param bodyParam
     * @param headerMap
     * @return list */
    public ResponseMessage searchMediaRelation(Map<String, Object> bodyParam, Map<String, String> headerMap) {
        /*AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null)
            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                    new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else {*/
            if (bodyParam == null || bodyParam.isEmpty())
                return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                           new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            else {
                String startTime = (String) bodyParam.get("startTime");
                String endTime = (String) bodyParam.get("endTime");
                
                List<Integer> dataSources = null;
                Object dataSourceObj = bodyParam.get("dataSource");
                if( dataSourceObj!=null )
                    dataSources = (List<Integer>) dataSourceObj;
                
                List<Integer> mediaTypes = null;
                Object mediaTypeObj = bodyParam.get("mediaType");
                if( mediaTypeObj!=null )
                    mediaTypes = (List<Integer>) mediaTypeObj;
                
                List<String> fileTypes = null;
                Object fileTypesObj = bodyParam.get("fileType");
                if( fileTypesObj!=null )
                    fileTypes = (List<String>) fileTypesObj;
                
                String sourceIps = (String) bodyParam.get("sourceIps");
                String destIps = (String) bodyParam.get("destIps");
                String objId = (String) bodyParam.get("objId");
                Integer currentPage = StringUtil.objectToInteger(bodyParam.get("currentPage"));
                Integer rowsPerPage = StringUtil.objectToInteger(bodyParam.get("rowsPerPage"));
                
                SearchListMediaRequest request = new SearchListMediaRequest(startTime, endTime, dataSources
                        , mediaTypes, fileTypes, sourceIps, destIps, objId, currentPage, rowsPerPage);
                
                String validationMsg = new MediaValidation().validateSearchList(request);
                if( validationMsg != null )
                    return new ResponseMessage(HttpStatus.OK.value(), validationMsg,
                               new MessageContent(HttpStatus.OK.value(), validationMsg, null));
                else {
                    MessageContent messageContent = this.mediaService.searchMediaRelation(request);
                    if ( messageContent==null || messageContent.getData()==null )
                        return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                                   new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
                    else
                        return new ResponseMessage(messageContent);
                }
            }
        //}
    }

    /** Tổng media của tàu/đối tượng ko xác định
     * Service public để RestClient gọi qua Gateway, có check Auth
     * @param bodyParam
     * @param headerMap
     * @return list */
    public ResponseMessage totalByObject(Map<String, Object> bodyParam, Map<String, String> headerMap) {
        /*AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null)
            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                    new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else {*/
            if (bodyParam == null || bodyParam.isEmpty())
                return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                           new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            else {
                String startTime = (String) bodyParam.get("startTime");
                String endTime = (String) bodyParam.get("endTime");
                
                List<Integer> dataSources = null;
                Object dataSourceObj = bodyParam.get("dataSource");
                if( dataSourceObj!=null )
                    dataSources = (List<Integer>) dataSourceObj;
                
                List<Integer> mediaTypes = null;
                Object mediaTypeObj = bodyParam.get("mediaType");
                if( mediaTypeObj!=null )
                    mediaTypes = (List<Integer>) mediaTypeObj;
                
                String sourceIps = (String) bodyParam.get("sourceIps");
                String destIps = (String) bodyParam.get("destIps");
                String objId = (String) bodyParam.get("objId");
                
                GetTotalMediaByObjectRequest request = new GetTotalMediaByObjectRequest(startTime, endTime, dataSources, mediaTypes, sourceIps, destIps, objId);
                
                String validationMsg = new MediaValidation().validateGetTotalByObjectId(request);
                if( validationMsg != null )
                    return new ResponseMessage(HttpStatus.OK.value(), validationMsg,
                               new MessageContent(HttpStatus.OK.value(), validationMsg, null));
                else {
                    MessageContent messageContent = this.mediaService.findTotalMediaByObjectId(request);
                    if ( messageContent==null || messageContent.getData()==null )
                        return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                                   new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
                    else
                        return new ResponseMessage(messageContent);
                }
            }
        //}
    }
    
    public ResponseMessage getDetailMediaRelation(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage responseMessage=new ResponseMessage();
        responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            GetDetailMediaRelationRequest data = mapper.convertValue(bodyParam, GetDetailMediaRelationRequest.class);
            MessageContent messageContent = mediaService.getDetailMediaRelation(data);
            if ( messageContent==null || messageContent.getData()==null )
                return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                        new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
            else
                return new ResponseMessage(messageContent);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
        }
        return responseMessage;
    }
    
    // Call internal
    public ResponseMessage getListMediaByRelationId(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage responseMessage=new ResponseMessage();
        responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            GetListlMediaByIdRelationRequest data = mapper.convertValue(bodyParam, GetListlMediaByIdRelationRequest.class);
            MessageContent messageContent = mediaService.getListMediaByRelationId(data);
            if ( messageContent==null || messageContent.getData()==null )
                return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                        new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
            else
                return new ResponseMessage(messageContent);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
        }
        return responseMessage;
    }
    
    public ResponseMessage getM3U8File(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage responseMessage=new ResponseMessage();
        responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            GetDetailMediaRequest data = mapper.convertValue(bodyParam, GetDetailMediaRequest.class);
            if( data==null || StringUtil.isNullOrEmpty(data.getFilePathLocal()) )
                return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                       new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            
            String m3u8FilePath = mediaService.getM3U8File(data);
            if ( StringUtil.isNullOrEmpty(m3u8FilePath) )
                return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                        new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
            else
                return new ResponseMessage(new MessageContent(m3u8FilePath));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
        }
        return responseMessage;
    }

    public ResponseMessage updateStatusMediaId(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response;
        /*AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null)
            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                    new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else if( !authorizeRBAC(methodType, auth.getUuid(), requestPath) )
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), String.format("Bạn không có quyền [%s] [%s]", methodType, requestPath)));
        else {*/
            if (bodyParam == null || bodyParam.isEmpty())
                return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                           new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            else {
                List<String> uuidKeys = null;
                Object uuidKeysObj = bodyParam.get("uuidKeys");
                if( uuidKeysObj!=null )
                    uuidKeys = (List<String>) uuidKeysObj;
                
                String updateType = (String) bodyParam.get("updateType");
                
                if( uuidKeys==null || uuidKeys.isEmpty() )
                    response = new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                                    new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                else {
                    if ( !mediaService.updateReadStatus(uuidKeys, StringUtil.nullToEmpty(updateType)) )
                        response = new ResponseMessage(HttpStatus.OK.value(), Constant.RESPONSE_UPDATE_FAILED,
                                   new MessageContent(HttpStatus.OK.value(), Constant.RESPONSE_UPDATE_FAILED, null));
                    else
                        response = new ResponseMessage(new MessageContent(true));
                }
            }
        //}
        return response;
    }

    public ResponseMessage addCommentCommon(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response;
        /*AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null)
            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                    new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else if( !authorizeRBAC(methodType, auth.getUuid(), requestPath) )
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), String.format("Bạn không có quyền [%s] [%s]", methodType, requestPath)));
        else {*/
            if (bodyParam == null || bodyParam.isEmpty())
                return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                           new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            else {
                Integer commentTypeId = StringUtil.objectToInteger(bodyParam.get("commentTypeId"));
                String refId = (String) bodyParam.get("refId");
                String hagtag = (String) bodyParam.get("hagtag");
                String content = (String) bodyParam.get("content");
                String createUser = (String) bodyParam.get("createUser");
                AddCommentRequest request = new AddCommentRequest(commentTypeId, refId, hagtag, content, createUser);
                
                String validationMsg = new MediaValidation().validateAddComment(request);
                if( validationMsg != null )
                    response = new ResponseMessage(HttpStatus.OK.value(), validationMsg,
                               new MessageContent(HttpStatus.OK.value(), validationMsg, null));
                else {
                    if ( !mediaService.addCommentCommon(request) )
                        response = new ResponseMessage(HttpStatus.OK.value(), Constant.RESPONSE_UPDATE_FAILED,
                                   new MessageContent(HttpStatus.OK.value(), Constant.RESPONSE_UPDATE_FAILED, null));
                    else
                        response = new ResponseMessage(new MessageContent(true));
                }
            }
        //}
        return response;
    }
    
    public ResponseMessage getListComment(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response;
        /*AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null)
            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                    new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else if( !authorizeRBAC(methodType, auth.getUuid(), requestPath) )
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), String.format("Bạn không có quyền [%s] [%s]", methodType, requestPath)));
        else {*/
            if (bodyParam == null || bodyParam.isEmpty())
                return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                           new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            else {
                String refId = (String) bodyParam.get("refId");
                Integer commentTypeId = (Integer) bodyParam.get("commentTypeId");
                if( StringUtil.isNullOrEmpty(refId) || commentTypeId == null )
                    response = new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                                    new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                else {
                    List<Comment> lst = mediaService.getListComment(refId, commentTypeId);
                    if ( lst==null || lst.isEmpty() )
                        response = new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                                new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
                    else
                        response = new ResponseMessage(new MessageContent(lst));
                }
            }
        //}
        return response;
    }
    
    public ResponseMessage deleteFile(Map<String, Object> bodyParam,Map<String, String> headerParam){
        ResponseMessage responseMessage=new ResponseMessage();
        responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            DeleteFileMediaRequest data = mapper.convertValue(bodyParam, DeleteFileMediaRequest.class);
            Boolean res= mediaService.deleteFileMedia(data);
            if(res!=null&&res!=false){
                responseMessage.setStatus(HttpStatus.OK.value());
//                responseMessage.setData(messageContent);
            }
            else{
                responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
                responseMessage.setMessage("delete file error.");
            }

        } catch (Exception e) {
            LOGGER.error("delete file ==> error : ", e);
        }

        return responseMessage;
    }
}
