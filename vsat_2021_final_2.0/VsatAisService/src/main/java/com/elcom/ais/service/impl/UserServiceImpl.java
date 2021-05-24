package com.elcom.ais.service.impl;

import com.elcom.ais.model.User;
import com.elcom.ais.model.UserSpecs;
import com.elcom.ais.model.dto.UserPagingDTO;
import com.elcom.ais.repository.UserCustomizeRepository;
import com.elcom.ais.repository.UserRepository;
import com.elcom.ais.service.UserService;
import io.netty.util.internal.StringUtil;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCustomizeRepository userCustomizeRepository;

    public long countAll() {
        return userRepository.count();
    }

    @Override
    public UserPagingDTO findAll(String keyword, Integer status, Integer currentPage,
            Integer rowsPerPage, String sort) {
        UserPagingDTO result = new UserPagingDTO();
        try {
            if (currentPage > 0) {
                currentPage--;
            }
            if (StringUtil.isNullOrEmpty(sort)) {
                sort = "createdAt";
            }
            Pageable paging = PageRequest.of(currentPage, rowsPerPage, Sort.by(sort).descending());
            Page<User> pagedResult = null;
            //LOGGER.info("keyword: " + keyword + ", status: " + status);
            if (StringUtil.isNullOrEmpty(keyword) && status == null) {
                pagedResult = userRepository.findAll(paging);
            } else if (StringUtil.isNullOrEmpty(keyword) && status != null) {
                pagedResult = userRepository.findByStatus(status, paging);
            } else if (!StringUtil.isNullOrEmpty(keyword) && status == null) {
                pagedResult = userRepository.findAll(Specification.where(UserSpecs.searchByKeyword(keyword)), paging);
            } else {
                pagedResult = userRepository.findAll(Specification.where(UserSpecs.searchByStatusAndKeyword(status, keyword)), paging);
            }
            if (pagedResult != null && pagedResult.hasContent()) {
                result.setDataRows(pagedResult.getContent());
                result.setTotalRows(pagedResult.getTotalElements());
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        return result;
    }

    @Override
    public User findByUuid(String uuid) {
        return userCustomizeRepository.findByUuid(uuid);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void remove(User user) {
        userRepository.delete(user);
    }

    @Override
    public boolean update(User user) {
        return userCustomizeRepository.updateUser(user);
    }

    @Override
    public User findByEmail(String email) {
        try {
            return userCustomizeRepository.findByEmail(email);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public User findByMobile(String mobile) {
        return userCustomizeRepository.findByMobile(mobile);
    }

    @Override
    public User findByUserName(String userName) {
        return userCustomizeRepository.findByUserName(userName);
    }

    @Override
    public User findByEmailOrMobile(String userInfo) {
        try {
            return userCustomizeRepository.findByEmailOrMobile(userInfo);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public List<User> findByUuidIn(List<String> uuidList) {
        return userRepository.findByUuidIn(uuidList);
    }

    @Override
    public boolean changeStatus(User user) {
        return userRepository.changeStatus(user.getStatus(), user.getUuid()) > 0;
    }

    @Override
    public List<User> findByStatus(Integer status) {
        return userRepository.findByStatus(status);
    }

    @Override
    public boolean changePassword(User user) {
        return userRepository.changePassword(user.getPassword(), user.getUuid()) > 0;
    }

}
