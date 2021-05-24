package com.elcom.ais.service;

import com.elcom.ais.model.dto.UpsertProcessResult;
import com.elcom.ais.model.dto.VesselDTO;
import com.elcom.ais.model.dto.request.AddObjectToGroup;
import com.elcom.ais.model.dto.request.AdvanceSearch;
import com.elcom.ais.model.vessel.ObjectGroup;
import com.elcom.gateway.message.MessageContent;
import com.elcom.ais.model.dto.request.media.SearchListAisRequest;
import com.elcom.ais.model.vessel.Countries;
import com.elcom.ais.model.vessel.VesselGroup;
import com.elcom.ais.model.vessel.VesselTypes;
import java.util.List;
import org.springframework.data.domain.Page;

public interface AisService {

    //<!-- AIS messagesmmsiGroups
    MessageContent searchAisList(SearchListAisRequest data);
    MessageContent searchAisListGeneral(SearchListAisRequest data);
    //-->

    //<!-- Vessel group
    Page<ObjectGroup> searchObjectGroupList(String name, Integer isActive, Integer currentPage, Integer rowsPerPage, String sortBy);
    UpsertProcessResult saveVesselGroup(ObjectGroup objectGroup);
    ObjectGroup saveAndReturnObjectGroup(ObjectGroup objectGroup);
    VesselGroup findDetailVesselGroup(Long id);
    boolean removeObjectGroup(Long id);
    //-->

    //<!-- Vessel
    Page<VesselDTO> searchVesselList(String mmsi, String vesselName, Integer countryId, Integer vesselTypeId, String ip, String phone,
             Integer currentPage, Integer rowsPerPage);

    Page<VesselDTO> searchVesselListAdvanced(String mmsi, String vesselName, Integer countryId, Integer vesselTypeId, String ip, String phone,
             Integer currentPage, Integer rowsPerPage, AdvanceSearch advanceSearch);

    MessageContent findDetailVessel(Long mmsi);
    //-->

    MessageContent addVesselToGroup(AddObjectToGroup data);

    List<Countries> getListCountry();

    List<VesselTypes> getListVesselType();
}
