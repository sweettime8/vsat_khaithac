package com.elcom.vsat.repository.monitor;

import com.elcom.vsat.model.MonHardwareRaw;
import com.elcom.vsat.model.MonSoftwareRaw;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonSoftwareRawRepository extends PagingAndSortingRepository<MonSoftwareRaw, Long> {

}
