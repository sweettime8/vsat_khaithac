package com.elcom.contact.service;

import com.elcom.contact.model.contactObject.ContactObjectIP;
import com.elcom.contact.model.contactObject.ObjectUnInfoDto;
import com.elcom.contact.model.dto.request.contact.SearchContactRequest;
import com.elcom.contact.model.dto.request.contactObject.AddContactObjectIpRequest;
import com.elcom.contact.model.dto.request.contactObject.AddObjectUnInfoRequest;
import com.elcom.contact.model.dto.request.contactObject.DelContactObjectIpRequest;
import com.elcom.contact.model.dto.request.contactObject.GetDetailObjectRequest;
import com.elcom.gateway.message.MessageContent;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface ContactObjectService {

    Page<ObjectUnInfoDto> search(SearchContactRequest data);

    Object getDetailObjectInfo(GetDetailObjectRequest data);

    ContactObjectIP checkIpObjectInfo(AddContactObjectIpRequest data);

    MessageContent addIpObjectInfo(AddContactObjectIpRequest data);

    Object delIpObjectInfo(DelContactObjectIpRequest data);

    Object addObjectUnInfo(AddObjectUnInfoRequest data);

    boolean updateObjectUnInfo(AddObjectUnInfoRequest data);
    
    void deleteObjectUnInfo(UUID uuid);
}
