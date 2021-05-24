/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.ais.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Admin
 */
@Component
public class ApplicationConfig {

    @Value("${app.master}") 
    public static boolean APP_MASTER;
    
    @Value("${limit.ais.msg.search}") 
    public static int LIMIT_AIS_MSG_SEARCH;
    
    @Value("${limit.ais.msg.for.detail.vessel}") 
    public static int LIMIT_AIS_MSG_FOR_DETAIL_VESSEL;

    @Autowired
    public ApplicationConfig(@Value("${app.master}") boolean isAppMaster,
                             @Value("${limit.ais.msg.search}") int limitAisMsgSearch,
                             @Value("${limit.ais.msg.for.detail.vessel}") int limitAisMsgForDetailVessel) {
        APP_MASTER = isAppMaster;
        LIMIT_AIS_MSG_SEARCH = limitAisMsgSearch;
        LIMIT_AIS_MSG_FOR_DETAIL_VESSEL = limitAisMsgForDetailVessel;
    }
}
