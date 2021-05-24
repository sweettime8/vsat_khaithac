package com.elcom.ais.bussiness;

import com.elcom.ais.config.ApplicationConfig;
import com.elcom.ais.constant.Constant;
import com.elcom.ais.controller.BaseController;
import com.elcom.ais.model.dto.UpsertProcessResult;
import com.elcom.ais.model.dto.VesselDTO;
import com.elcom.ais.model.dto.request.AddObjectToGroup;

import com.elcom.ais.model.dto.request.AdvanceSearch;
import com.elcom.ais.model.dto.request.media.SearchListAisRequest;
import com.elcom.ais.model.vessel.ObjectGroup;
import com.elcom.ais.model.vessel.VesselGroup;
import com.elcom.ais.service.AisService;
import com.elcom.ais.utils.StringUtil;
import com.elcom.ais.validation.AisValidation;
import com.elcom.gateway.message.MessageContent;
import com.elcom.gateway.message.ResponseMessage;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

/**
 *
 * @author anhdv
 */
@Controller
public class AisBusiness extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AisBusiness.class);
    
    @Autowired
    private AisService aisService;

    /**
     * Lấy danh sách TẤT CẢ vị trí của tất cả các đối tượng (màn hình khai thác
     * tập trung) Service public để RestClient gọi qua Gateway, có check Auth
     *
     * @param bodyParam
     * @param headerMap
     * @return list
     */
    public ResponseMessage searchAisList(Map<String, Object> bodyParam, Map<String, String> headerMap) {
        /*AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null)
            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else {*/
        if (bodyParam == null || bodyParam.isEmpty()) {
            return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
        } else {
            String startTime = (String) bodyParam.get("startTime");
            String endTime = (String) bodyParam.get("endTime");
            String sourceIps = (String) bodyParam.get("sourceIps");
            String destIps = (String) bodyParam.get("destIps");
            String mmsi = (String) bodyParam.get("mmsi");
            Integer dataSource = null;
            try {
                dataSource = (Integer) bodyParam.get("dataSource");
            } catch (Exception e) {
            }
            Integer typeId = null;
            try {
                typeId = (Integer) bodyParam.get("typeId");
            } catch (Exception e) {
            }
            Integer countryId = null;
            try {
                countryId = (Integer) bodyParam.get("countryId");
            } catch (Exception e) {
            }
            Integer isUfo = null;
            try {
                isUfo = (Integer) bodyParam.get("isUfo");
            } catch (Exception e) {
            }
            //String vesselGroupIds = (String) bodyParam.get("vesselGroupIds");

            Integer limit = (Integer) bodyParam.get("limit");
            if (limit == null || limit.equals(0)) // Không truyền limit thì cho lấy max theo số bản tin vị trí của xem chi tiết tàu
            {
                limit = ApplicationConfig.LIMIT_AIS_MSG_FOR_DETAIL_VESSEL;
            }
            if (limit > ApplicationConfig.LIMIT_AIS_MSG_SEARCH) { // Nếu vượt quá số bản tin vị trí được phép lấy.
                LOGGER.warn("limit value: [{}] exceed 100.000 records allowed, return BadRequest!", limit);
                return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            }

            SearchListAisRequest request = new SearchListAisRequest(startTime, endTime, sourceIps, destIps, mmsi,
                    dataSource, typeId, countryId, isUfo, null, null, limit);
            request.setSearchForList(true); // Important!

            String validationMsg = new AisValidation().validateSearchAisList(request);
            if (validationMsg != null) {
                return new ResponseMessage(HttpStatus.OK.value(), validationMsg,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, null));
            } else {
                MessageContent messageContent = this.aisService.searchAisList(request);
                if (messageContent == null || messageContent.getData() == null) {
                    return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                            new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
                } else {
                    return new ResponseMessage(messageContent);
                }
            }
        }
        //}
    }

    /**
     * Lấy danh sách vị trí HIỆN TẠI (mmsi unique) của tất cả các đối tượng (màn
     * hình khai thác tổng thể) Service public để RestClient gọi qua Gateway, có
     * check Auth
     *
     * @param bodyParam
     * @param headerMap
     * @return list
     */
    public ResponseMessage searchAisListGeneral(Map<String, Object> bodyParam, Map<String, String> headerMap) {
        /*AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null)
            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else {*/
        if (bodyParam == null || bodyParam.isEmpty()) {
            return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
        } else {
            String startTime = (String) bodyParam.get("startTime");
            String endTime = (String) bodyParam.get("endTime");
            String sourceIps = (String) bodyParam.get("sourceIps");
            String destIps = (String) bodyParam.get("destIps");
            String mmsi = (String) bodyParam.get("mmsi");
            Integer dataSource = null;
            try {
                dataSource = (Integer) bodyParam.get("dataSource");
            } catch (Exception e) {
            }
            Integer typeId = null;
            try {
                typeId = (Integer) bodyParam.get("typeId");
            } catch (Exception e) {
            }
            Integer countryId = null;
            try {
                countryId = (Integer) bodyParam.get("countryId");
            } catch (Exception e) {
            }
            Integer isUfo = 0;
            try {
                isUfo = (Integer) bodyParam.get("isUfo");
            } catch (Exception e) {
            }
//                String vesselGroupIds = (String) bodyParam.get("vesselGroupIds");

            List<Integer> areaIds = null;
            Object areaIdsObj = bodyParam.get("areaIds");
            if (areaIdsObj != null) {
                areaIds = (List<Integer>) areaIdsObj;
            }

            List<Integer> groupIds = null;
            Object groupIdsObj = bodyParam.get("groupIds");
            if (groupIdsObj != null) {
                groupIds = (List<Integer>) groupIdsObj;
            }

            SearchListAisRequest request = new SearchListAisRequest(startTime, endTime, sourceIps, destIps, mmsi,
                    dataSource, typeId, countryId, isUfo, areaIds, groupIds, null);

            String validationMsg = new AisValidation().validateSearchAisList(request);
            if (validationMsg != null) {
                return new ResponseMessage(HttpStatus.OK.value(), validationMsg,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, null));
            } else {
                MessageContent messageContent = this.aisService.searchAisListGeneral(request);
                if (messageContent == null || messageContent.getData() == null) {
                    return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                            new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
                } else {
                    return new ResponseMessage(messageContent);
                }
            }
        }
        //}
    }

    /**
     * Get list nhóm tàu Gọi qua Gateway, check Auth, check RBAC
     *
     * @param bodyParam
     * @param headerMap
     * @return list item
     */
    public ResponseMessage searchObjectGroupList(Map<String, Object> bodyParam, Map<String, String> headerMap) {
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

        String validationMsg = new AisValidation().validateSearchVesselGroupList(name, isActive, currentPage, rowsPerPage, sortBy);
        if (validationMsg != null) {
            return new ResponseMessage(HttpStatus.OK.value(), validationMsg,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, null));
        } else {
            Page<ObjectGroup> result = this.aisService.searchObjectGroupList(name, isActive, currentPage, rowsPerPage, sortBy);
            if (result == null || !result.hasContent()) {
                return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                        new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
            } else {
                return new ResponseMessage(new MessageContent(result.getContent(), result.getTotalElements()));
            }
        }
        //}
    }

    /**
     * Thêm/sửa nhóm tàu Gọi qua Gateway, check Auth, check RBAC
     *
     * @param bodyParam
     * @param headerMap
     * @param methodType
     * @param saveType
     * @param requestPath
     * @return true|false
     */
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
            if (bodyParam == null || bodyParam.isEmpty()) {
                response = new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            } else {
                Integer id = (Integer) bodyParam.get("id");
                String name = (String) bodyParam.get("name");
                String note = (String) bodyParam.get("note");
                //Integer groupType = StringUtil.objectToInteger(bodyParam.get("groupType"));
                Integer isActive = StringUtil.objectToInteger(bodyParam.get("isActive"));
                String assignTo = (String) bodyParam.get("assignTo");
                String createdBy = (String) bodyParam.get("createdBy");
                String updatedBy = (String) bodyParam.get("updatedBy");

                ObjectGroup objectGroup = new ObjectGroup(id != null ? id.longValue() : null, name, note, isActive,
                        createdBy, assignTo, updatedBy);
                String validationMsg = new AisValidation().validateSaveVesselGroup(objectGroup, saveType);
                if (validationMsg != null) {
                    response = new ResponseMessage(HttpStatus.OK.value(), validationMsg,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, null));
                } else {
                    UpsertProcessResult upsertProcessResult = this.aisService.saveVesselGroup(objectGroup);
                    if (!upsertProcessResult.isSuccess()) {
                        response = new ResponseMessage(HttpStatus.OK.value(), upsertProcessResult.getMessage(),
                                new MessageContent(HttpStatus.OK.value(), upsertProcessResult.getMessage(), false));
                    } else {
                        response = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.OK.toString(),
                                new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), true));
                    }
                }
            }
            //}
        } catch (Exception ex) {
            LOGGER.error(StringUtil.printException(ex));
        }
        return response;
    }

    /**
     * Lấy chi tiết nhóm tàu. Gọi qua Gateway, check Auth, check RBAC
     *
     * @param headerMap
     * @param id
     * @param methodType
     * @param requestPath
     * @return item detail
     */
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
        if (!StringUtil.isNumeric(id)) {
            response = new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
        } else {
            VesselGroup result = this.aisService.findDetailVesselGroup(Long.parseLong(id));
            response = new ResponseMessage(new MessageContent(result));
        }
        //}
        return response;
    }

    /**
     * Xóa nhóm tàu với điều kiện nhóm chưa chứa tàu nào. Gọi qua Gateway, check
     * Auth, check RBAC
     *
     * @param headerMap
     * @param id
     * @param methodType
     * @param requestPath
     * @return true|false
     */
    public ResponseMessage removeObjectGroup(Map<String, String> headerMap, String id, String methodType, String requestPath) {
        ResponseMessage response;
        /*AuthorizationResponseDTO auth = authenToken(headerMap);
        if( auth == null )
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else if( !authorizeRBAC(methodType, auth.getUuid(), requestPath) )
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), String.format("Bạn không có quyền [%s] [%s]", methodType, requestPath)));
        else {*/
        if (!StringUtil.isNumeric(id)) {
            response = new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
        } else {
            boolean result = this.aisService.removeObjectGroup(Long.parseLong(id));
            response = new ResponseMessage(new MessageContent(result));
        }
        //}
        return response;
    }

    /**
     * Get list tàu theo điều kiện tìm kiếm Gọi qua Gateway,
     *
     * @param bodyParam
     * @param headerMap
     * @return list item
     */
    public ResponseMessage searchVessel(Map<String, Object> bodyParam, Map<String, String> headerMap) {

        String mmsi = (String) bodyParam.get("mmsi");
        String vesselName = (String) bodyParam.get("vesselName");
        Integer countryId = StringUtil.objectToInteger(bodyParam.get("countryId"));
        Integer vesselTypeId = StringUtil.objectToInteger(bodyParam.get("vesselTypeId"));
        String ip = (String) bodyParam.get("ip");
        String phone = (String) bodyParam.get("phone");
        Integer currentPage = StringUtil.objectToInteger(bodyParam.get("currentPage"));
        Integer rowsPerPage = StringUtil.objectToInteger(bodyParam.get("rowsPerPage"));
        int advancedFlag = 0;
        AdvanceSearch advanceSearch = new AdvanceSearch();
        if (StringUtil.objectToInteger(bodyParam.get("advanceSearch")) == 1) { //tìm kiếm nâng cao
            advancedFlag = 1;
            advanceSearch.setAdvancePhone((String) bodyParam.get("advancePhone"));
            advanceSearch.setAdvancecallSign((String) bodyParam.get("advancecallSign"));
            if ((String) bodyParam.get("advanceMmsiMax") != "") {
                advanceSearch.setAdvanceMmsiMax(Integer.parseInt((String) bodyParam.get("advanceMmsiMax")));
            }
            if ((String) bodyParam.get("advanceMmsiMin") != "") {
                advanceSearch.setAdvanceMmsiMin(Integer.parseInt((String) bodyParam.get("advanceMmsiMin")));
            }
            if ((String) bodyParam.get("advanceHeightMax") != "") {
                advanceSearch.setAdvanceMmsiMax(Integer.parseInt((String) bodyParam.get("advanceHeightMax")));
            }
            if ((String) bodyParam.get("advanceHeightMin") != "") {
                advanceSearch.setAdvanceMmsiMax(Integer.parseInt((String) bodyParam.get("advanceHeightMin")));
            }
            if ((String) bodyParam.get("advanceIMOMin") != "") {
                advanceSearch.setAdvanceIMOMin(Integer.parseInt((String) bodyParam.get("advanceIMOMin")));
            }
            if ((String) bodyParam.get("advanceIMOMax") != "") {
                advanceSearch.setAdvanceIMOMax(Integer.parseInt((String) bodyParam.get("advanceIMOMax")));
            }
            if ((String) bodyParam.get("advanceWidthMin") != "") {
                advanceSearch.setAdvanceWidthMin(Integer.parseInt((String) bodyParam.get("advanceWidthMin")));
            }
            if ((String) bodyParam.get("advanceWidthMax") != "") {
                advanceSearch.setAdvanceWidthMax(Integer.parseInt((String) bodyParam.get("advanceWidthMax")));
            }
        }

        String validationMsg = new AisValidation().validateSearchVesselList(mmsi, vesselName, countryId, vesselTypeId, ip, phone, currentPage, rowsPerPage);
        if (validationMsg != null) {
            return new ResponseMessage(HttpStatus.OK.value(), validationMsg,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, null));
        } else {
            if (advancedFlag == 0) {
                Page<VesselDTO> result = this.aisService.searchVesselList(mmsi, vesselName, countryId, vesselTypeId, ip, phone, currentPage, rowsPerPage);
                if (result == null || !result.hasContent()) {
                    return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                            new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
                } else {
                    return new ResponseMessage(new MessageContent(result.getContent(), result.getTotalElements()));
                }
            } else {
                Page<VesselDTO> resultAdvance = this.aisService.searchVesselListAdvanced(mmsi, vesselName, countryId, vesselTypeId, ip, phone, currentPage, rowsPerPage, advanceSearch);
                if (resultAdvance == null || !resultAdvance.hasContent()) {
                    return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                            new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
                } else {
                    return new ResponseMessage(new MessageContent(resultAdvance.getContent(), resultAdvance.getTotalElements()));
                }
            }

        }

    }

    /**
     * Get list tàu Gọi qua Gateway, check Auth, check RBAC
     *
     * @param bodyParam
     * @param headerMap
     * @return list item
     */
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

        String validationMsg = new AisValidation().validateSearchVesselList(mmsi, vesselName, countryId, vesselTypeId, ip, phone, currentPage, rowsPerPage);
        if (validationMsg != null) {
            return new ResponseMessage(HttpStatus.OK.value(), validationMsg,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, null));
        } else {
            Page<VesselDTO> result = this.aisService.searchVesselList(mmsi, vesselName, countryId, vesselTypeId, ip, phone, currentPage, rowsPerPage);
            if (result == null || !result.hasContent()) {
                return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                        new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
            } else {
                return new ResponseMessage(new MessageContent(result.getContent(), result.getTotalElements()));
            }
        }
        //}
    }

    /**
     * Lấy chi tiết tàu. Gọi qua Gateway, check Auth, check RBAC
     *
     * @param headerMap
     * @param mmsi
     * @param methodType
     * @param requestPath
     * @return item detail
     */
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
        if (!StringUtil.isNumeric(mmsi)) {
            response = new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
        } else {
            MessageContent messageContent = this.aisService.findDetailVessel(new Long(mmsi));
            if (messageContent == null || messageContent.getData() == null) {
                return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                        new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
            } else {
                return new ResponseMessage(messageContent);
            }
        }
        //}
        return response;
    }

    public ResponseMessage addObjectToGroup(Map<String, Object> bodyParam, String pathParam, String methodType, String requestPath) {
        ResponseMessage response =new ResponseMessage();
        /*AuthorizationResponseDTO auth = authenToken(headerMap);
        if( auth == null )
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else if( !authorizeRBAC(methodType, auth.getUuid(), requestPath) )
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), String.format("Bạn không có quyền [%s] [%s]", methodType, requestPath)));
        else {*/

        ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
        AddObjectToGroup data = mapper.convertValue(bodyParam, AddObjectToGroup.class);
        if (data.objId == null) {
            response = new ResponseMessage(HttpStatus.OK.value(), "ID đối tượng không được để trống.",
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), "ID đối tượng không được để trống.", null));
            return response;
        }

        ObjectGroup resObjectGroup = null;
        if (data.getType() != null && data.getType() == 2) {
            ObjectGroup objectGroup = new ObjectGroup();
            objectGroup.setName(data.getGroupName());
            objectGroup.setGroupType(data.getGroupType());
            resObjectGroup = this.aisService.saveAndReturnObjectGroup(objectGroup);
            if (resObjectGroup == null) {
                response = new ResponseMessage(HttpStatus.OK.value(), "Nhóm đã tồn tại hoặc tạo nhóm mới thất bại.",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Nhóm đã tồn tại hoặc tạo nhóm mới thất bại.", null));
                return response;
            }
            data.setObjectGroupId(resObjectGroup.getId());
            MessageContent messageContent = this.aisService.addVesselToGroup(data);
            if (messageContent == null) {
                return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                        new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
            } else {
                response = new ResponseMessage(messageContent);
            }
        }
        else {
            MessageContent messageContent = this.aisService.addVesselToGroup(data);
            if (messageContent == null) {
                return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                        new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
            } else {
                response = new ResponseMessage(messageContent);
            }
        }
        //}
        return response;
    }

}
