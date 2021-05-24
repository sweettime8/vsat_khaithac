package com.elcom.media.schedulers;

import com.elcom.media.service.MediaService;
import com.elcom.media.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author anhdv
 */
@Service
public class Scheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);

    private final MediaService mediaService;

    @Autowired
    public Scheduler(MediaService mediaService) {
        this.mediaService = mediaService;
    }

//    @Scheduled(initialDelay = 200)
//    public void progressBlackList() {
//        if (ApplicationConfig.APP_MASTER && ApplicationConfig.BLACK_LIST_DATA_ENABLE)
//            this.authService.progressBlackList();
//    }

    @Scheduled(fixedDelay = 1800000, initialDelay = 2000) // 30 minutes
    public void generateMediaRelation() {
        try {
            LOGGER.info("GenerateMediaRelation is starting, now: " + new Date().toString());
            mediaService.setMediaRelation();
        }catch (Exception ex){
            LOGGER.error(StringUtil.printException(ex));
        }
    }

    @Bean
    public TaskScheduler taskScheduler() {
        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        return scheduler;
    }
}
