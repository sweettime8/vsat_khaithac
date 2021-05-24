package com.elcom.ais.constant;

/**
 *
 * @author Admin
 */
public class Constant {

    public static final String SRV_VER = "/v1.0";
    
    // Validation message
    public static final String VALIDATION_INVALID_PARAM_VALUE = "Invalid param value";
    public static final String VALIDATION_DATA_NOT_FOUND = "Data not found";
    
    public static final int PARTITION_TYPE_MONTH = 1;
    public static final int PARTITION_TYPE_DAY = 2;
    
    // Bản tin AIS có MMSI
    public static final int AIS_MSG_CLEAR_TYPE = 0;
    // Bản tin AIS ko có MMSI
    public static final int AIS_MSG_UNDEFINED_TYPE = 1;
}
