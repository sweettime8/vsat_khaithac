package com.elcom.statistic.messaging.rabbitmq;

import com.elcom.statistic.controller.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.elcom.gateway.message.RequestMessage;
import com.elcom.gateway.message.ResponseMessage;
import com.elcom.statistic.exception.ValidationException;
import io.netty.util.internal.StringUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Admin
 */
public class RpcServer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);
    
    @Autowired
    private StatisticController statisticController;
   
    @RabbitListener(queues = "${statistic.rpc.queue}")
    public String processService(String json) throws ValidationException {
        try {
            LOGGER.info(" [-->] Server received request for " + json);
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            RequestMessage request = mapper.readValue(json, RequestMessage.class);
            
            //Process here
            ResponseMessage response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
            if (request != null) {
                String requestPath = request.getRequestPath();
                String urlParam = request.getUrlParam();
                String pathParam = request.getPathParam();
                Map<String, Object> bodyParam = request.getBodyParam();
                Map<String, String> headerParam = request.getHeaderParam();
                //GatewayDebugUtil.debug(requestPath, urlParam, pathParam, bodyParam, headerParam);

                switch (request.getRequestMethod()) {
                    case "GET":

                        break;
                        
                    case "POST":     
                        //FLRL - Thống kê dung lượng theo chiều dữ liệu
                        if("/v1.0/statistic/flrl".equalsIgnoreCase(requestPath)) 
                            response = statisticController.searchFlrl(bodyParam, headerParam);
                        if("/v1.0/statistic/flRlChartist".equalsIgnoreCase(requestPath)) 
                            response = statisticController.statisticByFlRlChartist(bodyParam, headerParam);
                        //MEDIA - Thống kê dung lượng media theo chiều
                        if("/v1.0/statistic/media".equalsIgnoreCase(requestPath)) 
                            response = statisticController.searchMedia(bodyParam, headerParam);                        
                        if("/v1.0/statistic/mediaChartist".equalsIgnoreCase(requestPath)) 
                            response = statisticController.statisticByMediaChartist(bodyParam, headerParam);    
                        
                        // ======================== AMOUNT - Thống kê số bản tin vị trí theo chiều ========================
                        if("/v1.0/statistic/amount".equalsIgnoreCase(requestPath)) 
                            response = statisticController.searchAmount(bodyParam, headerParam); 
                        if("/v1.0/statistic/amountChartist".equalsIgnoreCase(requestPath)) 
                            response = statisticController.statisticByAmountChartist(bodyParam, headerParam);   
                        
                        // ======================== TOTAL - Thống kê dung lượng theo nguồn ========================
                        if("/v1.0/statistic/total".equalsIgnoreCase(requestPath)) 
                            response = statisticController.statisticBySource(bodyParam, headerParam);   
                        if("/v1.0/statistic/totalPieChart".equalsIgnoreCase(requestPath)) 
                            response = statisticController.statisticBySourcePieChart(bodyParam, headerParam);  
                        if("/v1.0/statistic/totalLineChart".equalsIgnoreCase(requestPath)) 
                            response = statisticController.statisticBySourceLineChart(bodyParam, headerParam);
                        
                        //typeservice - Thống kê theo loại hình dịch vụ
                        if("/v1.0/statistic/typeservice".equalsIgnoreCase(requestPath)) 
                            response = statisticController.statisticByTypeService(bodyParam, headerParam);  
                        if("/v1.0/statistic/typeservicePieChart".equalsIgnoreCase(requestPath)) 
                            response = statisticController.statisticForTypeServicePieChart(bodyParam, headerParam);
                        if("/v1.0/statistic/typeserviceLineChart".equalsIgnoreCase(requestPath)) 
                            response = statisticController.statisticForTypeServiceLineChart(bodyParam, headerParam);   
                        // VESSEL - Thống kê theo tàu
                        if("/v1.0/statistic/vessel".equalsIgnoreCase(requestPath))
                            response = statisticController.statisticByVessel(bodyParam, headerParam);    
                        // PCAP - Thống kê theo loại giao thức
                        if("/v1.0/statistic/pcap".equalsIgnoreCase(requestPath))
                            response = statisticController.statisticByPcap(bodyParam, headerParam);   
                        break;
                        
                    case "PUT":                      
                        break;
                        
                    case "PATCH":
                        break;
                        
                    case "DELETE":
                        break;
                        
                    default:
                        break;
                }
            }
            LOGGER.info(" [<--] Server returned " + response.toJsonString());
            return response.toJsonString();
        } catch (Exception ex) {
            LOGGER.error("Error to processService >>> " + ex.toString());
            ex.printStackTrace();
        }
        return null;
    }
}
