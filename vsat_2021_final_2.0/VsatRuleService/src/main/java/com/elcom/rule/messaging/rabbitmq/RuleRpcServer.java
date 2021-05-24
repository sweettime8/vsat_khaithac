package com.elcom.rule.messaging.rabbitmq;

import com.elcom.rule.bussiness.RuleBusiness;
import com.elcom.rule.bussiness.CommonBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.elcom.gateway.message.RequestMessage;
import com.elcom.gateway.message.ResponseMessage;
import com.elcom.rule.exception.ValidationException;
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
public class RuleRpcServer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleRpcServer.class);
    
    @Autowired
    private RuleBusiness ruleBusiness;
    
    @Autowired
    private CommonBusiness commonBusiness;
    
    @RabbitListener(queues = "${rule.rpc.queue}")
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
                        if( "/v1.0/rule/event-list".equalsIgnoreCase(requestPath) )
                            response = this.ruleBusiness.findRuleEvents(bodyParam, headerParam);
                        else if( "/v1.0/rule/event-count".equalsIgnoreCase(requestPath) )
                            response = this.ruleBusiness.countRuleEventByObjId(bodyParam, headerParam);
                        else if( "/v1.0/rule/update-read-status".equalsIgnoreCase(requestPath) )
                            response = this.ruleBusiness.updateRuleEventReadStatus(bodyParam, headerParam);
                        else if( "/v1.0/rule/event-detail".equalsIgnoreCase(requestPath) )
                            response = this.ruleBusiness.findDetailRuleEvent(bodyParam, headerParam);
                        
                        
                        else if ( "/v1.0/ais/vessel-group/detail".equalsIgnoreCase(requestPath) && pathParam != null && pathParam.length() > 0 )
                            response = this.ruleBusiness.findDetailVesselGroup(headerParam, pathParam, "POST", requestPath);
                        else if( "/v1.0/ais/vessel-group/search-list".equalsIgnoreCase(requestPath) )
                            response = this.ruleBusiness.searchVesselGroupList(bodyParam, headerParam);
                        else if ( "/v1.0/ais/vessel-group/upsert".equalsIgnoreCase(requestPath) )
                            response = this.ruleBusiness.saveVesselGroup(bodyParam, headerParam, "POST", "INSERT", requestPath);
                        else if( "/v1.0/ais/vessel/detail".equalsIgnoreCase(requestPath) && pathParam != null && pathParam.length() > 0 )
                            response = this.ruleBusiness.findDetailVessel(headerParam, pathParam, "POST", requestPath);
                        else if( "/v1.0/ais/vessel/add-to-group".equalsIgnoreCase(requestPath) )
                            response = this.ruleBusiness.addVesselToGroup(bodyParam, pathParam, "POST", requestPath);
                        break;
                    case "PUT":
                        if ( "/v1.0/ais/vessel-group/upsert".equalsIgnoreCase(requestPath) )
                            response = this.ruleBusiness.saveVesselGroup(bodyParam, headerParam, "PUT", "UPDATE", requestPath);
                        break;
                    case "PATCH":
                        response = null;
                        break;
                    case "DELETE":
                        if ( "/v1.0/ais/vessel-group/remove".equalsIgnoreCase(requestPath) && pathParam != null && pathParam.length() > 0 )
                            response = this.ruleBusiness.removeVesselGroup(headerParam, pathParam, "DELETE", requestPath);
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
