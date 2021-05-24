package com.elcom.id.repository;

import com.elcom.id.constant.Constant;
import com.elcom.id.model.User;
import com.elcom.id.utils.StringUtil;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author anhdv
 */
@Repository
public class UserCustomizeRepository extends BaseRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCustomizeRepository.class);

    @Autowired
    public UserCustomizeRepository(EntityManagerFactory factory) {
        super(factory);
    }

    public User findByUuid(String uuid) {
        Session session = openSession();
        try {
            User user = session.load(User.class, uuid);
            return user;
        } catch (EntityNotFoundException ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return null;
    }

    public User findByUsername(String userName) {
        Session session = openSession();
        Object result = null;
        try {
            Query query = session.createNativeQuery("SELECT * FROM user WHERE email = ?", User.class);
            query.setParameter(1, userName);
            result = query.getSingleResult();
        } catch (NoResultException ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return result != null ? (User) result : null;
    }

    public boolean updateLastLogin(String uuid, String loginIp) {
        Session session = openSession();
        try {
            Transaction tx = session.beginTransaction();
            String update = " ";
            if (!StringUtil.isNullOrEmpty(loginIp)) {
                update += ", login_ip = :loginIp ";
            }
            String sql = "Update user SET last_login = now() " + update + " WHERE uuid = :uuid ";
            Query query = session.createNativeQuery(sql);
            if (!StringUtil.isNullOrEmpty(loginIp)) {
                query.setParameter("loginIp", loginIp);
            }
            query.setParameter("uuid", uuid);
            int result = query.executeUpdate();
            tx.commit();
            return result > 0;
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
            ex.printStackTrace();
        } finally {
            closeSession(session);
        }
        return false;
    }

    public boolean updateUser(User user) {
        Session session = openSession();
        try {
            Transaction tx = session.beginTransaction();
            String update = " ";
            if (!StringUtil.isNullOrEmpty(user.getMobile())) {
                update += ", mobile = :mobile ";
            }
            if (!StringUtil.isNullOrEmpty(user.getFullName())) {
                update += ", fullname = :fullName ";
            }
            if (!StringUtil.isNullOrEmpty(user.getAvatar())) {
                update += ", avatar = :avatar ";
            }
            if (!StringUtil.isNullOrEmpty(user.getAddress())) {
                update += ", address = :address ";
            }
            if (!StringUtil.isNullOrEmpty(user.getBirthDay())) {
                update += ", birthday = :birthDay ";
            }
            if (user.getGender() != null) {
                update += ", gender = :gender ";
            }
            if (user.getIsDelete() != null) {
                update += ", is_delete = :isDelete ";
            }
            String sql = "Update vsat02.vsat_user SET last_update = now() " + update + " WHERE uuid = :uuid ";
            Query query = session.createNativeQuery(sql);
            if (!StringUtil.isNullOrEmpty(user.getMobile())) {
                query.setParameter("mobile", user.getMobile());
            }
            if (!StringUtil.isNullOrEmpty(user.getFullName())) {
                query.setParameter("fullName", user.getFullName());
            }
            if (!StringUtil.isNullOrEmpty(user.getAvatar())) {
                query.setParameter("avatar", user.getAvatar());
            }
            if (!StringUtil.isNullOrEmpty(user.getAddress())) {
                query.setParameter("address", user.getAddress());
            }
            if (!StringUtil.isNullOrEmpty(user.getBirthDay())) {
                query.setParameter("birthDay", user.getBirthDay());
            }
            if (user.getGender() != null) {
                query.setParameter("gender", user.getGender());
            }
            if (user.getIsDelete() != null) {
                query.setParameter("isDelete", user.getIsDelete());
            }
            query.setParameter("uuid", user.getUuid());
            int result = query.executeUpdate();
            tx.commit();
            return result > 0;
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
            ex.printStackTrace();
        } finally {
            closeSession(session);
        }
        return false;
    }

    public boolean insertTest() {
        Session session = openSession();
        try {
            for (int i = 1; i <= 4; i++) {
                Query query = session.createNativeQuery(" insert into elcom_user(username, password, full_name) "
                        + " values(:userName, :password, :fullName ) ");
                query.setParameter("userName", "anhdv_" + i);
                query.setParameter("password", "anhdv_pw_" + i);
                query.setParameter("fullName", "do viet anh_" + i);
                query.executeUpdate();
            }
        } catch (NoResultException ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return true;
    }

    public Boolean countUser(String username, String password) {

        Session session = openSession();
        try {
            StoredProcedureQuery query = session.createStoredProcedureQuery("countUser");

            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN); //userName
            query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN); //fullName
            query.registerStoredProcedureParameter(3, Integer.class, ParameterMode.OUT); //total count

            query.setParameter(1, username);
            query.setParameter(2, password);

            query.execute();

            Integer total = (Integer) query.getOutputParameterValue(3);
            LOGGER.info("total user: " + total);

        } catch (NoResultException ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return true; //enter your condition
    }

    public User findByEmail(String email) {
        Session session = openSession();
        Object result = null;
        try {
            Query query = session.createNativeQuery(" SELECT * FROM vsat02.vsat_user WHERE email = ? ", User.class);
            query.setParameter(1, email.trim());
            result = query.getSingleResult();
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return result != null ? (User) result : null;
    }

    public User findByMobile(String mobile) {
        Session session = openSession();
        Object result = null;
        try {
            Query query = session.createNativeQuery("SELECT * FROM vsat02.vsat_user WHERE mobile = ?", User.class);
            query.setParameter(1, mobile);
            result = query.getSingleResult();
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return result != null ? (User) result : null;
    }

    public User findByUserName(String userName) {
        Session session = openSession();
        Object result = null;
        try {
            Query query = session.createNativeQuery("SELECT * FROM vsat02.vsat_user WHERE username = ?", User.class);
            query.setParameter(1, userName);
            result = query.getSingleResult();
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return result != null ? (User) result : null;
    }

    public User findByEmailOrMobile(String userInfo) {
        Session session = openSession();
        Object result = null;
        try {
            Query query = session.createNativeQuery("SELECT * FROM user WHERE "
                    + " (email = ? AND is_delete = 0) OR (mobile = ? AND is_delete = 0) ", User.class);
            query.setParameter(1, userInfo);
            query.setParameter(2, userInfo);
            result = query.getSingleResult();
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return result != null ? (User) result : null;
    }

}
