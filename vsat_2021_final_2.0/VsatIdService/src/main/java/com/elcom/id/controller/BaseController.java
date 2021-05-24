/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.id.controller;

import com.elcom.gateway.message.RequestMessage;
import com.elcom.gateway.message.ResponseMessage;
import com.elcom.id.constant.Constant;
import com.elcom.id.messaging.rabbitmq.RabbitMQClient;
import com.elcom.id.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.id.model.TokenUserLogin;
import com.elcom.id.model.dto.OtpExpiredDTO;
import com.elcom.id.utils.JSONConverter;
import com.elcom.id.utils.StringUtil;
import com.elcom.id.utils.encrypt.RSAEncrypter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Admin
 */
public class BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * Check quyền của user ứng với request method và api đang gọi
     *
     * @param requestMethod : Request method POST, GET, PUT, DELETE
     * @param userUuid user uuid
     * @param apiPath api link
     * @return
     */
    public boolean authorizeRBAC(String requestMethod, String userUuid, String apiPath) {
        //Set body param
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("requestMethod", requestMethod);
        bodyParam.put("uuid", userUuid);
        bodyParam.put("apiPath", apiPath);

        //Authorize user action with api -> call rbac service
        RequestMessage rbacRpcRequest = new RequestMessage();
        rbacRpcRequest.setRequestMethod("POST");
        rbacRpcRequest.setRequestPath(RabbitMQProperties.RBAC_RPC_AUTHOR_URL);
        rbacRpcRequest.setBodyParam(bodyParam);
        rbacRpcRequest.setUrlParam(null);
        rbacRpcRequest.setHeaderParam(null);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.RBAC_RPC_EXCHANGE,
                RabbitMQProperties.RBAC_RPC_QUEUE, RabbitMQProperties.RBAC_RPC_KEY, rbacRpcRequest.toJsonString());
        LOGGER.info("authorizeRBAC - result: " + result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage response = null;
            try {
                response = mapper.readValue(result, ResponseMessage.class);
            } catch (JsonProcessingException ex) {
                LOGGER.info("Lỗi parse json khi gọi kiểm tra quyền từ rbac service: " + ex.toString());
                return false;
            }
            return (response != null && response.getStatus() == HttpStatus.OK.value());
        }
        return false;
    }

    public OtpExpiredDTO getOtpExpiredTime(String userUuid) {
        String folderKey = Constant.LEANR_OTP_KEY;
        OtpExpiredDTO result = null;
        try {
            if (redisTemplate.opsForHash().hasKey(folderKey, userUuid)) {
                result = (OtpExpiredDTO) redisTemplate.opsForHash().get(folderKey, userUuid);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.info("Lỗi lấy dữ liệu OTP Expired từ Redis: " + ex.toString());
        }
        return result;
    }

    public boolean pushOtpExpiredTime(String userUuid, OtpExpiredDTO result) {
        String folderKey = Constant.LEANR_OTP_KEY;
        try {
            if (result != null) {
                redisTemplate.opsForHash().put(folderKey, userUuid, result);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.info("Lỗi đẩy OTP Expired lên Redis: " + ex.toString());
            return false;
        }
        return true;
    }

    public boolean pushTokenLogin(String token, Timestamp createAt) {
        String folderKey = Constant.REDIS_TIME_TOKEN_LOGIN_CREATE;
        try {
            if (token != null && createAt != null) {
                redisTemplate.opsForHash().put(folderKey, token, createAt);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.info("Lỗi đẩy token login lên Redis: " + ex.toString());
            return false;
        }
        return true;
    }

    public Timestamp getTimeCreateTokenLogin(String token) {
        String folderKey = Constant.REDIS_TIME_TOKEN_LOGIN_CREATE;
        Timestamp result = null;
        try {
            if (redisTemplate.opsForHash().hasKey(folderKey, token)) {
                result = (Timestamp) redisTemplate.opsForHash().get(folderKey, token);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.info("Lỗi lấy dữ liệu token login từ Redis: " + ex.toString());
        }
        return result;
    }

    public String getTokenLogoutInBlackList(String token) {
        String folderKey = Constant.REDIS_BLACK_LIST_LOGOUT_TOKEN;
        String result = null;
        try {
            if (redisTemplate.opsForHash().hasKey(folderKey, token)) {
                result = (String) redisTemplate.opsForHash().get(folderKey, token);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.info("Lỗi lấy dữ liệu token login từ Redis: " + ex.toString());
        }
        return result;
    }
    
    public boolean pushTokenLogoutIntoBlackList(String token) {
        String folderKey = Constant.REDIS_BLACK_LIST_LOGOUT_TOKEN;
        try {
            if (token != null) {
                redisTemplate.opsForHash().put(folderKey, token, token);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.info("Lỗi đẩy token logout vào blackList lên Redis: " + ex.toString());
            return false;
        }
        return true;
    }
}
