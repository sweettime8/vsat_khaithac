package com.elcom.contact.service.impl;

import com.elcom.contact.model.contact.ContactIP;
import com.elcom.contact.model.contact.Phone;
import com.elcom.contact.model.contactLand.HeadQuarter;
import com.elcom.contact.model.contactLand.HeadQuarterDto;
import com.elcom.contact.model.dto.request.contacLand.AddContactLandIpRequest;
import com.elcom.contact.model.dto.request.contacLand.AddHeadquaterRequest;
import com.elcom.contact.model.dto.request.contacLand.ContactLandPhoneRequest;
import com.elcom.contact.model.dto.request.contacLand.DelContactLandIpRequest;
import com.elcom.contact.model.dto.request.contacLand.GetDetailHeadquatersRequest;
import com.elcom.contact.model.dto.request.contact.SearchContactRequest;
import com.elcom.contact.model.media.MediaRaw;
import com.elcom.contact.repository.contactLand.ContactLandCusRepository;
import com.elcom.contact.repository.contactLand.ContactLandRepository;
import com.elcom.contact.service.ContactLandService;
import com.elcom.gateway.message.MessageContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Service
public class ContactLandServiceImpl implements ContactLandService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactLandServiceImpl.class);
    @Autowired
    ContactLandRepository contactRepository;

    @Autowired
    ContactLandCusRepository contactLandCusRepository;

    public Page<HeadQuarterDto> search(SearchContactRequest data) {
        try {
            return contactRepository.search(data, PageRequest.of(data.getCurrentPage(), data.getRowsPerPage()));
        } catch (Exception e) {
            LOGGER.error("MediaListPro.getListMediaTypes ==> error : ", e);
        }
        return null;
    }

    public Object getDetailHeadQuarter(GetDetailHeadquatersRequest data) {
        try {
            return contactRepository.getHeadQuarterInfo(data);
        } catch (Exception e) {
            LOGGER.error("MediaListPro.getListMediaTypes ==> error : ", e);
        }
        return null;
    }

    public ContactIP checkIpHeadQuarter(AddContactLandIpRequest data) {
        try {
            return contactRepository.checkIpHeadQuarter(data);
        } catch (Exception e) {
            LOGGER.error("checkIpHeadQuarter ==> error : ", e);
        }
        return null;
    }

    public Object addIpHeadQuarter(AddContactLandIpRequest data) {
        try {
            if (this.checkIpHeadQuarter(data) == null) {
                return contactRepository.addIpHeadQuarter(data);
            } else {
                return null;
            }

        } catch (Exception e) {
            LOGGER.error("addIpHeadQuarter ==> error : ", e);
            throw e;
        }
    }

    public Object delIpHeadQuarter(DelContactLandIpRequest data) {
        try {
//            mediaRaw=mediaRepository.getInfoByMediaId(data);
            return contactRepository.delIpHeadQuarter(data);
        } catch (Exception e) {
            LOGGER.error("MediaListPro.getListMediaTypes ==> error : ", e);
        }
        return null;
    }

    public Object addHeadquater(AddHeadquaterRequest data) {
        try {
            HeadQuarter headQuarter = new HeadQuarter();
            headQuarter.setName(data.getName());
            headQuarter.setCountryId(data.getCountry());
            headQuarter.setLongitude(data.getLongitude());
            headQuarter.setLatitude(data.getLatitude());
            headQuarter.setVaiTro(data.getVai_tro());
            headQuarter.setChucNang(data.getChuc_nang());
            headQuarter.setTo_chuc(data.getTo_chuc());
            headQuarter.setStatus(data.getStatus());
            headQuarter.setDescription(data.getNote());
            headQuarter.setCreatedTime(new Timestamp(new Date().getTime()));
            return contactLandCusRepository.save(headQuarter);
        } catch (Exception e) {
            LOGGER.error("addHeadquater.addHeadquater ==> error : ", e);
            throw e;
        }
    }

    public boolean updateHeadquater(AddHeadquaterRequest data) {
        try {
            return contactRepository.updateHeadquater(data);
        } catch (Exception e) {
            LOGGER.error("addHeadquater.addHeadquater ==> error : ", e);
            throw e;
        }
    }

    @Override
    public void deleteHeadquaterById(long id) {
        try {
            contactLandCusRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error("deleteHeadquaterById ==> error : ", e);
            throw e;
        }
    }

    @Override
    public Object addPhoneHeadQuarter(ContactLandPhoneRequest data) {
        try {
            if (this.checkPhoneHeadQuarter(data) == null) {
                return contactRepository.addPhoneHeadQuarter(data);
            } else {
                return null;
            }

        } catch (Exception e) {
            LOGGER.error("addPhoneHeadQuarter ==> error : ", e);
            throw e;
        }
    }

    @Override
    public Object delPhoneHeadQuarter(ContactLandPhoneRequest data) {
        try {
            return contactRepository.delPhoneHeadQuarter(data);
        } catch (Exception e) {
            LOGGER.error("delPhoneHeadQuarter ==> error : ", e);
        }
        return null;
    }
    
    public Phone checkPhoneHeadQuarter(ContactLandPhoneRequest data) {
        try {
//            mediaRaw=mediaRepository.getInfoByMediaId(data);
            return contactRepository.checkPhoneHeadQuarter(data);
        } catch (Exception e) {
            LOGGER.error("checkPhoneHeadQuarter ==> error : ", e);
        }
        return null;
    }
}
