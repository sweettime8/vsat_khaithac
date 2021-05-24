package com.elcom.rule.repository.source;

import com.elcom.rule.model.Source;
import com.elcom.rule.repository.BaseRepository;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

@Repository
public class SourceRepository extends BaseRepository {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SourceRepository.class);
    
    @Autowired
    public SourceRepository(@Qualifier("vsatEntityManagerFactory") EntityManagerFactory factory) {
        super(factory, null);
        //ex ket noi ch query
//        try (
//            Connection connection = this.clDatasource.getConnection();
//            ResultSet rs = connection.createStatement().executeQuery("SELECT *\n" +
//                    "FROM VSAT_AIS");){
//            while (rs.next()) {
//                String lastName = rs.getString("MMSI");
//                System.out.println(lastName + "\n");
//            }
//        }
//        catch(Exception ex){
//
//        }

    }

    public List<Source> getAll(){
        Session session = openSession();
        List<Source> lstSource=null;
        try {
            Query query = session.createNativeQuery("select * from vsat02.source;",Source.class);
            lstSource = (List<Source>)query.getResultList();
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return lstSource != null ? lstSource : null;
    }
}
