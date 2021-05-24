package com.elcom.contact.repository.directIp;

import com.elcom.contact.model.contactObject.ContactObjectIP;
import com.elcom.contact.repository.BaseRepository;
import com.elcom.contact.repository.contact.ContactRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TemporalType;
import javax.sql.DataSource;
import java.util.Date;

@Repository
public class DirectionIPRepository extends BaseRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(DirectionIPRepository.class);
    @Autowired
    @Qualifier("clickHouseSession")
    SessionFactory sessionFactory;

    @Autowired
    public DirectionIPRepository(@Qualifier("vsatEntityManagerFactory") EntityManagerFactory factory, @Qualifier("vsatChDataSource") DataSource dataSource) {
        super(factory,dataSource);
    }

    public Boolean checkIsExitsIpDirection(String sourceId, int direction, int configType){
        Session session = openSession();
        try {
            String sql="SELECT COUNT(*) FROM vsat02.direct_ip_config where source_ip=:sourceIp AND direction = :direction AND config_type = :configType";
            NativeQuery query = session.createNativeQuery(sql);
            query.setParameter("sourceIp",sourceId);
            query.setParameter("direction",direction);
            query.setParameter("configType",configType);

            int count = ((Number) query.getSingleResult()).intValue();
            if(count==0){
                return false;
            }
            if(count>0){
                return true;
            }

        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return true;
    }

    public Boolean AddIpDirection(String sourceId, int direction, int configType){
        Session session = openSession();
        try {
            session.getTransaction().begin();
            Boolean isExitsIpDirection= checkIsExitsIpDirection(sourceId,direction,configType);
            if(isExitsIpDirection==false){
                String sql="INSERT INTO vsat02.direct_ip_config(\n" +
                        "\t source_ip, direction, created_by, created_time, config_type)\n" +
                        "\tVALUES (:sourceIp, :direction, :createdBy, :createdTime, :configType)";
                NativeQuery query = session.createNativeQuery(sql);
                query.setParameter("sourceIp",sourceId);
                query.setParameter("direction",direction);
                query.setParameter("configType",configType);
                query.setParameter("createdBy","");
                query.setParameter("createdTime",new Date(), TemporalType.TIMESTAMP);
                int i=query.executeUpdate();
                session.getTransaction().commit();
                if(i>0){
                    return true;
                }
            }
        } catch (EntityNotFoundException ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {

            closeSession(session);
        }
        return false;
    }



    public Boolean DelIpDirection(String sourceId, int direction, int configType){
        Session session = openSession();
        try {
            session.getTransaction().begin();
            Boolean isExitsIpDirection= checkIsExitsIpDirection(sourceId,direction,configType);
            if(isExitsIpDirection==true){
                String sql="DELETE FROM vsat02.direct_ip_config " +
                        "where source_ip=:sourceIp AND direction = :direction AND config_type = :configType";
                NativeQuery query = session.createNativeQuery(sql);
                query.setParameter("sourceIp",sourceId);
                query.setParameter("direction",direction);
                query.setParameter("configType",configType);
                int i=query.executeUpdate();
                session.getTransaction().commit();
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
}
