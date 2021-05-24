package com.elcom.id.schedulers;

import com.elcom.id.config.ApplicationConfig;
import com.elcom.id.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author anhdv
 */
@Service
public class Scheduler {

    //private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);

    private final AuthService authService;

    @Autowired
    public Scheduler(AuthService authService) {
        this.authService = authService;
    }

    @Scheduled(initialDelay = 200)
    public void progressBlackList() {
        if (ApplicationConfig.APP_MASTER && ApplicationConfig.BLACK_LIST_DATA_ENABLE)
            this.authService.progressBlackList();
    }

    @Bean
    public TaskScheduler taskScheduler() {
        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        return scheduler;
    }
}
