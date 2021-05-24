package com.elcom.contact.service.impl;

import com.elcom.contact.model.contactObject.ContactObjectIP;
import com.elcom.contact.model.contactObject.ObjectUnInfo;
import com.elcom.contact.model.contactObject.ObjectUnInfoDto;
import com.elcom.contact.model.dto.request.contact.SearchContactRequest;
import com.elcom.contact.model.dto.request.contactObject.AddContactObjectIpRequest;
import com.elcom.contact.model.dto.request.contactObject.AddObjectUnInfoRequest;
import com.elcom.contact.model.dto.request.contactObject.DelContactObjectIpRequest;
import com.elcom.contact.model.dto.request.contactObject.GetDetailObjectRequest;
import com.elcom.contact.model.media.MediaRaw;
import com.elcom.contact.repository.contactObject.ContactObjectCusRepository;
import com.elcom.contact.repository.contactObject.ContactObjectRepository;
import com.elcom.contact.service.ContactObjectService;
import com.elcom.gateway.message.MessageContent;
import java.sql.Timestamp;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ContactObjectServiceImpl implements ContactObjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactObjectServiceImpl.class);
    @Autowired
    ContactObjectRepository contactRepository;

    @Autowired
    ContactObjectCusRepository contactObjectCusRepository;

    public Page<ObjectUnInfoDto> search(SearchContactRequest data) {
        try {
            return contactRepository.search(data, PageRequest.of(data.getCurrentPage(), data.getRowsPerPage()));
        } catch (Exception e) {
            LOGGER.error("MediaListPro.getListMediaTypes ==> error : ", e);
        }
        return null;
    }

    public Object getDetailObjectInfo(GetDetailObjectRequest data) {
        try {
            return contactRepository.getObjectInfo(data);
        } catch (Exception e) {
            LOGGER.error("getDetailObjectInfo ==> error : ", e);
        }
        return null;
    }

    public ContactObjectIP checkIpObjectInfo(AddContactObjectIpRequest data) {
        try {
            return contactRepository.checkIpObjectInfo(data);
        } catch (Exception e) {
            LOGGER.error("MediaListPro.getListMediaTypes ==> error : ", e);
            throw e;
        }
//        return null;
    }

    public MessageContent addIpObjectInfo(AddContactObjectIpRequest data) {
        MessageContent messageContent=new MessageContent();
        try {
            messageContent.setStatus(HttpStatus.OK.value());
            if (this.checkIpObjectInfo(data) == null) {
                messageContent.setData(contactRepository.addIpObjectInfo(data));
            } else {
                messageContent.setStatus(HttpStatus.OK.value());
                messageContent.setMessage("Đối tượng đã tồn tại IP này.");
            }
        } catch (Exception e) {
            LOGGER.error("MediaListPro.getListMediaTypes ==> error : ", e);
            throw e;
        }
        return messageContent;
    }

    public Object delIpObjectInfo(DelContactObjectIpRequest data) {
        try {
            return contactRepository.delIpObjectInfo(data);
        } catch (Exception e) {
            LOGGER.error("MediaListPro.getListMediaTypes ==> error : ", e);
            throw e;
        }
    }

    public Object addObjectUnInfo(AddObjectUnInfoRequest data) {
        try {
            ObjectUnInfo objectUnInfo = new ObjectUnInfo();
            objectUnInfo.setCountry_id(data.getCountry_id());
            objectUnInfo.setDimA(data.getDimA());
            objectUnInfo.setDimB(data.getDimB());
            objectUnInfo.setDimC(data.getDimC());
            objectUnInfo.setDimD(data.getDimD());
            objectUnInfo.setName(data.getName());
            objectUnInfo.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            objectUnInfo.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
            objectUnInfo.setUserUpdate(data.getUserUpdate());
            objectUnInfo.setImagePath(data.getImagePath());
            objectUnInfo.setSourceIp(data.getSourceIP());
            objectUnInfo.setSourcePort(data.getSourcePort());
            objectUnInfo.setDestIp(data.getDestIP());
            objectUnInfo.setDestPort(data.getDestPort());
            return contactObjectCusRepository.save(objectUnInfo);
        } catch (Exception e) {
            LOGGER.error("MediaListPro.getListMediaTypes ==> error : ", e);
            throw e;
        }
    }

    @Override
    public boolean updateObjectUnInfo(AddObjectUnInfoRequest data) {
        try {
            return contactRepository.updateObjectUnInfo(data);
        } catch (Exception e) {
            LOGGER.error("addHeadquater.addHeadquater ==> error : ", e);
            throw e;
        }
    }

    @Override
    public void deleteObjectUnInfo(UUID uuid) {
        try {
            contactObjectCusRepository.deleteById(uuid);
        } catch (Exception e) {
            LOGGER.error("deleteObjectUnInfo ==> error : ", e);
            throw e;
        }
    }
}
