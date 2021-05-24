/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.statistic.constant;

/**
 *
 * @author Admin
 */
public class Constant {

    public static final String SRV_VER = "/v1.0";
    
    // Validation message
    public static final String VALIDATION_INVALID_PARAM_VALUE = "Invalid param value";
    public static final String VALIDATION_DATA_NOT_FOUND = "Data not found";
    public static final String VALIDATION_ACCOUNT_LOCKED = "Tài khoản đang bị khóa";
    //    parition type
    public static final int PARTITION_TYPE_MONTH = 1;
    public static final int PARTITION_TYPE_DAY = 2;
    // Response messages
    public static final String RESPONSE_UNKNOW_ERR = "Lỗi không xác định";

    
    //Forgot password
    public static final String MAIL_FORGOT_PW_SEND_FROM = "Vsat support";
    public static final String MAIL_FORGOT_PW_TITLE = "Qu\u00EAn m\u1EADt kh\u1EA9u t\u00E0i kho\u1EA3n %s";
    public static final String MAIL_FORGOT_PW_CONTENT = "Xin ch\u00E0o %s,"
            + "<br />\u0110\u00E2y l\u00E0 m\u1EADt kh\u1EA9u c\u1EE7a b\u1EA1n: <b>%s</b>"
            + "<br />Xin c\u1EA3m \u01A1n!";
    public static final String MAIL_FORGOT_PW_CONTENT_LINK = "Xin ch\u00E0o %s,"
            + "<br />Link \u0111\u1EB7t l\u1EA1i m\u1EADt kh\u1EA9u c\u1EE7a b\u1EA1n l\u00E0 : <a href='%s'>%s</a>"
            + "<br />Link t\u1ED3n t\u1EA1i trong v\u00F2ng <b>%s ph\u00FAt</b>."
            + "<br />Xin c\u1EA3m \u01A1n!";

    
    // User Log Type map
    public static final String USER_LOG_TYPE_LOGIN = "LOGIN";
    public static final String USER_LOG_TYPE_REGISTER = "REGISTER";
    
    
    // AES Key
    public static final String AES_KEY = "Elc0m2020@321456";   
    
    // Redis
    public static final String REDIS_TIME_TOKEN_LOGIN_CREATE = "VSAT_TIME_TOKEN_LOGIN_CREATE";
    public static final String REDIS_BLACK_LIST_LOGOUT_TOKEN = "VSAT_BLACK_LIST_TOKEN_LOGOUT";
}
