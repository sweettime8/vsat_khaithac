package com.elcom.media.repository.monitor;

import com.elcom.vsat.model.MonHardwareRaw;

import com.sun.org.apache.xpath.internal.objects.XNull;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonHardwareRawRepository extends PagingAndSortingRepository<MonHardwareRaw, Long> {

}
