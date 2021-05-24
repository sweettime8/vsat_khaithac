package com.elcom.vsat.controller;


import com.elcom.gateway.message.MessageContent;
import com.elcom.gateway.message.ResponseMessage;
import com.elcom.media.bussiness.MediaBusiness;
import com.elcom.vsat.model.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/media/ip")
public class MediaIpController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaBusiness.class);

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseMessage searchMedia(HttpServletRequest request) {
        Map<String, Object> model = new HashMap<>();
        model.put("data", new ArrayList());
        List<MediaType> lstData=new ArrayList<MediaType>();
        ResponseMessage responseMessage=new ResponseMessage();
        responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
//            lstData= mediaService.getListMediaTypes(request,null,null);
            model.put("data", lstData);
            responseMessage.setStatus(HttpStatus.OK.value());
            responseMessage.setData(new MessageContent(lstData));
        } catch (Exception e) {
            responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
            responseMessage.setMessage(e.getMessage());
            LOGGER.error("getListMediaTypes error: ", e);
        }
        return responseMessage;
    }
}
