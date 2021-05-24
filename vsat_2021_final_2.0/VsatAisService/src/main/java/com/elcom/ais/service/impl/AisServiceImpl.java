package com.elcom.ais.service.impl;

import com.elcom.ais.model.dto.UpsertProcessResult;
import com.elcom.ais.model.dto.VesselDTO;
import com.elcom.ais.model.dto.request.AddObjectToGroup;
import com.elcom.ais.model.dto.request.AdvanceSearch;
import com.elcom.ais.model.vessel.ObjectGroup;
import com.elcom.ais.repository.Object.CountriesRepository;
import com.elcom.ais.repository.Object.VesselGroupRepository;
import com.elcom.ais.repository.Object.VesselTypesRepository;
import com.elcom.gateway.message.MessageContent;
import com.elcom.ais.model.dto.request.media.SearchListAisRequest;
import com.elcom.ais.model.vessel.Countries;
import com.elcom.ais.repository.ais.AisRepository;
import com.elcom.ais.model.vessel.VesselGroup;
import com.elcom.ais.model.vessel.VesselTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.elcom.ais.service.AisService;
import com.elcom.ais.utils.StringUtil;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Service
@SuppressWarnings("unchecked")
public class AisServiceImpl implements AisService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AisServiceImpl.class);

    @Autowired
    private AisRepository aisRepository;

    @Autowired
    private VesselGroupRepository vesselGroupRepository;

    @Autowired
    private VesselTypesRepository vesselTypesRepository;

    @Autowired
    private CountriesRepository countriesRepository;

    //<!-- AIS messages
    @Override
    public MessageContent searchAisList(SearchListAisRequest data) {
        return aisRepository.searchAisList(data);
    }

    @Override
    public MessageContent searchAisListGeneral(SearchListAisRequest data) {
        return aisRepository.searchAisListGeneral(data);
    }
    //--->

    //<!-- Vessel group
    @Override
    public Page<ObjectGroup> searchObjectGroupList(String name, Integer isActive, Integer currentPage, Integer rowsPerPage, String sortBy) {
        try {
            if (currentPage > 0) currentPage--;
            return aisRepository.searchObjectGroupList(name, isActive, PageRequest.of(currentPage, rowsPerPage), sortBy);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
        }
        return null;
    }

    @Override
    public UpsertProcessResult saveVesselGroup(ObjectGroup objectGroup) {
        UpsertProcessResult upsertProcessResult = new UpsertProcessResult(true);
        try {
            if (objectGroup.getId() != null) {
                VesselGroup currentItem = this.aisRepository.findVesselGroupById(objectGroup.getId());
                if (currentItem == null) {
                    LOGGER.error("Couldn't find vessel group to update by id {}", objectGroup.getId());
                    return new UpsertProcessResult(false, "vesselId not found: [" + objectGroup.getId() + "]");
                }
                objectGroup.setCreatedBy(currentItem.getCreatedBy());
                objectGroup.setCreatedTime(currentItem.getCreatedTime());
            }
            if (this.aisRepository.existsObjectGroup(objectGroup.getName(), objectGroup.getId() != null ? "UPDATE" : "INSERT", objectGroup.getId())) {
                LOGGER.error("Duplicate row with name: {}", objectGroup.getName());
                return new UpsertProcessResult(false, "Duplicate row with name: [" + objectGroup.getName() + "]");
            }
            this.vesselGroupRepository.save(objectGroup);
        } catch (Exception ex) {
            LOGGER.error(StringUtil.printException(ex));
            return new UpsertProcessResult(false, "Internal server error");
        }
        return upsertProcessResult;
    }

    @Override
    public ObjectGroup saveAndReturnObjectGroup(ObjectGroup vesselGroup){
        ObjectGroup _objectGroup=null;
        try {
            if (this.aisRepository.existsObjectGroup(vesselGroup.getName(), null, null)) {
                LOGGER.error("Duplicate row with name: {}", vesselGroup.getName());
                return _objectGroup;
            }
            _objectGroup=this.vesselGroupRepository.save(vesselGroup);
        } catch (Exception ex) {
            LOGGER.error(StringUtil.printException(ex));
        }
        return _objectGroup;
    }

    @Override
    public boolean removeObjectGroup(Long id) {
        try {
            if (id != null) {
                VesselGroup currentItem = this.aisRepository.findVesselGroupById(id);
                if (currentItem == null) {
                    LOGGER.error("Couldn't find vessel group to delete by id {}", id);
                    return false;
                }
                this.aisRepository.removeObjectGroup(currentItem.getId());
            }
        } catch (Exception ex) {
            LOGGER.error(StringUtil.printException(ex));
            return false;
        }
        return false;
    }

    @Override
    public VesselGroup findDetailVesselGroup(Long id) {
        try {
            if (id != null) {
                VesselGroup currentItem = this.aisRepository.findVesselGroupById(id);
                if (currentItem == null) {
                    LOGGER.error("Couldn't find vessel group by id {}", id);
                    return null;
                }
                return currentItem;
            }
        } catch (Exception ex) {
            LOGGER.error(StringUtil.printException(ex));
        }
        return null;
    }
    //--->

    //<!-- Vessel
    @Override
    public Page<VesselDTO> searchVesselList(String mmsi, String vesselName, Integer countryId, Integer vesselTypeId, String ip, String phone,
             Integer currentPage, Integer rowsPerPage) {
        if (currentPage > 0) {
            currentPage--;
        }
        Page<VesselDTO> result = this.aisRepository.searchVesselList(mmsi, vesselName, countryId, vesselTypeId, ip, phone,
                 PageRequest.of(currentPage, rowsPerPage));
        return result;
    }

    @Override
    public MessageContent findDetailVessel(Long mmsi) {
        try {
            return aisRepository.findDetailVessel(mmsi);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
        }
        return null;
    }
    //--->

    @Override
    public List<Countries> getListCountry() {
        return (List<Countries>) countriesRepository.findAll();
    }

    @Override
    public MessageContent addVesselToGroup(AddObjectToGroup data){
        MessageContent messageContent =null;
        try {
            messageContent= this.aisRepository.addVesselToGroup(data);
        }
        catch (Exception ex){
            LOGGER.error(StringUtil.printException(ex));
        }
        return messageContent;
    }

    @Override
    public List<VesselTypes> getListVesselType() {
        List<VesselTypes> lst = null;
        try {
            lst = this.vesselTypesRepository.findAll();
        } catch (Exception ex) {
            LOGGER.error(StringUtil.printException(ex));
        }
        return lst;
    }

    @Override
    public Page<VesselDTO> searchVesselListAdvanced(String mmsi, String vesselName, Integer countryId, Integer vesselTypeId, String ip, String phone, Integer currentPage, Integer rowsPerPage,
            AdvanceSearch advanceSearch) {
        if (currentPage > 0) {
            currentPage--;
        }
        Page<VesselDTO> result = this.aisRepository.searchVesselListAdvanced(mmsi, vesselName, countryId, vesselTypeId, ip, phone,
                 PageRequest.of(currentPage, rowsPerPage),advanceSearch);
        return result;
    }
}
