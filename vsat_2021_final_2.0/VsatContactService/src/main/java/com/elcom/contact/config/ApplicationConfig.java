/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.contact.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Admin
 */
@Component
public class ApplicationConfig {

    //Link quen mat khau
    @Value("${frontend.forgotpass.url}")
    public static String FRONTEND_FORGOTPASS_URL;

    //Thoi han link
    @Value("${forgotpass.expired.time}")
    public static int FORGOTPASS_EXPIRED_TIME;
    
    @Value("${app.master}") 
    public static boolean APP_MASTER;
    
    @Value("${cron.progress.blacklist}") 
    public static boolean BLACK_LIST_DATA_ENABLE;

    @Autowired
    public ApplicationConfig(@Value("${frontend.forgotpass.url}") String forgotPassUrl,
            @Value("${forgotpass.expired.time}") int forgotPassExpiredTime) {
        FRONTEND_FORGOTPASS_URL = forgotPassUrl;
        FORGOTPASS_EXPIRED_TIME = forgotPassExpiredTime;
    }
}
