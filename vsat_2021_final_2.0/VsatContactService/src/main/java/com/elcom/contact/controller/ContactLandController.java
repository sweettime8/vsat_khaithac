package com.elcom.contact.controller;

import com.elcom.contact.model.contact.ContactIP;
import com.elcom.contact.model.contactLand.HeadQuarterDto;
import com.elcom.contact.model.dto.AuthorizationResponseDTO;
import com.elcom.contact.model.dto.request.contacLand.AddContactLandIpRequest;
import com.elcom.contact.model.dto.request.contacLand.AddHeadquaterRequest;
import com.elcom.contact.model.dto.request.contacLand.ContactLandPhoneRequest;
import com.elcom.contact.model.dto.request.contacLand.DelContactLandIpRequest;
import com.elcom.contact.model.dto.request.contacLand.GetDetailHeadquatersRequest;
import com.elcom.contact.model.dto.request.contact.SearchContactRequest;
import com.elcom.contact.service.ContactLandService;
import com.elcom.contact.validation.HeadquaterValidation;
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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

@Controller
public class ContactLandController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactLandController.class);
    @Autowired
    ContactLandService contactService;

    public ResponseMessage search(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            AuthorizationResponseDTO dto = authenToken(headerParam);
            if (dto == null) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                                "Bạn chưa đăng nhập"));
            }
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            SearchContactRequest data = mapper.convertValue(bodyParam, SearchContactRequest.class);
            Page<HeadQuarterDto> result = contactService.search(data);
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

    public ResponseMessage addHeadquater(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            AuthorizationResponseDTO dto = authenToken(headerParam);
            if (dto == null) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                                "Bạn chưa đăng nhập"));
            }
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            AddHeadquaterRequest data = mapper.convertValue(bodyParam, AddHeadquaterRequest.class);

            String invalidData = new HeadquaterValidation().validateInsertHeadquater(data);
            if (invalidData != null) {
                responseMessage = new ResponseMessage(HttpStatus.OK.value(), invalidData, new MessageContent(HttpStatus.OK.value(), invalidData, null));
            } else {
                Object objectUnInfo = contactService.addHeadquater(data);
                if (objectUnInfo != null) {
                    responseMessage = new ResponseMessage(new MessageContent(objectUnInfo));
                } else {
                    responseMessage = new ResponseMessage(HttpStatus.OK.value(), "Add headquater error", new MessageContent(HttpStatus.OK.value(), "update headquater error", null));   
                }
            }

        } catch (Exception e) {
            LOGGER.error("ContactListController.search ==> error : ", e);
        }

        return responseMessage;
    }

    public ResponseMessage updateHeadquater(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            AuthorizationResponseDTO dto = authenToken(headerParam);
            if (dto == null) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                                "Bạn chưa đăng nhập"));
            }
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            AddHeadquaterRequest data = mapper.convertValue(bodyParam, AddHeadquaterRequest.class);

            String invalidData = new HeadquaterValidation().validateUpdateHeadquater(data);
            if (invalidData != null) {
                responseMessage = new ResponseMessage(HttpStatus.OK.value(), invalidData, new MessageContent(HttpStatus.OK.value(), invalidData, null));
            } else {
                boolean result = contactService.updateHeadquater(data);
                if (result) {
                    responseMessage = new ResponseMessage(new MessageContent("update success"));
                } else {
                    responseMessage = new ResponseMessage(HttpStatus.OK.value(), "update headquater error", new MessageContent(HttpStatus.OK.value(), "update headquater error", null));
                }
            }

        } catch (Exception e) {
            LOGGER.error("ContactListController.search ==> error : ", e);
        }

        return responseMessage;
    }

    public ResponseMessage deleteHeadquater(String pathParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                    new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                            "Bạn chưa đăng nhập"));
        }
        int id = Integer.parseInt(pathParam);
        contactService.deleteHeadquaterById(id);

        response = new ResponseMessage(new MessageContent("delete success"));

        return response;
    }

    public ResponseMessage getDetailHeadQuarter(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            AuthorizationResponseDTO dto = authenToken(headerParam);
            if (dto == null) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                                "Bạn chưa đăng nhập"));
            }
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            GetDetailHeadquatersRequest data = mapper.convertValue(bodyParam, GetDetailHeadquatersRequest.class);

            Object objectUnInfo = contactService.getDetailHeadQuarter(data);
            if (objectUnInfo != null) {
                responseMessage = new ResponseMessage(new MessageContent(objectUnInfo));
            } else {
                responseMessage.setStatus(HttpStatus.OK.value());
                MessageContent messageContent = new MessageContent();
                messageContent.setMessage("Get list detail headQuarter error.");
                responseMessage.setData(messageContent);
            }

        } catch (Exception e) {
            LOGGER.error("ContactListController.search ==> error : ", e);
        }

        return responseMessage;
    }

    public ResponseMessage checkIpHeadQuarter(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            AddContactLandIpRequest data = mapper.convertValue(bodyParam, AddContactLandIpRequest.class);
            ContactIP contactIP = contactService.checkIpHeadQuarter(data);
            if (contactIP != null) {
                responseMessage.setStatus(HttpStatus.OK.value());
                MessageContent messageContent = new MessageContent();
                messageContent.setData(contactIP);
                responseMessage.setData(messageContent);
            } else {
                responseMessage.setStatus(HttpStatus.OK.value());
                MessageContent messageContent = new MessageContent();
                messageContent.setData(null);
                responseMessage.setData(messageContent);
            }

        } catch (Exception e) {
            LOGGER.error("ContactListController.search ==> error : ", e);
        }

        return responseMessage;
    }

    public ResponseMessage addIpHeadQuarter(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            AddContactLandIpRequest data = mapper.convertValue(bodyParam, AddContactLandIpRequest.class);
            String invalidData = new HeadquaterValidation().validateIP(data);
            if (invalidData != null) {
                responseMessage = new ResponseMessage(HttpStatus.OK.value(), invalidData, new MessageContent(HttpStatus.OK.value(), invalidData, null));
            } else {
                Object contactIP = contactService.addIpHeadQuarter(data);
                if (contactIP != null) {
                    responseMessage = new ResponseMessage(new MessageContent(contactIP));
                } else {
                    responseMessage = new ResponseMessage(HttpStatus.OK.value(), "Địa chỉ IP đã tồn tại", new MessageContent(HttpStatus.OK.value(), "Thêm IP lỗi", null));
                }
            }
                      

        } catch (Exception e) {
            LOGGER.error("ContactLandController.addIpHeadQuarter ==> error : ", e);
        }

        return responseMessage;
    }

    public ResponseMessage addPhoneHeadQuarter(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage responseMessage = null;
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            ContactLandPhoneRequest data = mapper.convertValue(bodyParam, ContactLandPhoneRequest.class);
            
            String invalidData = new HeadquaterValidation().validatePhone(data);
            if (invalidData != null) {
                responseMessage = new ResponseMessage(HttpStatus.OK.value(), invalidData, new MessageContent(HttpStatus.OK.value(), invalidData, null));
            } else {
                Object contactPhone = contactService.addPhoneHeadQuarter(data);
                if (contactPhone != null) {
                    responseMessage = new ResponseMessage(new MessageContent(contactPhone));
                } else {
                    responseMessage = new ResponseMessage(HttpStatus.OK.value(), "Phone đã tồn tại hoặc thêm Phone lỗi", new MessageContent(HttpStatus.OK.value(), "Phone đã tồn tại hoặc thêm Phone lỗi", null));
                }
            }

        } catch (Exception e) {
            LOGGER.error("addPhoneHeadQuarter ==> error : ", e);
        }

        return responseMessage;
    }

    public ResponseMessage delPhoneHeadQuarter(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            ContactLandPhoneRequest data = mapper.convertValue(bodyParam, ContactLandPhoneRequest.class);
            Object contactPhone = contactService.delPhoneHeadQuarter(data);
            if (contactPhone != null) {
                response = new ResponseMessage(new MessageContent("delete success"));
            } else {
                response = new ResponseMessage(new MessageContent("delete Phone Head Quarter error"));
            }

        } catch (Exception e) {
            LOGGER.error("ContactLandController.delPhoneHeadQuarter ==> error : ", e);
        }

        return response;
    }

    public ResponseMessage delIpHeadQuarter(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            DelContactLandIpRequest data = mapper.convertValue(bodyParam, DelContactLandIpRequest.class);
            Object contactIP = contactService.delIpHeadQuarter(data);
            if (contactIP != null) {
                response = new ResponseMessage(new MessageContent("delete success"));
            } else {
                response = new ResponseMessage(new MessageContent("delete Ip Head Quarter error"));
            }

        } catch (Exception e) {
            LOGGER.error("ContactListController.search ==> error : ", e);
        }

        return response;
    }
}
