package com.elcom.contact.service.impl;

import com.elcom.contact.model.contact.AisInfo;
import com.elcom.contact.model.contact.ContactIP;
import com.elcom.contact.model.contact.MmsiImage;
import com.elcom.contact.model.contact.VesselAltInfo;
import com.elcom.contact.model.dto.request.contact.AddContactIpRequest;
import com.elcom.contact.model.dto.request.contact.AddVesselRequest;
import com.elcom.contact.model.dto.request.contact.DelContactIpRequest;
import com.elcom.contact.model.dto.request.contact.GetDetailVesselRequest;
import com.elcom.contact.model.dto.request.contact.SearchContactRequest;
import com.elcom.contact.model.media.MediaRaw;
import com.elcom.contact.repository.contact.AisInfoRepository;
import com.elcom.contact.repository.contact.ContactRepository;
import com.elcom.contact.repository.contact.MmsiImageRepository;
import com.elcom.contact.repository.contact.VesselAltInfoRepository;
import com.elcom.contact.service.ContactService;
import com.elcom.gateway.message.MessageContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactServiceImpl.class);
    @Autowired
    ContactRepository contactRepository;

    @Autowired
    AisInfoRepository aisInfoRepository;

    @Autowired
    VesselAltInfoRepository vesselAltInfoRepository;

    @Autowired
    MmsiImageRepository mmsiImageRepository;

    public MessageContent search(SearchContactRequest data) {
        try {
//            mediaRaw=mediaRepository.getInfoByMediaId(data);
            return contactRepository.search(data);
        } catch (Exception e) {
            LOGGER.error("MediaListPro.getListMediaTypes ==> error : ", e);
        }
        return null;
    }

    public MessageContent getDetailVessel(GetDetailVesselRequest data) {
        try {
            return contactRepository.getDetailVessel(data);
        } catch (Exception e) {
            LOGGER.error("getDetailVessel ==> error : ", e);
        }
        return null;
    }
    
    public ContactIP checkIpVessel(AddContactIpRequest data) {
        try {
//            mediaRaw=mediaRepository.getInfoByMediaId(data);
            return contactRepository.checkIpVessel(data);
        } catch (Exception e) {
            LOGGER.error("MediaListPro.getListMediaTypes ==> error : ", e);
        }
        return null;
    }

    public Object addIpVessel(AddContactIpRequest data) {
        try {
//            mediaRaw=mediaRepository.getInfoByMediaId(data);
            if (this.checkIpVessel(data) == null) {
                return contactRepository.addIpVessel(data);
            } else {
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("MediaListPro.addIpVessel ==> error : ", e);
            throw e;
        }
    }

    public Object delIpVessel(DelContactIpRequest data) {
        try {
//            mediaRaw=mediaRepository.getInfoByMediaId(data);
            return contactRepository.delIpVessel(data);
        } catch (Exception e) {
            LOGGER.error("MediaListPro.delIpVessel ==> error : ", e);
            throw e;
        }
    }

    @Override
    public boolean save(AisInfo ais) {
        boolean result = true;
        try {
            aisInfoRepository.save(ais);
        } catch (Exception ex) {
            result = false;
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean save(VesselAltInfo vesselAltInfo) {
        boolean result = true;
        try {
            vesselAltInfoRepository.save(vesselAltInfo);
        } catch (Exception ex) {
            result = false;
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean save(MmsiImage mmsiImage) {
        boolean result = true;
        try {
            mmsiImageRepository.save(mmsiImage);
        } catch (Exception ex) {
            result = false;
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public AisInfo finAisInfo(long mmsi) {
        return aisInfoRepository.findByMmsi(mmsi);
    }

    @Override
    public boolean updateAisInfo(AddVesselRequest data) {
        try {
            return contactRepository.updateAisInfo(data);
        } catch (Exception e) {
            LOGGER.error("addHeadquater.addHeadquater ==> error : ", e);
            throw e;
        }
    }

    @Override
    public boolean updateVesselAltInfo(AddVesselRequest data) {
        try {
            return contactRepository.updateVesselAltInfo(data);
        } catch (Exception e) {
            LOGGER.error("addHeadquater.addHeadquater ==> error : ", e);
            throw e;
        }
    }

    @Override
    public VesselAltInfo findVesselInfo(long mmsi) {
        return vesselAltInfoRepository.findByMmsi(mmsi);
    }

    @Override
    public void deleteVesselById(long mmsi) {
        try {
            aisInfoRepository.deleteById(mmsi);
        } catch (Exception e) {
            LOGGER.error("deleteVesselById ==> error : ", e);
            throw e;
        }
    }
}
