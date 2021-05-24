/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.rbac.repository;

import com.elcom.rbac.model.RolePathPermission;
import com.elcom.rbac.model.dto.RolePathPermissionDTO;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import static org.apache.tomcat.jni.User.username;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
public class RolePathPermissionCustomRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(RolePathPermissionCustomRepository.class);

    private SessionFactory sessionFactory;

    @Autowired
    public RolePathPermissionCustomRepository(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }

        this.sessionFactory = factory.unwrap(SessionFactory.class);
    }
    
    public void saveRolePathPermission(RolePathPermissionDTO rolePathPermissionDTO){
        Session session = openSession();
        Transaction tx = session.beginTransaction();
        try {
            String sql = "INSERT INTO vsat02.role_path_permission(role_code, service_code, api_path, can_create, can_read, "
                       + " can_update, can_delete, created_at , is_delete) "
                       + " VALUES  (?, ? , ?, ?, ?, ?, ?, ?, ?)";

          int result =  session.createNativeQuery(sql)
                .setParameter(1, rolePathPermissionDTO.getRoleCode())
                .setParameter(2, rolePathPermissionDTO.getServiceCode())
                .setParameter(3, rolePathPermissionDTO.getApiPath())
                .setParameter(4, rolePathPermissionDTO.getCanCreate())
                .setParameter(5, rolePathPermissionDTO.getCanRead())
                .setParameter(6, rolePathPermissionDTO.getCanUpdate())
                .setParameter(7, rolePathPermissionDTO.getCanDelete())
                .setParameter(9, rolePathPermissionDTO.getCreatedAt())
                .setParameter(10, rolePathPermissionDTO.getIsDelete())
                .executeUpdate();
          
          tx.commit();

        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
    }
    
        public void removeRolePathPermission(Long id){
        Session session = openSession();
        Transaction tx = session.beginTransaction();
        try {
            String sql = "DELETE FROM vsat02.role_path_permission WHERE id = :id";

          int result =  session.createNativeQuery(sql)
                .setParameter("id", id)
                .executeUpdate();
          
          tx.commit();

        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
    }
    
    

    public List<RolePathPermissionDTO> findByRoleCodeAndIsDeleteNotOrderByRoleCodeAsc(String roleCode, Integer isDelete) {
        Session session = openSession();
        try {
            String sql = "SELECT id as id, role_code as roleCode, service_code as serviceCode, "
                    + " api_path as apiPath, can_create as canCreate, can_read as canRead, can_update as canUpdate, "
                    + " can_delete as canDelete, created_at as createdAt,"
                    + " is_delete as isDelete "
                    + " FROM vsat02.role_path_permission "
                    + " WHERE role_code = :roleCode ";
            return session.createNativeQuery(sql)
                    .setParameter("roleCode", roleCode)
                    .addScalar("id", LongType.INSTANCE)
                    .addScalar("roleCode", StringType.INSTANCE)
                    .addScalar("serviceCode", StringType.INSTANCE)
                    .addScalar("apiPath", StringType.INSTANCE)
                    .addScalar("canCreate", IntegerType.INSTANCE)
                    .addScalar("canRead", IntegerType.INSTANCE)
                    .addScalar("canUpdate", IntegerType.INSTANCE)
                    .addScalar("canDelete", IntegerType.INSTANCE)
                    .addScalar("createdAt", TimestampType.INSTANCE)
                    .addScalar("isDelete", IntegerType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(RolePathPermissionDTO.class))
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
