package com.elcom.id.repository;

import com.elcom.id.model.User;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Admin
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String>, JpaSpecificationExecutor<User>  {

    List<User> findByUuidIn(List<String> uuidList);

    Page<User> findByStatus(Integer status, Pageable pageable);
    
    List<User> findByStatus(Integer status);
    
    @Override
    Page<User> findAll(Specification<User> spec, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.password = :password, u.lastUpdate = now() WHERE u.uuid = :uuid ")
    int changePassword(@Param("password") String password, @Param("uuid") String uuid);
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.status = :status, u.lastUpdate = now() WHERE u.uuid = :uuid ")
    int changeStatus(@Param("status") Integer status, @Param("uuid") String uuid);
    
}
