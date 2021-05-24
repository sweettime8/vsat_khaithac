package com.elcom.contact.repository.contactLand;

import com.elcom.contact.model.contact.*;
import com.elcom.contact.model.contactLand.HeadQuarterDto;
import com.elcom.contact.model.dto.request.contacLand.AddContactLandIpRequest;
import com.elcom.contact.model.dto.request.contacLand.AddHeadquaterRequest;
import com.elcom.contact.model.dto.request.contacLand.ContactLandPhoneRequest;
import com.elcom.contact.model.dto.request.contacLand.DelContactLandIpRequest;
import com.elcom.contact.model.dto.request.contacLand.GetDetailHeadquatersRequest;
import com.elcom.contact.model.dto.request.contacLand.HeadQuarterDtoFull;
import com.elcom.contact.model.dto.request.contact.GetDetailVesselRequest;
import com.elcom.contact.model.dto.request.contact.SearchContactRequest;
import com.elcom.contact.repository.BaseRepository;
import com.elcom.contact.repository.directIp.DirectionIPRepository;
import com.elcom.contact.utils.StringUtil;
import com.elcom.gateway.message.MessageContent;
import java.math.BigInteger;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

@Repository
public class ContactLandRepository extends BaseRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactLandRepository.class);

    @Autowired
    @Qualifier("clickHouseSession")
    SessionFactory sessionFactory;
    @Autowired
    DirectionIPRepository directionIPRepository;

    @Autowired
    public ContactLandRepository(@Qualifier("vsatEntityManagerFactory") EntityManagerFactory factory, @Qualifier("vsatChDataSource") DataSource dataSource) {
        super(factory, dataSource);
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

    public boolean updateHeadquater(AddHeadquaterRequest data) {
        Session session = openSession();
        Object obj = null;
        try {
            session.getTransaction().begin();
            String sql = "UPDATE vsat02.headquarters SET updated_time = now(), ";
            String condition = "";

            condition += " country_id =:countryId, longitude = :longitude, latitude =:latitude, status = :status ";
            if (!StringUtil.isNullOrEmpty(data.getName())) {
                condition += ", name =:name ";
            }

            if (!StringUtil.isNullOrEmpty(data.getUsername())) {
                condition += ", user_updated =:userUpdated ";
            }
            if (!StringUtil.isNullOrEmpty(data.getVai_tro())) {
                condition += ", vai_tro =:vaitro ";
            }
            if (!StringUtil.isNullOrEmpty(data.getChuc_nang())) {
                condition += ", chuc_nang =:chucnang ";
            }
            if (!StringUtil.isNullOrEmpty(data.getTo_chuc())) {
                condition += ", to_chuc =:tochuc ";
            }
            if (!StringUtil.isNullOrEmpty(data.getNote())) {
                condition += ", description =:note ";
            }

            condition += " WHERE id = :id ";
            String sqlRun = sql + condition;
            NativeQuery query = session.createNativeQuery(sqlRun);
            query.setParameter("id", Integer.parseInt(data.getId()));
            query.setParameter("countryId", data.getCountry());
            query.setParameter("longitude", data.getLongitude());
            query.setParameter("latitude", data.getLatitude());
            query.setParameter("status", data.getStatus());
            if (!StringUtil.isNullOrEmpty(data.getName())) {
                query.setParameter("name", data.getName());
            }
            if (!StringUtil.isNullOrEmpty(data.getUsername())) {
                query.setParameter("userUpdated", data.getUsername());
            }
            if (!StringUtil.isNullOrEmpty(data.getVai_tro())) {
                query.setParameter("vaitro", data.getVai_tro());
            }
            if (!StringUtil.isNullOrEmpty(data.getChuc_nang())) {
                query.setParameter("chucnang", data.getChuc_nang());
            }
            if (!StringUtil.isNullOrEmpty(data.getTo_chuc())) {
                query.setParameter("tochuc", data.getTo_chuc());
            }
            if (!StringUtil.isNullOrEmpty(data.getNote())) {
                query.setParameter("note", data.getNote());
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

    public Page<HeadQuarterDto> search(SearchContactRequest data, Pageable pageable) {
        Session session = openSession();
        List<HeadQuarterDto> lstRes = new ArrayList<>();
        long count = 0;

        try {
            if (data.getCurrentPage() > 0) {
                data.setCurrentPage(data.getCurrentPage() - 1);
            }
            pageable = (Pageable) PageRequest.of(data.getCurrentPage(), data.getRowsPerPage());
            String sql = "";
            String sqlCountRow  = "";
            if (!StringUtil.isNullOrEmpty(data.getKeyword())) {
                sql = " select hq.id, hq.name, hq.country_id, ac.name as country_name, hq.longitude, "
                        + " hq.latitude, hq.description, hq.status, hq.user_updated, hq.updated_time, "
                        + " hq.vai_tro, hq.chuc_nang, hq.to_chuc "
                        + " FROM vsat02.headquarters hq left join vsat02.countries ac on (hq.country_id=ac.id) "
                        + " WHERE LOWER(hq.name) like :name OR LOWER(ac.name) like :countryName "
                        + " ORDER BY hq.created_time DESC";
                sqlCountRow = " SELECT count(hq.id) "
                            + " FROM vsat02.headquarters hq left join vsat02.countries ac on (hq.country_id = ac.id) "
                            + " WHERE LOWER(hq.name) like :name OR LOWER(ac.name) like :countryName ";
            } else {
                sql = " select hq.id, hq.name, hq.country_id, ac.name as country_name, hq.longitude, "
                        + " hq.latitude, hq.description, hq.status, hq.user_updated, hq.updated_time, "
                        + " hq.vai_tro, hq.chuc_nang, hq.to_chuc "
                        + " FROM vsat02.headquarters hq left join vsat02.countries ac on (hq.country_id=ac.id) ORDER BY hq.created_time DESC";
                
                sqlCountRow = "SELECT count(hq.id) FROM vsat02.headquarters hq left join vsat02.countries ac on (hq.country_id = ac.id)";
            }

            

            NativeQuery query = session.createNativeQuery(sql, HeadQuarterDto.class);
            if (!StringUtil.isNullOrEmpty(data.getKeyword())) {
                query.setParameter("name", '%'+ data.getKeyword().toLowerCase() +'%');
                query.setParameter("countryName", '%'+ data.getKeyword().toLowerCase()+ '%');
            }
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            lstRes = (List<HeadQuarterDto>) query.getResultList();
            
            NativeQuery queryCount = session.createNativeQuery(sqlCountRow);
            if (!StringUtil.isNullOrEmpty(data.getKeyword())) {
                queryCount.setParameter("name", '%'+ data.getKeyword().toLowerCase() +'%');
                queryCount.setParameter("countryName", '%'+ data.getKeyword().toLowerCase()+ '%');
                count = ((BigInteger) queryCount.getSingleResult()).longValue();
            }else{
                count = ((BigInteger) session.createNativeQuery(sqlCountRow)
                    .getSingleResult()).longValue();     
            }
            

        } catch (EntityNotFoundException ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        MessageContent messageContent = new MessageContent();
        messageContent.setData(lstRes);
        messageContent.setTotal(count);
        messageContent.setStatus(HttpStatus.OK.value());
        messageContent.setMessage(HttpStatus.OK.toString());

        return new PageImpl<>(lstRes, pageable, count);

    }

    public Object getHeadQuarterInfo(GetDetailHeadquatersRequest data) {
        Session session = openSession();
        HeadQuarterDto vesselInfo = new HeadQuarterDto();
        HeadQuarterDtoFull headFull = new HeadQuarterDtoFull();
        try {
            String sql = "select hq.id, hq.name, hq.country_id, ac.name as country_name, hq.longitude, hq.latitude, hq.description, hq.status, hq.user_updated, hq.updated_time,\n"
                    + "      hq.vai_tro, hq.chuc_nang, hq.to_chuc\n"
                    + "      from vsat02.headquarters hq left join vsat02.countries ac on (hq.country_id=ac.id)\n"
                    + "   \t\twhere hq.id=:id";
            NativeQuery query = session.createNativeQuery(sql, HeadQuarterDto.class);
            query.setParameter("id", data.getId());

            vesselInfo = (HeadQuarterDto) query.getSingleResult();

            List<ContactIP> lstContactIP = this.getListIp(data);
            List<Phone> lstPhone = this.getListPhone(data);

            headFull.setHeadQuarterDto(vesselInfo);
            headFull.setIps(lstContactIP);
            headFull.setPhones(lstPhone);

        } catch (Exception ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return headFull;
    }

    public List<ContactIP> getListIp(GetDetailHeadquatersRequest data) {
        Session session = openSession();
        List<ContactIP> listContactIP = new ArrayList<>();
        try {
            String sql = " SELECT mip.mmsi, mip.ip_address, mip.type,mip.note ,mip.created_time,mip.updated_time "
                    + " FROM vsat02.mmsi_ip as mip "
                    + " WHERE mip.mmsi=:pMmsi and mip.type <> 1 order by mip.ip_address";
            NativeQuery query = session.createNativeQuery(sql, ContactIP.class);
            query.setParameter("pMmsi", data.getId());
            listContactIP = (List<ContactIP>)query.getResultList();

        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return listContactIP;
    }

    public List<Phone> getListPhone(GetDetailHeadquatersRequest data) {
        Session session = openSession();
        List<Phone> listPhone = new ArrayList<>();
        try {
            String sql = "select mmsi, phone_number,type, note, created_time, updated_time"
                    + " from vsat02.mmsi_phone"
                    + " where mmsi=:pMmsi order by created_time;";
            NativeQuery query = session.createNativeQuery(sql, Phone.class);

            query.setParameter("pMmsi", data.getId());

            listPhone = (List<Phone>) query.getResultList();
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return listPhone;
    }

    public Object getListHaiTrinh(GetDetailVesselRequest data) {
        Session session = openSession();
        VesselInfo vesselInfo = new VesselInfo();
        try {
            String sql = "SELECT a.mmsi, a.imo, a.country_id, a.type_id, (a.dim_a + a.dim_b) as chieudai,\n"
                    + "                   (a.dim_c + a.dim_d) as chieurong, a.callsign, a.name, a.created_time,\n"
                    + "                   a.updated_time, a.name_static, a.is_master, a.image_path,\n"
                    + "                   a.satellite_phone_code, a.year_of_build, a.place_of_build_code,\n"
                    + "                   a.draugth, a.engine_type, a.gross_tonnage, a.dead_weight,\n"
                    + "                   a.name_place, a.speed_avg, a.speed_max, a.displacement, a.crew,\n"
                    + "                   a.weapons, a.endurance, b.name as country_name, b.flag as country_flag, c.type_name as vessel_type_name, c.type_desc as vessel_type_desc\n"
                    + "                FROM vsat02.ais_info a\n"
                    + "\t\t\t\tINNER JOIN vsat02.countries b on a.country_id = b.id\n"
                    + "\t\t\t\tINNER JOIN vsat02.vessel_types c on a.type_id =c.type_id\n"
                    + "\t\t\t\tWhere a.mmsi = :p_mmsi;";
            NativeQuery query = session.createNativeQuery(sql, VesselInfo.class);

            query.setParameter("p_mmsi", data.getMmsi());

            vesselInfo = (VesselInfo) query.getSingleResult();
        } catch (EntityNotFoundException ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return vesselInfo;
    }

    public List<ContactIP> getListIp(GetDetailVesselRequest data) {
        Session session = openSession();
        List<ContactIP> listContactIP = new ArrayList<>();
        try {
            String sql = "select mip.mmsi, mip.ip_address, mip.type,mip.note ,mip.created_time,mip.updated_time"
                    + " from vsat02.mmsi_ip as mip \n"
                    + "        where mip.mmsi=:pMmsi and mip.type <> 1 order by mip.ip_address";
            NativeQuery query = session.createNativeQuery(sql, ContactIP.class);

            query.setParameter("pMmsi", data.getMmsi());

            listContactIP = (List<ContactIP>) query.getResultList();
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return listContactIP;
    }

    public List<Phone> getListPhone(GetDetailVesselRequest data) {
        Session session = openSession();
        List<Phone> listPhone = new ArrayList<>();
        try {
            String sql = "select mmsi, phone_number,type, note, created_time, updated_time"
                    + " from vsat02.mmsi_phone"
                    + " where mmsi=:pMmsi order by phone_number;";
            NativeQuery query = session.createNativeQuery(sql, Phone.class);

            query.setParameter("pMmsi", data.getMmsi());

            listPhone = (List<Phone>) query.getResultList();
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return listPhone;
    }

    public List<Image> getListImage(GetDetailVesselRequest data) {
        Session session = openSession();
        List<Image> listImage = new ArrayList<>();
        try {
            String sql = "select mmsi, image_name from vsat02.image where MMSI = :p_mmsi";
            NativeQuery query = session.createNativeQuery(sql, Image.class);

            query.setParameter("p_mmsi", data.getMmsi());

            listImage = (List<Image>) query.getResultList();
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return listImage;
    }

    public List<Image> getListVoyage(GetDetailVesselRequest data) {
        Session session = this.sessionFactory.openSession();
        List<Image> listImage = new ArrayList<>();
        try {
            String sql = "select mmsi, image_name from vsat02.image where MMSI = :p_mmsi ;";
            NativeQuery query = session.createNativeQuery(sql, Image.class);

            query.setParameter("p_mmsi", data.getMmsi());

            listImage = (List<Image>) query.getResultList();
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return listImage;
    }

    public ContactIP checkIpHeadQuarter(AddContactLandIpRequest data) {
        Session session = openSession();
        Boolean isExitsIP = false;
        List<ContactIP> lstContactIp = null;
        try {
            String sql = "select * from vsat02.mmsi_ip ip where ip.ip_address=:ip_address";
            NativeQuery query = session.createNativeQuery(sql, ContactIP.class);

            query.setParameter("ip_address", data.getIp_address());

            lstContactIp = query.getResultList();
            if (lstContactIp.size() > 0) {
                return lstContactIp.get(0);
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
            throw ex;
        } finally {
            closeSession(session);
        }
        return null;
    }

    public Object addIpHeadQuarter(AddContactLandIpRequest data) {
        Session session = openSession();
        Boolean isExitsIP = false;
        Boolean res = false;
        try {
            session.getTransaction().begin();
            String sql = "INSERT INTO vsat02.mmsi_ip("
                    + " mmsi, type, ip_address, note, created_time)\n"
                    + " VALUES (:mmsi, :type, :ip_address, :note, :created_time)";
            NativeQuery query = session.createNativeQuery(sql);

            query.setParameter("mmsi", data.getId());
            query.setParameter("type", 0);
            query.setParameter("ip_address", data.getIp_address());
            query.setParameter("note", data.getNote());
            query.setParameter("created_time", new Date(), TemporalType.TIMESTAMP);

            int resI = query.executeUpdate();
            directionIPRepository.AddIpDirection(data.getIp_address(), 1, 1);
            session.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
            throw ex;
        } finally {
            closeSession(session);
        }
    }

    public Object delIpHeadQuarter(DelContactLandIpRequest data) {
        Session session = openSession();
        Boolean isExitsIP = false;
        Boolean res = false;
        try {
            session.getTransaction().begin();
            String sql = "DELETE FROM vsat02.mmsi_ip "
                    + " WHERE mmsi=:mmsi and ip_address=:ipAddress ";
            NativeQuery query = session.createNativeQuery(sql);

            query.setParameter("mmsi", data.getId());
            query.setParameter("ipAddress", data.getIp_address());

            int resI = query.executeUpdate();
            directionIPRepository.DelIpDirection(data.getIp_address(), 1, 1);
            session.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }

        return res;
    }

    public Object addHeadquater(AddHeadquaterRequest data) {

        return null;
    }
    
    public Phone checkPhoneHeadQuarter(ContactLandPhoneRequest data) {
        Session session = openSession();
        Boolean isExitsPhone = false;
        List<Phone> lstContactPhone = null;
        try {
            String sql = "select * from vsat02.mmsi_phone ph where ph.phone_number=:phoneNumber";
            NativeQuery query = session.createNativeQuery(sql, ContactIP.class);

            query.setParameter("phoneNumber", data.getPhoneNumber());

            lstContactPhone = query.getResultList();
            if (lstContactPhone.size() > 0) {
                return lstContactPhone.get(0);
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
            throw ex;
        } finally {
            closeSession(session);
        }
        return null;
    }
    
    public Object addPhoneHeadQuarter(ContactLandPhoneRequest data) {
        Session session = openSession();
        try {
            session.getTransaction().begin();
            String sql = "INSERT INTO vsat02.mmsi_phone("
                    + " mmsi, type, phone_number, note, created_time) "
                    + " VALUES (:mmsi, :type, :phone_number, :note, :created_time)";
            NativeQuery query = session.createNativeQuery(sql);

            query.setParameter("mmsi", data.getId());
            query.setParameter("type", 0);
            query.setParameter("phone_number", data.getPhoneNumber());
            query.setParameter("note", data.getNote());
            query.setParameter("created_time", new Date(), TemporalType.TIMESTAMP);

            int resP = query.executeUpdate();
            session.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
            throw ex;
        } finally {
            closeSession(session);
        }
    }    
    
    public Object delPhoneHeadQuarter(ContactLandPhoneRequest data) {
        Session session = openSession();
        Boolean res = false;
        try {
            session.getTransaction().begin();
            String sql = "DELETE FROM vsat02.mmsi_phone "
                    + " WHERE mmsi=:mmsi and phone_number=:phone_number ";
            NativeQuery query = session.createNativeQuery(sql);

            query.setParameter("mmsi", data.getId());
            query.setParameter("phone_number", data.getPhoneNumber());

            int resI = query.executeUpdate();
            session.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }

        return res;
    }    
    
}
