package com.elcom.id.service.impl;

import com.elcom.id.auth.CustomUserDetails;
import com.elcom.id.constant.Constant;
import com.elcom.id.model.User;
import com.elcom.id.repository.UserCustomizeRepository;
import com.elcom.id.service.AuthService;
import java.util.Set;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    
    @Autowired
    private RedisTemplate redisTemplate;
    
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

    @Override
    public void progressBlackList() {
        LOGGER.info("progressBlackList start ==============>>>>>>>>>");
        Set<String> keys = redisTemplate.keys(Constant.REDIS_TIME_TOKEN_LOGIN_CREATE);
    }
}
