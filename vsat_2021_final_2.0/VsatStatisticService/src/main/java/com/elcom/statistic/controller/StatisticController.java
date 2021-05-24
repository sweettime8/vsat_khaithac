/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.statistic.controller;

import com.elcom.gateway.message.MessageContent;
import com.elcom.gateway.message.ResponseMessage;
import com.elcom.statistic.model.dto.AuthorizationResponseDTO;
import com.elcom.statistic.model.dto.DatasourceDTO;
import com.elcom.statistic.model.dto.MediaFLRLDTO;
import com.elcom.statistic.model.dto.SearchMediaRequest;
import com.elcom.statistic.model.dto.SearchRequest;
import com.elcom.statistic.model.dto.StatisticMediaDTO;
import com.elcom.statistic.model.dto.StatisticPcapDTO;
import com.elcom.statistic.model.dto.StatisticVesselDTO;
import com.elcom.statistic.model.dto.TypeServiceDTO;
import com.elcom.statistic.service.StatisticService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Admin
 */
@Controller
public class StatisticController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticController.class);

    @Autowired
    StatisticService statisticService;

    public ResponseMessage searchFlrl(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        try {
            AuthorizationResponseDTO dto = authenToken(headerParam);
            if (dto == null) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                                "Bạn chưa đăng nhập"));
            }

            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            SearchRequest data = mapper.convertValue(bodyParam, SearchRequest.class);
            Page<StatisticMediaDTO> result = statisticService.searchFlrl(data);

            if (result != null || !result.hasContent()) {
                response = new ResponseMessage(new MessageContent(result.getContent(), result.getTotalElements()));
            } else {
                response = new ResponseMessage(HttpStatus.OK.value(), "Data not found",
                        new MessageContent(HttpStatus.OK.value(), "Data not found", null));
            }

        } catch (Exception e) {
            LOGGER.error("StatisticController.searchFlrl ==> error : ", e);
        }

        return response;
    }

    public ResponseMessage statisticByFlRlChartist(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        try {
            AuthorizationResponseDTO dto = authenToken(headerParam);
            if (dto == null) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                                "Bạn chưa đăng nhập"));
            }

            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            SearchRequest data = mapper.convertValue(bodyParam, SearchRequest.class);
            List<StatisticMediaDTO> result = statisticService.statisticByFlRlChartist(data);

            if (result != null) {
                response = new ResponseMessage(new MessageContent(result));
            } else {
                response = new ResponseMessage(HttpStatus.OK.value(), "Data not found",
                        new MessageContent(HttpStatus.OK.value(), "Data not found", null));
            }

        } catch (Exception e) {
            LOGGER.error("StatisticController.searchFlrl ==> error : ", e);
        }

        return response;
    }


    public ResponseMessage searchMedia(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        try {
            AuthorizationResponseDTO dto = authenToken(headerParam);
            if (dto == null) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                                "Bạn chưa đăng nhập"));
            }

            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            SearchMediaRequest data = mapper.convertValue(bodyParam, SearchMediaRequest.class);
            Page<StatisticMediaDTO> result = statisticService.searchMediaFlRl(data);

            if (result != null || !result.hasContent()) {
                response = new ResponseMessage(new MessageContent(result.getContent(), result.getTotalElements()));
            } else {
                response = new ResponseMessage(HttpStatus.OK.value(), "Data not found",
                        new MessageContent(HttpStatus.OK.value(), "Data not found", null));
            }

        } catch (Exception e) {
            LOGGER.error("StatisticController.searchFlrl ==> error : ", e);
        }

        return response;
    }

    public ResponseMessage statisticByMediaChartist(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        try {
            AuthorizationResponseDTO dto = authenToken(headerParam);
            if (dto == null) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                                "Bạn chưa đăng nhập"));
            }

            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            SearchMediaRequest data = mapper.convertValue(bodyParam, SearchMediaRequest.class);
            List<StatisticMediaDTO> result = statisticService.statisticByMediaChartist(data);

            if (result != null) {
                response = new ResponseMessage(new MessageContent(result));
            } else {
                response = new ResponseMessage(HttpStatus.OK.value(), "Data not found",
                        new MessageContent(HttpStatus.OK.value(), "Data not found", null));
            }

        } catch (Exception e) {
            LOGGER.error("StatisticController.searchFlrl ==> error : ", e);
        }

        return response;
    }

    public ResponseMessage searchAmount(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        try {
            AuthorizationResponseDTO dto = authenToken(headerParam);
            if (dto == null) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                                "Bạn chưa đăng nhập"));
            }

            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            SearchRequest data = mapper.convertValue(bodyParam, SearchRequest.class);
            Page<StatisticMediaDTO> result = statisticService.searchAmountFlRl(data);

            if (result != null || !result.hasContent()) {
                response = new ResponseMessage(new MessageContent(result.getContent(), result.getTotalElements()));
            } else {
                response = new ResponseMessage(HttpStatus.OK.value(), "Data not found",
                        new MessageContent(HttpStatus.OK.value(), "Data not found", null));
            }

        } catch (Exception e) {
            LOGGER.error("StatisticController.searchFlrl ==> error : ", e);
        }

        return response;
    }

    public ResponseMessage statisticByAmountChartist(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        try {
            AuthorizationResponseDTO dto = authenToken(headerParam);
            if (dto == null) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                                "Bạn chưa đăng nhập"));
            }

            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            SearchRequest data = mapper.convertValue(bodyParam, SearchRequest.class);
            List<StatisticMediaDTO> result = statisticService.statisticByAmountChartist(data);

            if (result != null) {
                response = new ResponseMessage(new MessageContent(result));
            } else {
                response = new ResponseMessage(HttpStatus.OK.value(), "Data not found",
                        new MessageContent(HttpStatus.OK.value(), "Data not found", null));
            }

        } catch (Exception e) {
            LOGGER.error("StatisticController.searchFlrl ==> error : ", e);
        }

        return response;
    }

    public ResponseMessage statisticBySource(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        try {
            AuthorizationResponseDTO dto = authenToken(headerParam);
            if (dto == null) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                                "Bạn chưa đăng nhập"));
            }

            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            SearchRequest data = mapper.convertValue(bodyParam, SearchRequest.class);
            Page<DatasourceDTO> result = statisticService.statisticBySource(data);

            if (result != null || !result.hasContent()) {
                response = new ResponseMessage(new MessageContent(result.getContent(), result.getTotalElements()));
            } else {
                response = new ResponseMessage(HttpStatus.OK.value(), "Data not found",
                        new MessageContent(HttpStatus.OK.value(), "Data not found", null));
            }

        } catch (Exception e) {
            LOGGER.error("StatisticController.searchFlrl ==> error : ", e);
        }

        return response;
    }

    public ResponseMessage statisticBySourcePieChart(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        try {
            AuthorizationResponseDTO dto = authenToken(headerParam);
            if (dto == null) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                                "Bạn chưa đăng nhập"));
            }

            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            SearchRequest data = mapper.convertValue(bodyParam, SearchRequest.class);
            List<DatasourceDTO> result = statisticService.statisticBySourcePieChart(data);

            if (result != null) {
                response = new ResponseMessage(new MessageContent(result));
            } else {
                response = new ResponseMessage(HttpStatus.OK.value(), "Data not found",
                        new MessageContent(HttpStatus.OK.value(), "Data not found", null));
            }

        } catch (Exception e) {
            LOGGER.error("statisticBySourcePieChart ==> error : ", e);
        }

        return response;
    }

    public ResponseMessage statisticBySourceLineChart(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        try {
            AuthorizationResponseDTO dto = authenToken(headerParam);
            if (dto == null) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                                "Bạn chưa đăng nhập"));
            }

            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            SearchRequest data = mapper.convertValue(bodyParam, SearchRequest.class);
            List<DatasourceDTO> result = statisticService.statisticBySourceLineChart(data);

            if (result != null) {
                response = new ResponseMessage(new MessageContent(result));
            } else {
                response = new ResponseMessage(HttpStatus.OK.value(), "Data not found",
                        new MessageContent(HttpStatus.OK.value(), "Data not found", null));
            }

        } catch (Exception e) {
            LOGGER.error("statisticBySourceLineChart ==> error : ", e);
        }

        return response;
    }

    public ResponseMessage statisticByTypeService(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        try {
            AuthorizationResponseDTO dto = authenToken(headerParam);
            if (dto == null) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                                "Bạn chưa đăng nhập"));
            }

            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            SearchRequest data = mapper.convertValue(bodyParam, SearchRequest.class);
            Page<TypeServiceDTO> result = statisticService.statisticByTypeService(data);

            if (result != null || !result.hasContent()) {
                response = new ResponseMessage(new MessageContent(result.getContent(), result.getTotalElements()));
            } else {
                response = new ResponseMessage(HttpStatus.OK.value(), "Data not found",
                        new MessageContent(HttpStatus.OK.value(), "Data not found", null));
            }

        } catch (Exception e) {
            LOGGER.error("statisticByTypeService ==> error : ", e);
        }

        return response;
    }

    public ResponseMessage statisticForTypeServicePieChart(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        try {
            AuthorizationResponseDTO dto = authenToken(headerParam);
            if (dto == null) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                                "Bạn chưa đăng nhập"));
            }

            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            SearchRequest data = mapper.convertValue(bodyParam, SearchRequest.class);
            List<TypeServiceDTO> result = statisticService.statisticForTypeServicePieChart(data);

            if (result != null) {
                response = new ResponseMessage(new MessageContent(result));
            } else {
                response = new ResponseMessage(HttpStatus.OK.value(), "Data not found",
                        new MessageContent(HttpStatus.OK.value(), "Data not found", null));
            }

        } catch (Exception e) {
            LOGGER.error("statisticForTypeServicePieChart ==> error : ", e);
        }

        return response;
    }

    public ResponseMessage statisticForTypeServiceLineChart(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        try {
            AuthorizationResponseDTO dto = authenToken(headerParam);
            if (dto == null) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                                "Bạn chưa đăng nhập"));
            }

            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            SearchRequest data = mapper.convertValue(bodyParam, SearchRequest.class);
            List<TypeServiceDTO> result = statisticService.statisticForTypeServiceLineChart(data);

            if (result != null) {
                response = new ResponseMessage(new MessageContent(result));
            } else {
                response = new ResponseMessage(HttpStatus.OK.value(), "Data not found",
                        new MessageContent(HttpStatus.OK.value(), "Data not found", null));
            }

        } catch (Exception e) {
            LOGGER.error("StatisticController.statisticByTypeService ==> error : ", e);
        }

        return response;
    }

    public ResponseMessage statisticByPcap(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        try {
            AuthorizationResponseDTO dto = authenToken(headerParam);
            if (dto == null) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                                "Bạn chưa đăng nhập"));
            }

            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            SearchRequest data = mapper.convertValue(bodyParam, SearchRequest.class);
            List<StatisticPcapDTO> result = statisticService.statisticByPcap(data);
            if (result != null) {
                response = new ResponseMessage(new MessageContent(result));
            } else {
                response = new ResponseMessage(HttpStatus.OK.value(), "Data not found",
                        new MessageContent(HttpStatus.OK.value(), "Data not found", null));
            }
        } catch (Exception e) {
            LOGGER.error("StatisticController.statisticByPcap ==> error : ", e);
        }

        return response;
    }

    public ResponseMessage statisticByVessel(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        try {
            AuthorizationResponseDTO dto = authenToken(headerParam);
            if (dto == null) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                                "Bạn chưa đăng nhập"));
            }

            ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            SearchRequest data = mapper.convertValue(bodyParam, SearchRequest.class);
            Page<StatisticVesselDTO> result = statisticService.statisticByVessel(data);

            if (result != null || !result.hasContent()) {
                response = new ResponseMessage(new MessageContent(result.getContent(), result.getTotalElements()));
            } else {
                response = new ResponseMessage(HttpStatus.OK.value(), "Data not found",
                        new MessageContent(HttpStatus.OK.value(), "Data not found", null));
            }

        } catch (Exception e) {
            LOGGER.error("StatisticController.statisticByTypeService ==> error : ", e);
        }

        return response;
    }
}
