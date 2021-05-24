package com.elcom.media.service.impl;


import com.elcom.media.model.Source;
import com.elcom.media.repository.source.SourceRepository;
import com.elcom.media.service.SourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SourceServiceImpl implements SourceService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SourceServiceImpl.class);

    @Autowired
    private SourceRepository sourceRepository;
    
    @Override
    public List<Source> getAll() {
        List<Source> lstSource=null;
        try{
            lstSource = sourceRepository.getAll();
        }
        catch (Exception ex){
            LOGGER.error("get all source ==> error : ", ex);
        }
        return lstSource;
    }
}
