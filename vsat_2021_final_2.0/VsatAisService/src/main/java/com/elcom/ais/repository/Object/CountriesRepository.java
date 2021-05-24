/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.ais.repository.Object;

import com.elcom.ais.model.vessel.Countries;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ducnh
 */
@Repository
public interface CountriesRepository extends CrudRepository<Countries, Long> {
    
}
