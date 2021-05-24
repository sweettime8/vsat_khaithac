package com.elcom.rule.bussiness;

import com.elcom.rule.constant.Constant;
import com.elcom.rule.controller.BaseController;
import com.elcom.rule.model.dto.UpsertProcessResult;
import com.elcom.rule.model.dto.VesselDTO;
import com.elcom.rule.model.dto.request.AddVessellToGroup;
import com.elcom.rule.model.vessel.VesselGroup;
import com.elcom.rule.utils.StringUtil;
import com.elcom.rule.validation.RuleValidation;
import com.elcom.gateway.message.MessageContent;
import com.elcom.gateway.message.ResponseMessage;
import com.elcom.rule.model.dto.DetailRuleEventDTO;
import com.elcom.rule.model.dto.RuleEventNotifiDTO;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import com.elcom.rule.service.RuleService;

/**
 *
 * @author anhdv
 */
@Controller
public class RuleBusiness extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleBusiness.class);
    
    @Autowired
    private RuleService ruleBusiness;
    
    /** Get danh sách sự kiện theo rule
     * Gọi qua Gateway, check Auth, check RBAC
     * @param bodyParam
     * @param headerMap
     * @return list item */
    public ResponseMessage findRuleEvents(Map<String, Object> bodyParam, Map<String, String> headerMap) {
        /*AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null)
            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else if( !authorizeRBAC(methodType, auth.getUuid(), requestPath) )
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                           new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), String.format("Bạn không có quyền [%s] [%s]", methodType, requestPath)));
        else {*/
            String startTime = (String) bodyParam.get("startTime");
            String endTime = (String) bodyParam.get("endTime");
            Integer ruleId = StringUtil.objectToInteger(bodyParam.get("ruleId"));
            Integer groupObjectId = StringUtil.objectToInteger(bodyParam.get("groupObjectId"));
            String objId = (String) bodyParam.get("objId");
            String objName = (String) bodyParam.get("objName");
            Integer currentPage = StringUtil.objectToInteger(bodyParam.get("currentPage"));
            Integer rowsPerPage = StringUtil.objectToInteger(bodyParam.get("rowsPerPage"));

            String validationMsg = new RuleValidation().validateSearchRuleEventList(startTime, endTime, currentPage, rowsPerPage);
            if( validationMsg != null )
                return new ResponseMessage(HttpStatus.OK.value(), validationMsg,
                           new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, null));
            else {
                Page<RuleEventNotifiDTO> result = this.ruleBusiness.findRuleEvents(startTime, endTime
                        , ruleId != null ? ruleId.longValue() : null, groupObjectId != null ? groupObjectId.longValue() : null
                        , objId, objName, currentPage, rowsPerPage);
                if ( result == null || !result.hasContent() )
                    return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                               new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
                else
                    return new ResponseMessage(new MessageContent(result.getContent(), result.getTotalElements()));
            }
        //}
    }
    
    /** Get chi tiết sự kiện theo rule
     * Gọi qua Gateway, check Auth, check RBAC
     * @param bodyParam
     * @param headerMap
     * @return list item */
    public ResponseMessage findDetailRuleEvent(Map<String, Object> bodyParam, Map<String, String> headerMap) {
        /*AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null)
            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else if( !authorizeRBAC(methodType, auth.getUuid(), requestPath) )
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                           new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), String.format("Bạn không có quyền [%s] [%s]", methodType, requestPath)));
        else {*/
            String ruleNotifyUuid = (String) bodyParam.get("ruleNotifyUuid");
            String objId = (String) bodyParam.get("objId");

            String validationMsg = new RuleValidation().validateFindDetailRuleEvent(ruleNotifyUuid, objId);
            if( validationMsg != null )
                return new ResponseMessage(HttpStatus.OK.value(), validationMsg,
                           new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, null));
            else {
                DetailRuleEventDTO result = this.ruleBusiness.findDetailRuleEvent(ruleNotifyUuid, objId);
                if ( result == null )
                    return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                               new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
                else
                    return new ResponseMessage(new MessageContent(result));
            }
        //}
    }
    
    /** Tổng sự kiện theo đối tượng
     * Gọi qua Gateway, check Auth, check RBAC
     * @param bodyParam
     * @param headerMap
     * @return list item */
    public ResponseMessage countRuleEventByObjId(Map<String, Object> bodyParam, Map<String, String> headerMap) {
        /*AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null)
            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else if( !authorizeRBAC(methodType, auth.getUuid(), requestPath) )
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                           new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), String.format("Bạn không có quyền [%s] [%s]", methodType, requestPath)));
        else {*/
            String startTime = (String) bodyParam.get("startTime");
            String endTime = (String) bodyParam.get("endTime");
            String objId = (String) bodyParam.get("objId");

            String validationMsg = new RuleValidation().validateCountRuleEventByObjId(startTime, endTime, objId);
            if( validationMsg != null )
                return new ResponseMessage(HttpStatus.OK.value(), validationMsg,
                           new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, null));
            else {
                Long result = this.ruleBusiness.countRuleEventByObjId(startTime, endTime, objId);
                return new ResponseMessage(new MessageContent(result));
            }
        //}
    }
    
    /** Đánh dấu là đã xem cho sự kiện
     * Gọi qua Gateway, check Auth, check RBAC
     * @param bodyParam
     * @param headerMap
     * @return list item */
    public ResponseMessage updateRuleEventReadStatus(Map<String, Object> bodyParam, Map<String, String> headerMap) {
        /*AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null)
            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else if( !authorizeRBAC(methodType, auth.getUuid(), requestPath) )
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                           new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), String.format("Bạn không có quyền [%s] [%s]", methodType, requestPath)));
        else {*/
            String ruleNotifyUuid = (String) bodyParam.get("ruleNotifyUuid");
            if( !StringUtil.validUuid(ruleNotifyUuid) )
                return new ResponseMessage(HttpStatus.OK.value(), "ruleNotifyUuid không hợp lệ",
                           new MessageContent(HttpStatus.BAD_REQUEST.value(), "ruleNotifyUuid không hợp lệ", null));
            else
                return new ResponseMessage(
                            new MessageContent(
                                this.ruleBusiness.updateRuleEventReadStatus(ruleNotifyUuid)
                            )
                );
        //}
    }
    
    
    
    
    
    /** Get list nhóm tàu
     * Gọi qua Gateway, check Auth, check RBAC
     * @param bodyParam
     * @param headerMap
     * @return list item */
    public ResponseMessage searchVesselGroupList(Map<String, Object> bodyParam, Map<String, String> headerMap) {
        /*AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null)
            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else if( !authorizeRBAC(methodType, auth.getUuid(), requestPath) )
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                           new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), String.format("Bạn không có quyền [%s] [%s]", methodType, requestPath)));
        else {*/
            String name = (String) bodyParam.get("name");
            String sortBy = (String) bodyParam.get("sortBy");
            Integer isActive = StringUtil.objectToInteger(bodyParam.get("isActive"));
            Integer currentPage = StringUtil.objectToInteger(bodyParam.get("currentPage"));
            Integer rowsPerPage = StringUtil.objectToInteger(bodyParam.get("rowsPerPage"));

            String validationMsg = new RuleValidation().validateSearchVesselGroupList(name, isActive, currentPage, rowsPerPage, sortBy);
            if( validationMsg != null )
                return new ResponseMessage(HttpStatus.OK.value(), validationMsg,
                           new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, null));
            else {
                Page<VesselGroup> result = this.ruleBusiness.searchVesselGroupList(name, isActive, currentPage, rowsPerPage, sortBy);
                if ( result==null || !result.hasContent() )
                    return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                               new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
                else
                    return new ResponseMessage(new MessageContent(result.getContent(), result.getTotalElements()));
            }
        //}
    }
    
    /** Thêm/sửa nhóm tàu
     *  Gọi qua Gateway, check Auth, check RBAC
     * @param bodyParam
     * @param headerMap
     * @param methodType
     * @param saveType
     * @param requestPath
     * @return true|false */
    public ResponseMessage saveVesselGroup(Map<String, Object> bodyParam, Map<String, String> headerMap, String methodType, String saveType, String requestPath) {
        ResponseMessage response = null;
        try {
            /*AuthorizationResponseDTO auth = authenToken(headerMap);
            if( auth == null )
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                           new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
            else if( !authorizeRBAC(methodType, auth.getUuid(), requestPath) )
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                           new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), String.format("Bạn không có quyền [%s] [%s]", methodType, requestPath)));
            else {*/
                if (bodyParam == null || bodyParam.isEmpty())
                    response = new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                               new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                else {
                    Integer id = (Integer) bodyParam.get("id");
                    String name = (String) bodyParam.get("name");
                    String note = (String) bodyParam.get("note");
                    //Integer groupType = StringUtil.objectToInteger(bodyParam.get("groupType"));
                    Integer isActive = StringUtil.objectToInteger(bodyParam.get("isActive"));
                    String assignTo = (String) bodyParam.get("assignTo");
                    String createdBy = (String) bodyParam.get("createdBy");
                    String updatedBy = (String) bodyParam.get("updatedBy");
                    
                    VesselGroup vesselGroup = new VesselGroup(id != null ? id.longValue() : null, name, note, isActive,
                                                            createdBy, assignTo, updatedBy);
                    String validationMsg = new RuleValidation().validateSaveVesselGroup(vesselGroup, saveType);
                    if( validationMsg != null )
                        response = new ResponseMessage(HttpStatus.OK.value(), validationMsg,
                                   new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, null));
                    else {
                        UpsertProcessResult upsertProcessResult = this.ruleBusiness.saveVesselGroup(vesselGroup);
                        if( !upsertProcessResult.isSuccess() )
                            response = new ResponseMessage(HttpStatus.OK.value(), upsertProcessResult.getMessage(),
                                       new MessageContent(HttpStatus.OK.value(), upsertProcessResult.getMessage(), false));
                        else
                            response = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.OK.toString()
                                        , new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), true));
                    }
                }
            //}
        } catch (Exception ex) {
            LOGGER.error(StringUtil.printException(ex));
        }
        return response;
    }
    
    /** Lấy chi tiết nhóm tàu.
     * Gọi qua Gateway, check Auth, check RBAC
     * @param headerMap
     * @param id
     * @param methodType
     * @param requestPath
     * @return item detail */
    public ResponseMessage findDetailVesselGroup(Map<String, String> headerMap, String id, String methodType, String requestPath) {
        ResponseMessage response;
        /*AuthorizationResponseDTO auth = authenToken(headerMap);
        if( auth == null )
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else if( !authorizeRBAC(methodType, auth.getUuid(), requestPath) )
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), String.format("Bạn không có quyền [%s] [%s]", methodType, requestPath)));
        else {*/
            if( !StringUtil.isNumeric(id) )
                response = new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                               new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            else {
                VesselGroup result = this.ruleBusiness.findDetailVesselGroup(Long.parseLong(id));
                response = new ResponseMessage(new MessageContent(result));
            }
        //}
        return response;
    }
    
    /** Xóa nhóm tàu với điều kiện nhóm chưa chứa tàu nào.
     * Gọi qua Gateway, check Auth, check RBAC
     * @param headerMap
     * @param id
     * @param methodType
     * @param requestPath
     * @return true|false */
    public ResponseMessage removeVesselGroup(Map<String, String> headerMap, String id, String methodType, String requestPath) {
        ResponseMessage response;
        /*AuthorizationResponseDTO auth = authenToken(headerMap);
        if( auth == null )
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else if( !authorizeRBAC(methodType, auth.getUuid(), requestPath) )
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), String.format("Bạn không có quyền [%s] [%s]", methodType, requestPath)));
        else {*/
            if( !StringUtil.isNumeric(id) )
                response = new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                               new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            else {
                boolean result = this.ruleBusiness.removeVesselGroup(Long.parseLong(id));
                response = new ResponseMessage(new MessageContent(result));
            }
        //}
        return response;
    }
    
    /** Get list tàu
     * Gọi qua Gateway, check Auth, check RBAC
     * @param bodyParam
     * @param headerMap
     * @return list item */
    public ResponseMessage searchVesselList(Map<String, Object> bodyParam, Map<String, String> headerMap) {
        /*AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null)
            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else if( !authorizeRBAC(methodType, auth.getUuid(), requestPath) )
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                           new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), String.format("Bạn không có quyền [%s] [%s]", methodType, requestPath)));
        else {*/
            String mmsi = (String) bodyParam.get("mmsi");
            String vesselName = (String) bodyParam.get("vesselName");
            Integer countryId = StringUtil.objectToInteger(bodyParam.get("countryId"));
            Integer vesselTypeId = StringUtil.objectToInteger(bodyParam.get("vesselTypeId"));
            String ip = (String) bodyParam.get("ip");
            String phone = (String) bodyParam.get("phone");
            Integer currentPage = StringUtil.objectToInteger(bodyParam.get("currentPage"));
            Integer rowsPerPage = StringUtil.objectToInteger(bodyParam.get("rowsPerPage"));

            String validationMsg = new RuleValidation().validateSearchVesselList(mmsi, vesselName, countryId, vesselTypeId, ip, phone, currentPage, rowsPerPage);
            if( validationMsg != null )
                return new ResponseMessage(HttpStatus.OK.value(), validationMsg,
                           new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, null));
            else {
                Page<VesselDTO> result = this.ruleBusiness.searchVesselList(mmsi, vesselName, countryId, vesselTypeId, ip, phone, currentPage, rowsPerPage);
                if ( result==null || !result.hasContent() )
                    return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                               new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
                else
                    return new ResponseMessage(new MessageContent(result.getContent(), result.getTotalElements()));
            }
        //}
    }
    
    /** Lấy chi tiết tàu.
     * Gọi qua Gateway, check Auth, check RBAC
     * @param headerMap
     * @param mmsi
     * @param methodType
     * @param requestPath
     * @return item detail */
    public ResponseMessage findDetailVessel(Map<String, String> headerMap, String mmsi, String methodType, String requestPath) {
        ResponseMessage response;
        /*AuthorizationResponseDTO auth = authenToken(headerMap);
        if( auth == null )
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else if( !authorizeRBAC(methodType, auth.getUuid(), requestPath) )
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), String.format("Bạn không có quyền [%s] [%s]", methodType, requestPath)));
        else {*/
            if( !StringUtil.isNumeric(mmsi) )
                response = new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                               new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            else {
                //MessageContent messageContent = this.ruleBusiness.findDetailVessel(new Long(mmsi));
                MessageContent messageContent = null;
                if ( messageContent==null || messageContent.getData()==null )
                    return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                               new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
                else
                    return new ResponseMessage(messageContent);
            }
        //}
        return response;
    }

    public ResponseMessage addVesselToGroup(Map<String, Object> bodyParam, String pathParam, String methodType, String requestPath) {
        ResponseMessage response;
        /*AuthorizationResponseDTO auth = authenToken(headerMap);
        if( auth == null )
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else if( !authorizeRBAC(methodType, auth.getUuid(), requestPath) )
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), String.format("Bạn không có quyền [%s] [%s]", methodType, requestPath)));
        else {*/

        ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
        AddVessellToGroup data = mapper.convertValue(bodyParam, AddVessellToGroup.class);
        if(data.getMmsi()==null){
            response = new ResponseMessage(HttpStatus.OK.value(), "ID đối tượng không được để trống.",
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), "ID đối tượng không được để trống.", null));
            return response;
        }

        VesselGroup resVesselGroup=null;
        if(data.getType()!=null&&data.getType()==2){
            VesselGroup vesselGroup =new VesselGroup();
            vesselGroup.setName(data.getGroupName());
            vesselGroup.setGroupType(data.getGroupType());
            resVesselGroup=this.ruleBusiness.saveAndReturnVesselGroup(vesselGroup);
            if(resVesselGroup==null){
                response = new ResponseMessage(HttpStatus.OK.value(), "Nhóm đã tồn tại hoặc tạo nhóm mới thất bại.",
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), "Nhóm đã tồn tại hoặc tạo nhóm mới thất bại.", null));
                return response;
            }
            data.setVesselGroupId(resVesselGroup.getId());
        }

//        if( !StringUtil.isNumeric(pathParam) )
//            response = new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
//                    new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
//        else {
            MessageContent messageContent = this.ruleBusiness.addVesselToGroup(data);
            if ( messageContent==null ) {
                return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                        new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
            }
            else {
                response= new ResponseMessage(messageContent);
            }
//        }
        //}
        return response;
    }


}
