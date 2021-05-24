package com.elcom.map.repository.monitor;

import com.elcom.map.model.media.MonSoftwareRaw;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonSoftwareRawRepository extends PagingAndSortingRepository<MonSoftwareRaw, Long> {

}
