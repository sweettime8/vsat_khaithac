/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.statistic.service.impl;

import com.elcom.statistic.model.dto.DatasourceDTO;
import com.elcom.statistic.model.dto.MediaFLRLDTO;
import com.elcom.statistic.model.dto.SearchMediaRequest;
import com.elcom.statistic.model.dto.SearchRequest;
import com.elcom.statistic.model.dto.StatisticMediaDTO;
import com.elcom.statistic.model.dto.StatisticPcapDTO;
import com.elcom.statistic.model.dto.StatisticVesselDTO;
import com.elcom.statistic.model.dto.TypeServiceDTO;
import com.elcom.statistic.repository.MediaCustomRepository;
import com.elcom.statistic.repository.MediaRepository;
import com.elcom.statistic.service.StatisticService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class StatisticServiceImpl implements StatisticService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticServiceImpl.class);

    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    MediaCustomRepository mediaCustomRepository;

    @Override
    public Page<StatisticMediaDTO> searchFlrl(SearchRequest data) {
        try {
            return mediaCustomRepository.searchFlrl(data, PageRequest.of(data.getCurrentPage(), data.getRowsPerPage()));
        } catch (Exception e) {
            LOGGER.error("searchFlrl ==> error : ", e);
        }
        return null;
    }

    @Override
    public Page<StatisticMediaDTO> searchMediaFlRl(SearchMediaRequest data) {
        try {
            return mediaCustomRepository.searchMediaFlRl(data, PageRequest.of(data.getCurrentPage(), data.getRowsPerPage()));
        } catch (Exception e) {
            LOGGER.error("searchMediaFlRl ==> error : ", e);
        }
        return null;
    }

    @Override
    public Page<StatisticMediaDTO> searchAmountFlRl(SearchRequest data) {
        try {
            return mediaCustomRepository.searchAmountFlRl(data, PageRequest.of(data.getCurrentPage(), data.getRowsPerPage()));
        } catch (Exception e) {
            LOGGER.error("searchAmountFlRl ==> error : ", e);
        }
        return null;
    }

    @Override
    public Page<DatasourceDTO> statisticBySource(SearchRequest data) {
        try {
            return mediaCustomRepository.statisticBySource(data, PageRequest.of(data.getCurrentPage(), data.getRowsPerPage()));
        } catch (Exception e) {
            LOGGER.error("searchAmountFlRl ==> error : ", e);
        }
        return null;
    }

    @Override
    public Page<TypeServiceDTO> statisticByTypeService(SearchRequest data) {
        try {
            return mediaCustomRepository.statisticByTypeService(data, PageRequest.of(data.getCurrentPage(), data.getRowsPerPage()));
        } catch (Exception e) {
            LOGGER.error("searchAmountFlRl ==> error : ", e);
        }
        return null;
    }

    @Override
    public List<TypeServiceDTO> statisticForTypeServicePieChart(SearchRequest data) {
        try {
            return mediaCustomRepository.statisticForTypeServicePieChart(data);
        } catch (Exception e) {
            LOGGER.error("searchAmountFlRl ==> error : ", e);
        }
        return null;
    }

    @Override
    public List<TypeServiceDTO> statisticForTypeServiceLineChart(SearchRequest data) {
        try {
            return mediaCustomRepository.statisticForTypeServiceLineChart(data);
        } catch (Exception e) {
            LOGGER.error("searchAmountFlRl ==> error : ", e);
        }
        return null;
    }

    @Override
    public Page<StatisticVesselDTO> statisticByVessel(SearchRequest data) {
        try {
            return mediaCustomRepository.statisticByVessel(data, PageRequest.of(data.getCurrentPage(), data.getRowsPerPage()));
        } catch (Exception e) {
            LOGGER.error("statisticByVessel ==> error : ", e);
        }
        return null;
    }

    @Override
    public List<DatasourceDTO> statisticBySourceLineChart(SearchRequest data) {
        try {
            return mediaCustomRepository.statisticBySourceLineChart(data);
        } catch (Exception e) {
            LOGGER.error("statisticBySourceLineChart ==> error : ", e);
        }
        return null;
    }

    @Override
    public List<DatasourceDTO> statisticBySourcePieChart(SearchRequest data) {
        try {
            return mediaCustomRepository.statisticBySourcePieChart(data);
        } catch (Exception e) {
            LOGGER.error("statisticBySourcePieChart ==> error : ", e);
        }
        return null;
    }

    @Override
    public List<StatisticPcapDTO> statisticByPcap(SearchRequest data) {
        try {
            return mediaCustomRepository.statisticByPcap(data);
        } catch (Exception e) {
            LOGGER.error("statisticByPcap ==> error : ", e);
        }
        return null;
    }

    @Override
    public List<StatisticMediaDTO> statisticByMediaChartist(SearchMediaRequest data) {
        try {
            return mediaCustomRepository.statisticByMediaChartist(data);
        } catch (Exception e) {
            LOGGER.error("statisticByMediaChartist ==> error : ", e);
        }
        return null;
    }

    @Override
    public List<StatisticMediaDTO> statisticByAmountChartist(SearchRequest data) {
        try {
            return mediaCustomRepository.statisticByAmountChartist(data);
        } catch (Exception e) {
            LOGGER.error("statisticBySourcePieChart ==> error : ", e);
        }
        return null;
    }

    @Override
    public List<StatisticMediaDTO> statisticByFlRlChartist(SearchRequest data) {
        try {
            return mediaCustomRepository.statisticByFlRlChartist(data);
        } catch (Exception e) {
            LOGGER.error("statisticByFlRlLineChart ==> error : ", e);
        }
        return null;
    }


}
