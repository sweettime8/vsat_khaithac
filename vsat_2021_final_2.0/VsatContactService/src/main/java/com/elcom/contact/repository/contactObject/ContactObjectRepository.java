package com.elcom.contact.repository.contactObject;

import com.elcom.contact.model.contact.*;
import com.elcom.contact.model.contactLand.HeadQuarterDto;
import com.elcom.contact.model.contactObject.ContactObjectIP;
import com.elcom.contact.model.contactObject.ObjectUnInfoDto;
import com.elcom.contact.model.dto.request.contact.GetDetailVesselRequest;
import com.elcom.contact.model.dto.request.contact.SearchContactRequest;
import com.elcom.contact.model.dto.request.contactObject.AddContactObjectIpRequest;
import com.elcom.contact.model.dto.request.contactObject.AddObjectUnInfoRequest;
import com.elcom.contact.model.dto.request.contactObject.DelContactObjectIpRequest;
import com.elcom.contact.model.dto.request.contactObject.GetDetailObjectRequest;
import com.elcom.contact.model.dto.request.contactObject.ObjectUndefineDtoFull;
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
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Repository
public class ContactObjectRepository extends BaseRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactObjectRepository.class);

    @Autowired
    @Qualifier("clickHouseSession")
    SessionFactory sessionFactory;

    @Autowired
    DirectionIPRepository directionIPRepository;

    @Autowired
    public ContactObjectRepository(@Qualifier("vsatEntityManagerFactory") EntityManagerFactory factory, @Qualifier("vsatChDataSource") DataSource dataSource) {
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

    public boolean updateObjectUnInfo(AddObjectUnInfoRequest data) {
        Session session = openSession();
        Object obj = null;
        try {
            session.getTransaction().begin();
            String sql = "UPDATE vsat02.object_undefined_info SET ";
            String condition = "";

            condition += " country_id =:countryId, dim_b = 0, dim_d = 0, updated_time = now() ";
            if (data.getChieudai() > 0) {
                condition += ", dim_a =:chieudai ";
            }
            if (data.getChieurong() > 0) {
                condition += ", dim_c =:chieurong ";
            }
            if (!StringUtil.isNullOrEmpty(data.getName())) {
                condition += ", name =:name ";
            }
            if (!StringUtil.isNullOrEmpty(data.getImagePath())) {
                condition += ", image_path =:imagePath ";
            }
            if (!StringUtil.isNullOrEmpty(data.getUserUpdate())) {
                condition += ", user_updated =:userUpdated ";
            }
            if (!StringUtil.isNullOrEmpty(data.getSourceIP())) {
                condition += ", source_ip =:sourceIp ";
            }
            if (data.getSourcePort() > 0) {
                condition += ", source_port =:sourcePort ";
            }
            if (!StringUtil.isNullOrEmpty(data.getDestIP())) {
                condition += ", dest_ip =:destIp ";
            }
            if (data.getDestPort() > 0) {
                condition += ", dest_port =:destPort ";
            }

            condition += " WHERE uuid = :uuid ";
            String sqlRun = sql + condition;
            NativeQuery query = session.createNativeQuery(sqlRun);
            query.setParameter("uuid", data.getUuid());
            query.setParameter("countryId", data.getCountry_id());

            if (data.getChieudai() > 0) {
                query.setParameter("chieudai", data.getChieudai());
            }
            if (data.getChieurong() > 0) {
                query.setParameter("chieurong", data.getChieurong());
            }
            if (!StringUtil.isNullOrEmpty(data.getName())) {
                query.setParameter("name", data.getName());
            }
            if (!StringUtil.isNullOrEmpty(data.getImagePath())) {
                query.setParameter("imagePath", data.getImagePath());
            }
            if (!StringUtil.isNullOrEmpty(data.getUserUpdate())) {
                query.setParameter("userUpdated", data.getUserUpdate());
            }
            if (!StringUtil.isNullOrEmpty(data.getSourceIP())) {
                query.setParameter("sourceIp", data.getSourceIP());
            }
            if (data.getSourcePort() > 0) {
                query.setParameter("sourcePort", data.getSourcePort());
            }
            if (!StringUtil.isNullOrEmpty(data.getDestIP())) {
                query.setParameter("destIp", data.getDestIP());
            }
            if (data.getDestPort() > 0) {
                query.setParameter("destPort", data.getDestPort());
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

    public Page<ObjectUnInfoDto> search(SearchContactRequest data, Pageable pageable) {
        Session session = openSession();
        List<ObjectUnInfoDto> lstObjectUnInfoDto = new ArrayList<>();
        long count = 0;
        try {
            if (data.getCurrentPage() > 0) {
                data.setCurrentPage(data.getCurrentPage() - 1);
            }
            pageable = (Pageable) PageRequest.of(data.getCurrentPage(), data.getRowsPerPage());
            String sql = "";
            String sqlCountRow = "";
            if (!StringUtil.isNullOrEmpty(data.getKeyword())) {
                sql = "SELECT a.uuid,  a.country_id,  (a.dim_a + a.dim_b) as chieudai,"
                        + " (a.dim_c + a.dim_d) as chieurong,  a.name, a.created_time,"
                        + " a.updated_time,  a.image_path,a.user_update, a.source_ip, a.source_port,"
                        + " a.dest_ip, a.dest_port, "
                        + " b.name as country_name, b.flag as country_flag"
                        + " FROM vsat02.object_undefined_info a, vsat02.countries b "
                        + " WHERE a.country_id = b.id "
                        + " AND LOWER(a.name) like :name OR LOWER(b.name) like :countryName"
                        + " ORDER BY created_time DESC";

                sqlCountRow = "SELECT count(a.uuid)  "
                        + " FROM vsat02.object_undefined_info a, vsat02.countries b "
                        + " WHERE a.country_id = b.id "
                        + " AND LOWER(a.name) like :name OR LOWER(b.name) like :countryName";
            } else {
                sql = "SELECT a.uuid,  a.country_id,  (a.dim_a + a.dim_b) as chieudai,"
                        + " (a.dim_c + a.dim_d) as chieurong,  a.name, a.created_time,"
                        + " a.updated_time,  a.image_path,a.user_update, a.source_ip, a.source_port,"
                        + " a.dest_ip, a.dest_port, "
                        + " b.name as country_name, b.flag as country_flag"
                        + " FROM vsat02.object_undefined_info a, vsat02.countries b "
                        + " WHERE a.country_id = b.id "
                        + " ORDER BY created_time DESC";

                sqlCountRow = "SELECT count(a.uuid) "
                        + " FROM vsat02.object_undefined_info a, vsat02.countries b "
                        + " WHERE a.country_id = b.id ";
            }

            NativeQuery query = session.createNativeQuery(sql, ObjectUnInfoDto.class);
            if (!StringUtil.isNullOrEmpty(data.getKeyword())) {
                query.setParameter("name", '%' + data.getKeyword().toLowerCase() + '%');
                query.setParameter("countryName", '%' + data.getKeyword().toLowerCase() + '%');
            }
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            lstObjectUnInfoDto = (List<ObjectUnInfoDto>) query.getResultList();

            NativeQuery queryCount = session.createNativeQuery(sqlCountRow);
            if (!StringUtil.isNullOrEmpty(data.getKeyword())) {
                queryCount.setParameter("name", '%' + data.getKeyword().toLowerCase() + '%');
                queryCount.setParameter("countryName", '%' + data.getKeyword().toLowerCase() + '%');
                count = ((BigInteger) queryCount.getSingleResult()).longValue();
            } else {
                count = ((BigInteger) session.createNativeQuery(sqlCountRow)
                        .getSingleResult()).longValue();
            }


        } catch (Exception ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return new PageImpl<>(lstObjectUnInfoDto, pageable, count);
    }

    public Object getObjectInfo(GetDetailObjectRequest data) {
        Session session = openSession();
        ObjectUnInfoDto objectUnInfoDto = null;
        ObjectUndefineDtoFull objectUndefineDtoFull = new ObjectUndefineDtoFull();
        try {
            String sql = "SELECT a.uuid,  a.country_id,  (a.dim_a + a.dim_b) as chieudai, "
                    + " (a.dim_c + a.dim_d) as chieurong,  a.name, a.created_time, "
                    + " a.updated_time,  a.image_path, a.user_update, a.source_ip, a.source_port,"
                    + " a.dest_ip, a.dest_port, "
                    + " b.name as country_name, b.flag as country_flag "
                    + " FROM object_undefined_info a "
                    + " INNER JOIN countries b on a.country_id = b.id "
                    + " where a.uuid = '" +data.getUuid()+"'";
            NativeQuery query = session.createNativeQuery(sql,ObjectUnInfoDto.class);
//           query.setParameter("parram", data.getUuid());
            objectUnInfoDto = (ObjectUnInfoDto) query.getSingleResult();
//            Object s=  query.getResultList();
            List<UfoIP> lstUfoIP = this.getListUfoIp(data);
            objectUndefineDtoFull.setObjectUnInfoDto(objectUnInfoDto);
            objectUndefineDtoFull.setIps(lstUfoIP);
            
        } catch (Exception ex) {

            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return objectUndefineDtoFull;
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

    public List<ContactIP> getListIp(GetDetailObjectRequest data) {
        Session session = openSession();
        List<ContactIP> listContactIP = new ArrayList<>();
        try {
            String sql = "select mip.mmsi, mip.ip_address, mip.type,mip.note ,mip.created_time,mip.updated_time"
                    + " from vsat02.mmsi_ip as mip \n"
                    + "        where mip.mmsi=:pMmsi and mip.type <> 1 order by mip.ip_address";
            NativeQuery query = session.createNativeQuery(sql, ContactIP.class);

            query.setParameter("pMmsi", data.getUuid());

            listContactIP = (List<ContactIP>) query.getResultList();
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return listContactIP;
    }

    public List<Phone> getListPhone(GetDetailObjectRequest data) {
        Session session = openSession();
        List<Phone> listPhone = new ArrayList<>();
        try {
            String sql = "select mmsi, phone_number,type, note, created_time, updated_time"
                    + " from vsat02.mmsi_phone"
                    + " where mmsi=:pMmsi order by phone_number;";
            NativeQuery query = session.createNativeQuery(sql, Phone.class);

            query.setParameter("pMmsi", data.getUuid());

            listPhone = (List<Phone>) query.getResultList();
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return listPhone;
    }

    public List<Image> getListImage(GetDetailObjectRequest data) {
        Session session = openSession();
        List<Image> listImage = new ArrayList<>();
        try {
            String sql = "select mmsi, image_name from vsat02.image where MMSI = :p_mmsi";
            NativeQuery query = session.createNativeQuery(sql, Image.class);

            query.setParameter("p_mmsi", data.getUuid());

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

//    public MessageContent getDetailVessel(GetDetailObjectRequest data) {
//        Session session = openSession();
//        ContactFullDetailDto contactFullDetailDto = new ContactFullDetailDto();
//        try {
//            ObjectUnInfoDto objectUnInfoDto = this.getObjectInfo(data);
//            List<Image> lstImage = this.getListImage(data);
//            List<ContactIP> lstContactIP = this.getListIp(data);
//            List<Phone> lstPhone = this.getListPhone(data);
////            contactFullDetailDto.setInfo(vesselInfo);
//            contactFullDetailDto.setImages(lstImage);
//            contactFullDetailDto.setIps(lstContactIP);
//            contactFullDetailDto.setPhones(lstPhone);
//        } catch (EntityNotFoundException ex) {
//            session.getTransaction().rollback();
//            LOGGER.error(ex.toString());
//        } finally {
//            closeSession(session);
//        }
//        MessageContent messageContent = new MessageContent();
//        messageContent.setData(contactFullDetailDto);
//        return messageContent;
//    }

    public ContactObjectIP checkIpObjectInfo(AddContactObjectIpRequest data) {
        Session session = openSession();
        Boolean isExitsIP = false;
        List<ContactObjectIP> lstContactObjectIP = null;
        try {
            String sql = "select * from vsat02.ufo_ip as ip where ip.ip_address=:ipAddress";
            NativeQuery query = session.createNativeQuery(sql, ContactObjectIP.class);

//            query.setParameter("pMmsi",data.getUuid());
            query.setParameter("ipAddress", data.getIp_address());

            lstContactObjectIP = query.getResultList();
            if (lstContactObjectIP.size() > 0) {
                return lstContactObjectIP.get(0);
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
            throw ex;
        } finally {
            closeSession(session);
        }

        return null;
    }

    public Object addIpObjectInfo(AddContactObjectIpRequest data) {
        Session session = openSession();
        Boolean isExitsIP = false;
        Boolean res = false;
        try {
            session.getTransaction().begin();
            String sql = " INSERT INTO vsat02.ufo_ip(ufo_id, type, ip_address, note, created_time) "
                    + "   VALUES (:ufo_id, :type, :ip_address, :note, :created_time) ";
            NativeQuery query = session.createNativeQuery(sql);

            query.setParameter("ufo_id", data.getUuid());
            query.setParameter("type", 0);
            query.setParameter("ip_address", data.getIp_address());
            query.setParameter("note", data.getNote());
            query.setParameter("created_time", new Date(), TemporalType.TIMESTAMP);

            int resI = query.executeUpdate();

            directionIPRepository.AddIpDirection(data.getIp_address(), 2, 0);

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

    public Object delIpObjectInfo(DelContactObjectIpRequest data) {
        Session session = openSession();
        Boolean isExitsIP = false;
        Boolean res = false;
        try {
            session.getTransaction().begin();
            String sql = "DELETE FROM vsat02.ufo_ip WHERE ufo_id=:ufo_id and ip_address=:ipAddress";
            NativeQuery query = session.createNativeQuery(sql);

            query.setParameter("ufo_id", UUID.fromString(data.getUuid()));
            query.setParameter("ipAddress", data.getIp_address());

            int resI = query.executeUpdate();
            directionIPRepository.DelIpDirection(data.getIp_address(), 2, 0);
            session.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
            throw ex;
        } finally {
            closeSession(session);
        }
    }

    private List<UfoIP> getListUfoIp(GetDetailObjectRequest data) {
        Session session = openSession();
        List<UfoIP> listUfoIP = new ArrayList<>();
        try {
            String sql = "select mip.id ,mip.ufo_id, mip.ip_address , mip.type, mip.note, mip.created_time, mip.updated_time"
                    + " from vsat02.ufo_ip as mip "
                    + " where mip.ufo_id = :uuid and mip.type <> 1 order by mip.ip_address";
            NativeQuery query = session.createNativeQuery(sql, UfoIP.class);

            query.setParameter("uuid", data.getUuid());

            listUfoIP = (List<UfoIP>) query.getResultList();
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return listUfoIP;
    }
}
