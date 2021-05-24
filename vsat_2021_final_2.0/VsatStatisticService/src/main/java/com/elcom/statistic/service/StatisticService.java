/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.statistic.service;

import com.elcom.statistic.model.dto.DatasourceDTO;
import com.elcom.statistic.model.dto.MediaFLRLDTO;
import com.elcom.statistic.model.dto.SearchMediaRequest;
import com.elcom.statistic.model.dto.SearchRequest;
import com.elcom.statistic.model.dto.StatisticMediaDTO;
import com.elcom.statistic.model.dto.StatisticPcapDTO;
import com.elcom.statistic.model.dto.StatisticVesselDTO;
import com.elcom.statistic.model.dto.TypeServiceDTO;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 *
 * @author Admin
 */
public interface StatisticService {

    Page<StatisticMediaDTO> searchFlrl(SearchRequest data);

    Page<StatisticMediaDTO> searchMediaFlRl(SearchMediaRequest data);

    Page<StatisticMediaDTO> searchAmountFlRl(SearchRequest data);

    Page<DatasourceDTO> statisticBySource(SearchRequest data);

    Page<TypeServiceDTO> statisticByTypeService(SearchRequest data);
    
    List<TypeServiceDTO> statisticForTypeServicePieChart(SearchRequest data);
    
    List<TypeServiceDTO> statisticForTypeServiceLineChart(SearchRequest data);
    
    Page<StatisticVesselDTO> statisticByVessel(SearchRequest data);

    List<DatasourceDTO> statisticBySourceLineChart(SearchRequest data);

    List<DatasourceDTO> statisticBySourcePieChart(SearchRequest data);

    List<StatisticPcapDTO> statisticByPcap(SearchRequest data);

    List<StatisticMediaDTO> statisticByMediaChartist(SearchMediaRequest data);

    List<StatisticMediaDTO> statisticByAmountChartist(SearchRequest data);

    List<StatisticMediaDTO> statisticByFlRlChartist(SearchRequest data);

}
