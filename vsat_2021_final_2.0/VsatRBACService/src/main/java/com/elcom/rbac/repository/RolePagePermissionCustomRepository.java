/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.rbac.repository;

import com.elcom.rbac.model.RolePagePermission;
import com.elcom.rbac.model.dto.RolePathPermissionDTO;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
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
public class RolePagePermissionCustomRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(RolePagePermissionCustomRepository.class);

    private SessionFactory sessionFactory;

    @Autowired
    public RolePagePermissionCustomRepository(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }

        this.sessionFactory = factory.unwrap(SessionFactory.class);
    }

    public void removeRolePagePermissionByRoleCode(String roleCode) {
        Session session = openSession();
        try {
            session.getTransaction().begin();
            String sql = " DELETE FROM vsat02.role_page_permission "
                       + " WHERE role_code = :roleCode ";
            NativeQuery query = session.createNativeQuery(sql);
            query.setParameter("roleCode", roleCode);
            int result = query.executeUpdate();            
            session.getTransaction().commit();
            
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
    }

    public List<RolePagePermission> findByRoleCodeAndIsDeleteNotOrderByRoleCodeAsc(String roleCode, Integer isDelete) {
        Session session = openSession();
        try {
            String sql = "SELECT id as id, role_code as roleCode, page_url as pageUrl, "
                    + " page_name as pageName, can_create as canCreate, can_read as canRead, can_update as canUpdate, "
                    + " can_delete as canDelete, created_at as createdAt,"
                    + " is_delete as isDelete "
                    + " FROM vsat02.role_page_permission "
                    + " WHERE role_code = :roleCode ";
            return session.createNativeQuery(sql)
                    .setParameter("roleCode", roleCode)
                    .addScalar("id", LongType.INSTANCE)
                    .addScalar("roleCode", StringType.INSTANCE)
                    .addScalar("pageUrl", StringType.INSTANCE)
                    .addScalar("pageName", StringType.INSTANCE)
                    .addScalar("canCreate", IntegerType.INSTANCE)
                    .addScalar("canRead", IntegerType.INSTANCE)
                    .addScalar("canUpdate", IntegerType.INSTANCE)
                    .addScalar("canDelete", IntegerType.INSTANCE)
                    .addScalar("createdAt", TimestampType.INSTANCE)
                    .addScalar("isDelete", IntegerType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(RolePagePermission.class))
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
