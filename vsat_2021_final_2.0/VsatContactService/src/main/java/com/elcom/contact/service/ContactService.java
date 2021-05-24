package com.elcom.contact.service;

import com.elcom.contact.model.contact.AisInfo;
import com.elcom.contact.model.contact.ContactIP;
import com.elcom.contact.model.contact.MmsiImage;
import com.elcom.contact.model.contact.VesselAltInfo;
import com.elcom.contact.model.dto.request.contact.AddContactIpRequest;
import com.elcom.contact.model.dto.request.contact.AddVesselRequest;
import com.elcom.contact.model.dto.request.contact.DelContactIpRequest;
import com.elcom.contact.model.dto.request.contact.GetDetailVesselRequest;
import com.elcom.contact.model.dto.request.contact.SearchContactRequest;
import com.elcom.gateway.message.MessageContent;

public interface ContactService {

    MessageContent search(SearchContactRequest data);

    MessageContent getDetailVessel(GetDetailVesselRequest data);   

    ContactIP checkIpVessel(AddContactIpRequest data);

    Object addIpVessel(AddContactIpRequest data);

    Object delIpVessel(DelContactIpRequest data);

    boolean save(AisInfo ais);

    boolean save(VesselAltInfo vesselAltInfo);

    boolean save(MmsiImage mmsiImage);

    AisInfo finAisInfo(long mmsi);

    boolean updateAisInfo(AddVesselRequest data);

    boolean updateVesselAltInfo(AddVesselRequest data);
    
    VesselAltInfo findVesselInfo(long mmsi);
    
    void deleteVesselById(long mmsi);
}
