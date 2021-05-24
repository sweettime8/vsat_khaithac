package com.elcom.rule.repository.monitor;

import com.elcom.rule.model.media.MonHardwareRaw;
import com.elcom.rule.model.media.MonSoftwareRaw;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonSoftwareRawRepository extends PagingAndSortingRepository<MonSoftwareRaw, Long> {

}
