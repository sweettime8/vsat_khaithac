/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.contact.repository.contact;

import com.elcom.contact.model.contact.VesselAltInfo;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author Admin
 */
public interface VesselAltInfoRepository extends PagingAndSortingRepository<VesselAltInfo, Long>  {

    public VesselAltInfo findByMmsi(long mmsi);
    
}
