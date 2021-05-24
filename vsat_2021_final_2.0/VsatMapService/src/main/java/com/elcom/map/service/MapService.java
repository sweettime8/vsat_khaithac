package com.elcom.map.service;

import com.elcom.gateway.message.MessageContent;
import com.elcom.map.model.dto.request.map.AddAreaRequest;
import com.elcom.map.model.dto.request.map.DeleteAreaRequest;
import com.elcom.map.model.dto.request.map.GetListAreaRequest;

import java.util.List;

public interface MapService {
    MessageContent saveArea(List<AddAreaRequest> data);

    MessageContent getListArea(GetListAreaRequest data);

    MessageContent delArea(DeleteAreaRequest data);
}
