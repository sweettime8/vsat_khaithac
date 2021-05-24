package com.elcom.map.repository.map;

import com.elcom.gateway.message.MessageContent;
import com.elcom.map.config.ApplicationConfig;
import com.elcom.map.constant.Constant;
import com.elcom.map.model.area.Area;
import com.elcom.map.model.dto.*;
import com.elcom.map.model.dto.request.map.AddAreaRequest;
import com.elcom.map.model.dto.request.map.DeleteAreaRequest;
import com.elcom.map.model.dto.request.map.GetListAreaRequest;
import com.elcom.map.model.dto.request.media.SearchListAisRequest;
import com.elcom.map.model.vessel.VesselGroup;
import com.elcom.map.repository.BaseRepository;
import com.elcom.map.utils.DateUtil;
import com.elcom.map.utils.StringUtil;
import com.vladmihalcea.hibernate.type.array.IntArrayType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MapRepository extends BaseRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapRepository.class);

    @Autowired
    @Qualifier("clickHouseSession")
    SessionFactory clickHouseSessionFactory;

    @Autowired
    public MapRepository(@Qualifier("vsatEntityManagerFactory") EntityManagerFactory factory, @Qualifier("vsatChDataSource") DataSource dataSource) {
        super(factory, dataSource);
    }

    public MessageContent saveArea(AddAreaRequest data){
        Session session = this.clickHouseSessionFactory.openSession();
        try {

            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            if (session != null && session.isOpen()) {
                session.disconnect();
                session.close();
            }
        }
        return null;
    }

    public MessageContent getListArea(GetListAreaRequest data){
        Session session = this.openSession();
        MessageContent messageContent=new MessageContent();
        try {

            String sql = "SELECT * FROM areas ";
                    if(data.getStartTime()!=null&&data.getEndTime()!=null){
                        sql+= "where created_time>:startTime and created_time < :endTime ";
                    }
            sql+="order by created_time desc ;";
            NativeQuery query = session.createNativeQuery(sql, Area.class);
            if(data.getStartTime()!=null&&data.getEndTime()!=null){
            query.setParameter("startTime", data.getStartTime(), TemporalType.TIMESTAMP);
            query.setParameter("endTime", data.getEndTime(), TemporalType.TIMESTAMP);
            }
            List<Area> result = (List<Area>)query.getResultList();
            messageContent.setStatus(HttpStatus.OK.value());
            messageContent.setData(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
            messageContent.setStatus(HttpStatus.BAD_REQUEST.value());
        } finally {
            if (session != null && session.isOpen()) {
                session.disconnect();
                session.close();
            }
        }
        return messageContent;
    }

    public MessageContent delArea(DeleteAreaRequest data){
        Session session = this.openSession();
        MessageContent messageContent=new MessageContent();
        Transaction tx = session.beginTransaction();
        try {

            String sql = "DELETE FROM areas WHERE id=:id";
            NativeQuery query = session.createNativeQuery(sql);
            query.setParameter("id", data.getId());

            int result = query.executeUpdate();
            if(result>=1){
                messageContent.setStatus(HttpStatus.OK.value());
                messageContent.setData(result);
            }
            else{
                messageContent.setStatus(HttpStatus.BAD_REQUEST.value());
                messageContent.setMessage("Xóa vùng thất bại.");
            }


            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
            ex.printStackTrace();
            LOGGER.error(StringUtil.printException(ex));
            messageContent.setStatus(HttpStatus.BAD_REQUEST.value());
        } finally {
            if (session != null && session.isOpen()) {
                session.disconnect();
                session.close();
            }
        }
        return messageContent;
    }
}
