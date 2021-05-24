package com.elcom.contact.repository.monitor;

import com.elcom.vsat.model.MonHardwareRaw;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonHardwareRawRepository extends PagingAndSortingRepository<MonHardwareRaw, Long> {

}
