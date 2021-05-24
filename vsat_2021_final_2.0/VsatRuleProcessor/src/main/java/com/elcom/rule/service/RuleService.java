package com.elcom.rule.service;

import com.elcom.rule.model.dto.UpsertProcessResult;
import com.elcom.rule.model.dto.VesselDTO;
import com.elcom.rule.model.dto.request.AddVessellToGroup;
import com.elcom.gateway.message.MessageContent;
import com.elcom.rule.model.dto.AreaWithObjectId;
import com.elcom.rule.model.dto.RuleNotifyInsertDTO;
import com.elcom.rule.model.vessel.Countries;
import com.elcom.rule.model.vessel.VesselGroup;
import com.elcom.rule.model.vessel.VesselTypes;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;

public interface RuleService {
    
    /** ruleActionId = 1: luật vào vùng,    ruleActionId = 2: luật ra vùng
     * @param ruleActionId
     * @return List */
    List<AreaWithObjectId> findListRuleArea(Integer ruleActionId);
    
    String insertRuleNotify(RuleNotifyInsertDTO ruleNotify);
    
    Set<Integer> findAreaIdsByObjectId(String startTime, String endTime, String objId);
    
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
