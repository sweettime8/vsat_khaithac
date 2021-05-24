package com.elcom.map.repository.vessel;

import com.elcom.map.model.vessel.VesselGroup;
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
