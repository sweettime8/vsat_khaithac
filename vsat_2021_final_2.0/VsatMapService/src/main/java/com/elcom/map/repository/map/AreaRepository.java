package com.elcom.map.repository.map;

import com.elcom.map.model.area.Area;
import com.elcom.map.repository.BaseRepository;
import com.elcom.map.utils.StringUtil;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.lang.annotation.Native;

@Repository
public class AreaRepository extends BaseRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(AreaRepository.class);
    @Autowired
    public AreaRepository(@Qualifier("vsatEntityManagerFactory") EntityManagerFactory factory, @Qualifier("vsatChDataSource") DataSource dataSource) {
        super(factory, dataSource);
    }

    public Boolean checkExitsAeraByName(String areaName){
        Session session =openSession();
        try{
            String sql = " select count(*) from areas a where a.name =:areaName ";
            NativeQuery nQuery = session.createNativeQuery(sql);
            nQuery.setParameter("areaName",areaName);
            int count = ((Number) nQuery.getSingleResult()).intValue();
            if(count>0){
                return true;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            if (session != null && session.isOpen()) {
                session.disconnect();
                session.close();
            }
        }
        return false;
    }

    public Area getAeraById(int id){
        Session session =openSession();
        Area area=null;
        try{
            String sql = " select * from areas a where a.id =:id ";
            NativeQuery nQuery = session.createNativeQuery(sql,Area.class);
            nQuery.setParameter("id",id);
            area = (Area) nQuery.getSingleResult();
        }
        catch (Exception ex){
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            if (session != null && session.isOpen()) {
                session.disconnect();
                session.close();
            }
        }
        return area;
    }
}
