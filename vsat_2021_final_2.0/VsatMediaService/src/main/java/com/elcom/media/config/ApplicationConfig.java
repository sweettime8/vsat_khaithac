/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.media.config;

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
    
    @Value("${rootFolderFilePathInternal}") 
    public static String ROOT_FOLDER_FILE_PATH_INTERNAL;
    
    @Value("${mediaLinkRootApi}") 
    public static String MEDIA_LINK_ROOT_API;
    
    @Autowired
    public ApplicationConfig(@Value("${app.master}") boolean isAppMaster,
                             @Value("${rootFolderFilePathInternal}") String rootFolderFilePathInternal,
                             @Value("${mediaLinkRootApi}") String mediaLinkRootApi) {
        APP_MASTER = isAppMaster;
        ROOT_FOLDER_FILE_PATH_INTERNAL = rootFolderFilePathInternal;
        MEDIA_LINK_ROOT_API = mediaLinkRootApi;
    }
}
