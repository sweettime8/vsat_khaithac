package com.elcom.id.service;

import com.elcom.id.model.User;
import com.elcom.id.model.dto.UserPagingDTO;
import java.util.List;

/**
 *
 * @author anhdv
 */
public interface UserService {

    UserPagingDTO findAll(String keyword, Integer status, Integer currentPage, Integer rowsPerPage, String sort);
    
    User findByUuid(String uuid);
    
    User findByEmail(String email);
    
    User findByMobile(String mobile);
    
    User findByUserName(String userName);
      
    User findByEmailOrMobile(String userInfo);
    
    List<User> findByUuidIn(List<String> uuidList);
    
    List<User> findByStatus(Integer status);
    
    void save(User user);
    
    boolean update(User user);
    
    boolean changePassword(User user);

    void remove(User user);
    
    boolean insertTest();
    
    boolean updateLastLogin(String uuid, String loginIp);

}