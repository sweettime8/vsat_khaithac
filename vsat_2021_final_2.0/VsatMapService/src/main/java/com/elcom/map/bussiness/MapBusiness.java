package com.elcom.map.bussiness;

import com.elcom.map.constant.Constant;
import com.elcom.map.controller.BaseController;
import com.elcom.map.model.dto.request.map.AddAreaRequest;
import com.elcom.map.model.dto.request.map.DeleteAreaRequest;
import com.elcom.map.model.dto.request.map.GetListAreaRequest;
import com.elcom.map.model.dto.request.map.ListAddAreaRequest;
import com.elcom.map.service.MapService;
import com.elcom.map.validation.MapValidation;
import com.elcom.gateway.message.MessageContent;
import com.elcom.gateway.message.ResponseMessage;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

/**
 *
 * @author anhdv
 */
@Controller
public class MapBusiness extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MapBusiness.class);
    
    @Autowired
    private MapService mapService;
    
    /** Save vung
     * Service public để RestClient gọi qua Gateway, có check Auth
     * @param bodyParam
     * @param headerMap
     * @return list */
    public ResponseMessage saveArea(Map<String, Object> bodyParam, Map<String, String> headerMap) {
        /*AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null)
            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else {*/
            if (bodyParam == null || bodyParam.isEmpty())
                return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                           new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            else {
                ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
                ListAddAreaRequest data = mapper.convertValue(bodyParam, ListAddAreaRequest.class);


                String validationMsg=null;
//                String validationMsg = new MapValidation().validateAddAreaRequest(data.getListAddAreaRequest());
                if( validationMsg != null )
                    return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), validationMsg,
                               new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, null));
                else {
                    MessageContent messageContent = this.mapService.saveArea(data.getListAddAreaRequest());
                    if ( messageContent==null || messageContent.getData()==null )
                        return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                                   new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
                    else
                        return new ResponseMessage(messageContent);
                }
            }
        //}
    }

    public ResponseMessage getListArea(Map<String, Object> bodyParam, Map<String, String> headerMap) {
        /*AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null)
            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else {*/
//        if (bodyParam == null || bodyParam.isEmpty())
//            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
//                    new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
//        else {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            GetListAreaRequest data = mapper.convertValue(bodyParam, GetListAreaRequest.class);


            String validationMsg=null;
//                String validationMsg = new MapValidation().validateAddAreaRequest(data.getListAddAreaRequest());
            if( validationMsg != null )
                return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), validationMsg,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, null));
            else {
                MessageContent messageContent = this.mapService.getListArea(data);
                if ( messageContent==null || messageContent.getData()==null )
                    return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                            new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
                else
                    return new ResponseMessage(messageContent);
            }
//        }
        //}
    }

    public ResponseMessage delArea(Map<String, Object> bodyParam, Map<String, String> headerMap) {
        /*AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null)
            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else {*/
//        if (bodyParam == null || bodyParam.isEmpty())
//            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
//                    new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
//        else {
        ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
        DeleteAreaRequest data = mapper.convertValue(bodyParam, DeleteAreaRequest.class);


        String validationMsg=null;
//                String validationMsg = new MapValidation().validateAddAreaRequest(data.getListAddAreaRequest());
        if( validationMsg != null )
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), validationMsg,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, null));
        else {
            MessageContent messageContent = this.mapService.delArea(data);
            if ( messageContent==null || messageContent.getData()==null )
                return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                        new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
            else
                return new ResponseMessage(messageContent);
        }
//        }
        //}
    }
}
