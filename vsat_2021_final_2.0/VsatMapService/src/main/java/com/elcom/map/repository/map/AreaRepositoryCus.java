package com.elcom.map.repository.map;

import com.elcom.gateway.message.MessageContent;
import com.elcom.map.model.area.Area;
import com.elcom.map.model.dto.request.map.AddAreaRequest;
import com.elcom.map.repository.BaseRepository;
import com.elcom.map.utils.StringUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Repository
public interface AreaRepositoryCus extends PagingAndSortingRepository<Area, String> {

}
