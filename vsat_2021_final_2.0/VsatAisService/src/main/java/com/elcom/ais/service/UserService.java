package com.elcom.ais.service;

import com.elcom.ais.model.User;
import com.elcom.ais.model.dto.UserPagingDTO;
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