package com.elcom.contact.repository.contactLand;

import com.elcom.contact.model.contactLand.HeadQuarter;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactLandCusRepository extends PagingAndSortingRepository<HeadQuarter, Long> {

}
