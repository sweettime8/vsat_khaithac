package com.elcom.contact.repository.contact;

import com.elcom.contact.model.contact.*;
import com.elcom.contact.model.dto.request.contact.AddContactIpRequest;
import com.elcom.contact.model.dto.request.contact.AddVesselRequest;
import com.elcom.contact.model.dto.request.contact.DelContactIpRequest;
import com.elcom.contact.model.dto.request.contact.GetDetailVesselRequest;
import com.elcom.contact.model.dto.request.contact.SearchContactRequest;
import com.elcom.contact.repository.BaseRepository;
import com.elcom.contact.repository.directIp.DirectionIPRepository;
import com.elcom.contact.repository.media.MediaRepository;
import com.elcom.contact.utils.StringUtil;
import com.elcom.gateway.message.MessageContent;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

@Repository
public class ContactRepository extends BaseRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactRepository.class);

    @Autowired
    @Qualifier("clickHouseSession")
    SessionFactory sessionFactory;

    @Autowired
    DirectionIPRepository directionIPRepository;

    @Autowired
    public ContactRepository(@Qualifier("vsatEntityManagerFactory") EntityManagerFactory factory, @Qualifier("vsatChDataSource") DataSource dataSource) {
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

    public MessageContent search(SearchContactRequest data) {
        Session session = openSession();
        List<VesselInfo> lstVesselInfo = new ArrayList<>();
        long count = 0;
        try {
            if (data.getCurrentPage() > 0) {
                data.setCurrentPage(data.getCurrentPage() - 1);
            }
            Pageable pageable = (Pageable) PageRequest.of(data.getCurrentPage(), data.getRowsPerPage());

            String sql = "SELECT a.mmsi, a.imo, a.country_id, a.type_id, (a.dim_a + a.dim_b) as chieudai,\n"
                    + "                   (a.dim_c + a.dim_d) as chieurong, a.callsign, a.name, a.created_time,\n"
                    + "                   a.updated_time, a.name_static, a.is_master, a.image_path,\n"
                    + "                   a.satellite_phone_code, a.year_of_build, a.place_of_build_code,\n"
                    + "                   a.draugth, a.engine_type, a.gross_tonnage, a.dead_weight,\n"
                    + "                   a.name_place, a.speed_avg, a.speed_max, a.displacement, a.crew,\n"
                    + "                   a.weapons, a.endurance,  b.name as country_name, b.flag as country_flag, c.type_name as vessel_type_name, c.type_desc as vessel_type_desc\n"
                    + " FROM vsat02.ais_info a\n"
                    + " INNER JOIN vsat02.countries b on a.country_id = b.id\n"
                    + " INNER JOIN vsat02.vessel_types c on a.type_id =c.type_id";

            NativeQuery query = session.createNativeQuery(sql, VesselInfo.class);
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            lstVesselInfo = (List<VesselInfo>) query.getResultList();
            count = ((Number) query.getSingleResult()).intValue();

        } catch (EntityNotFoundException ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        MessageContent messageContent = new MessageContent();
        messageContent.setData(lstVesselInfo);
        messageContent.setTotal(count);
        messageContent.setStatus(HttpStatus.OK.value());
        messageContent.setMessage(HttpStatus.OK.toString());

        return messageContent;
    }

    public VesselAltInfo getVesselAltInfo(GetDetailVesselRequest data) {
        Session session = openSession();
        VesselAltInfo vesselAltInfo = new VesselAltInfo();
        try {
            String sql = "SELECT a.mmsi, a.operation_unit, a.other_info, a.owner, "
                    + " a.status, a.unit, a.created_time, a.updated_time "
                    +"  FROM vsat02.vessel_alt_info a"
                    + " WHERE a.mmsi=:pMmsi";
            NativeQuery query = session.createNativeQuery(sql, VesselAltInfo.class);
            query.setParameter("pMmsi", data.getMmsi());

            vesselAltInfo = (VesselAltInfo) query.getSingleResult();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return vesselAltInfo;
    }

    public VesselInfo getVesselInfo(GetDetailVesselRequest data) {
        Session session = openSession();
        VesselInfo vesselInfo = new VesselInfo();
        try {
            String sql = "SELECT a.mmsi, a.imo, a.country_id, a.type_id, (a.dim_a + a.dim_b) as chieudai,\n"
                    + "    (a.dim_c + a.dim_d) as chieurong, a.callsign, a.name, a.created_time,\n"
                    + "    a.updated_time, a.name_static, a.is_master, a.image_path,\n"
                    + "    a.satellite_phone_code, a.year_of_build, a.place_of_build_code,\n"
                    + "    a.draugth, a.engine_type, a.gross_tonnage, a.dead_weight,\n"
                    + "    a.name_place, a.speed_avg, a.speed_max, a.displacement, a.crew,\n"
                    + "    a.weapons, a.endurance, b.name as country_name, b.flag as country_flag, c.type_name as vessel_type_name, c.type_desc as vessel_type_desc "
                    + " FROM vsat02.ais_info a "
                    + " LEFT JOIN vsat02.countries b on a.country_id = b.id\n"
                    + " LEFT JOIN vsat02.vessel_types c on a.type_id =c.type_id\n"
                    + " WHERE a.mmsi=:pMmsi";
            NativeQuery query = session.createNativeQuery(sql, VesselInfo.class);
            query.setParameter("pMmsi", data.getMmsi());

            vesselInfo = (VesselInfo) query.getSingleResult();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return vesselInfo;
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
            String sql = " SELECT mip.mmsi, mip.ip_address, mip.type,mip.note ,mip.created_time,mip.updated_time "
                    + " FROM vsat02.mmsi_ip as mip "
                    + " WHERE mip.mmsi=:pMmsi and mip.type <> 1 order by mip.ip_address";
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

    public List<MmsiImage> getListImage(GetDetailVesselRequest data) {
        Session session = openSession();
        List<MmsiImage> listImage = new ArrayList<>();
        try {
            String sql = "select id, mmsi, type, image_path from vsat02.mmsi_image where mmsi = :p_mmsi";
            NativeQuery query = session.createNativeQuery(sql, MmsiImage.class);

            query.setParameter("p_mmsi", data.getMmsi());

            listImage = (List<MmsiImage>) query.getResultList();
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
        
    public MessageContent getDetailVessel(GetDetailVesselRequest data) {
        Session session = openSession();
        ContactFullDetailDto contactFullDetailDto = new ContactFullDetailDto();
        try {
            VesselInfo vesselInfo = this.getVesselInfo(data);
            VesselAltInfo vesselAltInfo = this.getVesselAltInfo(data);
            List<MmsiImage> lstImage = this.getListImage(data);
            List<ContactIP> lstContactIP = this.getListIp(data);
            List<Phone> lstPhone = this.getListPhone(data);
            contactFullDetailDto.setInfo(vesselInfo);
            contactFullDetailDto.setVesselAltInfo(vesselAltInfo);
            contactFullDetailDto.setImages(lstImage);
            contactFullDetailDto.setIps(lstContactIP);
            contactFullDetailDto.setPhones(lstPhone);
        } catch (EntityNotFoundException ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        MessageContent messageContent = new MessageContent();
        messageContent.setData(contactFullDetailDto);
        return messageContent;
    }

    public ContactIP checkIpVessel(AddContactIpRequest data) {
        Session session = openSession();
        Boolean isExitsIP = false;
        List<ContactIP> lstContactIp = null;
        try {
            String sql = "select * from vsat02.mmsi_ip ip where ip.ip_address=:ip_address";
            NativeQuery query = session.createNativeQuery(sql, ContactIP.class);

//            query.setParameter("p_mmsi",data.getMmsi());
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

    public Object addIpVessel(AddContactIpRequest data) {
        Session session = openSession();
        Boolean isExitsIP = false;
        Boolean res = false;
        try {
            session.getTransaction().begin();
            String sql = "INSERT INTO vsat02.mmsi_ip("
                    + " mmsi, type, ip_address, note, created_time)\n"
                    + " VALUES (:mmsi, :type, :ip_address, :note, :created_time)";
            NativeQuery query = session.createNativeQuery(sql);

            query.setParameter("mmsi", data.getMmsi());
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

    public boolean updateAisInfo(AddVesselRequest data) {
        Session session = openSession();
        try {
            session.getTransaction().begin();
            String sql = "UPDATE vsat02.ais_info SET updated_time = now() ";
            String condition = "";

            if (data.getImo() != null) {
                condition += ", imo =:imo";
            }
            if (data.getCountryId() != null) {
                condition += ", country_id =:countryId";
            }
            if (data.getVesselType() != null) {
                condition += ", type_id =:typeId";
            }
            if (data.getHeight() != null) {
                condition += ", dim_c =:dimc";
            }
            if (data.getWidth() != null) {
                condition += ", dim_a =:dima";
            }
            if (data.getCallsign() != null) {
                condition += ", callsign =:callsign";
            }
            if (data.getVesselName() != null) {
                condition += ", name =:veselName";
            }
            if (data.getNameStatic() != null) {
                condition += ", name_static =:nameStatic";
            }
            if (data.getImagePath() != null) {
                condition += ", image_path =:imagePath";
            }
            if (data.getSatellitePhoneCode() != null) {
                condition += ", satellite_phone_code =:satellitePhoneCode";
            }
            if (data.getYearOfBuild() != null) {
                condition += ", year_of_build =:yearOfBuild";
            }
            if (data.getPlaceOfBuildCode() != null) {
                condition += ", place_of_build_code =:placeOfBuildCode";
            }
            if (data.getDraugth() != null) {
                condition += ", draugth =:draugth";
            }
            if (data.getEngineType() != null) {
                condition += ", engine_type =:engineType";
            }
            if (data.getGrossTonnage() != null) {
                condition += ", gross_tonnage =:grossTonnage";
            }
            if (data.getDeadWeight() != null) {
                condition += ", dead_weight =:deadWeight";
            }
            if (data.getNamePlace() != null) {
                condition += ", name_place =:namePlace";
            }
            if (data.getSpeedMax() != null) {
                condition += ", speed_max =:speedMax";
            }
            if (data.getSpeedAvg() != null) {
                condition += ", speed_avg =:speedAvg";
            }
            if (data.getDisplacement() != null) {
                condition += ", displacement =:displacement";
            }
            if (data.getCrew() != null) {
                condition += ", crew =:crew";
            }
            if (data.getWeapons() != null) {
                condition += ", weapons =:weapons";
            }
            if (data.getEndurance() != null) {
                condition += ", endurance =:endurance";
            }
            if (data.getUserUpdate() != null) {
                condition += ", user_update =:userUpdate";
            }

            condition += " WHERE mmsi = :mmsi ";
            String sqlRun = sql + condition;
            NativeQuery query = session.createNativeQuery(sqlRun);
            query.setParameter("mmsi", data.getMmsi());
            if (data.getImo() != null) {
                query.setParameter("imo", data.getImo());
            }
            if (data.getCountryId() != null) {
                query.setParameter("countryId", data.getCountryId());
            }
            if (data.getVesselType() != null) {
                query.setParameter("typeId", data.getVesselType());
            }
            if (data.getHeight() != null) {
                query.setParameter("dimc", data.getHeight());
            }
            if (data.getWidth() != null) {
                query.setParameter("dima", data.getWidth());
            }
            if (data.getCallsign() != null) {
                query.setParameter("callsign", data.getCallsign());
            }
            if (data.getVesselName() != null) {
                query.setParameter("veselName", data.getVesselName());
            }
            if (data.getNameStatic() != null) {
                query.setParameter("nameStatic", data.getNameStatic());
            }
            if (data.getImagePath() != null) {
                query.setParameter("imagePath", data.getImagePath());
            }
            if (data.getSatellitePhoneCode() != null) {
                query.setParameter("satellitePhoneCode", data.getSatellitePhoneCode());
            }
            if (data.getYearOfBuild() != null) {
                query.setParameter("yearOfBuild", data.getYearOfBuild());
            }
            if (data.getPlaceOfBuildCode() != null) {
                query.setParameter("placeOfBuildCode", data.getPlaceOfBuildCode());
            }
            if (data.getDraugth() != null) {
                query.setParameter("draugth", data.getDraugth());
            }
            if (data.getEngineType() != null) {
                query.setParameter("engineType", data.getEngineType());
            }
            if (data.getGrossTonnage() != null) {
                query.setParameter("grossTonnage", data.getGrossTonnage());
            }
            if (data.getDeadWeight() != null) {
                query.setParameter("deadWeight", data.getDeadWeight());
            }
            if (data.getNamePlace() != null) {
                query.setParameter("namePlace", data.getNamePlace());
            }
            if (data.getSpeedMax() != null) {
                query.setParameter("speedMax", data.getSpeedMax());
            }
            if (data.getSpeedAvg() != null) {
                query.setParameter("speedAvg", data.getSpeedAvg());
            }
            if (data.getDisplacement() != null) {
                query.setParameter("displacement", data.getDisplacement());
            }
            if (data.getCrew() != null) {
                query.setParameter("crew", data.getCrew());
            }
            if (data.getWeapons() != null) {
                query.setParameter("weapons", data.getWeapons());
            }
            if (data.getEndurance() != null) {
                query.setParameter("endurance", data.getEndurance());
            }
            if (data.getUserUpdate() != null) {
                query.setParameter("userUpdate", data.getUserUpdate());
            }

            int result = query.executeUpdate();
            session.getTransaction().commit();
            if (result > 0) {
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

    public Object delIpVessel(DelContactIpRequest data) {
        Session session = openSession();
        Boolean isExitsIP = false;
        Boolean res = false;
        try {
            session.getTransaction().begin();
            String sql = "DELETE FROM vsat02.mmsi_ip "
                    + " WHERE mmsi=:mmsi and ip_address=:ipAddress ";
            NativeQuery query = session.createNativeQuery(sql);

            query.setParameter("mmsi", data.getMmsi());
            query.setParameter("ipAddress", data.getIp_address());
            directionIPRepository.DelIpDirection(data.getIp_address(), 2, 0);
            int resI = query.executeUpdate();
            session.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
            throw ex;
        } finally {
            closeSession(session);
        }
    }

    public boolean updateVesselAltInfo(AddVesselRequest data) {
        Session session = openSession();
        try {
            session.getTransaction().begin();
            String sql = "UPDATE vsat02.vessel_alt_info SET updated_time = now() ";
            String condition = "";

            if (data.getOperationUnit()!= null) {
                condition += ", operation_unit =:operationUnit";
            }
            if (data.getOtherInfo()!= null) {
                condition += ", other_info =:otherInfo";
            }
            if (data.getOwner()!= null) {
                condition += ", owner =:owner";
            }
            if (data.getStatus()!= null) {
                condition += ", status =:status";
            }
            if (data.getUnit()!= null) {
                condition += ", unit =:unit";
            }          

            condition += " WHERE mmsi = :mmsi ";
            String sqlRun = sql + condition;
            NativeQuery query = session.createNativeQuery(sqlRun);
            query.setParameter("mmsi", data.getMmsi());
            
            if (data.getOperationUnit()!= null) {
                query.setParameter("operationUnit", data.getOperationUnit());
            }
            if (data.getOtherInfo()!= null) {
                query.setParameter("otherInfo", data.getOtherInfo());
            }
            if (data.getOwner()!= null) {
                query.setParameter("owner", data.getOwner());
            }
            if (data.getStatus()!= null) {
                query.setParameter("status", data.getStatus());
            }
            if (data.getUnit()!= null) {
                query.setParameter("unit", data.getUnit());
            }

            int result = query.executeUpdate();
            session.getTransaction().commit();
            if (result > 0) {
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
