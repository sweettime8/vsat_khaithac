package com.elcom.contact.service.impl;

import com.elcom.gateway.message.MessageContent;
import com.elcom.contact.model.dto.request.media.AddCommentRequest;
import com.elcom.contact.model.dto.request.media.DeleteFileMediaRequest;
import com.elcom.contact.model.dto.request.media.GetInfoByMediaIdRequest;
import com.elcom.contact.model.dto.request.media.UpdateStatusByMediaIdRequest;
import com.elcom.contact.model.media.MediaRaw;
import com.elcom.contact.repository.media.MediaRepository;

import com.elcom.contact.service.MediaService;
import com.elcom.vsat.model.MediaType;

import com.elcom.vsat.model.dto.controller.request.media.SearchListMediaRequest;

import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

@Service
@SuppressWarnings("unchecked")
public class MediaServiceImpl implements MediaService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaServiceImpl.class);

    @Autowired
    private MediaRepository mediaRepository;
    
//    @Autowired
//    private VsatMediaRepository vsatMediaRepository;
    
//    @Resource
//    @Autowired
//    private MediaMapper mediaMapper;

    public List<MediaType> getListMediaTypes(HttpServletRequest request, MultiValueMap postInput, String token) {
        List<MediaType> lstData=null;
//        try {
//            lstData = mediaRepository.getListMediaTypes();
//        } catch (Exception e) {
//            LOGGER.error("MediaListPro.getListMediaTypes ==> error : ", e);
//        }

        return lstData;
    }

    public MessageContent search(SearchListMediaRequest data){
        MessageContent messageContent=null;
        try {
            messageContent = mediaRepository.searchMedia(data);
        } catch (Exception e) {
            LOGGER.error("MediaListPro.getListMediaTypes ==> error : ", e);
        }
        return messageContent;
    }

    public MediaRaw getInfoByMediaId(GetInfoByMediaIdRequest data){
        MediaRaw mediaRaw =null;
        try {
            mediaRaw=mediaRepository.getInfoByMediaId(data);
        } catch (Exception e) {
            LOGGER.error("MediaListPro.getListMediaTypes ==> error : ", e);
        }
        return mediaRaw;
    }
    public Boolean updateStatusMediaId(UpdateStatusByMediaIdRequest data){
        try {
            return mediaRepository.updatestatus(data);
        } catch (Exception e) {
            LOGGER.error("MediaListPro.getListMediaTypes ==> error : ", e);
        }
        return null;
    }

    public Boolean addCommentCommon(AddCommentRequest addCommentRequest){
        try {
            return mediaRepository.addCommentCommon(addCommentRequest);
        } catch (Exception e) {
            LOGGER.error("MediaListPro.getListMediaTypes ==> error : ", e);
        }
        return null;
    }

    public Boolean deleteFileMedia(DeleteFileMediaRequest deleteFileMediaRequest){
        try {
            return mediaRepository.deleteFileMedia(deleteFileMediaRequest);
        } catch (Exception e) {
            LOGGER.error("MediaListPro.getListMediaTypes ==> error : ", e);
        }
        return null;
    }

//    @Override
//    public void testFindMedia(String startDate, String endDate) {
//        mediaRepository.testFindMedia2(startDate, endDate);
////        List<Media> lst = mediaMapper.selectList();
////        LOGGER.info("lst.size: {}", lst.size());
////        List<Media> lst = vsatMediaRepository.testFindMedia(startDate, endDate);
////        LOGGER.info("lst.size: {}", lst.size());
//    }
    
//    @Override
//    public void testFindAis(String startDate, String endDate) {
//        mediaRepository.testFindAis(startDate, endDate);
//    }

//    @Override
//    public void testInsertAis(Long ruleId, Long mmsi) {
//
//        BlockingQueue<String> ipQueue = new LinkedBlockingDeque<>();
//        generateRandomIp(200002, ipQueue);
//        System.out.println("Finish generated!");
//
//        Map<String, String> mapMssiWithIp = new HashMap<>();
//        Map<String, String> mapMmsiWithLongLat = new HashMap<>();
//
//        int numOfRelay = 50;
//        for( int j=0; j<numOfRelay; j++ ) {
//            mediaRepository.testInsertAis(ipQueue, mapMssiWithIp, mapMmsiWithLongLat);
//            try {
//                System.out.println("Sleep " + (j+1) + " times....");
//                Thread.currentThread().sleep(60000);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//        System.out.println("Finish JOB! queue remain: " + ipQueue);
//    }
    
//    @Override
//    public void testInsertMedia(Long ruleId, Long mmsi) {
//
//        BlockingQueue<String> ipQueue = new LinkedBlockingDeque<>();
//        generateRandomIp(1000002, ipQueue);
//        System.out.println("Finish generated!");
//
//        int numOfRelay = 10;
//        for( int j=0; j<numOfRelay; j++ ) {
//            mediaRepository.testInsertMedia(ipQueue);
//            try {
//                System.out.println("Sleep " + (j+1) + " times....");
//                Thread.currentThread().sleep(60000);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//        System.out.println("Finish JOB! queue remain: " + ipQueue);
//    }
    
    public static void main(String[] args) {
//        LinkedHashMap<Integer,String> linkedHashMap = new LinkedHashMap<>();
//        /* Populate */
//        linkedHashMap.put(0,"value0");
//        linkedHashMap.put(1,"value1");
//        linkedHashMap.put(2,"value2");
//        /* Get by position */
//        String v1 = (new ArrayList<>(linkedHashMap.values())).get(0);
//        String v2 = (new ArrayList<>(linkedHashMap.values())).get(1);
//        String v3 = (new ArrayList<>(linkedHashMap.values())).get(2);
//        System.out.println("v1: " + v1);
//        System.out.println("v2: " + v2);
//        System.out.println("v3: " + v3);
    }
    
    private void generateRandomIp(int numOfIp, BlockingQueue<String> queue) {
        try {
            PrintWriter writer = new PrintWriter("H:\\ip-100k-03032021-for-MEDIA.txt", "UTF-8");
            Random r = new Random();
            String ip = "";
            for( int i=0; i<numOfIp; i++ ) {

                if( i % 10000 == 0 )
                    System.out.println("Still generating ...");

                try {
                    ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
                    if( !queue.contains(ip) ) {
                        queue.put(ip);
                        writer.println(ip);
                    }
                    else {
    //                    System.out.println("DUPLICATED Level_1");
                        ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
                        queue.put(ip);
                        if( !queue.contains(ip) ) {
                            queue.put(ip);
                            writer.println(ip);
                        }else {
    //                        System.out.println("DUPLICATED Level_2");
                            ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
                            queue.put(ip);
                            if( !queue.contains(ip) ) {
                                queue.put(ip);
                                writer.println(ip);
                            }else {
    //                            System.out.println("DUPLICATED Level_3");
                                ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
                                queue.put(ip);
                                if( !queue.contains(ip) ) {
                                    queue.put(ip);
                                    writer.println(ip);
                                }else {
    //                                System.out.println("DUPLICATED Level_4");
                                    ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
                                    queue.put(ip);
                                    if( !queue.contains(ip) ) {
                                        queue.put(ip);
                                        writer.println(ip);
                                    }else {
    //                                    System.out.println("DUPLICATED Level_5");
                                        ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
                                        queue.put(ip);
                                        if( !queue.contains(ip) ) {
                                            queue.put(ip);
                                            writer.println(ip);
                                        }else {
    //                                        System.out.println("DUPLICATED Level_6");
                                            ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
                                            queue.put(ip);
                                            if( !queue.contains(ip) ) {
                                                queue.put(ip);
                                                writer.println(ip);
                                            }else {
    //                                            System.out.println("DUPLICATED Level_7");
                                                ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
                                                queue.put(ip);
                                                if( !queue.contains(ip) ) {
                                                    queue.put(ip);
                                                    writer.println(ip);
                                                }else {
    //                                                System.out.println("DUPLICATED Level_8");
                                                    ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
                                                    queue.put(ip);
                                                    if( !queue.contains(ip) ) {
                                                        queue.put(ip);
                                                        writer.println(ip);
                                                    }else {
    //                                                    System.out.println("DUPLICATED Level_9");
                                                        ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
                                                        queue.put(ip);
                                                        if( !queue.contains(ip) ) {
                                                            queue.put(ip);
                                                            writer.println(ip);
                                                        }else {
    //                                                        System.out.println("DUPLICATED Level_10");
                                                            ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
                                                            queue.put(ip);
                                                            writer.println(ip);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.getStackTrace();
                }
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
