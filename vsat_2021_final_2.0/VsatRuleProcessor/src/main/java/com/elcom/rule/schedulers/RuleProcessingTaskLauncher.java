package com.elcom.rule.schedulers;

import com.elcom.gateway.message.RequestMessage;
import com.elcom.rule.constant.Constant;
import com.elcom.rule.messaging.rabbitmq.RabbitMQClient;
import com.elcom.rule.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.rule.model.dto.AreaWithObjectId;
import com.elcom.rule.model.dto.RuleNotifyInsertDTO;
import com.elcom.rule.model.kafka.consumer.AisMessage;
import com.elcom.rule.service.RuleService;
import com.elcom.rule.thread.ThreadManager;
import com.elcom.rule.utils.JSONConverter;
import com.elcom.rule.utils.StringUtil;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

/**
 *
 * @author anhdv
 */
@Component
public class RuleProcessingTaskLauncher implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleProcessingTaskLauncher.class);
    
    @Value("${kafka.consumer.id}")
    private String kafkaConsumerListenerId;
    
    @Autowired
    private RedisTemplate redisTemplate;
    
    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    
    @Autowired
    private RabbitMQClient rabbitMQClient;
    
    @Autowired
    private ThreadManager threadManager;
    
    @Autowired
    private RuleService ruleService;
    
    private boolean runProcessingForRuleInsideArea;
    
    private boolean runProcessingForRuleOutsideArea;
    
    // Key ch???a nh???ng ?????i t?????ng ???? t???ng ???????c c???nh b??o ??i v??o v??ng
    private static final String LST_OBJECT_ALERTED_INSIDE = "VSAT_V2_OBJECT_LST_ALERTED_INSIDE";
    // Key ch???a nh???ng ?????i t?????ng ???? t???ng ???????c c???nh b??o ra kh???i v??ng
    private static final String LST_OBJECT_ALERTED_OUTSIDE = "VSAT_V2_OBJECT_LST_ALERTED_OUTSIDE";
    
    // Key ch???a l???ch s??? v??o v??ng c???a ?????i t?????ng
//    private static final String HISTORY_OBJECT_GO_TO_INSIDE = "VSAT_V2_OBJECT_HISTORY_INTO_AREA";
    
    // Key ch???a danh s??ch v??ng k??m theo danh s??ch ?????i t?????ng ???????c ??p d???ng lu???t c???nh b??o ??i v??o v??ng
    private static final String REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE = "VSAT_V2_LST_AREA_WITH_RULE_INSIDE";
    // Key ch???a danh s??ch v??ng k??m theo danh s??ch ?????i t?????ng ???????c ??p d???ng lu???t c???nh b??o ra kh???i v??ng
    private static final String REDIS_KEY_AREA_OBJ_ID_WITH_RULE_OUTSIDE = "VSAT_V2_LST_AREA_WITH_RULE_OUTSIDE";
    
    private Map<Integer, String> arrAreaRuleInside;
    
    private Set<String> arrObjIdRuleOutside;
    private Map<Integer, String> arrAreaRuleOutside;
    
    //@KafkaListener( topics = "VSAT_AIS_NEW_T_02", id = "rule-consumer-id-01", groupId = "rule-consumer-group-id-01" )
    @KafkaListener(id = "${kafka.consumer.id}", groupId = "${kafka.consumer.groupId}"
        , topicPartitions = {
        @TopicPartition(topic = "${kafka.consumer.assaigned.topic.name}"
                        //, partitions = {"${kafka.consumer.assaigned.partition.number.1st}", "${kafka.consumer.assaigned.partition.number.2nd}"})
                        , partitions = {"0", "1"} )
//        @TopicPartition(topic = "${kafka.consumer.assaigned.topic.name}",
//            partitionOffsets = { @PartitionOffset(partition = "${kafka.consumer.assaigned.partition.number.1st}", initialOffset = "0")
//                                , @PartitionOffset(partition = "${kafka.consumer.assaigned.partition.number.2nd}", initialOffset = "0") }
//        )
    }, autoStartup = "false")
    public void receiveKafkaMessage(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition, @Header(KafkaHeaders.OFFSET) int offset) {
        if( message != null ) {
            
            LOGGER.info("Received data, partition: [{}], message: [{}], offset: [{}]", partition, message, offset);
            AisMessage ais = JSONConverter.toObject(message, AisMessage.class);
            
            if( this.runProcessingForRuleInsideArea
                    && this.getArrAreaRuleInside() != null && !this.getArrAreaRuleInside().isEmpty() )
                this.processForRuleInsideArea(ais);
            
            if( this.runProcessingForRuleOutsideArea
                    && this.getArrObjIdRuleOutside() != null && !this.getArrObjIdRuleOutside().isEmpty() )
                this.processForRuleOutsideArea(ais);
        }
    }
    
    private void processForRuleInsideArea(AisMessage ais) {
        if( ais != null && ais.getAreaIds() != null && !ais.getAreaIds().isEmpty() && !ais.getAreaIds().get(0).equals(0) ) {
            try {
                ais.getAreaIds().forEach((evtAreaId) -> {
                    
                    // T???o map ch???a key v?? value l?? areaId v?? value l?? ruleId
//                    Map<Integer, String> mapArrAreaRuleInside = new HashMap<>();
//                    this.getArrAreaRuleInside().forEach( s -> {
//                        mapArrAreaRuleInside.put(Integer.parseInt(s.split("_")[0]), s.split("_")[1]);
//                    });
                    
                    // Ch??? x??? l?? n???u areaId n??y n???m trong danh s??ch area c???n check c???a rule c???nh b??o v??o v??ng
                    if( this.getArrAreaRuleInside().keySet().contains(evtAreaId) ) {
                        LOGGER.info("Processing for objId: [{}], areaId: [{}]", ais.getObjId(), evtAreaId);
                        String objIdInAreaStr = (String) this.redisTemplate.opsForHash().get(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE, evtAreaId + "_" + this.getArrAreaRuleInside().get(evtAreaId));
                        if( !StringUtil.isNullOrEmpty(objIdInAreaStr) ) {
                            String[] lstObjIdInAreas = objIdInAreaStr.split(",");

                            // X??? l?? n???u objId n??y n???m trong v??ng rule ???????c ??p d???ng c???nh b??o v??o v??ng
                            if( lstObjIdInAreas != null && lstObjIdInAreas.length > 0 && Arrays.asList(lstObjIdInAreas).contains(ais.getObjId()) ) {

                                // N???u ch??a t???ng x??? l?? c???nh b??o ?????i v???i (objId & areaId) n??y th?? m???i x??? l?? c???nh b??o
                                if( !this.redisTemplate.opsForHash().hasKey(LST_OBJECT_ALERTED_INSIDE, ais.getObjId() + "_" + this.getArrAreaRuleInside().get(evtAreaId)) ) {
                                    LOGGER.info("Begin alert in-side area ==> objId: {}, areaId: {}", ais.getObjId(), evtAreaId);
                                    // Insert v??o b???ng rule_notify v???i objId, areaId, actionId = 1 (ki???u c???nh b??o l?? v??o v??ng)
                                    String msg = "?????i t?????ng c?? id: [" + ais.getObjId() + "] ??i v??o v??ng ";
                                    
                                    // H??m insert n???u th??nh c??ng tr??? v??? t??n v??ng
                                    String insertRuleResultWithAreaName = this.ruleService.insertRuleNotify(new RuleNotifyInsertDTO(
                                            ais.getObjId(), msg, new Timestamp(ais.getEventTime()), evtAreaId.longValue()
                                            , ais.getLongitude(), ais.getLatitude(), Constant.RULE_ACTION_TYPE_CHECK_INSIDE
                                    ));
                                    if( !StringUtil.isNullOrEmpty(insertRuleResultWithAreaName) ) {
                                        
                                        threadManager.execute(() -> {
                                            this.sendMessageToClient(msg + "\"" + insertRuleResultWithAreaName + "\"");
                                        });

                                        // ????nh d???u l?? ???? c???nh b??o v???i areaId n??y v?? objId n??y, ????? l???n sau ko c???nh b??o n???a
                                        this.redisTemplate.opsForHash().put(LST_OBJECT_ALERTED_INSIDE, ais.getObjId() + "_" + this.getArrAreaRuleInside().get(evtAreaId), 1);

                                        // L??u v??o Redis v???i key l?? objId, value l?? areaId, ????? ph???c v??? vi???c t??nh to??n c???nh b??o ra v??ng.
//                                        if( !this.redisTemplate.opsForHash().hasKey(HISTORY_OBJECT_GO_TO_INSIDE, ais.getObjId()) )
//                                            this.redisTemplate.opsForHash().put(HISTORY_OBJECT_GO_TO_INSIDE, ais.getObjId(), evtAreaId + "");
//                                        else {
//                                            String currentAreaIdsOfObjId = (String) this.redisTemplate.opsForHash().get(HISTORY_OBJECT_GO_TO_INSIDE, ais.getObjId());
//                                            this.redisTemplate.opsForHash().put(HISTORY_OBJECT_GO_TO_INSIDE, ais.getObjId(), currentAreaIdsOfObjId + "," + evtAreaId);
//                                        }
                                    }
                                }else
                                    LOGGER.info("Was alerted in-side area before ==> objId: {}, areaId: {}, return.", ais.getObjId(), evtAreaId);
                            }
                        }
                    }
                });
            } catch (Exception ex) {
                StringUtil.printException(ex);
            }
        }
    }
    
    private void processForRuleOutsideArea(AisMessage ais) {
        if( ais != null ) {
            try {
                // Ch??? x??? l?? nh???ng ?????i t?????ng n???m trong danh s??ch rule out-side c???n check
                if( this.getArrObjIdRuleOutside().contains(ais.getObjId()) ) {
                    
                    // L???y ra danh s??ch key ?????i di???n cho area trong redis hash
                    Set<String> keyAreaLst = this.redisTemplate.opsForHash().keys(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_OUTSIDE);
                    
//                    String ruleId = "";
                    for( String key : keyAreaLst ) {
                        
                        if( !key.contains("_") )
                            return;
                        
                        String[] arr = key.split("_");
                        
                        if( arr == null || arr.length < 4 )
                            return;
                        
                        final String areaId = arr[0];
                        final String fromDate = arr[2];
                        final String toDate = arr[3];
                        
                        // L???y ra danh s??ch nh???ng ?????i t?????ng ???ng v???i area c???n check

                        String row = (String) this.redisTemplate.opsForHash().get(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_OUTSIDE, key);
                        if( StringUtil.isNullOrEmpty(row) )
                            return;
                        
                        String[] lstObjIdInAreas = row.split(",");
                        
                        // N???u ?????i t?????ng trong b???n tin ais n??y thu???c di???n c???n check th?? x??? l??
                        if( lstObjIdInAreas != null && lstObjIdInAreas.length > 0
                                && Arrays.asList(lstObjIdInAreas).contains(ais.getObjId()) ) {
                            LOGGER.info("Processing for objId: [{}], areaId: [{}]", ais.getObjId(), areaId);
                            
                            // N???u danh s??ch v??ng hi???n t???i c???a b???n tin ais n??y kh??ng ch???a v??ng c???n check ==> m???i th???a m??n 1 ph???n, c???n ti???p t???c check
                            if( !ais.getAreaIds().contains(Integer.parseInt(areaId)) ) {
                                    
                                // N???u ch??a t???ng x??? l?? c???nh b??o ?????i v???i (objId & areaId) n??y th?? m???i x??? l?? c???nh b??o
                                if( !this.redisTemplate.opsForHash().hasKey(LST_OBJECT_ALERTED_OUTSIDE, ais.getObjId() + "_"
                                        + this.getArrAreaRuleOutside().get(Integer.parseInt(areaId))) ) {
                                    threadManager.execute(() -> {
                                        // Check ti???p ??i???u ki???n: tr?????c ???? ?????i t?????ng n??y ???? t???ng ??i v??o v??ng n??y ch??a? n???u ???? t???ng v??o th?? th???a m??n ??i???u ki???n: ra kh???i v??ng.                                
                                        // TODO check l???i ch??? n??y, l???y l???ch s??? ??i v??o v??ng c???a ?????i t?????ng b???ng c??ch n??o???
                                        Set<Integer> areaIdsHistory = this.ruleService
                                                .findAreaIdsByObjectId(fromDate, toDate, ais.getObjId());
                                        if( true ) {
                                            LOGGER.info("Begin alert out-side area ==> objId: {}, areaId: {}", ais.getObjId(), areaId);
                                            // Insert v??o b???ng rule_notify v???i objId, areaId, actionId = 2 (ki???u c???nh b??o l?? ra kh???i v??ng)
                                            String msg = "?????i t?????ng c?? id: [" + ais.getObjId() + "] ra kh???i v??ng ";

                                            // H??m insert n???u th??nh c??ng tr??? v??? t??n v??ng
                                            String insertRuleResultWithAreaName = this.ruleService.insertRuleNotify(new RuleNotifyInsertDTO(
                                                    ais.getObjId(), msg, new Timestamp(ais.getEventTime()), Long.parseLong(areaId)
                                                    , ais.getLongitude(), ais.getLatitude(), Constant.RULE_ACTION_TYPE_CHECK_OUTSIDE
                                            ));
                                            if( !StringUtil.isNullOrEmpty(insertRuleResultWithAreaName) ) {

                                                threadManager.execute(() -> {
                                                    this.sendMessageToClient(msg + "\"" + insertRuleResultWithAreaName + "\"");
                                                });

                                                // ????nh d???u l?? ???? c???nh b??o v???i areaId n??y v?? objId n??y, ????? l???n sau ko c???nh b??o n???a
                                                this.redisTemplate.opsForHash().put(LST_OBJECT_ALERTED_OUTSIDE
                                                        , ais.getObjId() + "_" + this.getArrAreaRuleOutside().get(Integer.parseInt(areaId)), 1);
                                            }
                                        }
                                    });
                                }else
                                    LOGGER.info("Was alerted out-side area before ==> objId: {}, areaId: {}, return.", ais.getObjId(), areaId);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                StringUtil.printException(ex);
            }
        }
    }
    
    private void sendMessageToClient(String message) {
        try {
            byte[] msgBytesArray = null;
            try {
                msgBytesArray = message.getBytes("UTF-8");
            } catch (Exception ex) {
                StringUtil.printException(ex);
            }

            Map<String, Object> bodyParam = new HashMap<>();
            bodyParam.put("messageContent", msgBytesArray != null ? new String(msgBytesArray) : "N/A");
            RequestMessage requestMessage = new RequestMessage("POST", Constant.SRV_RULE_NOTIFY_AREA, null, null, bodyParam, null);
            this.rabbitMQClient.callWorkerService(RabbitMQProperties.VSAT_RULE_WORKER_QUEUE, JSONConverter.toJSON(requestMessage));
        } catch (Exception ex) {
            StringUtil.printException(ex);
        }
    }
    
    private void processArrAreaForRuleInside() {
        try {
            if( this.redisTemplate.hasKey(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE) ) {
                this.setArrAreaForRuleInside();
                return;
            }
            
            this.syncDataRuleInsideFromDatabaseToRedis();
            
        } catch (Exception ex) {
            StringUtil.printException(ex);
        }
    }
    
    // Sau khi kh???i ch???y 60s th?? ch???y l???n ?????u, sau ???? 5' ch???y 1 l???n
    @Scheduled( initialDelay = 1000 * 60, fixedDelay = 1000 * 60 * 5 )
    private void syncDataRuleInsideFromDatabaseToRedis() {
        
        List<AreaWithObjectId> ruleLst = this.ruleService.findListRuleArea(Constant.RULE_ACTION_TYPE_CHECK_INSIDE);
        if( ruleLst != null && !ruleLst.isEmpty() ) {

            if( this.redisTemplate.hasKey(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE) )
                this.redisTemplate.delete(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE);

            String itemKey;
            for( AreaWithObjectId item : ruleLst ) {
                
                //[0]: areaId    [1]: ruleId
                itemKey = item.getAreaId() + "_" + item.getRuleId();
                
                if( !this.redisTemplate.opsForHash().hasKey(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE, itemKey) )
                    this.redisTemplate.opsForHash().put(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE, itemKey, item.getObjId());
                else {
                    String currentVal = (String) this.redisTemplate.opsForHash().get(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE, itemKey);
                    this.redisTemplate.opsForHash().put(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE, itemKey, currentVal + "," + item.getObjId());
                }
//                LOGGER.info("push val to key SUCCESS!");
            }
            
            this.setArrAreaForRuleInside();
            
        }else // N???u DB ko c?? set null cho setOfKey
            this.setArrAreaRuleInside(null);
        
        LOGGER.info("Sync DB Rule Inside to Redis & setOfKey success.");
    }
    
    private void processArrAreaForRuleOutside() {
        try {
            if( this.redisTemplate.hasKey(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_OUTSIDE) ) {
                this.setArrObjIdForRuleOutside();
                return;
            }
            
            this.syncDataRuleOutsideFromDatabaseToRedis();
            
        } catch (Exception ex) {
            StringUtil.printException(ex);
        }
    }
    
    // Sau khi kh???i ch???y 60s th?? ch???y l???n ?????u, sau ???? 5' ch???y 1 l???n
    @Scheduled( initialDelay = 1000 * 60, fixedDelay = 1000 * 60 * 5 )
    private void syncDataRuleOutsideFromDatabaseToRedis() {
        List<AreaWithObjectId> ruleLst = this.ruleService.findListRuleArea(Constant.RULE_ACTION_TYPE_CHECK_OUTSIDE);
        if( ruleLst != null && !ruleLst.isEmpty() ) {

            if( this.redisTemplate.hasKey(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_OUTSIDE) )
                this.redisTemplate.delete(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_OUTSIDE);

            String itemKey;
            for( AreaWithObjectId item : ruleLst ) {
                
                //[0]: areaId    [1]: ruleId     [2]: fromDate     [3]: toDate
                itemKey = item.getAreaId() + "_" + item.getRuleId() + "_" + item.getFromDate()+ "_" + item.getToDate();
                
                if( !this.redisTemplate.opsForHash().hasKey(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_OUTSIDE, itemKey) )
                    this.redisTemplate.opsForHash().put(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_OUTSIDE, itemKey, item.getObjId());
                else {
                    String currentVal = (String) this.redisTemplate.opsForHash().get(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_OUTSIDE, itemKey);
                    this.redisTemplate.opsForHash().put(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_OUTSIDE, itemKey, currentVal + "," + item.getObjId());
                }
//                LOGGER.info("push val to key SUCCESS!");
            }
            
            this.setArrObjIdForRuleOutside();
            
        }else { // N???u DB ko c?? set null cho setOfKey
            this.setArrObjIdRuleOutside(null);
            this.setArrAreaRuleOutside(null);
        }
        
        LOGGER.info("Sync DB Rule Outside to Redis & setOfKey success.");
    }
    
    private void setArrAreaForRuleInside() {
        try {
            Set<String> setResult = this.redisTemplate.opsForHash().keys(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE);
            if( setResult == null || setResult.isEmpty() ) {
                LOGGER.info("arrAreaRuleInside is null or empty");
                return;
            }
            LOGGER.info("arrAreaRuleInside: " + setResult.toString());
            
            // T???o map ch???a key v?? value l?? areaId v?? value l?? ruleId
            Map<Integer, String> mapArrAreaRuleInside = new HashMap<>();
            setResult.forEach( s -> {
                mapArrAreaRuleInside.put(Integer.parseInt(s.split("_")[0]), s.split("_")[1]);
            });
            
            this.setArrAreaRuleInside(mapArrAreaRuleInside);
        } catch (Exception ex) {
            StringUtil.printException(ex);
        }
    }
    
    private void setArrObjIdForRuleOutside() {
        try {
            Set<String> setOfValue = new HashSet<>();
            Set<String> setOfKey = this.redisTemplate.opsForHash().keys(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_OUTSIDE);
            for( String s : setOfKey ) {
                String row = (String) this.redisTemplate.opsForHash().get(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_OUTSIDE, s);
//                LOGGER.info("row: " + row);
                if( !StringUtil.isNullOrEmpty(row) ) {
                    String[] arr = row.split(",");
                    for( String j : arr ) {
                        setOfValue.add(j);
                    }
                }
            }
            if( setOfValue.isEmpty() ) {
                LOGGER.info("arrObjIdRuleOutside is null or empty");
                return;
            }
            LOGGER.info("arrObjIdRuleOutside: " + setOfValue.toString());
            
            // T???o map ch???a key v?? value l?? areaId v?? value l?? ruleId
            Map<Integer, String> mapArrAreaRuleOutside = new HashMap<>();
            setOfKey.forEach( s -> {
                mapArrAreaRuleOutside.put(Integer.parseInt(s.split("_")[0]), s.split("_")[1]);
            });
            
            this.setArrObjIdRuleOutside(setOfValue);
            
            this.setArrAreaRuleOutside(mapArrAreaRuleOutside);
            
        } catch (Exception ex) {
            StringUtil.printException(ex);
        }
    }
    
    @Override
    public void run(String... args) throws Exception {
        try {
//            long startTime = System.currentTimeMillis();
//            Set<Integer> areaIdsHistory = this.ruleService
//                    .findAreaIdsByObjectId("2021-04-18 00:00:00", "2022-04-04 00:00:00", "100000490");
//            LOGGER.info("areaIdsHistory1.elapseTime: " + getElapsedTime(System.currentTimeMillis() - startTime));
            
            if( this.getArrAreaRuleInside() == null || this.getArrAreaRuleInside().isEmpty() )
                this.processArrAreaForRuleInside();
            if( this.getArrAreaRuleInside() == null || this.getArrAreaRuleInside().isEmpty() )
                LOGGER.info("ruleList inside is empty, not running process in-side");
            else
                this.runProcessingForRuleInsideArea = true;
            
            if( this.getArrObjIdRuleOutside() == null || this.getArrObjIdRuleOutside().isEmpty() )
                this.processArrAreaForRuleOutside();
            if( this.getArrObjIdRuleOutside() == null || this.getArrObjIdRuleOutside().isEmpty() )
                LOGGER.info("ruleList outside is empty, not running process out-side");
            else
                this.runProcessingForRuleOutsideArea = true;
            
            // N???u c?? danh s??ch rule v??o v??ng, ho???c ra v??ng, ho???c c?? c??? 2 danh s??ch rule c???n x??? l?? (arrKeys not null), th?? b???t ?????u ti???n tr??nh
            if( this.runProcessingForRuleInsideArea || this.runProcessingForRuleOutsideArea ) {
                this.kafkaListenerEndpointRegistry.getListenerContainer(kafkaConsumerListenerId).start();
                LOGGER.info("Begin kafka listener for processing rule, runProcessingForRuleInsideArea: {}, runProcessingForRuleOutsideArea: {}"
                            , this.runProcessingForRuleInsideArea, this.runProcessingForRuleOutsideArea);
            }
            
//            long startTime2 = System.currentTimeMillis();
//            Set<Integer> areaIdsHistory2 = this.ruleService
//                    .findAreaIdsByObjectId("2021-04-18 00:00:00", "2022-04-04 00:00:00", "100000490");
//            LOGGER.info("areaIdsHistory2.elapseTime: " + getElapsedTime(System.currentTimeMillis() - startTime2));
        } catch (Exception ex) {
            StringUtil.printException(ex);
        }
    }

    /**
     * @return the arrAreaRuleInside
     */
    public Map<Integer, String> getArrAreaRuleInside() {
        return arrAreaRuleInside;
    }
    
    /**
     * @param arrAreaRuleInside the arrAreaRuleInside to set
     */
    public void setArrAreaRuleInside(Map<Integer, String> arrAreaRuleInside) {
        this.arrAreaRuleInside = arrAreaRuleInside;
    }
    
    /**
     * @return the arrObjIdRuleOutside
     */
    public Set<String> getArrObjIdRuleOutside() {
        return arrObjIdRuleOutside;
    }

    /**
     * @param arrObjIdRuleOutside the arrObjIdRuleOutside to set
     */
    public void setArrObjIdRuleOutside(Set<String> arrObjIdRuleOutside) {
        this.arrObjIdRuleOutside = arrObjIdRuleOutside;
    }
    
    /**
     * @return the arrAreaRuleOutside
     */
    public Map<Integer, String> getArrAreaRuleOutside() {
        return arrAreaRuleOutside;
    }

    /**
     * @param arrAreaRuleOutside the arrAreaRuleOutside to set
     */
    public void setArrAreaRuleOutside(Map<Integer, String> arrAreaRuleOutside) {
        this.arrAreaRuleOutside = arrAreaRuleOutside;
    }
    
    @Bean
    public TaskScheduler taskScheduler() {
        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(3);
        return scheduler;
    }
    
//    private String getElapsedTime(long miliseconds) {
//        return miliseconds + " (ms)";
//    }
}
