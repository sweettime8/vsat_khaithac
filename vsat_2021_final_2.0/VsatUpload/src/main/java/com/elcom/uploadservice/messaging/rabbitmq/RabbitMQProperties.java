/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.uploadservice.messaging.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Admin
 */
@Component
public class RabbitMQProperties {

    //User service
    @Value("${user.rpc.exchange}")
    public static String USER_RPC_EXCHANGE;

    @Value("${user.rpc.queue}")
    public static String USER_RPC_QUEUE;

    @Value("${user.rpc.key}")
    public static String USER_RPC_KEY;

    @Value("${user.rpc.authen.url}")
    public static String USER_RPC_AUTHEN_URL;
    
    // Media
    @Value("${media.rpc.exchange}")
    public static String MEDIA_RPC_EXCHANGE;
    @Value("${media.rpc.queue}")
    public static String MEDIA_RPC_QUEUE;
    @Value("${media.rpc.key}")
    public static String MEDIA_RPC_KEY;
    // -->
    
    //RBAC
    //Payment
    @Autowired
    public RabbitMQProperties(@Value("${user.rpc.exchange}") String userRpcExchange,
            @Value("${user.rpc.queue}") String userRpcQueue,
            @Value("${user.rpc.key}") String userRpcKey,
            @Value("${user.rpc.authen.url}") String userRpcAuthenUrl,
            @Value("${media.rpc.exchange}") String mediaRpcExchange,
            @Value("${media.rpc.queue}") String mediaRpcQueue,
            @Value("${media.rpc.key}") String mediaRpcKey) {
        USER_RPC_EXCHANGE = userRpcExchange;
        USER_RPC_QUEUE = userRpcQueue;
        USER_RPC_KEY = userRpcKey;
        USER_RPC_AUTHEN_URL = userRpcAuthenUrl;
        MEDIA_RPC_EXCHANGE = mediaRpcExchange;
        MEDIA_RPC_QUEUE = mediaRpcQueue;
        MEDIA_RPC_KEY = mediaRpcKey;
    }
}
