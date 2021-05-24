/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.contact.repository.contact;

import com.elcom.contact.model.contact.AisInfo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public interface AisInfoRepository extends PagingAndSortingRepository<AisInfo, Long>  {

    public AisInfo findByMmsi(long mmsi);
   
}
