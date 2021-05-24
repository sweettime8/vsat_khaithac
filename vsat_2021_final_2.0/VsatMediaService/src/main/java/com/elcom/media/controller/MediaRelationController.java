package com.elcom.media.controller;

import com.elcom.media.bussiness.MediaBusiness;
import com.elcom.gateway.message.MessageContent;
import com.elcom.gateway.message.ResponseMessage;
import com.elcom.media.constant.Constant;
import com.elcom.media.model.dto.request.media.SearchListMediaRequest;
import com.elcom.media.service.MediaRelationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.springframework.stereotype.Controller;

@Controller
public class MediaRelationController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaBusiness.class);
    
    @Autowired
    private MediaRelationService mediaService;

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseMessage search(Map<String, Object> bodyParam,Map<String, String> headerParam) {

        ResponseMessage responseMessage=new ResponseMessage();
        responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            SearchListMediaRequest data = mapper.convertValue(bodyParam, SearchListMediaRequest.class);
            MessageContent messageContent = mediaService.search(data);
            
            if ( messageContent==null || messageContent.getData()==null )
                return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                        new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
            else
                return new ResponseMessage(messageContent);

        } catch (Exception e) {
            LOGGER.error("MediaListController.search ==> error : ", e);
        }

        return responseMessage;
    }

}
