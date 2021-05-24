package com.elcom.rule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//@EnableCaching
public class RuleServiceMainApplication {
    
    public static void main(String[] args) {
        // Fix lỗi "UDP failed setting ip_ttl | Method not implemented" khi start app trên Windows
//        System.setProperty("java.net.preferIPv4Stack", "true");
        
        SpringApplication.run(RuleServiceMainApplication.class, args);
    }
}
