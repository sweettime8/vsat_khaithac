package com.elcom.map.service;

import com.elcom.map.model.User;
import com.elcom.map.model.dto.UserPagingDTO;
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
    
    boolean changePassword(User user);
    
    User findByEmailOrMobile(String userInfo);   
    
    List<User> findByUuidIn(List<String> uuidList);
    
    List<User> findByStatus(Integer status);
    
    void save(User user);
    
    boolean update(User user);
       
    boolean changeStatus(User user);   

    void remove(User user);   
    
}