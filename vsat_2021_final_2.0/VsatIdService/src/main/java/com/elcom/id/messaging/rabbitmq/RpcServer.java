package com.elcom.id.messaging.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.elcom.gateway.message.RequestMessage;
import com.elcom.gateway.message.ResponseMessage;
import com.elcom.id.controller.AuthenController;
import com.elcom.id.controller.UserController;
import com.elcom.id.exception.ValidationException;
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
    private AuthenController authenController;
    
    @Autowired
    private UserController userController;
    
    @RabbitListener(queues = "${user.rpc.queue}")
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
                        //if ("/v1.0/user".equalsIgnoreCase(requestPath) && urlParam != null && urlParam.length() > 0) // Get all
                        //    response = userController.getAllUser(urlParam, headerParam);
                        //else 
                        if ("/v1.0/user".equalsIgnoreCase(requestPath) && pathParam != null && pathParam.length() > 0) // Get details
                            response = userController.getDetailUser(pathParam, headerParam);
                        else if ("/v1.0/user/exist".equalsIgnoreCase(requestPath)) // Check exist email or mobile
                            response = userController.checkUserExist(urlParam);
                        else if ("/v1.0/user/forgotPassword".equalsIgnoreCase(requestPath)) // Forgot password
                            response = userController.forgotPassword(urlParam);
                        else if ("/v1.0/user/cms".equalsIgnoreCase(requestPath)) // CMS user management
                            response = userController.getAllUser(urlParam, headerParam);  
                        break;
                    case "POST":
                        if ("/v1.0/user".equalsIgnoreCase(requestPath)) // Insert/update
                            response = userController.createUser(bodyParam,headerParam);
                        else if("/v1.0/user/login".equalsIgnoreCase(requestPath)) //Login
                            response = authenController.userLogin(bodyParam);
                        else if("/v1.0/user/logout".equalsIgnoreCase(requestPath)) //Login
                            response = authenController.userLogout(headerParam);                      
                        else if("/v1.0/user/authentication".equalsIgnoreCase(requestPath)) //authentication web
                            response = authenController.authorized(headerParam);

                        else if("/v1.0/user/uuidLst".equalsIgnoreCase(requestPath)) // List user by list uuid
                            response = userController.findByUuid(headerParam, bodyParam);
                        else if("/v1.0/user/uuidAvatarLst".equalsIgnoreCase(requestPath)) // List user avatar by list uuid
                            response = userController.findAvatarByUuid(bodyParam);
                        else if("/v1.0/user/forgotPassword/checkToken".equalsIgnoreCase(requestPath)) // Check token
                            response = userController.checkToken(bodyParam, headerParam);
                        else if("/v1.0/user/sendEmail".equalsIgnoreCase(requestPath)) // Check token
                            response = userController.sendEmail(bodyParam, headerParam);
                        break;
                    case "PUT":
                        if ("/v1.0/user/internal".equalsIgnoreCase(requestPath) && !StringUtil.isNullOrEmpty(pathParam))//Update via service
                            response = userController.updateUserInternal(bodyParam, headerParam, pathParam);
                        else if ("/v1.0/user".equalsIgnoreCase(requestPath))//Update JWT
                            response = userController.updateUser(bodyParam, headerParam);
                        else if ("/v1.0/user/edit-user".equalsIgnoreCase(requestPath))//Update JWT
                            response = userController.updateUserByUserLogin(bodyParam, headerParam);    
                        else if("/v1.0/user/password".equalsIgnoreCase(requestPath))//Change password
                            response = userController.updatePassword(bodyParam, headerParam);
                        else if("/v1.0/user/reset-password".equalsIgnoreCase(requestPath))//Change password
                            response = userController.resetPassword(bodyParam, headerParam);                      
                        else if("/v1.0/user/forgotPassword".equalsIgnoreCase(requestPath))//Change password from forgot password
                            response = userController.updateForgotPassword(bodyParam, headerParam);
                        break;
                    case "PATCH":
                        break;
                    case "DELETE":
                        if ("/v1.0/user".equalsIgnoreCase(requestPath) && pathParam != null && pathParam.length() > 0) // Delete by id
                            response = userController.deleteUser(pathParam);
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
