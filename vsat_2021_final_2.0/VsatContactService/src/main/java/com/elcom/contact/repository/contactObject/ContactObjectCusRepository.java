package com.elcom.contact.repository.contactObject;

import com.elcom.contact.model.contactObject.ObjectUnInfo;
import com.elcom.contact.model.contactObject.ObjectUnInfoDto;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContactObjectCusRepository extends PagingAndSortingRepository<ObjectUnInfo, UUID> {

}
