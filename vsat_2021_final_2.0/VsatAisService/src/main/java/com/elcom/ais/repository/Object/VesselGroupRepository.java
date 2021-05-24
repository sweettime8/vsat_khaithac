package com.elcom.ais.repository.Object;

import com.elcom.ais.model.vessel.ObjectGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author anhdv
 */
@Repository
public interface VesselGroupRepository extends CrudRepository<ObjectGroup, Long> {
    
//    List<PointType> findAll(Sort sort);
//    List<PointType> findByIsActive(Sort sort, Integer isActive);
}
