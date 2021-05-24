package com.elcom.media.service.impl;

import com.elcom.gateway.message.MessageContent;
import com.elcom.media.model.dto.request.media.SearchListMediaRequest;

import com.elcom.media.repository.media.MediaRelationRepository;
import com.elcom.media.service.MediaRelationService;
import com.elcom.media.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MediaRelationServiceImpl implements MediaRelationService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaRelationServiceImpl.class);
    @Autowired
    private MediaRelationRepository mediaRelationRepository;

    @Override
    public MessageContent search(SearchListMediaRequest data){
        MessageContent messageContent=null;
        try {
            messageContent = mediaRelationRepository.searchMediaRelation(data);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
        }
        return messageContent;
    }
}
