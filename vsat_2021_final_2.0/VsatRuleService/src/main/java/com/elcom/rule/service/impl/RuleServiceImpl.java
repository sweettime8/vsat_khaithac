package com.elcom.rule.service.impl;

import com.elcom.rule.model.dto.UpsertProcessResult;
import com.elcom.rule.model.dto.VesselDTO;
import com.elcom.rule.model.dto.request.AddVessellToGroup;
import com.elcom.gateway.message.MessageContent;
import com.elcom.rule.model.dto.DetailRuleEventDTO;
import com.elcom.rule.model.dto.RuleEventNotifiDTO;
import com.elcom.rule.model.vessel.Countries;
import com.elcom.rule.repository.rule.RuleRepository;
import com.elcom.rule.model.vessel.VesselGroup;
import com.elcom.rule.model.vessel.VesselTypes;
import com.elcom.rule.repository.vessel.CountriesRepository;
import com.elcom.rule.repository.vessel.VesselGroupRepository;
import com.elcom.rule.repository.vessel.VesselTypesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.elcom.rule.utils.StringUtil;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.elcom.rule.service.RuleService;

@Service
@SuppressWarnings("unchecked")
public class RuleServiceImpl implements RuleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleServiceImpl.class);

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private VesselGroupRepository vesselGroupRepository;

    @Autowired
    private VesselTypesRepository vesselTypesRepository;

    @Autowired
    private CountriesRepository countriesRepository;

    //<!-- Rule
    @Override
    public Page<RuleEventNotifiDTO> findRuleEvents(String startTime, String endTime, Long ruleId, Long groupObjectId
                                                , String objId, String objName, Integer currentPage, Integer rowsPerPage) {
        if (currentPage > 0) currentPage--;
        return this.ruleRepository.findRuleEvents(startTime, endTime, ruleId, groupObjectId, objId, objName, PageRequest.of(currentPage, rowsPerPage));
    }
    
    @Override
    public Long countRuleEventByObjId(String startTime, String endTime, String objId) {
        return this.ruleRepository.countRuleEventByObjId(startTime, endTime, objId);
    }
    
    @Override
    public boolean updateRuleEventReadStatus(String ruleNotifyUuid) {
        return this.ruleRepository.updateRuleEventReadStatus(ruleNotifyUuid);
    }
    
    @Override
    public DetailRuleEventDTO findDetailRuleEvent(String ruleNotifyUuid, String objId) {
        if( StringUtil.validUuid(objId) )
            return this.ruleRepository.findDetailRuleEventWithObjectUndefined(ruleNotifyUuid);
        else if( StringUtil.isNumeric(objId) )
            return this.ruleRepository.findDetailRuleEventWithVessel(ruleNotifyUuid);
        else // Invalid objId
            return null;
    }
    //--->
    
    
    
    //<!-- Vessel group
    @Override
    public Page<VesselGroup> searchVesselGroupList(String name, Integer isActive, Integer currentPage, Integer rowsPerPage, String sortBy) {
        try {
            if (currentPage > 0) currentPage--;
            return ruleRepository.searchVesselGroupList(name, isActive, PageRequest.of(currentPage, rowsPerPage), sortBy);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
        }
        return null;
    }

    @Override
    public UpsertProcessResult saveVesselGroup(VesselGroup vesselGroup) {
        UpsertProcessResult upsertProcessResult = new UpsertProcessResult(true);
        try {
            if (vesselGroup.getId() != null) {
                VesselGroup currentItem = this.ruleRepository.findVesselGroupById(vesselGroup.getId());
                if (currentItem == null) {
                    LOGGER.error("Couldn't find vessel group to update by id {}", vesselGroup.getId());
                    return new UpsertProcessResult(false, "vesselId not found: [" + vesselGroup.getId() + "]");
                }
                vesselGroup.setCreatedBy(currentItem.getCreatedBy());
                vesselGroup.setCreatedTime(currentItem.getCreatedTime());
            }
            if (this.ruleRepository.existsVesselGroup(vesselGroup.getName(), vesselGroup.getId() != null ? "UPDATE" : "INSERT", vesselGroup.getId())) {
                LOGGER.error("Duplicate row with name: {}", vesselGroup.getName());
                return new UpsertProcessResult(false, "Duplicate row with name: [" + vesselGroup.getName() + "]");
            }
            this.vesselGroupRepository.save(vesselGroup);
        } catch (Exception ex) {
            LOGGER.error(StringUtil.printException(ex));
            return new UpsertProcessResult(false, "Internal server error");
        }
        return upsertProcessResult;
    }

    @Override
    public VesselGroup saveAndReturnVesselGroup(VesselGroup vesselGroup){
        VesselGroup _vesselGroup=null;
        try {
            if (this.ruleRepository.existsVesselGroup(vesselGroup.getName(), null, null)) {
                LOGGER.error("Duplicate row with name: {}", vesselGroup.getName());
                return _vesselGroup;
            }
            _vesselGroup=this.vesselGroupRepository.save(vesselGroup);
        } catch (Exception ex) {
            LOGGER.error(StringUtil.printException(ex));
        }
        return _vesselGroup;
    }

    @Override
    public boolean removeVesselGroup(Long id) {
        try {
            if (id != null) {
                VesselGroup currentItem = this.ruleRepository.findVesselGroupById(id);
                if (currentItem == null) {
                    LOGGER.error("Couldn't find vessel group to delete by id {}", id);
                    return false;
                }
                this.ruleRepository.removeVesselGroup(currentItem.getId());
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
                VesselGroup currentItem = this.ruleRepository.findVesselGroupById(id);
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
        Page<VesselDTO> result = this.ruleRepository.searchVesselList(mmsi, vesselName, countryId, vesselTypeId, ip, phone,
                 PageRequest.of(currentPage, rowsPerPage));
        return result;
    }
    //--->

    @Override
    public List<Countries> getListCountry() {
        return (List<Countries>) countriesRepository.findAll();
    }

    @Override
    public MessageContent addVesselToGroup(AddVessellToGroup data){
        MessageContent messageContent =null;
        try {
            messageContent= this.ruleRepository.addVesselToGroup(data);
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
}
