package com.elcom.media.messaging.rabbitmq;

import com.elcom.media.bussiness.MediaBusiness;
import com.elcom.media.controller.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.elcom.gateway.message.RequestMessage;
import com.elcom.gateway.message.ResponseMessage;
import com.elcom.media.exception.ValidationException;
import com.elcom.media.utils.StringUtil;
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
public class MediaRpcServer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaRpcServer.class);

    @Autowired
    private MediaBusiness mediaController;

    @Autowired
    private SourceController sourceController;
    
    @RabbitListener(queues = "${media.rpc.queue}")
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
                        if ("/v1.0/manager/media-type/get-all".equalsIgnoreCase(requestPath))
                            response = mediaController.getListMediaTypes(headerParam);
                        else if ("/v1.0/manager/source/get-all".equalsIgnoreCase(requestPath))
                            response = sourceController.getAll(headerParam);
                        break;
                    case "POST":
                        if("/v1.0/media/list/search".equalsIgnoreCase(requestPath))
                            response = mediaController.search(bodyParam, headerParam);
                        else if("/v1.0/media/detail-relation".equalsIgnoreCase(requestPath)) // Chi tiết cặp media đã xử lý
                            response = mediaController.getDetailMediaRelation(bodyParam, headerParam);
                        else if("/v1.0/media/list-by-relation-id".equalsIgnoreCase(requestPath)) // Danh sách media raws theo list uuid (Call internal)
                            response = mediaController.getListMediaByRelationId(bodyParam, headerParam);
                        if("/v1.0/media/total-by-object".equalsIgnoreCase(requestPath))
                            response = mediaController.totalByObject(bodyParam, headerParam); // Tổng media của tàu/đối tượng ko xác định
                        else if("/v1.0/media/list-comments".equalsIgnoreCase(requestPath)) // Lấy danh sách comment
                            response = mediaController.getListComment(bodyParam, headerParam);
                        else if("/v1.0/media/fetch-m3u8-file".equalsIgnoreCase(requestPath)) // Chi tiết từng media
                            response = mediaController.getM3U8File(bodyParam, headerParam);
                        else if("/v1.0/media/list/updateStatusMediaId".equalsIgnoreCase(requestPath))
                            response = mediaController.updateStatusMediaId(bodyParam, headerParam);
                        else if("/v1.0/media/list/addCommentCommon".equalsIgnoreCase(requestPath))
                            response = mediaController.addCommentCommon(bodyParam, headerParam);
                        else if("/v1.0/media/list-relation/search".equalsIgnoreCase(requestPath))
                            response = mediaController.searchMediaRelation(bodyParam, headerParam);
//                        else if("/v1.0/manager/ctrlsource/getall".equalsIgnoreCase(requestPath))
//                            response = sourceController.getall(bodyParam, headerParam);
                        break;
                    case "PUT":
//                        if ("/v1.0/user/internal".equalsIgnoreCase(requestPath) && !StringUtil.isNullOrEmpty(pathParam))//Update via service
//                            response = userController.updateUserInternal(bodyParam, headerParam, pathParam);
//                        else if ("/v1.0/user".equalsIgnoreCase(requestPath))//Update JWT
//                            response = userController.updateUser(bodyParam, headerParam);
//                        else if("/v1.0/user/password".equalsIgnoreCase(requestPath))//Change password
//                            response = userController.updatePassword(bodyParam, headerParam);
//                        else if("/v1.0/user/status".equalsIgnoreCase(requestPath))//Change status
//                            response = userController.updateStatus(bodyParam, headerParam);
//                        else if("/v1.0/user/forgotPassword".equalsIgnoreCase(requestPath))//Change password from forgot password
//                            response = userController.updateForgotPassword(bodyParam, headerParam);
                        response = null;
                        break;
                    case "PATCH":
                        break;
                    case "DELETE":
//                        if ("/v1.0/user".equalsIgnoreCase(requestPath) && pathParam != null && pathParam.length() > 0) // Delete by id
//                            response = userController.deleteUser(pathParam);
                        response = null;
                        break;
                    default:
                        break;
                }
            }
            LOGGER.info(" [<--] Server returned " + (response!=null ? response.toJsonString() : null));
            return response!=null ? response.toJsonString() : null;
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        }
        return null;
    }
}
