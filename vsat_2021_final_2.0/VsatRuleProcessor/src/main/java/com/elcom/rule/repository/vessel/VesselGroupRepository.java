package com.elcom.rule.repository.vessel;

import com.elcom.rule.model.vessel.VesselGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author anhdv
 */
@Repository
public interface VesselGroupRepository extends CrudRepository<VesselGroup, Long> {
    
//    List<PointType> findAll(Sort sort);
//    List<PointType> findByIsActive(Sort sort, Integer isActive);
}
