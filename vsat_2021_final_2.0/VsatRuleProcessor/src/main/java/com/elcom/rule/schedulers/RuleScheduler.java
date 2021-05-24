package com.elcom.rule.schedulers;

import com.elcom.rule.config.ApplicationConfig;
import com.elcom.rule.constant.Constant;
import com.elcom.rule.model.dto.AreaWithObjectId;
import com.elcom.rule.model.kafka.consumer.AisMessage;
import com.elcom.rule.service.RuleService;
import com.elcom.rule.utils.JSONConverter;
import com.elcom.rule.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 *
 * @author anhdv
 */
@Service
public class RuleScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleScheduler.class);

    // Key chứa danh sách vùng kèm theo danh sách đối tượng được áp dụng luật cảnh báo đi vào vùng
    private static final String REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE = "VSAT_V2_LST_AREA_WITH_RULE_INSIDE";
    
    // Key chứa danh sách vùng kèm theo danh sách đối tượng được áp dụng luật cảnh báo ra khỏi vùng
    private static final String REDIS_KEY_AREA_OBJ_ID_WITH_RULE_OUTSIDE = "VSAT_V2_LST_AREA_WITH_RULE_OUTSIDE";
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    private RedisTemplate redisTemplate;
    
    @Autowired
    private RuleService ruleService;
    
    // Sau khi app khởi chạy thì 2' sau chạy hàm này, và mỗi 5 phút chạy lại 1 lần
//    @Scheduled( initialDelay = 1000 * 60 * 2, fixedDelay = 1000 * 60 * 5 )
    public void reloadRuleListInsideFromDBToRedis() {
        if( ApplicationConfig.APP_MASTER ) {
            try {
                List<AreaWithObjectId> ruleInsideLst = this.ruleService
                                                    .findListRuleArea(Constant.RULE_ACTION_TYPE_CHECK_INSIDE);
                if( ruleInsideLst != null && !ruleInsideLst.isEmpty() ) {

                    if( this.redisTemplate.hasKey(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE) )
                        this.redisTemplate.delete(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE);

                    for( AreaWithObjectId item : ruleInsideLst ) {
                        if( !this.redisTemplate.opsForHash().hasKey(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE, item.getAreaId()) )
                            this.redisTemplate.opsForHash().put(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE, item.getAreaId(), item.getObjId());
                        else {
                            String currentVal = (String) this.redisTemplate.opsForHash().get(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE, item.getAreaId());
                            this.redisTemplate.opsForHash().put(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE, item.getAreaId(), currentVal + "," + item.getObjId());
                        }
                        //LOGGER.info("push val to key SUCCESS!");
                    }
                }else if( this.redisTemplate.hasKey(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE) ) { // Nếu list get từ DB rỗng, thì set lại cho Redis rỗng (hoặc xóa key đi)
                    LOGGER.info("ruleInsideLst is null or empty.");
                    this.redisTemplate.delete(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE);
                }
                LOGGER.info("Re-sync redis-key [{}] success", REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE);
            } catch (Exception ex) {
                StringUtil.printException(ex);
            }
        }
    }
    
    // Sau khi app khởi chạy thì 2' sau mới chạy hàm này, và mỗi 5 phút chạy lại hàm 1 lần
//    @Scheduled( initialDelay = 1000 * 60 * 2, fixedDelay = 1000 * 60 * 5 )
    public void reloadRuleListOutsideFromDBToRedis() {
        if( ApplicationConfig.APP_MASTER ) {
            try {
                List<AreaWithObjectId> ruleInsideLst = this.ruleService
                                                        .findListRuleArea(Constant.RULE_ACTION_TYPE_CHECK_OUTSIDE);
                if( ruleInsideLst != null && !ruleInsideLst.isEmpty() ) {

                    if( this.redisTemplate.hasKey(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_OUTSIDE) )
                        this.redisTemplate.delete(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_OUTSIDE);

                    for( AreaWithObjectId item : ruleInsideLst ) {
                        if( !this.redisTemplate.opsForHash().hasKey(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE, item.getAreaId()) )
                            this.redisTemplate.opsForHash().put(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE, item.getAreaId(), item.getObjId());
                        else {
                            String currentVal = (String) this.redisTemplate.opsForHash().get(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE, item.getAreaId());
                            this.redisTemplate.opsForHash().put(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE, item.getAreaId(), currentVal + "," + item.getObjId());
                        }
                        //LOGGER.info("push val to key SUCCESS!");
                    }
                }else if( this.redisTemplate.hasKey(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_OUTSIDE) ) { // Nếu list get từ DB rỗng, thì set lại cho Redis rỗng (hoặc xóa key đi)
                    LOGGER.info("ruleOutsideLst is null or empty.");
                    this.redisTemplate.delete(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_OUTSIDE);
                }
                LOGGER.info("Re-sync redis-key [{}] success", REDIS_KEY_AREA_OBJ_ID_WITH_RULE_OUTSIDE);
            } catch (Exception ex) {
                StringUtil.printException(ex);
            }
        }
    }
    
    private int count = 0;
//    @Scheduled( fixedRate = 1000 * 1, initialDelay = 1000 * 10 )
    public void kafkaPushTestInside() {
        count++;
        
        if( count >= 3 )
            return;
        
        String topicName = "VSAT_AIS_NEW_T";
        
        List<Integer> areaIds = new ArrayList<>();
        areaIds.add(68);
        List<Integer> groupIds = new ArrayList<>();
        groupIds.add(0);
        String objId = count % 2 == 0 ? "413903130" : "412402628";
        
        long currentTime = System.currentTimeMillis();
        
        AisMessage ais = new AisMessage(objId, "0", 1, 2, 1, 2, 3, 4, new Float(1.5), new Float(2.5), new Float(3.5), new Float(4.5)
                , new Float(125.6453), new Float(35.02433), 1, 1, "", "", "", ""
                , currentTime - 60000, currentTime + 60000 , "", 1, 0L, 0L, 1, 1, "10.78.111.220", "125.78.111.55", 1
                , false, new Long("6"), "C6B_CH_ADV_CCG", areaIds, groupIds, new Long("5"), 1, "20210419", "202104190130", "441552000-202104190130");
        
        String msgAsJson = JSONConverter.toJSON(ais);
        
        ListenableFuture<SendResult<String, String>> sendFuture
                = kafkaTemplate.send(topicName, count % 2 == 0 ? 1 : 0, ais.getObjId(), msgAsJson);
        sendFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
//                LOGGER.info("Sent message = [" + msgAsJson + "] with offset = [" + result.getRecordMetadata().offset() + "] success");
            }
            @Override
            public void onFailure(Throwable ex) {
                LOGGER.info("Unable to send message = [" + msgAsJson + "], due to : " + ex.getMessage());
            }
        });
    }
    
    private int count2 = 0;
//    @Scheduled( fixedRate = 1000 * 1, initialDelay = 1000 * 15 )
    public void kafkaPushTestOutside() {
        count2++;
        
        if( count2 >= 3 )
            return;
        
        String topicName = "VSAT_AIS_NEW_T";
        
        List<Integer> areaIds = new ArrayList<>();
        areaIds.add(72);
        areaIds.add(71);
        List<Integer> groupIds = new ArrayList<>();
        groupIds.add(0);
        String objId = count2 % 2 == 0 ? "412402628" : "413903130";
        long currentTime = System.currentTimeMillis();
        
        AisMessage ais = new AisMessage(objId, "0", 1, 2, 1, 2, 3, 4, new Float(1.5), new Float(2.5), new Float(3.5), new Float(4.5)
                , new Float(125.6453), new Float(35.02433), 1, 1, "", "", "", ""
                , currentTime - 60000, currentTime + 60000 , "", 1, 0L, 0L, 1, 1, "10.78.111.220", "125.78.111.55", 1
                , false, new Long("6"), "C6B_CH_ADV_CCG", areaIds, groupIds, new Long("5"), 1, "20210419", "202104190130", "441552000-202104190130");
        
        String msgAsJson = JSONConverter.toJSON(ais);
        
        ListenableFuture<SendResult<String, String>> sendFuture
                = kafkaTemplate.send(topicName, count % 2 == 0 ? 1 : 0, ais.getObjId(), msgAsJson);
        sendFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
//                LOGGER.info("Sent message = [" + msgAsJson + "] with offset = [" + result.getRecordMetadata().offset() + "] success");
            }
            @Override
            public void onFailure(Throwable ex) {
                LOGGER.info("Unable to send message = [" + msgAsJson + "], due to : " + ex.getMessage());
            }
        });
    }
    
//    @Bean
//    public TaskScheduler taskScheduler2() {
//        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//        scheduler.setPoolSize(2);
//        return scheduler;
//    }
}
