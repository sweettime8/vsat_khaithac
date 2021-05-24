/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.contact.model.dto.request.contactObject;

import com.elcom.contact.model.contact.UfoIP;
import com.elcom.contact.model.contactObject.ObjectUnInfoDto;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ObjectUndefineDtoFull {
    private ObjectUnInfoDto objectUnInfoDto;
    private List<UfoIP> ips;

    public ObjectUnInfoDto getObjectUnInfoDto() {
        return objectUnInfoDto;
    }

    public void setObjectUnInfoDto(ObjectUnInfoDto objectUnInfoDto) {
        this.objectUnInfoDto = objectUnInfoDto;
    }

    public List<UfoIP> getIps() {
        return ips;
    }

    public void setIps(List<UfoIP> ips) {
        this.ips = ips;
    }
    
    
    
}
