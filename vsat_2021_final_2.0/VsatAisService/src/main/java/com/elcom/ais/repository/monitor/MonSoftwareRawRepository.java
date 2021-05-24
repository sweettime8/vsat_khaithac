package com.elcom.ais.repository.monitor;

import com.elcom.ais.model.media.MonHardwareRaw;
import com.elcom.ais.model.media.MonSoftwareRaw;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonSoftwareRawRepository extends PagingAndSortingRepository<MonSoftwareRaw, Long> {

}
