package com.elcom.map.service.impl;

import com.elcom.gateway.message.MessageContent;
import com.elcom.map.model.area.Area;

import com.elcom.map.model.dto.request.map.AddAreaRequest;

import com.elcom.map.model.dto.request.map.DeleteAreaRequest;
import com.elcom.map.model.dto.request.map.GetListAreaRequest;
import com.elcom.map.repository.map.AreaRepository;
import com.elcom.map.repository.map.AreaRepositoryCus;
import com.elcom.map.repository.map.MapRepository;
import com.elcom.map.repository.vessel.VesselGroupRepository;
import com.elcom.map.service.MapService;
import com.elcom.map.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@SuppressWarnings("unchecked")
public class MapServiceImpl implements MapService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MapServiceImpl.class);

    @Autowired
    private AreaRepositoryCus areaRepositoryCus;
    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private MapRepository mapRepository;
    
    @Autowired
    private VesselGroupRepository vesselGroupRepository;

    @Override
    public MessageContent saveArea(List<AddAreaRequest> listAddAreaRequest){
        MessageContent messageContent=null;
        try {

            List<Area> lstArea=new ArrayList<>();
            String message="";
            for (AddAreaRequest areaRequest:listAddAreaRequest) {
                    if(areaRequest.getId()!=null){
                        Area area= areaRepository.getAeraById(areaRequest.getId());
                        if(area==null){
                            message += "Vùng:  "+areaRequest.getName()+ " không tồn tại để sửa ";
                        }
                        area.setType(areaRequest.getType());
                        area.setName(areaRequest.getName());
                        area.setValue(areaRequest.getValue());
                        area.setUpdatedTime(new Timestamp(new Date().getTime()));
                        areaRepositoryCus.save(area);
                        lstArea.add(area);

                    }
                    else {
                        Boolean bExitsArea = areaRepository.checkExitsAeraByName(areaRequest.getName());
                        if (bExitsArea == false) {
                            Area area = new Area();
                            area.setType(areaRequest.getType());
                            area.setName(areaRequest.getName());
                            area.setValue(areaRequest.getValue());
                            area.setCreatedTime(new Timestamp(new Date().getTime()));
                            areaRepositoryCus.save(area);
                            lstArea.add(area);
                        } else {
                            message += "Vùng: " + areaRequest.getName() + ", ";
                        }
                    }
            }

            messageContent=new MessageContent();
            messageContent.setStatus(HttpStatus.OK.value());
            messageContent.setData(lstArea);
            if(StringUtil.isNullOrEmpty(message)==false){
                message+="Đã tồn tại.";
                messageContent.setMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
        }
        return messageContent;
    }

    @Override
    public MessageContent getListArea(GetListAreaRequest data){
        MessageContent messageContent=null;
        try{
            messageContent= mapRepository.getListArea(data);
        }
        catch (Exception e){
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
            messageContent=new MessageContent();
            messageContent.setStatus(HttpStatus.BAD_REQUEST.value());
            messageContent.setMessage(e.getMessage());
        }
        return messageContent;
    }
    public MessageContent delArea(DeleteAreaRequest data){
        MessageContent messageContent=null;
        try{
            messageContent= mapRepository.delArea(data);
        }
        catch (Exception e){
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
            messageContent=new MessageContent();
            messageContent.setStatus(HttpStatus.BAD_REQUEST.value());
            messageContent.setMessage(e.getMessage());
        }
        return messageContent;
    }
}
