package com.elcom.map.repository.monitor;

import com.elcom.map.model.media.MonHardwareRaw;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonHardwareRawRepository extends PagingAndSortingRepository<MonHardwareRaw, Long> {

}
