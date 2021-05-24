package com.elcom.statistic.schedulers;

import com.elcom.statistic.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

/**
 *
 * @author anhdv
 */
@Service
public class Scheduler {

    //private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);

    @Bean
    public TaskScheduler taskScheduler() {
        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        return scheduler;
    }
}
