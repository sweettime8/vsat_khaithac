/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.rbac.repository;

import com.elcom.rbac.model.RolePagePermission;
import com.elcom.rbac.model.RoleUser;
import com.elcom.rbac.model.dto.RoleUserDTO;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public class RoleUserCustomRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleUserCustomRepository.class);

    private SessionFactory sessionFactory;

    @Autowired
    public RoleUserCustomRepository(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }

        this.sessionFactory = factory.unwrap(SessionFactory.class);
    }
    
    public List<RoleUserDTO> findAllRoleUser() {
        Session session = openSession();
        try {
            String sql = "SELECT id as id, uuid_user as uuidUser, role_code as roleCode "
                    + " FROM vsat02.role_user ";
            return session.createNativeQuery(sql)
                    .addScalar("id", LongType.INSTANCE)
                    .addScalar("uuidUser", StringType.INSTANCE)
                    .addScalar("roleCode", StringType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(RoleUserDTO.class))
                    .getResultList();
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return null;
    }    

    private Session openSession() {
        Session session = this.sessionFactory.openSession();
        return session;
    }

    private void closeSession(Session session) {
        if (session.isOpen()) {
            session.disconnect();
            session.close();
        }
    }
}
