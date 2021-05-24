/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.statistic.repository;

import com.elcom.statistic.model.dto.DatasourceDTO;
import com.elcom.statistic.model.dto.MediaFLRLDTO;
import com.elcom.statistic.model.dto.SearchMediaRequest;
import com.elcom.statistic.model.dto.SearchRequest;
import com.elcom.statistic.model.dto.StatisticMediaDTO;
import com.elcom.statistic.model.dto.StatisticPcapDTO;
import com.elcom.statistic.model.dto.StatisticVesselDTO;
import com.elcom.statistic.model.dto.TypeServiceDTO;
import com.elcom.statistic.utils.StringUtil;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.sql.DataSource;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public class MediaCustomRepository extends BaseRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MediaCustomRepository.class);

    @Autowired
    @Qualifier("clickHouseSession")
    SessionFactory sessionFactory;

    @Autowired
    public MediaCustomRepository(@Qualifier("vsatEntityManagerFactory") EntityManagerFactory factory, @Qualifier("vsatChDataSource") DataSource dataSource) {
        super(factory, dataSource);
    }

    public Page<StatisticMediaDTO> searchFlrl(SearchRequest data, Pageable pageable) throws ParseException {
        Session session = this.sessionFactory.openSession();
        List<StatisticMediaDTO> lstRes = null;
        long count = 0;
        int sizeConfig = 30;

        try {
            if (data.getCurrentPage() > 0) {
                data.setCurrentPage(data.getCurrentPage() - 1);
            }
            pageable = (Pageable) PageRequest.of(data.getCurrentPage(), data.getRowsPerPage());
            String sql = "";
            String sqlCountRow = "";
            String condition = "";

            String startDate = data.getStartDate();
            String endDate = data.getEndDate();

            condition += " eventTime >= :startDate AND eventTime<= :endDate ";
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                condition += " AND dataSource = :dataSource ";
            }

            sql = "select dataSource,dataSourceName, direction, byDate, totalALL as fileSize FROM "
                    + "(Select  dataSource , dataSourceName ,direction , partName as byDate , SUM(fileSize) as totalALL "
                    + "				FROM vsat_media "
                    + "				where  "
                    + condition
                    + "	  			group by  dataSource ,dataSourceName, direction, partName "
                    + " UNION ALL "
                    + " Select  dataSource , dataSourceName ,direction , partName as byDate , count(objId) * :sizeConfig as totalALL "
                    + "                FROM vsat_ais "
                    + "                where   "
                    + condition
                    + "                 group by  dataSource ,dataSourceName, direction, partName  )"
                    + "                 order By byDate, dataSource  ";

            sqlCountRow = "Select Count(dataSource) "
                    + " FROM "
                    + " (select dataSource,dataSourceName, direction, byDate, totalALL as fileSize FROM\n"
                    + "(Select  dataSource , dataSourceName ,direction , partName as byDate , SUM(fileSize) as totalALL\n"
                    + "				FROM vsat_media\n"
                    + "				where  "
                    + condition
                    + "	  			group by  dataSource ,dataSourceName, direction, partName \n"
                    + "UNION ALL			"
                    + "Select  dataSource , dataSourceName ,direction , partName as byDate , count(objId) as totalALL \n"
                    + "                FROM vsat_ais \n"
                    + "                where   \n"
                    + condition
                    + "                 group by  dataSource ,dataSourceName, direction, partName  )\n"
                    + "                 order By byDate, dataSource  )";

            NativeQuery query = session.createSQLQuery(sql);
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                query.setParameter("dataSource", data.getDataSource());
            }
            query.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .addScalar("dataSource", StringType.INSTANCE)
                    .setParameter("sizeConfig", sizeConfig)
                    .addScalar("dataSourceName", StringType.INSTANCE)
                    .addScalar("direction", StringType.INSTANCE)
                    .addScalar("byDate", StringType.INSTANCE)
                    .addScalar("fileSize", LongType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(StatisticMediaDTO.class));
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            lstRes = (List<StatisticMediaDTO>) query.getResultList();

            NativeQuery queryCount = session.createNativeQuery(sqlCountRow);
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                queryCount.setParameter("dataSource", data.getDataSource());
            }
            queryCount.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate);
            count = ((BigInteger) queryCount
                    .getSingleResult()).longValue();

        } catch (EntityNotFoundException ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }

        return new PageImpl<>(lstRes, pageable, count);
    }

    public Page<StatisticMediaDTO> searchMediaFlRl(SearchMediaRequest data, Pageable pageable) throws ParseException {
        Session session = this.sessionFactory.openSession();
        List<StatisticMediaDTO> lstRes = null;
        long count = 0;

        try {
            if (data.getCurrentPage() > 0) {
                data.setCurrentPage(data.getCurrentPage() - 1);
            }
            pageable = (Pageable) PageRequest.of(data.getCurrentPage(), data.getRowsPerPage());
            String sql = "";
            String sqlCountRow = "";
            String condition = "";

            String startDate = data.getStartDate();
            String endDate = data.getEndDate();

            condition += " eventTime >= :startDate AND eventTime<= :endDate ";
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                condition += " AND dataSource = :dataSource ";

            }
            if (data.getMediaType() != null && data.getMediaType().size() > 0) {
                condition += " AND mediaTypeId IN :mediaTypeId ";
            }

            sql = " Select dataSource as dataSource ,dataSourceName as dataSourceName, partName as byDate, direction as direction, SUM(fileSize) as fileSize "
                    + " FROM vsat_media vm "
                    + " WHERE "
                    + condition
                    + " GROUP by dataSource,dataSourceName, partName, direction "
                    + " ORDER by dataSource, partName";

            sqlCountRow = "Select Count(tt.dataSource) "
                    + "  FROM "
                    + "  (Select  dataSource as dataSource , dataSourceName as dataSourceName, direction as direction, partName as byDate , SUM(fileSize) as fileSize "
                    + "       FROM vsat_media "
                    + "       WHERE "
                    + condition
                    + "  group by  dataSource, dataSourceName , partName, direction ) tt ";

            NativeQuery query = session.createSQLQuery(sql);
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                query.setParameter("dataSource", data.getDataSource());
            }
            if (data.getMediaType() != null && data.getMediaType().size() > 0) {
                query.setParameter("mediaTypeId", data.getMediaType());
            }

            query.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .addScalar("dataSource", StringType.INSTANCE)
                    .addScalar("dataSourceName", StringType.INSTANCE)
                    .addScalar("direction", StringType.INSTANCE)
                    .addScalar("byDate", StringType.INSTANCE)
                    .addScalar("fileSize", LongType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(StatisticMediaDTO.class));
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            lstRes = (List<StatisticMediaDTO>) query.getResultList();

            NativeQuery queryCount = session.createNativeQuery(sqlCountRow);
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                queryCount.setParameter("dataSource", data.getDataSource());
            }
            if (data.getMediaType() != null && data.getMediaType().size() > 0) {
                queryCount.setParameter("mediaTypeId", data.getMediaType());
            }
            queryCount.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate);
            count = ((BigInteger) queryCount
                    .getSingleResult()).longValue();

        } catch (EntityNotFoundException ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }

        return new PageImpl<>(lstRes, pageable, count);
    }

    public Page<StatisticMediaDTO> searchAmountFlRl(SearchRequest data, Pageable pageable) throws ParseException {
        Session session = this.sessionFactory.openSession();
        List<StatisticMediaDTO> lstRes = null;
        long count = 0;

        try {
            if (data.getCurrentPage() > 0) {
                data.setCurrentPage(data.getCurrentPage() - 1);
            }
            pageable = (Pageable) PageRequest.of(data.getCurrentPage(), data.getRowsPerPage());
            String condition = "";

            String startDate = data.getStartDate();
            String endDate = data.getEndDate();

            condition += " eventTime >= :startDate AND eventTime<= :endDate ";
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                condition += " AND dataSource = :dataSource ";
            }

            String sql = "Select dataSource as dataSource ,dataSourceName as dataSourceName, partName as byDate , direction as direction, count(objId) as fileSize "
                    + "	FROM vsat_ais "
                    + "	WHERE "
                    + condition
                    + "	group by  dataSource , dataSourceName, partName, direction   "
                    + " ORDER by partName, dataSource";

            String sqlCountRow = "Select Count(tta.dataSource) "
                    + " FROM "
                    + " (Select dataSource as dataSource ,dataSourceName as dataSourceName, direction as direction, partName as byDate , count(objId) as fileSize "
                    + "	FROM vsat_ais "
                    + "	WHERE "
                    + condition
                    + "	group by dataSource , dataSourceName, partName, direction  ) tta ";

            NativeQuery query = session.createSQLQuery(sql);
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                query.setParameter("dataSource", data.getDataSource());
            }
            query.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .addScalar("dataSource", StringType.INSTANCE)
                    .addScalar("dataSourceName", StringType.INSTANCE)
                    .addScalar("direction", StringType.INSTANCE)
                    .addScalar("byDate", StringType.INSTANCE)
                    .addScalar("fileSize", LongType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(StatisticMediaDTO.class));
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            lstRes = (List<StatisticMediaDTO>) query.getResultList();

            NativeQuery queryCount = session.createNativeQuery(sqlCountRow);
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                queryCount.setParameter("dataSource", data.getDataSource());
            }
            queryCount.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate);
            count = ((BigInteger) queryCount
                    .getSingleResult()).longValue();

        } catch (EntityNotFoundException ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }

        return new PageImpl<>(lstRes, pageable, count);
    }

    public Page<DatasourceDTO> statisticBySource(SearchRequest data, Pageable pageable) throws ParseException {
        Session session = this.sessionFactory.openSession();
        List<DatasourceDTO> lstRes = null;
        long count = 0;
        int sizeConfig = 30;
        try {
            if (data.getCurrentPage() > 0) {
                data.setCurrentPage(data.getCurrentPage() - 1);
            }
            pageable = (Pageable) PageRequest.of(data.getCurrentPage(), data.getRowsPerPage());
            String sql = "";
            String sqlCountRow = "";
            String condition = "";

            String startDate = data.getStartDate();
            String endDate = data.getEndDate();

            condition += " eventTime >= :startDate AND eventTime<= :endDate ";
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                condition += " AND dataSource = :dataSource ";
            }

            sql = "Select tt.dataSource as dataSource ,tt.dataSourceName as dataSourceName, tt.byDate as byDate , (tt.totalALL + tta.totalAISALL * :sizeConfig) as totalData , (tta.totalAISALL + tt.totalMediaTopic) as totalTopic  "
                    + " FROM "
                    + " (Select  dataSource as dataSource , dataSourceName as dataSourceName, partName as byDate , SUM(fileSize) as totalALL , count(dataSource) as totalMediaTopic "
                    + "	FROM vsat_media "
                    + "	where "
                    + condition
                    + "	group by  dataSource , dataSourceName, partName ) tt "
                    + " LEFT JOIN"
                    + " (Select  dataSource as dataSource , partName as byDate , count(objId) as totalAISALL "
                    + "	FROM vsat_ais "
                    + "	WHERE "
                    + condition
                    + "	group by  dataSource , partName  ) tta "
                    + "	ON (tt.dataSource = tta.dataSource AND  tt.byDate = tta.byDate) "
                    + " ORDER by byDate, dataSource";

            sqlCountRow = "Select Count(tt.dataSource) "
                    + " FROM "
                    + " (Select  dataSource as dataSource , dataSourceName as dataSourceName, partName as byDate , SUM(fileSize) as totalALL "
                    + "	FROM vsat_media "
                    + "	WHERE "
                    + condition
                    + "	group by  dataSource ,dataSourceName, partName ) tt "
                    + " left join "
                    + " (Select  dataSource as dataSource , partName as byDate , count(objId) as totalAISALL "
                    + "	FROM vsat_ais "
                    + "	WHERE "
                    + condition
                    + "	group by  dataSource , partName  ) tta "
                    + "	ON (tt.dataSource = tta.dataSource AND  tt.byDate = tta.byDate) ";

            NativeQuery query = session.createSQLQuery(sql);
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                query.setParameter("dataSource", data.getDataSource());
            }
            query.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .setParameter("sizeConfig", sizeConfig)
                    .addScalar("dataSource", StringType.INSTANCE)
                    .addScalar("dataSourceName", StringType.INSTANCE)
                    .addScalar("byDate", StringType.INSTANCE)
                    .addScalar("totalData", LongType.INSTANCE)
                    .addScalar("totalTopic", LongType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(DatasourceDTO.class));
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            lstRes = (List<DatasourceDTO>) query.getResultList();

            NativeQuery queryCount = session.createNativeQuery(sqlCountRow);
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                queryCount.setParameter("dataSource", data.getDataSource());
            }
            queryCount.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate);
            count = ((BigInteger) queryCount
                    .getSingleResult()).longValue();

        } catch (EntityNotFoundException ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }

        return new PageImpl<>(lstRes, pageable, count);
    }

    public Page<TypeServiceDTO> statisticByTypeService(SearchRequest data, Pageable pageable) throws ParseException {
        Session session = this.sessionFactory.openSession();
        List<TypeServiceDTO> lstRes = null;
        long count = 0;
        int sizeConfig = 30;
        try {
            if (data.getCurrentPage() > 0) {
                data.setCurrentPage(data.getCurrentPage() - 1);
            }
            pageable = (Pageable) PageRequest.of(data.getCurrentPage(), data.getRowsPerPage());
            String sql = "";
            String sqlCountRow = "";
            String condition = "";

            String startDate = data.getStartDate();
            String endDate = data.getEndDate();

            condition += " eventTime >= :startDate AND eventTime<= :endDate ";
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                condition += " AND dataSource = :dataSource ";
            }

            sql = "Select * FROM "
                    + "(Select tt.dataSource as dataSource , tt.mediaTypeName as mediaTypeName , tt.byDate as byDate , tt.totalALL as totalData,\n"
                    + "		tt.totalTopic as totalTopic\n"
                    + "FROM  \n"
                    + "	(Select  dataSource as dataSource , partName as byDate , mediaTypeName as mediaTypeName , "
                    + "	SUM(fileSize) as totalALL , count(mediaTypeName) as totalTopic	FROM vsat_media "
                    + "	where  "
                    + condition
                    + "	group by  dataSource , mediaTypeName, partName ) tt "
                    + " UNION ALL "
                    + " Select tta.dataSource as dataSource , 'AIS' as mediaTypeName , tta.byDate as byDate , (tta.totalAISALL * :sizeConfig) as totalData , \n"
                    + "		tta.totalAISALL as totalTopic\n"
                    + " FROM\n"
                    + " (Select dataSource as dataSource , partName as byDate , count(objId)  as totalAISALL "
                    + " FROM vsat_ais 	\n"
                    + " WHERE "
                    + condition
                    + "    group by  dataSource , partName)   tta "
                    + "    )\n"
                    + "ORDER by dataSource  ";

            sqlCountRow = "Select Count(dataSource) FROM "
                    + "(Select tt.dataSource as dataSource , tt.mediaTypeName as mediaTypeName , tt.byDate as byDate , tt.totalALL as totalData, "
                    + "	 tt.totalTopic as totalTopic"
                    + " FROM  "
                    + "	(Select  dataSource as dataSource , partName as byDate , mediaTypeName as mediaTypeName , "
                    + "	SUM(fileSize) as totalALL , count(mediaTypeName) as totalTopic	FROM vsat_media "
                    + "	where  "
                    + condition
                    + "	group by  dataSource , mediaTypeName, partName ) tt "
                    + " UNION ALL "
                    + " Select tta.dataSource as dataSource , 'AIS' as mediaTypeName , tta.byDate as byDate , (tta.totalAISALL) as totalData , \n"
                    + "		tta.totalAISALL as totalTopic "
                    + " FROM "
                    + " (Select dataSource as dataSource , partName as byDate , count(objId)  as totalAISALL "
                    + " FROM vsat_ais "
                    + " WHERE "
                    + condition
                    + "    group by  dataSource , partName)   tta "
                    + "   )";

            NativeQuery query = session.createSQLQuery(sql);
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                query.setParameter("dataSource", data.getDataSource());
            }
            query.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .setParameter("sizeConfig", sizeConfig)
                    .addScalar("dataSource", StringType.INSTANCE)
                    .addScalar("mediaTypeName", StringType.INSTANCE)
                    .addScalar("byDate", StringType.INSTANCE)
                    .addScalar("totalData", LongType.INSTANCE)
                    .addScalar("totalTopic", LongType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(TypeServiceDTO.class));
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            lstRes = (List<TypeServiceDTO>) query.getResultList();

            NativeQuery queryCount = session.createNativeQuery(sqlCountRow);
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                queryCount.setParameter("dataSource", data.getDataSource());
            }
            queryCount.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate);
            count = ((BigInteger) queryCount
                    .getSingleResult()).longValue();

        } catch (EntityNotFoundException ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }

        return new PageImpl<>(lstRes, pageable, count);
    }

    public List<TypeServiceDTO> statisticForTypeServicePieChart(SearchRequest data) {
        Session session = this.sessionFactory.openSession();
        List<TypeServiceDTO> lstRes = null;
        long count = 0;
        int sizeConfig = 30;
        try {
            String sql = "";
            String condition = "";

            String startDate = data.getStartDate();
            String endDate = data.getEndDate();

            condition += " eventTime >= :startDate AND eventTime<= :endDate ";
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                condition += " AND dataSource = :dataSource ";
            }

            sql = "Select * FROM \n"
                    + " (Select tt.dataSource as dataSource , tt.mediaTypeName as mediaTypeName , tt.totalALL as totalData, "
                    + " tt.totalTopic as totalTopic "
                    + " FROM "
                    + " (Select  dataSource as dataSource ,  mediaTypeName as mediaTypeName , "
                    + "         SUM(fileSize) as totalALL , count(mediaTypeName) as totalTopic	FROM vsat_media "
                    + " where  "
                    + condition
                    + "    group by  dataSource , mediaTypeName ) tt "
                    + " UNION ALL "
                    + " Select tta.dataSource as dataSource , 'AIS' as mediaTypeName , (tta.totalAISALL * :sizeConfig) as totalData , "
                    + "   tta.totalAISALL as totalTopic "
                    + " FROM "
                    + " (Select dataSource as dataSource , count(objId)  as totalAISALL "
                    + " FROM vsat_ais "
                    + " WHERE "
                    + condition
                    + " group by  dataSource ) tta "
                    + ") "
                    + "ORDER by dataSource  ";

            NativeQuery query = session.createSQLQuery(sql);
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                query.setParameter("dataSource", data.getDataSource());
            }
            query.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .setParameter("sizeConfig", sizeConfig)
                    .addScalar("dataSource", StringType.INSTANCE)
                    .addScalar("mediaTypeName", StringType.INSTANCE)
                    .addScalar("totalData", LongType.INSTANCE)
                    .addScalar("totalTopic", LongType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(TypeServiceDTO.class));

            lstRes = (List<TypeServiceDTO>) query.getResultList();
            return lstRes;
        } catch (EntityNotFoundException ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }

        return null;
    }

    public List<TypeServiceDTO> statisticForTypeServiceLineChart(SearchRequest data) {
        Session session = this.sessionFactory.openSession();
        List<TypeServiceDTO> lstRes = null;
        long count = 0;
        int sizeConfig = 30;
        try {
            String sql = "";
            String condition = "";

            String startDate = data.getStartDate();
            String endDate = data.getEndDate();

            condition += " eventTime >= :startDate AND eventTime<= :endDate ";
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                condition += " AND dataSource = :dataSource ";
            }

            sql = "Select * FROM "
                    + " (Select  tt.mediaTypeName as mediaTypeName , tt.byDate as byDate , tt.totalALL as totalData, "
                    + "		tt.totalTopic as totalTopic "
                    + " FROM "
                    + "	(Select  partName as byDate , mediaTypeName as mediaTypeName ,\n"
                    + "	SUM(fileSize) as totalALL , count(mediaTypeName) as totalTopic	FROM vsat_media 	\n"
                    + "	where  "
                    + condition
                    + "	group by  mediaTypeName, partName ) tt \n"
                    + " \n"
                    + "UNION ALL\n"
                    + "\n"
                    + "Select 'AIS' as mediaTypeName , tta.byDate as byDate , (tta.totalAISALL * :sizeConfig) as totalData , \n"
                    + "		tta.totalAISALL as totalTopic\n"
                    + "FROM\n"
                    + "(Select partName as byDate , count(objId)  as totalAISALL\n"
                    + "  FROM vsat_ais "
                    + "    WHERE "
                    + condition
                    + "    group by  partName, 'AIS')   tta    \n"
                    + "    )\n"
                    + "ORDER by byDate  ";

            NativeQuery query = session.createSQLQuery(sql);
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                query.setParameter("dataSource", data.getDataSource());
            }
            query.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .setParameter("sizeConfig", sizeConfig)
                    .addScalar("byDate", StringType.INSTANCE)
                    .addScalar("mediaTypeName", StringType.INSTANCE)
                    .addScalar("totalData", LongType.INSTANCE)
                    .addScalar("totalTopic", LongType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(TypeServiceDTO.class));

            lstRes = (List<TypeServiceDTO>) query.getResultList();
            return lstRes;
        } catch (EntityNotFoundException ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }

        return null;
    }

    public Page<StatisticVesselDTO> statisticByVessel(SearchRequest data, Pageable pageable) throws ParseException {
        Session session = this.sessionFactory.openSession();
        List<StatisticVesselDTO> lstRes = null;
        long count = 0;
        int sizeConfig = 30;
        try {
            if (data.getCurrentPage() > 0) {
                data.setCurrentPage(data.getCurrentPage() - 1);
            }
            pageable = (Pageable) PageRequest.of(data.getCurrentPage(), data.getRowsPerPage());
            String sql = "";
            String sqlCountRow = "";
            String condition = "";

            String startDate = data.getStartDate();
            String endDate = data.getEndDate();

            condition += " eventTime >= :startDate AND eventTime<= :endDate ";

            sql = "Select sourceIp as sourceIP , srcName as sourceName , destIp as destIP , destName as destName, mediaTypeName as mediaTypeName, "
                    + "	SUM(fileSize) as totalData , count(mediaTypeName) as totalAmount ,partName as byDate "
                    + " FROM vsat_media "
                    + " where  "
                    + condition
                    + " group by  mediaTypeName, partName ,sourceIP,sourceName,destIP,destName  "
                    + " ORDER by byDate, sourceIP, sourceName, destIP, destName, mediaTypeName";

            sqlCountRow = "Select Count(sourceIP) FROM "
                    + "	(Select sourceIp as sourceIP , srcName as sourceName , destIp as destIP , destName as destName, mediaTypeName as mediaTypeName, "
                    + "	SUM(fileSize) as totalData , count(mediaTypeName) as totalAmount ,partName as byDate "
                    + " FROM vsat_media "
                    + " where  "
                    + condition
                    + " group by  mediaTypeName, partName ,sourceIP,sourceName,destIP,destName )";

            NativeQuery query = session.createSQLQuery(sql);
            query.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .addScalar("sourceIP", StringType.INSTANCE)
                    .addScalar("sourceName", StringType.INSTANCE)
                    .addScalar("destIP", StringType.INSTANCE)
                    .addScalar("destName", StringType.INSTANCE)
                    .addScalar("mediaTypeName", StringType.INSTANCE)
                    .addScalar("byDate", StringType.INSTANCE)
                    .addScalar("totalData", LongType.INSTANCE)
                    .addScalar("totalAmount", LongType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(StatisticVesselDTO.class));

            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            lstRes = (List<StatisticVesselDTO>) query.getResultList();

            NativeQuery queryCount = session.createNativeQuery(sqlCountRow);
            queryCount.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate);
            count = ((BigInteger) queryCount
                    .getSingleResult()).longValue();

        } catch (EntityNotFoundException ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }

        return new PageImpl<>(lstRes, pageable, count);
    }

    public List<DatasourceDTO> statisticBySourcePieChart(SearchRequest data) {
        Session session = this.sessionFactory.openSession();
        List<DatasourceDTO> lstRes = null;
        int sizeConfig = 30;
        try {
            String sql = "";
            String condition = "";

            String startDate = data.getStartDate();
            String endDate = data.getEndDate();

            condition += " eventTime >= :startDate AND eventTime<= :endDate ";
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                condition += " AND dataSource = :dataSource ";
            }

            sql = "Select tt.dataSource as dataSource ,tt.dataSourceName as dataSourceName,  "
                    + "		SUM((tt.totalALL + tta.totalAISALL * :sizeConfig)) as totalData \n"
                    + "        FROM \n"
                    + "      (Select  dataSource as dataSource , dataSourceName as dataSourceName, partName as byDate , SUM(fileSize) as totalALL , count(dataSource) as totalMediaTopic \n"
                    + "       FROM vsat_media \n"
                    + "         where \n"
                    + condition
                    + "       	group by  dataSource ,dataSourceName, partName ) tt \n"
                    + "      LEFT JOIN\n"
                    + "        (Select  dataSource as dataSource , partName as byDate , count(objId) as totalAISALL \n"
                    + "       	FROM vsat_ais \n"
                    + "       	WHERE \n"
                    + condition
                    + "      	group by  dataSource , partName  ) tta \n"
                    + "    	ON (tt.dataSource = tta.dataSource AND  tt.byDate = tta.byDate)\n"
                    + "       GROUP by  dataSource, dataSourceName "
                    + "       ORDER by dataSource";

            NativeQuery query = session.createSQLQuery(sql);
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                query.setParameter("dataSource", data.getDataSource());
            }
            query.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .setParameter("sizeConfig", sizeConfig)
                    .addScalar("dataSource", StringType.INSTANCE)
                    .addScalar("dataSourceName", StringType.INSTANCE)
                    .addScalar("totalData", LongType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(DatasourceDTO.class));

            lstRes = (List<DatasourceDTO>) query.getResultList();
            return lstRes;
        } catch (EntityNotFoundException ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }

        return null;
    }

    public List<DatasourceDTO> statisticBySourceLineChart(SearchRequest data) {
        Session session = this.sessionFactory.openSession();
        List<DatasourceDTO> lstRes = null;
        int sizeConfig = 30;
        try {
            String sql = "";
            String condition = "";

            String startDate = data.getStartDate();
            String endDate = data.getEndDate();

            condition += " eventTime >= :startDate AND eventTime<= :endDate ";
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                condition += " AND dataSource = :dataSource ";
            }

            sql = "Select tt.dataSource as dataSource , tt.dataSourceName as dataSourceName , tt.byDate as byDate , (tt.totalALL + tta.totalAISALL * :sizeConfig) as totalData , tta.totalAISALL as totalTopic "
                    + " FROM "
                    + " (Select  dataSource as dataSource , dataSourceName as dataSourceName, partName as byDate , SUM(fileSize) as totalALL , count(dataSource) as totalMediaTopic "
                    + "	FROM vsat_media "
                    + "	where "
                    + condition
                    + "	group by  dataSource , dataSourceName, partName ) tt "
                    + " LEFT JOIN"
                    + " (Select  dataSource as dataSource , partName as byDate , count(objId) as totalAISALL "
                    + "	FROM vsat_ais "
                    + "	WHERE "
                    + condition
                    + "	group by  dataSource , partName  ) tta "
                    + "	ON (tt.dataSource = tta.dataSource AND  tt.byDate = tta.byDate) "
                    + " ORDER by dataSource";

            NativeQuery query = session.createSQLQuery(sql);
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                query.setParameter("dataSource", data.getDataSource());
            }
            query.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .setParameter("sizeConfig", sizeConfig)
                    .addScalar("dataSource", StringType.INSTANCE)
                    .addScalar("dataSourceName", StringType.INSTANCE)
                    .addScalar("byDate", StringType.INSTANCE)
                    .addScalar("totalData", LongType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(DatasourceDTO.class));

            lstRes = (List<DatasourceDTO>) query.getResultList();
            return lstRes;
        } catch (EntityNotFoundException ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }

        return null;
    }

    public List<StatisticPcapDTO> statisticByPcap(SearchRequest data) {
        Session session = this.sessionFactory.openSession();
        List<StatisticPcapDTO> lstRes = null;
        try {
            String sql = "";
            String condition = "";

            String startDate = data.getStartDate();
            String endDate = data.getEndDate();

            condition += " start_time >= :startDate AND start_time<= :endDate  ";
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                condition += " AND dataSource = :dataSource ";
            }

            sql = "  SELECT id AS uuid, parent_id AS parentUuid, name AS name, fullpath AS fullPath, "
                    + " byte_count AS byteCount, data_percent AS dataPercent, data_source AS dataSource, start_time AS startTime, to_time AS endTime "
                    + " FROM vsat_protocol "
                    + " WHERE "
                    + condition
                    + "	order by start_time   ";

            NativeQuery query = session.createSQLQuery(sql);
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                query.setParameter("dataSource", data.getDataSource());
            }
            query.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .addScalar("uuid", StringType.INSTANCE)
                    .addScalar("parentUuid", StringType.INSTANCE)
                    .addScalar("name", StringType.INSTANCE)
                    .addScalar("fullPath", StringType.INSTANCE)
                    .addScalar("byteCount", LongType.INSTANCE)
                    .addScalar("dataPercent", DoubleType.INSTANCE)
                    .addScalar("dataSource", IntegerType.INSTANCE)
                    .addScalar("startTime", StringType.INSTANCE)
                    .addScalar("endTime", StringType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(StatisticPcapDTO.class));

            lstRes = (List<StatisticPcapDTO>) query.getResultList();
            return lstRes;
        } catch (EntityNotFoundException ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }

        return null;
    }

    public List<StatisticMediaDTO> statisticByMediaChartist(SearchMediaRequest data) {
        Session session = this.sessionFactory.openSession();
        List<StatisticMediaDTO> lstRes = null;
        try {

            String sql = "";
            String condition = "";

            String startDate = data.getStartDate();
            String endDate = data.getEndDate();

            condition += " eventTime >= :startDate AND eventTime<= :endDate ";
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                condition += " AND dataSource = :dataSource ";

            }
            if (data.getMediaType() != null && data.getMediaType().size() > 0) {
                condition += " AND mediaTypeId IN :mediaTypeId ";
            }

            sql = " Select dataSource as dataSource ,dataSourceName as dataSourceName, partName as byDate, direction as direction, SUM(fileSize) as fileSize "
                    + " FROM vsat_media vm "
                    + " WHERE "
                    + condition
                    + " GROUP by dataSource,dataSourceName, partName, direction "
                    + " ORDER by dataSource, partName";

            NativeQuery query = session.createSQLQuery(sql);
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                query.setParameter("dataSource", data.getDataSource());
            }
            if (data.getMediaType() != null && data.getMediaType().size() > 0) {
                query.setParameter("mediaTypeId", data.getMediaType());
            }

            query.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .addScalar("dataSource", StringType.INSTANCE)
                    .addScalar("dataSourceName", StringType.INSTANCE)
                    .addScalar("direction", StringType.INSTANCE)
                    .addScalar("byDate", StringType.INSTANCE)
                    .addScalar("fileSize", LongType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(StatisticMediaDTO.class));

            lstRes = (List<StatisticMediaDTO>) query.getResultList();
            return lstRes;
        } catch (EntityNotFoundException ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }

        return null;
    }

    public List<StatisticMediaDTO> statisticByAmountChartist(SearchRequest data) {
        Session session = this.sessionFactory.openSession();
        List<StatisticMediaDTO> lstRes = null;

        try {
            String condition = "";

            String startDate = data.getStartDate();
            String endDate = data.getEndDate();

            condition += " eventTime >= :startDate AND eventTime<= :endDate ";
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                condition += " AND dataSource = :dataSource ";
            }

            String sql = "Select dataSource as dataSource ,dataSourceName as dataSourceName, partName as byDate , direction as direction, count(objId) as fileSize "
                    + "	FROM vsat_ais "
                    + "	WHERE "
                    + condition
                    + "	group by  dataSource , dataSourceName, partName, direction   "
                    + " ORDER by dataSource";

            NativeQuery query = session.createSQLQuery(sql);
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                query.setParameter("dataSource", data.getDataSource());
            }
            query.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .addScalar("dataSource", StringType.INSTANCE)
                    .addScalar("dataSourceName", StringType.INSTANCE)
                    .addScalar("direction", StringType.INSTANCE)
                    .addScalar("byDate", StringType.INSTANCE)
                    .addScalar("fileSize", LongType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(StatisticMediaDTO.class));

            lstRes = (List<StatisticMediaDTO>) query.getResultList();

            return lstRes;
        } catch (EntityNotFoundException ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }

        return null;
    }

    public List<StatisticMediaDTO> statisticByFlRlChartist(SearchRequest data) {
        Session session = this.sessionFactory.openSession();
        List<StatisticMediaDTO> lstRes = null;
        int sizeConfig = 30;

        try {

            String sql = "";
            String condition = "";

            String startDate = data.getStartDate();
            String endDate = data.getEndDate();

            condition += " eventTime >= :startDate AND eventTime<= :endDate ";
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                condition += " AND dataSource = :dataSource ";
            }

            sql = "select dataSource,dataSourceName, direction, byDate, totalALL as fileSize FROM "
                    + "(Select  dataSource , dataSourceName ,direction , partName as byDate , SUM(fileSize) as totalALL "
                    + "				FROM vsat_media "
                    + "				where  "
                    + condition
                    + "	  			group by  dataSource ,dataSourceName, direction, partName "
                    + " UNION ALL "
                    + " Select  dataSource , dataSourceName ,direction , partName as byDate , count(objId) * :sizeConfig as totalALL "
                    + "                FROM vsat_ais "
                    + "                where   "
                    + condition
                    + "                 group by  dataSource ,dataSourceName, direction, partName  )"
                    + "                 order By byDate, dataSource  ";

            NativeQuery query = session.createSQLQuery(sql);
            if (!StringUtil.isNullOrEmpty(data.getDataSource())) {
                query.setParameter("dataSource", data.getDataSource());
            }
            query.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .addScalar("dataSource", StringType.INSTANCE)
                    .setParameter("sizeConfig", sizeConfig)
                    .addScalar("dataSourceName", StringType.INSTANCE)
                    .addScalar("direction", StringType.INSTANCE)
                    .addScalar("byDate", StringType.INSTANCE)
                    .addScalar("fileSize", LongType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(StatisticMediaDTO.class));

            lstRes = (List<StatisticMediaDTO>) query.getResultList();

            return lstRes;
        } catch (EntityNotFoundException ex) {
            session.getTransaction().rollback();
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }

        return null;
    }

}
