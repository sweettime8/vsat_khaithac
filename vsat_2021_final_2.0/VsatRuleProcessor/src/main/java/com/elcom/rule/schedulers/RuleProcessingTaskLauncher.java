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
    
    // Key chứa những đối tượng đã từng được cảnh báo đi vào vùng
    private static final String LST_OBJECT_ALERTED_INSIDE = "VSAT_V2_OBJECT_LST_ALERTED_INSIDE";
    // Key chứa những đối tượng đã từng được cảnh báo ra khỏi vùng
    private static final String LST_OBJECT_ALERTED_OUTSIDE = "VSAT_V2_OBJECT_LST_ALERTED_OUTSIDE";
    
    // Key chứa lịch sử vào vùng của đối tượng
//    private static final String HISTORY_OBJECT_GO_TO_INSIDE = "VSAT_V2_OBJECT_HISTORY_INTO_AREA";
    
    // Key chứa danh sách vùng kèm theo danh sách đối tượng được áp dụng luật cảnh báo đi vào vùng
    private static final String REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE = "VSAT_V2_LST_AREA_WITH_RULE_INSIDE";
    // Key chứa danh sách vùng kèm theo danh sách đối tượng được áp dụng luật cảnh báo ra khỏi vùng
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
                    
                    // Tạo map chứa key và value là areaId và value là ruleId
//                    Map<Integer, String> mapArrAreaRuleInside = new HashMap<>();
//                    this.getArrAreaRuleInside().forEach( s -> {
//                        mapArrAreaRuleInside.put(Integer.parseInt(s.split("_")[0]), s.split("_")[1]);
//                    });
                    
                    // Chỉ xử lý nếu areaId này nằm trong danh sách area cần check của rule cảnh báo vào vùng
                    if( this.getArrAreaRuleInside().keySet().contains(evtAreaId) ) {
                        LOGGER.info("Processing for objId: [{}], areaId: [{}]", ais.getObjId(), evtAreaId);
                        String objIdInAreaStr = (String) this.redisTemplate.opsForHash().get(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_INSIDE, evtAreaId + "_" + this.getArrAreaRuleInside().get(evtAreaId));
                        if( !StringUtil.isNullOrEmpty(objIdInAreaStr) ) {
                            String[] lstObjIdInAreas = objIdInAreaStr.split(",");

                            // Xử lý nếu objId này nằm trong vùng rule được áp dụng cảnh báo vào vùng
                            if( lstObjIdInAreas != null && lstObjIdInAreas.length > 0 && Arrays.asList(lstObjIdInAreas).contains(ais.getObjId()) ) {

                                // Nếu chưa từng xử lý cảnh báo đối với (objId & areaId) này thì mới xử lý cảnh báo
                                if( !this.redisTemplate.opsForHash().hasKey(LST_OBJECT_ALERTED_INSIDE, ais.getObjId() + "_" + this.getArrAreaRuleInside().get(evtAreaId)) ) {
                                    LOGGER.info("Begin alert in-side area ==> objId: {}, areaId: {}", ais.getObjId(), evtAreaId);
                                    // Insert vào bảng rule_notify với objId, areaId, actionId = 1 (kiểu cảnh báo là vào vùng)
                                    String msg = "đối tượng có id: [" + ais.getObjId() + "] đi vào vùng ";
                                    
                                    // Hàm insert nếu thành công trả về tên vùng
                                    String insertRuleResultWithAreaName = this.ruleService.insertRuleNotify(new RuleNotifyInsertDTO(
                                            ais.getObjId(), msg, new Timestamp(ais.getEventTime()), evtAreaId.longValue()
                                            , ais.getLongitude(), ais.getLatitude(), Constant.RULE_ACTION_TYPE_CHECK_INSIDE
                                    ));
                                    if( !StringUtil.isNullOrEmpty(insertRuleResultWithAreaName) ) {
                                        
                                        threadManager.execute(() -> {
                                            this.sendMessageToClient(msg + "\"" + insertRuleResultWithAreaName + "\"");
                                        });

                                        // Đánh dấu là đã cảnh báo với areaId này và objId này, để lần sau ko cảnh báo nữa
                                        this.redisTemplate.opsForHash().put(LST_OBJECT_ALERTED_INSIDE, ais.getObjId() + "_" + this.getArrAreaRuleInside().get(evtAreaId), 1);

                                        // Lưu vào Redis với key là objId, value là areaId, để phục vụ việc tính toán cảnh báo ra vùng.
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
                // Chỉ xử lý những đối tượng nằm trong danh sách rule out-side cần check
                if( this.getArrObjIdRuleOutside().contains(ais.getObjId()) ) {
                    
                    // Lấy ra danh sách key đại diện cho area trong redis hash
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
                        
                        // Lấy ra danh sách những đối tượng ứng với area cần check

                        String row = (String) this.redisTemplate.opsForHash().get(REDIS_KEY_AREA_OBJ_ID_WITH_RULE_OUTSIDE, key);
                        if( StringUtil.isNullOrEmpty(row) )
                            return;
                        
                        String[] lstObjIdInAreas = row.split(",");
                        
                        // Nếu đối tượng trong bản tin ais này thuộc diện cần check thì xử lý
                        if( lstObjIdInAreas != null && lstObjIdInAreas.length > 0
                                && Arrays.asList(lstObjIdInAreas).contains(ais.getObjId()) ) {
                            LOGGER.info("Processing for objId: [{}], areaId: [{}]", ais.getObjId(), areaId);
                            
                            // Nếu danh sách vùng hiện tại của bản tin ais này không chứa vùng cần check ==> mới thỏa mãn 1 phần, cần tiếp tục check
                            if( !ais.getAreaIds().contains(Integer.parseInt(areaId)) ) {
                                    
                                // Nếu chưa từng xử lý cảnh báo đối với (objId & areaId) này thì mới xử lý cảnh báo
                                if( !this.redisTemplate.opsForHash().hasKey(LST_OBJECT_ALERTED_OUTSIDE, ais.getObjId() + "_"
                                        + this.getArrAreaRuleOutside().get(Integer.parseInt(areaId))) ) {
                                    threadManager.execute(() -> {
                                        // Check tiếp điều kiện: trước đó đối tượng này đã từng đi vào vùng này chưa? nếu đã từng vào thì thỏa mãn điều kiện: ra khỏi vùng.                                
                                        // TODO check lại chỗ này, lấy lịch sử đi vào vùng của đối tượng bằng cách nào???
                                        Set<Integer> areaIdsHistory = this.ruleService
                                                .findAreaIdsByObjectId(fromDate, toDate, ais.getObjId());
                                        if( true ) {
                                            LOGGER.info("Begin alert out-side area ==> objId: {}, areaId: {}", ais.getObjId(), areaId);
                                            // Insert vào bảng rule_notify với objId, areaId, actionId = 2 (kiểu cảnh báo là ra khỏi vùng)
                                            String msg = "đối tượng có id: [" + ais.getObjId() + "] ra khỏi vùng ";

                                            // Hàm insert nếu thành công trả về tên vùng
                                            String insertRuleResultWithAreaName = this.ruleService.insertRuleNotify(new RuleNotifyInsertDTO(
                                                    ais.getObjId(), msg, new Timestamp(ais.getEventTime()), Long.parseLong(areaId)
                                                    , ais.getLongitude(), ais.getLatitude(), Constant.RULE_ACTION_TYPE_CHECK_OUTSIDE
                                            ));
                                            if( !StringUtil.isNullOrEmpty(insertRuleResultWithAreaName) ) {

                                                threadManager.execute(() -> {
                                                    this.sendMessageToClient(msg + "\"" + insertRuleResultWithAreaName + "\"");
                                                });

                                                // Đánh dấu là đã cảnh báo với areaId này và objId này, để lần sau ko cảnh báo nữa
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
    
    // Sau khi khởi chạy 60s thì chạy lần đầu, sau đó 5' chạy 1 lần
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
            
        }else // Nếu DB ko có set null cho setOfKey
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
    
    // Sau khi khởi chạy 60s thì chạy lần đầu, sau đó 5' chạy 1 lần
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
            
        }else { // Nếu DB ko có set null cho setOfKey
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
            
            // Tạo map chứa key và value là areaId và value là ruleId
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
            
            // Tạo map chứa key và value là areaId và value là ruleId
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
            
            // Nếu có danh sách rule vào vùng, hoặc ra vùng, hoặc có cả 2 danh sách rule cần xử lý (arrKeys not null), thì bắt đầu tiến trình
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
