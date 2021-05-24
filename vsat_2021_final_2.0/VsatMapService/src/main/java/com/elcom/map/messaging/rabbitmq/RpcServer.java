package com.elcom.map.messaging.rabbitmq;

import com.elcom.map.bussiness.MapBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.elcom.gateway.message.RequestMessage;
import com.elcom.gateway.message.ResponseMessage;
import com.elcom.map.exception.ValidationException;
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
public class RpcServer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);
    
    @Autowired
    private MapBusiness mapBusiness;
    
    @RabbitListener(queues = "${map.rpc.queue}")
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
//                String urlParam = request.getUrlParam();
                String pathParam = request.getPathParam();
                Map<String, Object> bodyParam = request.getBodyParam();
                Map<String, String> headerParam = request.getHeaderParam();

                switch (request.getRequestMethod()) {
                    case "GET":
//                        if ("/v1.0/user".equalsIgnoreCase(requestPath) && pathParam != null && pathParam.length() > 0) // Get details
//                            response = userController.getDetailUser(pathParam, headerParam);
//                        else if ("/v1.0/user/exist".equalsIgnoreCase(requestPath)) // Check exist email or mobile
//                            response = userController.checkUserExist(urlParam);
                        break;
                    case "POST":
                        if( "/v1.0/map/save-area".equalsIgnoreCase(requestPath) )
                            response = this.mapBusiness.saveArea(bodyParam, headerParam);
                        if( "/v1.0/map/get-list-area".equalsIgnoreCase(requestPath) )
                            response = this.mapBusiness.getListArea(bodyParam, headerParam);
                        if( "/v1.0/map/del-area".equalsIgnoreCase(requestPath) )
                            response = this.mapBusiness.delArea(bodyParam, headerParam);
                        break;
                    case "PUT":

                        break;
                    case "PATCH":
                        break;
                    case "DELETE":
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
