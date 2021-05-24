package com.elcom.rule.service;

import com.elcom.rule.model.dto.UpsertProcessResult;
import com.elcom.rule.model.dto.VesselDTO;
import com.elcom.rule.model.dto.request.AddVessellToGroup;
import com.elcom.gateway.message.MessageContent;
import com.elcom.rule.model.dto.DetailRuleEventDTO;
import com.elcom.rule.model.dto.RuleEventNotifiDTO;
import com.elcom.rule.model.vessel.Countries;
import com.elcom.rule.model.vessel.VesselGroup;
import com.elcom.rule.model.vessel.VesselTypes;
import java.util.List;
import org.springframework.data.domain.Page;

public interface RuleService {
    
    //<!-- Rule
    Page<RuleEventNotifiDTO> findRuleEvents(String startTime, String endTime, Long ruleId, Long groupObjectId, String objId, String objName, Integer currentPage, Integer rowsPerPage);
    
    Long countRuleEventByObjId(String startTime, String endTime, String objId);
    
    boolean updateRuleEventReadStatus(String ruleNotifyUuid);
    
    DetailRuleEventDTO findDetailRuleEvent(String ruleNotifyUuid, String objId);
    //--->
    
    
    
    
    //<!-- Vessel group
    Page<VesselGroup> searchVesselGroupList(String name, Integer isActive, Integer currentPage, Integer rowsPerPage, String sortBy);
    UpsertProcessResult saveVesselGroup(VesselGroup vesselGroup);
    VesselGroup saveAndReturnVesselGroup(VesselGroup vesselGroup);
    VesselGroup findDetailVesselGroup(Long id);
    boolean removeVesselGroup(Long id);
    //-->
    
    //<!-- Vessel
    Page<VesselDTO> searchVesselList(String mmsi, String vesselName, Integer countryId, Integer vesselTypeId, String ip, String phone
                                    , Integer currentPage, Integer rowsPerPage);
    //-->

    MessageContent addVesselToGroup(AddVessellToGroup data);


    List<Countries> getListCountry();
    
    List<VesselTypes> getListVesselType();
}
