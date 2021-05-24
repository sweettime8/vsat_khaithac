/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.contact.model.dto.request.contacLand;

import com.elcom.contact.model.contact.ContactIP;
import com.elcom.contact.model.contact.Phone;
import com.elcom.contact.model.contactLand.HeadQuarterDto;
import java.util.List;

/**
 *
 * @author Admin
 */
public class HeadQuarterDtoFull {
    private HeadQuarterDto headQuarterDto;
    private List<ContactIP> ips;
    private List<Phone> phones;

    public HeadQuarterDto getHeadQuarterDto() {
        return headQuarterDto;
    }

    public void setHeadQuarterDto(HeadQuarterDto headQuarterDto) {
        this.headQuarterDto = headQuarterDto;
    }

    public List<ContactIP> getIps() {
        return ips;
    }

    public void setIps(List<ContactIP> ips) {
        this.ips = ips;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }
    
    
}
