package com.elcom.media.service.impl;

import com.elcom.media.auth.CustomUserDetails;
import com.elcom.media.model.User;
import com.elcom.media.repository.UserCustomizeRepository;
import com.elcom.media.service.AuthService;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author anhdv
 */
@Service
public class AuthServiceImpl implements UserDetailsService, AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);
    
    @Autowired
    private UserCustomizeRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) {
        // Kiểm tra xem user có tồn tại trong database không?
        User user = userRepository.findByUserName(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with userInfo : " + username);
        } else {
            LOGGER.info("Find user with " + username + " ==> uuid: " + user.getUuid());
        }
        return new CustomUserDetails(user);
    }

    // JWTAuthenticationFilter sẽ sử dụng hàm này
    @Transactional
    @Override
    public UserDetails loadUserByUuid(String uuid) {
        User user = userRepository.findByUuid(uuid);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with uuid : " + uuid);
        }
        return new CustomUserDetails(user);
    }
}
