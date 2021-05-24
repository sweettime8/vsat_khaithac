package com.elcom.contact.controller;

import com.elcom.contact.model.contactLand.HeadQuarterDto;
import com.elcom.contact.model.contactObject.ContactObjectIP;
import com.elcom.contact.model.contactObject.ObjectUnInfoDto;
import com.elcom.contact.model.dto.AuthorizationResponseDTO;
import com.elcom.contact.model.dto.request.contacLand.AddHeadquaterRequest;
import com.elcom.contact.model.dto.request.contact.SearchContactRequest;
import com.elcom.contact.model.dto.request.contactObject.AddContactObjectIpRequest;
import com.elcom.contact.model.dto.request.contactObject.AddObjectUnInfoRequest;
import com.elcom.contact.model.dto.request.contactObject.DelContactObjectIpRequest;
import com.elcom.contact.model.dto.request.contactObject.GetDetailObjectRequest;
import com.elcom.contact.service.ContactObjectService;
import com.elcom.contact.validation.UfoValidation;
import com.elcom.gateway.message.MessageContent;
import com.elcom.gateway.message.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

@Controller
public class ContactObjectController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactObjectController.class);
    @Autowired
    ContactObjectService contactService;

    public ResponseMessage search(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage responseMessage = null;
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            SearchContactRequest data = mapper.convertValue(bodyParam, SearchContactRequest.class);

            Page<ObjectUnInfoDto> result = contactService.search(data);
            if (result != null || !result.hasContent()) {
                responseMessage = new ResponseMessage(new MessageContent(result.getContent(), result.getTotalElements()));
            } else {
                responseMessage = new ResponseMessage(HttpStatus.OK.value(), "Data not found",
                        new MessageContent(HttpStatus.OK.value(), "Data not found", null));
            }

        } catch (Exception e) {
            LOGGER.error("ContactListController.search ==> error : ", e);
        }

        return responseMessage;
    }

    public ResponseMessage getDetailObjectInfo(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            GetDetailObjectRequest data = mapper.convertValue(bodyParam, GetDetailObjectRequest.class);
            Object objectUnInfoDto = contactService.getDetailObjectInfo(data);
            if (objectUnInfoDto != null) {
                responseMessage = new ResponseMessage(new MessageContent(objectUnInfoDto));
            } else {
                responseMessage.setStatus(HttpStatus.OK.value());
                MessageContent messageContent = new MessageContent();
                messageContent.setMessage("Get detail objectUnInfoDto error.");
                responseMessage.setData(messageContent);
            }
        } catch (Exception e) {
            LOGGER.error("ContactListController.search ==> error : ", e);
        }

        return responseMessage;
    }

    public ResponseMessage checkIpObjectInfo(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            AddContactObjectIpRequest data = mapper.convertValue(bodyParam, AddContactObjectIpRequest.class);
            ContactObjectIP contactObjectIP = contactService.checkIpObjectInfo(data);
            if (contactObjectIP != null) {
                responseMessage.setStatus(HttpStatus.OK.value());
                MessageContent messageContent = new MessageContent();
                messageContent.setData(contactObjectIP);
                responseMessage.setData(messageContent);
            } else {
                responseMessage.setStatus(HttpStatus.OK.value());
                MessageContent messageContent = new MessageContent();
                messageContent.setMessage("Ip Object is exits or add error.");
                responseMessage.setData(messageContent);
            }
        } catch (Exception e) {
            LOGGER.error("ContactListController.search ==> error : ", e);
        }

        return responseMessage;
    }

    public ResponseMessage addIpObjectInfo(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            AddContactObjectIpRequest data = mapper.convertValue(bodyParam, AddContactObjectIpRequest.class);
            String invalidData = new UfoValidation().validateIP(data);
            if (invalidData != null) {
                responseMessage = new ResponseMessage(HttpStatus.OK.value(), invalidData, new MessageContent(HttpStatus.OK.value(), invalidData, null));
            } else {
                Object obj = contactService.addIpObjectInfo(data);
                if (obj != null) {
                    responseMessage = new ResponseMessage(new MessageContent(obj));
                } else {
                    responseMessage = new ResponseMessage(HttpStatus.OK.value(), "addIpObjectInfo error", new MessageContent(HttpStatus.OK.value(), "addIpObjectInfo error", null));
                }
            }
        } catch (Exception e) {
            LOGGER.error("addIpObjectInfo ==> error : ", e);
        }

        return responseMessage;
    }

    @RequestMapping(value = "/delIpObjectInfo", method = RequestMethod.POST)
    public ResponseMessage delIpObjectInfo(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            DelContactObjectIpRequest data = mapper.convertValue(bodyParam, DelContactObjectIpRequest.class);
            Object contactIP = contactService.delIpObjectInfo(data);
            responseMessage = new ResponseMessage(new MessageContent(contactIP));

        } catch (Exception e) {
            LOGGER.error("delIpObjectInfo.search ==> error : ", e);
        }

        return responseMessage;
    }

    public ResponseMessage addObjectUnInfo(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage responseMessage = null;
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            AddObjectUnInfoRequest data = mapper.convertValue(bodyParam, AddObjectUnInfoRequest.class);
            String invalidData = new UfoValidation().validateInsertUfo(data);
            if (invalidData != null) {
                responseMessage = new ResponseMessage(HttpStatus.OK.value(), invalidData, new MessageContent(HttpStatus.OK.value(), invalidData, null));
            } else {
                Object objectUnInfo = contactService.addObjectUnInfo(data);
                if (objectUnInfo != null) {
                    responseMessage = new ResponseMessage(new MessageContent(objectUnInfo));
                } else {
                    responseMessage = new ResponseMessage(HttpStatus.OK.value(), "Add ObjectUndefine error", new MessageContent(HttpStatus.OK.value(), "Add ObjectUndefine error", null));
                }
            }

        } catch (Exception e) {
            LOGGER.error("ContactListController.search ==> error : ", e);
        }

        return responseMessage;
    }

    public ResponseMessage updateObjectUnInfo(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            AddObjectUnInfoRequest data = mapper.convertValue(bodyParam, AddObjectUnInfoRequest.class);
            String invalidData = new UfoValidation().validateUpdateUfo(data);
            if (invalidData != null) {
                responseMessage = new ResponseMessage(HttpStatus.OK.value(), invalidData, new MessageContent(HttpStatus.OK.value(), invalidData, null));
            } else {
                boolean result = contactService.updateObjectUnInfo(data);
                if (result) {
                    responseMessage = new ResponseMessage(new MessageContent("update success"));
                } else {
                    responseMessage = new ResponseMessage(HttpStatus.OK.value(), "update ObjectUndefine error", new MessageContent(HttpStatus.OK.value(), "update ObjectUndefine error", null));
                }
            }

        } catch (Exception e) {
            LOGGER.error("updateObjectUnInfo ==> error : ", e);
        }

        return responseMessage;
    }

    public ResponseMessage deleteObjectUnInfo(String pathParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                    new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                            "Bạn chưa đăng nhập"));
        }
        UUID uid = UUID.fromString(pathParam);
        contactService.deleteObjectUnInfo(uid);
        response = new ResponseMessage(new MessageContent("delete success"));

        return response;
    }

}
