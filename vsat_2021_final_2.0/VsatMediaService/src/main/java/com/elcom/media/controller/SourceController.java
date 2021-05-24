package com.elcom.media.controller;

import com.elcom.gateway.message.MessageContent;
import com.elcom.gateway.message.ResponseMessage;
import com.elcom.media.constant.Constant;
import com.elcom.media.model.Source;
import com.elcom.media.service.SourceService;
import com.elcom.media.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;

@Controller
public class SourceController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SourceController.class);
    
    @Autowired
    private SourceService sourceService;
    
    public ResponseMessage getAll(Map<String, String> headerParam) {
        ResponseMessage responseMessage=new ResponseMessage();
        responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            List<Source> lstSource= sourceService.getAll();
            if ( lstSource==null || lstSource.isEmpty() )
                return new ResponseMessage(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                        new MessageContent(HttpStatus.OK.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
            else
                return new ResponseMessage(new MessageContent(lstSource));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
        }
        return responseMessage;
    }
}
