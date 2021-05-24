package com.elcom.ais.repository.monitor;

import com.elcom.ais.model.media.MonHardwareRaw;

import com.sun.org.apache.xpath.internal.objects.XNull;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonHardwareRawRepository extends PagingAndSortingRepository<MonHardwareRaw, Long> {

}
