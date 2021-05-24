///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.elcom.uploadservice.config;
//
//import org.apache.catalina.connector.Connector;
//import org.apache.coyote.http11.AbstractHttp11Protocol;
//import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
//import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
//import org.springframework.boot.web.server.WebServerFactoryCustomizer;
//import org.springframework.stereotype.Component;
//
///**
// *
// * @author Admin
// */
//@Component
//public class TomcatCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
//
//    @Override
//    public void customize(TomcatServletWebServerFactory factory) {
//        factory.addConnectorCustomizers((TomcatConnectorCustomizer) (Connector connector) -> {
//            if (connector.getProtocolHandler() instanceof AbstractHttp11Protocol) {
//                ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
//            }
//        });
//    }
//}
