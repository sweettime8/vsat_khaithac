package com.elcom.ais.messaging.rabbitmq;

import com.elcom.ais.bussiness.AisBusiness;
import com.elcom.ais.bussiness.CommonBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.elcom.gateway.message.RequestMessage;
import com.elcom.gateway.message.ResponseMessage;
import com.elcom.ais.exception.ValidationException;
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
 * @author anhdv
 */
public class AisRpcServer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AisRpcServer.class);
    
    @Autowired
    private AisBusiness aisBusiness;
    
    @Autowired
    private CommonBusiness commonBusiness;
    
    @RabbitListener(queues = "${ais.rpc.queue}")
    public String processService(String json) throws ValidationException {
        try {
            LOGGER.info(" [-->] Server received request for " + json);
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            RequestMessage request = mapper.readValue(json, RequestMessage.class);
            
            ResponseMessage response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
            if (request != null) {
                String requestPath = request.getRequestPath();
                String pathParam = request.getPathParam();
                Map<String, Object> bodyParam = request.getBodyParam();
                Map<String, String> headerParam = request.getHeaderParam();

                switch (request.getRequestMethod()) {
                    case "GET":
                        if("/v1.0/ais/country".equalsIgnoreCase(requestPath)) //List country 
                            response = this.commonBusiness.getListCountry(headerParam);    
                        else if("/v1.0/ais/vessel-type".equalsIgnoreCase(requestPath)) //List country 
                            response = this.commonBusiness.getListVesselType(headerParam);  
                        break;
                    case "POST":
                        if( "/v1.0/ais/search-list".equalsIgnoreCase(requestPath) ) 
                            response = this.aisBusiness.searchAisList(bodyParam, headerParam);
                        if( "/v1.0/ais/vessel/search-list".equalsIgnoreCase(requestPath) )     //ducnh code get list Vessel(danh ba tau)
                            response = this.aisBusiness.searchVessel(bodyParam, headerParam);
                        else if( "/v1.0/ais/search-list-general".equalsIgnoreCase(requestPath) ) 
                            response = this.aisBusiness.searchAisListGeneral(bodyParam, headerParam);
                        else if ( "/v1.0/ais/object-group/detail".equalsIgnoreCase(requestPath) && pathParam != null && pathParam.length() > 0 )
                            response = this.aisBusiness.findDetailVesselGroup(headerParam, pathParam, "POST", requestPath);
                        else if( "/v1.0/ais/object-group/search-list".equalsIgnoreCase(requestPath) )
                            response = this.aisBusiness.searchObjectGroupList(bodyParam, headerParam);
                        else if ( "/v1.0/ais/object-group/upsert".equalsIgnoreCase(requestPath) )
                            response = this.aisBusiness.saveVesselGroup(bodyParam, headerParam, "POST", "INSERT", requestPath);
                        else if( "/v1.0/ais/vessel/search-list".equalsIgnoreCase(requestPath) )
                            response = this.aisBusiness.searchVesselList(bodyParam, headerParam);
                        else if( "/v1.0/ais/vessel/detail".equalsIgnoreCase(requestPath) && pathParam != null && pathParam.length() > 0 )
                            response = this.aisBusiness.findDetailVessel(headerParam, pathParam, "POST", requestPath);
                        else if( "/v1.0/ais/object/add-to-group".equalsIgnoreCase(requestPath) )
                            response = this.aisBusiness.addObjectToGroup(bodyParam, pathParam, "POST", requestPath);
                        break;
                    case "PUT":
                        if ( "/v1.0/ais/vessel-group/upsert".equalsIgnoreCase(requestPath) )
                            response = this.aisBusiness.saveVesselGroup(bodyParam, headerParam, "PUT", "UPDATE", requestPath);
                        break;
                    case "PATCH":
                        break;
                    case "DELETE":
                        if ( "/v1.0/ais/object-group/remove".equalsIgnoreCase(requestPath) && pathParam != null && pathParam.length() > 0 )
                            response = this.aisBusiness.removeObjectGroup(headerParam, pathParam, "DELETE", requestPath);
                        break;
                    default:
                        break;
                }
            }
            //LOGGER.info(" [<--] Server returned " + response.toJsonString());
            LOGGER.info(" [<--] Server returned " + response!=null && response.getData()!=null ? "response" : "EMPTY | NULL");
            return response.toJsonString();
        } catch (Exception ex) {
            LOGGER.error("Error to processService >>> " + ex.toString());
            ex.printStackTrace();
        }
        return null;
    }
}
