package com.elcom.uploadservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author anhdv
 */
@Component
public class PropertiesConfig {
    
    @Value("${tech.env}")
    public static String TECH_ENV;
    
    @Value("${app.master}")
    public static boolean APP_MASTER;
    
    @Value("${rootFolderFilePathInternal}") 
    public static String ROOT_FOLDER_FILE_PATH_INTERNAL;
    
    @Value("${rootFolderFileMergeAudio}") 
    public static String ROOT_FOLDER_FILE_MERGE_AUDIO;
    
    @Autowired
    public PropertiesConfig(@Value("${tech.env}") String techEnv,
                            @Value("${app.master}") boolean isAppMaster,
                            @Value("${rootFolderFilePathInternal}") String rootFolderFilePathInternal,
                            @Value("${rootFolderFileMergeAudio}") String rootFolderFileMergeAudio) {
        TECH_ENV = techEnv;
        APP_MASTER = isAppMaster;
        ROOT_FOLDER_FILE_PATH_INTERNAL = rootFolderFilePathInternal;
        ROOT_FOLDER_FILE_MERGE_AUDIO = rootFolderFileMergeAudio;
    }
}
