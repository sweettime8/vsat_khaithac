package com.elcom.media.service.impl;



import com.elcom.media.repository.monitor.MonitorRepository;
import com.elcom.media.service.MonitorService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@SuppressWarnings("unchecked")
public class MonitorServiceImpl implements MonitorService {
    @Autowired
    private MonitorRepository monitorRepository;

//    public List<ComputerMonitor> getMonitorCenter(HttpServletRequest request,List<String> ip){
//        List<ComputerMonitor> listComputerMonitor=monitorRepository.getMonitorCenter(ip);
//        return listComputerMonitor;
//    }
//    public List<ComputerMonitor> getMonitorReceiver(HttpServletRequest request,List<String> ip){
//        List<ComputerMonitor> listComputerMonitor=monitorRepository.getMonitorReceiver(ip);
//        return listComputerMonitor;
//    }
}
