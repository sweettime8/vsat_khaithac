/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.rbac.repository;

import com.elcom.rbac.model.Role;
import com.elcom.rbac.model.dto.RoleDTO;
import com.elcom.rbac.utils.StringUtil;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
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
public class RoleCustomizeRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleCustomizeRepository.class);

    private SessionFactory sessionFactory;

    @Autowired
    public RoleCustomizeRepository(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }

        this.sessionFactory = factory.unwrap(SessionFactory.class);
    }

    public boolean updateRoleCode(String newRoleCode, String newRoleDetail, String roleCodeOld) {
        Session session = openSession();
        try {
            session.getTransaction().begin();
            String sql = "UPDATE vsat02.role SET ";
            String condition = "";

            if (!StringUtil.isNullOrEmpty(newRoleCode)) {
                condition += " role_code = :newRoleCode ";
            }
            if (!StringUtil.isNullOrEmpty(newRoleDetail)) {
                condition += ", role_detail = :newRoleDetail ";
            }
            if (!StringUtil.isNullOrEmpty(newRoleCode)) {
                condition += ", role_name = :newRoleCode ";
            }

            condition += " WHERE role_code = :roleCodeOld";
            String sqlRun = sql + condition;

            NativeQuery query = session.createNativeQuery(sqlRun);
            if (!StringUtil.isNullOrEmpty(newRoleCode)) {
                query.setParameter("newRoleCode", newRoleCode);
            }
            if (!StringUtil.isNullOrEmpty(newRoleDetail)) {
                query.setParameter("newRoleDetail", newRoleDetail);
            }
            if (!StringUtil.isNullOrEmpty(roleCodeOld)) {
                query.setParameter("roleCodeOld", roleCodeOld);
            }
            int i = query.executeUpdate();
            session.getTransaction().commit();
            if (i > 0) {
                return true;
            }
            
        } catch (EntityNotFoundException ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }

        return false;

    }

    public List<RoleDTO> findByCreatedByAndIsDelete(String username, Integer isDelete) {
        Session session = openSession();
        try {
            String sql = "SELECT id as id, role_name as roleName, role_code as roleCode, role_detail as roleDetail, "
                    + " is_admin as isAdmin, created_by AS createdBy , created_at as createdAt,"
                    + " modify_date as modifyDate, is_delete as isDelete, modify_by as modifyBy "
                    + " FROM vsat02.role "
                    + " WHERE created_by = :createBy ORDER BY id";
            return session.createNativeQuery(sql)
                    .setParameter("createBy", username)
                    .addScalar("id", IntegerType.INSTANCE)
                    .addScalar("roleName", StringType.INSTANCE)
                    .addScalar("roleCode", StringType.INSTANCE)
                    .addScalar("roleDetail", StringType.INSTANCE)
                    .addScalar("isAdmin", IntegerType.INSTANCE)
                    .addScalar("createdBy", StringType.INSTANCE)
                    .addScalar("createdAt", TimestampType.INSTANCE)
                    .addScalar("modifyDate", TimestampType.INSTANCE)
                    .addScalar("isDelete", IntegerType.INSTANCE)
                    .addScalar("modifyBy", StringType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(RoleDTO.class))
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
