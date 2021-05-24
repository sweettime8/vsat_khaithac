package com.elcom.contact.service;

import com.elcom.contact.model.contact.ContactIP;
import com.elcom.contact.model.contactLand.HeadQuarterDto;
import com.elcom.contact.model.dto.request.contacLand.AddContactLandIpRequest;
import com.elcom.contact.model.dto.request.contacLand.AddHeadquaterRequest;
import com.elcom.contact.model.dto.request.contacLand.ContactLandPhoneRequest;
import com.elcom.contact.model.dto.request.contacLand.DelContactLandIpRequest;
import com.elcom.contact.model.dto.request.contacLand.GetDetailHeadquatersRequest;
import com.elcom.contact.model.dto.request.contact.SearchContactRequest;
import com.elcom.gateway.message.MessageContent;
import org.springframework.data.domain.Page;

public interface ContactLandService {

    Page<HeadQuarterDto> search(SearchContactRequest data);

    Object getDetailHeadQuarter(GetDetailHeadquatersRequest data);

    ContactIP checkIpHeadQuarter(AddContactLandIpRequest data);

    Object addIpHeadQuarter(AddContactLandIpRequest data);

    Object delIpHeadQuarter(DelContactLandIpRequest data);

    Object addPhoneHeadQuarter(ContactLandPhoneRequest data);

    Object delPhoneHeadQuarter(ContactLandPhoneRequest data);

    Object addHeadquater(AddHeadquaterRequest data);

    boolean updateHeadquater(AddHeadquaterRequest data);

    void deleteHeadquaterById(long id);
}
