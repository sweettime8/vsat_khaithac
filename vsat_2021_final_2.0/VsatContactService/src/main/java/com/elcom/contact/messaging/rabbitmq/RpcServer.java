package com.elcom.contact.messaging.rabbitmq;

import com.elcom.contact.controller.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.elcom.gateway.message.RequestMessage;
import com.elcom.gateway.message.ResponseMessage;
import com.elcom.contact.exception.ValidationException;
import io.netty.util.internal.StringUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Admin
 */
public class RpcServer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);
    
    @Autowired
    private ContactController contactController;
    @Autowired
    private ContactLandController contactLandController;
    @Autowired
    private ContactObjectController contactObjectController;
    
    @RabbitListener(queues = "${contact.rpc.queue}")
    public String processService(String json) throws ValidationException {
        try {
            LOGGER.info(" [-->] Server received request for " + json);
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            RequestMessage request = mapper.readValue(json, RequestMessage.class);
            
            //Process here
            ResponseMessage response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
            if (request != null) {
                String requestPath = request.getRequestPath();
                String urlParam = request.getUrlParam();
                String pathParam = request.getPathParam();
                Map<String, Object> bodyParam = request.getBodyParam();
                Map<String, String> headerParam = request.getHeaderParam();
                //GatewayDebugUtil.debug(requestPath, urlParam, pathParam, bodyParam, headerParam);

                switch (request.getRequestMethod()) {
                    case "GET":
                        break;
                    case "POST":
                        if("/v1.0/contact/vessel/search".equalsIgnoreCase(requestPath)) // Check token
                            response = contactController.search(bodyParam, headerParam);
                        else if("/v1.0/contact/vessel/detail/getDetailVessel".equalsIgnoreCase(requestPath))
                            response = contactController.getDetailVessel(bodyParam, headerParam);                     
                        else if("/v1.0/contact/vessel/checkIpVessel".equalsIgnoreCase(requestPath))
                            response = contactController.checkIpVessel(bodyParam, headerParam);
                        else if("/v1.0/contact/vessel/addIpVessel".equalsIgnoreCase(requestPath))
                            response = contactController.addIpVessel(bodyParam, headerParam);
                        else if("/v1.0/contact/vessel/delIpVessel".equalsIgnoreCase(requestPath))
                            response = contactController.delIpVessel(bodyParam, headerParam);
                        else if("/v1.0/contact/vessel".equalsIgnoreCase(requestPath))
                            response = contactController.addVessel(bodyParam, headerParam);
                        
                        //land
                        else if("/v1.0/contact/land/search".equalsIgnoreCase(requestPath)) // Check token
                            response = contactLandController.search(bodyParam, headerParam);
                        else if("/v1.0/contact/land/detail/getDetailHeadquarter".equalsIgnoreCase(requestPath))
                            response = contactLandController.getDetailHeadQuarter(bodyParam, headerParam);
                        else if("/v1.0/contact/land/checkIpHeadQuarter".equalsIgnoreCase(requestPath))
                            response = contactLandController.checkIpHeadQuarter(bodyParam, headerParam);
                        else if("/v1.0/contact/land/addIpHeadQuarter".equalsIgnoreCase(requestPath))
                            response = contactLandController.addIpHeadQuarter(bodyParam, headerParam);
                        else if("/v1.0/contact/land/delIpHeadQuarter".equalsIgnoreCase(requestPath))
                            response = contactLandController.delIpHeadQuarter(bodyParam, headerParam);
                        else if("/v1.0/contact/land/addHeadquater".equalsIgnoreCase(requestPath))          //add headquater
                            response = contactLandController.addHeadquater(bodyParam, headerParam);
                        else if("/v1.0/contact/land/updateHeadquater".equalsIgnoreCase(requestPath))       //update headquater
                            response = contactLandController.updateHeadquater(bodyParam, headerParam);
                        else if("/v1.0/contact/land/addPhoneHeadQuarter".equalsIgnoreCase(requestPath))
                            response = contactLandController.addPhoneHeadQuarter(bodyParam, headerParam);
                        else if("/v1.0/contact/land/delPhoneHeadQuarter".equalsIgnoreCase(requestPath))
                            response = contactLandController.delPhoneHeadQuarter(bodyParam, headerParam);
                        //object
                        else if("/v1.0/contact/Object/search".equalsIgnoreCase(requestPath)) // Check token
                            response = contactObjectController.search(bodyParam, headerParam);
                        else if("/v1.0/contact/Object/detail/getDetailObjectInfo".equalsIgnoreCase(requestPath))
                            response = contactObjectController.getDetailObjectInfo(bodyParam, headerParam);
                        else if("/v1.0/contact/Object/checkIpObjectInfo".equalsIgnoreCase(requestPath))
                            response = contactObjectController.checkIpObjectInfo(bodyParam, headerParam);
                        else if("/v1.0/contact/Object/addIpObjectInfo".equalsIgnoreCase(requestPath))
                            response = contactObjectController.addIpObjectInfo(bodyParam, headerParam);
                        else if("/v1.0/contact/Object/delIpObjectInfo".equalsIgnoreCase(requestPath))
                            response = contactObjectController.delIpObjectInfo(bodyParam, headerParam);
                        else if("/v1.0/contact/Object/addObjectUnInfo".equalsIgnoreCase(requestPath))
                            response = contactObjectController.addObjectUnInfo(bodyParam, headerParam);
                        else if("/v1.0/contact/Object/editObjectUnInfo".equalsIgnoreCase(requestPath))       //update object
                            response = contactObjectController.updateObjectUnInfo(bodyParam, headerParam);

                        break;
                    case "PUT":
                        if("/v1.0/contact/vessel".equalsIgnoreCase(requestPath))
                            response = contactController.updateVessel(bodyParam, headerParam);
                        break;
                    case "PATCH":
                        break;
                    case "DELETE":
                        //land
                        if("/v1.0/contact/land/deleteHeadquater".equalsIgnoreCase(requestPath))
                            response = contactLandController.deleteHeadquater(pathParam, headerParam);                      
                        //vessel
                        else if("/v1.0/contact/vessel".equalsIgnoreCase(requestPath))
                            response = contactController.deleteVessel(pathParam, headerParam);
                        //Object Undefine
                        else if("/v1.0/contact/Object/delObjectUnInfo".equalsIgnoreCase(requestPath))       //update object
                            response = contactObjectController.deleteObjectUnInfo(pathParam, headerParam);
                        break;
                    default:
                        break;
                }
            }
            LOGGER.info(" [<--] Server returned " + response.toJsonString());
            return response.toJsonString();
        } catch (Exception ex) {
            LOGGER.error("Error to processService >>> " + ex.toString());
            ex.printStackTrace();
        }
        return null;
    }
}
