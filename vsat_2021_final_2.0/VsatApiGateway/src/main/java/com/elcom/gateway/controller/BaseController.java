/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.gateway.controller;

import com.elcom.gateway.config.GatewayConfig;
import com.elcom.gateway.exception.ValidationException;
import com.elcom.gateway.message.MessageContent;
import com.elcom.gateway.message.RequestMessage;
import com.elcom.gateway.message.ResponseMessage;
import com.elcom.gateway.messaging.rabbitmq.RabbitMQClient;
import com.elcom.gateway.utils.StringUtil;
import com.elcom.gateway.validation.GatewayValidation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Admin
 */
public class BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private RabbitMQClient rabbitMQClient;

    public ResponseEntity<String> processRequest(String requestMethod, Map<String, String> urlParamMap,
            Map<String, Object> bodyParamMap, Map<String, String> headerParamMap,
            HttpServletRequest req) throws JsonProcessingException {
        long startTime = System.currentTimeMillis();
        //Get all value
        String requestPath = req.getRequestURI();
        String urlParam = StringUtil.generateMapString(urlParamMap);
        String pathParam = null;

        //Service
        int index = requestPath.indexOf("/", GatewayConfig.API_ROOT_PATH.length());
        String service = null;
        if (index != -1) {
            service = requestPath.substring(GatewayConfig.API_ROOT_PATH.length(), index);
        } else {
            service = requestPath.replace(GatewayConfig.API_ROOT_PATH, "");
        }

        //Check has path param
        int lastIndex = requestPath.lastIndexOf("/");
        if (lastIndex != -1) {
            String lastStr = requestPath.substring(lastIndex + 1);
            if (StringUtil.isNumberic(lastStr) || StringUtil.isUUID(lastStr)) {
                requestPath = requestPath.substring(0, lastIndex);
                pathParam = lastStr;
            }
        }

        //Log request info
//        LOGGER.info("[{}] to requestPath: {} - urlParam: {} - pathParm: {} - bodyParam: {} - headerParam: {}",
//                requestMethod, requestPath, urlParam, pathParam, bodyParamMap, StringUtil.generateMapString(headerParamMap));
        LOGGER.info("[{}] to requestPath: {} - urlParam: {} - pathParm: {} - headerParam: {}",
                requestMethod, requestPath, urlParam, pathParam, StringUtil.generateMapString(headerParamMap));
        //Validate
        new GatewayValidation().validate(requestPath, service);

        RequestMessage request = new RequestMessage(requestMethod, requestPath, urlParam,
                pathParam, bodyParamMap, headerParamMap);
        String result = null;

        //Get rabbit type
        String rabbitType = GatewayConfig.RABBIT_TYPE_MAP.get(requestMethod + " "
                + requestPath.replace(GatewayConfig.API_ROOT_PATH, "/"));
        LOGGER.info("Get Rabbit type for {} {} ==> Rabbit: {}", requestMethod,
                requestPath.replace(GatewayConfig.API_ROOT_PATH, "/"), rabbitType);
        if ("rpc".equalsIgnoreCase(rabbitType)) {
            String rpcQueue = GatewayConfig.SERVICE_MAP.get(service + ".rpc.queue");
            String rpcExchange = GatewayConfig.SERVICE_MAP.get(service + ".rpc.exchange");
            String rpcKey = GatewayConfig.SERVICE_MAP.get(service + ".rpc.key");
            if (StringUtil.isNullOrEmpty(rpcQueue) || StringUtil.isNullOrEmpty(rpcExchange) || StringUtil.isNullOrEmpty(rpcKey)) {
                throw new ValidationException("Không tìm thấy rabbit mq cho service " + service);
            }
            result = rabbitMQClient.callRpcService(rpcExchange, rpcQueue, rpcKey, request.toJsonString());
            LOGGER.info("Elapsed [{}] for requestUri: [{}], requestSession: [{}]"
                    , getElapsedTime(System.currentTimeMillis() - startTime)
                    , req.getRequestURI(), req.getSession().getId());
            //LOGGER.info("result: " + result);
        } else if ("worker".equalsIgnoreCase(rabbitType)) {
            String workerQueue = GatewayConfig.SERVICE_MAP.get(service + ".worker.queue");
            if (StringUtil.isNullOrEmpty(workerQueue)) {
                throw new ValidationException("Không tìm thấy rabbit mq cho service " + service);
            }
            //Call worker
            if (rabbitMQClient.callWorkerService(workerQueue, request.toJsonString())) {
                MessageContent mc = new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), "OK");
                ResponseMessage responseMessage = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.OK.toString(), mc);
                result = responseMessage.toJsonString();
            } else {
                MessageContent mc = new MessageContent(HttpStatus.EXPECTATION_FAILED.value(), HttpStatus.EXPECTATION_FAILED.toString(), "Error");
                ResponseMessage responseMessage = new ResponseMessage(HttpStatus.EXPECTATION_FAILED.value(), HttpStatus.EXPECTATION_FAILED.toString(), mc);
                result = responseMessage.toJsonString();
            }
        } else if ("publish".equalsIgnoreCase(rabbitType)) {
            String directExchange = GatewayConfig.SERVICE_MAP.get(service + ".direct.exchange");
            String directKey = GatewayConfig.SERVICE_MAP.get(service + ".direct.key");
            if (StringUtil.isNullOrEmpty(directExchange) || StringUtil.isNullOrEmpty(directKey)) {
                throw new ValidationException("Không tìm thấy rabbit mq cho service " + service);
            }
            //Call publisher
            if (rabbitMQClient.callPublishService(directExchange, directKey, request.toJsonString())) {
                MessageContent mc = new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), "OK");
                ResponseMessage responseMessage = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.OK.toString(), mc);
                result = responseMessage.toJsonString();
            } else {
                MessageContent mc = new MessageContent(HttpStatus.EXPECTATION_FAILED.value(), HttpStatus.EXPECTATION_FAILED.toString(), "Error");
                ResponseMessage responseMessage = new ResponseMessage(HttpStatus.EXPECTATION_FAILED.value(), HttpStatus.EXPECTATION_FAILED.toString(), mc);
                result = responseMessage.toJsonString();
            }
        } else {
            MessageContent mc = new MessageContent(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString(), "Error");
            ResponseMessage responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString(), mc);
            result = responseMessage.toJsonString();
            throw new ValidationException("Không tìm thấy xử lý cho kiểu rabbit " + rabbitType);
        }
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //mapper.setDateFormat(df);
            ResponseMessage response = mapper.readValue(result, ResponseMessage.class);
            return new ResponseEntity(response.getData(), HttpStatus.valueOf(response.getStatus()));
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST);
    }
    
    public String getElapsedTime(long miliseconds) {
        //return (miliseconds / 1000.0) + "(s)";
        return miliseconds + " (ms)";
    }
}
