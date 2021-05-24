/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.rbac.constant;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class Constant {
    
    //Typesafe config
    public static final String  CONFIG_DIR = System.getProperty("user.dir") + File.separator + "config" + File.separator;
    private static final Config CONFIG;
    
    //Constant
    public static String  CONSTANT_STR1 = "";
    public static int     CONSTANT_INT2 = 0;
    public static boolean CONSTANT_BOOL3 = false;
    public static final String DOWNLOAD_FILE_PATH = "downloadFile";
    
    // Validation message
    public static final String VALIDATION_INVALID_PARAM_VALUE = "Invalid param value";
    public static final String VALIDATION_DATA_NOT_FOUND = "Data not found";
    
    // Redis constant
    public static final String REDIS_ROLE_USER_KEY = "VSAT_ROLE_USER";
    public static final String REDIS_MODULE_PATH_KEY = "VSAT_MODULE_PATH";
    public static final String REDIS_SERVICE_PATH_KEY = "VSAT_SERVICE_PATH";
    public static final String REDIS_ROLE_MODULE_PERMISSION_KEY = "VSAT_ROLE_MODULE_PERMISSION";
    
    // Default Role
    public static final String DEFAULT_ROLE = "VSAT_USER";
    
    // Fix ADMIN service
    // USER_SERVICE ==> USER_ADMIN, STORE_SERVICE ==> STORE_ADMIN,...
    public static final Map<String, String> SERVICE_ADMIN_MAP = new HashMap<>();
    
    static {
        Config baseConfig = ConfigFactory.load("constant");
        CONFIG = ConfigFactory.parseFile(new File(CONFIG_DIR + "constant.conf")).withFallback(baseConfig);
        loadConfig();
    }
    
    public static void loadConfig() {
        CONSTANT_STR1 = CONFIG.getString("CONSTANT_STR1");
        CONSTANT_INT2 = CONFIG.getInt("CONSTANT_INT2");
        CONSTANT_BOOL3 = CONFIG.getBoolean("CONSTANT_BOOL3");
    }
}
