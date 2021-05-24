package com.elcom.media.service;

import com.elcom.gateway.message.MessageContent;
import com.elcom.media.model.dto.request.media.SearchListMediaRequest;


public interface MediaRelationService {
    MessageContent search(SearchListMediaRequest data);
}
