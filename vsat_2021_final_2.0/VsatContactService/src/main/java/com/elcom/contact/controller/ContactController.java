package com.elcom.contact.controller;

import com.elcom.contact.constant.Constant;
import com.elcom.contact.model.contact.AisInfo;
import com.elcom.contact.model.contact.ContactIP;
import com.elcom.contact.model.contact.MmsiImage;
import com.elcom.contact.model.contact.VesselAltInfo;
import com.elcom.contact.model.dto.AuthorizationResponseDTO;
import com.elcom.contact.model.dto.request.contact.AddContactIpRequest;
import com.elcom.contact.model.dto.request.contact.AddVesselRequest;
import com.elcom.contact.model.dto.request.contact.DelContactIpRequest;
import com.elcom.contact.model.dto.request.contact.GetDetailVesselRequest;
import com.elcom.contact.model.dto.request.contact.SearchContactRequest;
import com.elcom.contact.service.ContactService;
import com.elcom.contact.utils.StringUtil;
import com.elcom.contact.validation.UfoValidation;
import com.elcom.contact.validation.VesselValidation;
import com.elcom.gateway.message.MessageContent;
import com.elcom.gateway.message.ResponseMessage;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import org.springframework.stereotype.Controller;

@Controller
public class ContactController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactController.class);
    @Autowired
    ContactService contactService;

    public ResponseMessage search(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            SearchContactRequest data = mapper.convertValue(bodyParam, SearchContactRequest.class);
            MessageContent messageContent = new MessageContent();
            messageContent.setData(contactService.search(data));
            if (messageContent != null) {
                responseMessage.setStatus(HttpStatus.OK.value());
                responseMessage.setData(messageContent);
            } else {
                responseMessage.setStatus(HttpStatus.OK.value());
                messageContent.setMessage("Get list Vessel error.");
                responseMessage.setData(messageContent);
            }

        } catch (Exception e) {
            LOGGER.error("ContactListController.search ==> error : ", e);
        }

        return responseMessage;
    }

    public ResponseMessage getDetailVessel(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            GetDetailVesselRequest data = mapper.convertValue(bodyParam, GetDetailVesselRequest.class);
            MessageContent messageContent = contactService.getDetailVessel(data);
            if (messageContent != null) {
                messageContent.setStatus(HttpStatus.OK.value());
                responseMessage.setStatus(HttpStatus.OK.value());
                responseMessage.setData(messageContent);

            } else {
                responseMessage.setStatus(HttpStatus.OK.value());
                messageContent.setMessage("Get detail Vessel error.");
                responseMessage.setData(messageContent);
            }

        } catch (Exception e) {
            LOGGER.error("ContactListController.search ==> error : ", e);
        }

        return responseMessage;
    }

    @RequestMapping(value = "/checkIpVessel", method = RequestMethod.POST)
    public ResponseMessage checkIpVessel(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            AddContactIpRequest data = mapper.convertValue(bodyParam, AddContactIpRequest.class);
            ContactIP contactIP = contactService.checkIpVessel(data);
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

    public ResponseMessage addVessel(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
            String mmsiValid = (String) bodyParam.get("mmsi");
            String invalidData = null;
            if (mmsiValid.length() > 10) {
                invalidData = "MMSI không được quá 10 ký tự";
                return new ResponseMessage(HttpStatus.OK.value(), invalidData, new MessageContent(HttpStatus.OK.value(), invalidData, null));
            } else {
                AddVesselRequest data = mapper.convertValue(bodyParam, AddVesselRequest.class);

                invalidData = new VesselValidation().validateInsertVessel(data);  //ddang validate thieu , bo sung sau
                if (invalidData != null) {
                    return new ResponseMessage(HttpStatus.OK.value(), invalidData, new MessageContent(HttpStatus.OK.value(), invalidData, null));
                } else {
                    AisInfo ais = new AisInfo();
                    ais.setMmsi(data.getMmsi());
                    ais.setImo(data.getImo());
                    ais.setCountryId(data.getCountryId());
                    ais.setDimA(data.getHeight());
                    ais.setDimB(0);
                    ais.setDimC(data.getWidth());
                    ais.setDimD(0);
                    ais.setCallsign(data.getCallsign());
                    ais.setName(data.getVesselName());
                    ais.setNameStatic(data.getNameStatic());
                    ais.setIsMaster(0);
                    ais.setImagePath(data.getImagePath());
                    ais.setSatellitePhoneCode(data.getSatellitePhoneCode());
                    ais.setYearOfBuild(data.getYearOfBuild());
                    ais.setPlaceOfBuildCode(data.getPlaceOfBuildCode());
                    ais.setDraught(data.getDraugth());
                    ais.setEngineType(data.getEngineType());
                    ais.setGrossTonnage(data.getGrossTonnage());
                    ais.setDeadWeight(data.getDeadWeight());
                    ais.setNamePlace(data.getNamePlace());
                    ais.setSpeedAvg(data.getSpeedAvg());
                    ais.setSpeedMax(data.getSpeedMax());
                    ais.setDisplacement(data.getDisplacement());
                    ais.setCrew(data.getCrew());
                    ais.setWeapons(data.getWeapons());
                    ais.setEndurance(data.getEndurance());
                    ais.setUserUpdate(data.getUserUpdate());
                    ais.setCreatedTime(new Timestamp(System.currentTimeMillis()));
                    ais.setUpdatedTime(new Timestamp(System.currentTimeMillis()));

                    //check MMSI đã tồn tại chưa
                    AisInfo existVessel = contactService.finAisInfo(data.getMmsi());
                    if (existVessel != null) {
                        response = new ResponseMessage(HttpStatus.OK.value(), "Đã tồn tại MMSI: " + existVessel.getMmsi(),
                                new MessageContent(HttpStatus.OK.value(), "Đã tồn tại MMSI: " + existVessel.getMmsi(), null));

                    } else {
                        boolean result = contactService.save(ais);
                        if (result) {
                            VesselAltInfo vesselAltInfo = new VesselAltInfo();
                            vesselAltInfo.setMmsi(data.getMmsi());
                            vesselAltInfo.setOperationUnit(data.getOperationUnit());
                            vesselAltInfo.setOtherInfo(data.getOtherInfo());
                            vesselAltInfo.setOwner(data.getOwner());
                            vesselAltInfo.setStatus(data.getStatus());
                            vesselAltInfo.setUnit(data.getUnit());
                            vesselAltInfo.setCreatedTime(new Timestamp(System.currentTimeMillis()));
                            vesselAltInfo.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
                            boolean resultSave = contactService.save(vesselAltInfo);

                            if (data.getImagePath() != null && !StringUtil.isNullOrEmpty(data.getImagePath())) {
                                MmsiImage mmsiImage = new MmsiImage();
                                mmsiImage.setMmsi(data.getMmsi());
                                mmsiImage.setType(0);
                                mmsiImage.setImagePath(data.getImagePath());
                                boolean resultImage = contactService.save(mmsiImage);
                            }

                            response = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.OK.toString(),
                                    new MessageContent(data));
                        }
                    }

                }
            }

        } catch (Exception e) {
            LOGGER.error("ContactListController.addVessel ==> error : ", e);
        }

        return response;
    }

    public ResponseMessage updateVessel(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        try {
            AuthorizationResponseDTO dto = authenToken(headerParam);
            if (dto == null) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                                "Bạn chưa đăng nhập"));
            }
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            AddVesselRequest data = mapper.convertValue(bodyParam, AddVesselRequest.class);
            //check vessel exist
            AisInfo existVessel = contactService.finAisInfo(data.getMmsi());
            if (existVessel == null) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                                "Không tồn tại mmsi: " + existVessel.getMmsi()));
            } else {
                boolean result = contactService.updateAisInfo(data);

                //check altinfo tồn tại với mmsi truyền vào chưa
                VesselAltInfo vesselAltInfoExist = contactService.findVesselInfo(data.getMmsi());
                if (vesselAltInfoExist != null) {
                    contactService.updateVesselAltInfo(data);
                    if (data.getImagePath() != null && !StringUtil.isNullOrEmpty(data.getImagePath())) {
                        MmsiImage mmsiImage = new MmsiImage();
                        mmsiImage.setMmsi(data.getMmsi());
                        mmsiImage.setType(0);
                        mmsiImage.setImagePath(data.getImagePath());
                        boolean resultImage = contactService.save(mmsiImage);
                    }
                } else {
                    VesselAltInfo vesselAltInfo = new VesselAltInfo();
                    vesselAltInfo.setMmsi(data.getMmsi());
                    vesselAltInfo.setOperationUnit(data.getOperationUnit());
                    vesselAltInfo.setOtherInfo(data.getOtherInfo());
                    vesselAltInfo.setOwner(data.getOwner());
                    vesselAltInfo.setStatus(data.getStatus());
                    vesselAltInfo.setUnit(data.getUnit());
                    vesselAltInfo.setCreatedTime(new Timestamp(System.currentTimeMillis()));
                    vesselAltInfo.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
                    boolean resultSave = contactService.save(vesselAltInfo);

                }
                if (result) {
                    response = new ResponseMessage(new MessageContent("update success"));
                }
            }

        } catch (Exception e) {
            LOGGER.error("ContactListController.addVessel ==> error : ", e);
        }
        return response;
    }

    public ResponseMessage deleteVessel(String pathParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                    new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                            "Bạn chưa đăng nhập"));
        }
        int id = Integer.parseInt(pathParam);
        contactService.deleteVesselById(id);

        response = new ResponseMessage(new MessageContent("delete success"));

        return response;
    }

    public ResponseMessage addIpVessel(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            AddContactIpRequest data = mapper.convertValue(bodyParam, AddContactIpRequest.class);
            String invalidData = new VesselValidation().validateIP(data);
            if (invalidData != null) {
                responseMessage = new ResponseMessage(HttpStatus.OK.value(), invalidData, new MessageContent(HttpStatus.OK.value(), invalidData, null));
            } else {
                Object contactIP = contactService.addIpVessel(data);
                if (contactIP != null) {
                    responseMessage = new ResponseMessage(new MessageContent(contactIP));
                } else {
                    responseMessage = new ResponseMessage(HttpStatus.OK.value(), "Ip Vessel is exits or add error.", new MessageContent(HttpStatus.OK.value(), "Ip Vessel is exits or add error.", null));
                }
            }

        } catch (Exception e) {
            LOGGER.error("ContactListController.search ==> error : ", e);
        }
        return responseMessage;
    }

    @RequestMapping(value = "/delIpVessel", method = RequestMethod.POST)
    public ResponseMessage delIpVessel(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            DelContactIpRequest data = mapper.convertValue(bodyParam, DelContactIpRequest.class);
            Object contactIP = contactService.delIpVessel(data);
            if (contactIP != null) {
                responseMessage.setStatus(HttpStatus.OK.value());
                MessageContent messageContent = new MessageContent();
                messageContent.setData(contactIP);
                responseMessage.setData(messageContent);
            } else {
                responseMessage.setStatus(HttpStatus.OK.value());
                MessageContent messageContent = new MessageContent();
                messageContent.setMessage("delIpVessel error.");
                responseMessage.setData(messageContent);
            }

        } catch (Exception e) {
            LOGGER.error("ContactListController.search ==> error : ", e);
        }

        return responseMessage;
    }
}
