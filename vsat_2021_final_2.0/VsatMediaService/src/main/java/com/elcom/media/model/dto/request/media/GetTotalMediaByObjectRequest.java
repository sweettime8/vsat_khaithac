package com.elcom.media.model.dto.request.media;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetTotalMediaByObjectRequest implements Serializable {
    
    private String startTime;
    private String endTime;
    private List<Integer> dataSource;
    private List<Integer> mediaType;
    private String sourceIps;
    private String destIps;
    private String objId;

    public GetTotalMediaByObjectRequest(String startTime, String endTime, List<Integer> dataSource, List<Integer> mediaType
                                , String sourceIps, String destIps, String objId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.dataSource = dataSource;
        this.mediaType = mediaType;
        this.sourceIps = sourceIps;
        this.destIps = destIps;
        this.objId = objId;
    }
    
//    @FunctionalInterface
//    interface Math {
//
//        int calculate(int a, int b);
//
//        default int addDefault(int a, int b) {
//            return a + b + 10;
//        }
//
//        static int addStatic(int a, int b) {
//            return a + b + 100;
//        }
//    }
//    
//    public static void main(String[] args) {
//        Math mathAdd = (a, b) -> a + b;
//        Math mathSubtract = (a, b) -> a - b;
//        System.out.println("result: " + mathAdd.calculate(1, 2));
//        System.out.println("result: " + mathSubtract.calculate(4, 2));
//        
//        BiFunction<Integer, Integer, String> biFuntion = (a, b) -> {
//            return "res_" + a + b;
//        };
//        System.out.println("biFuntion.result: " +biFuntion.apply(1, 2));
//    }
    
    /**
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the dataSource
     */
    public List<Integer> getDataSource() {
        return dataSource;
    }

    /**
     * @param dataSource the dataSource to set
     */
    public void setDataSource(List<Integer> dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @return the mediaType
     */
    public List<Integer> getMediaType() {
        return mediaType;
    }

    /**
     * @param mediaType the mediaType to set
     */
    public void setMediaType(List<Integer> mediaType) {
        this.mediaType = mediaType;
    }

    /**
     * @return the sourceIps
     */
    public String getSourceIps() {
        return sourceIps;
    }

    /**
     * @param sourceIps the sourceIps to set
     */
    public void setSourceIps(String sourceIps) {
        this.sourceIps = sourceIps;
    }

    /**
     * @return the destIps
     */
    public String getDestIps() {
        return destIps;
    }

    /**
     * @param destIps the destIps to set
     */
    public void setDestIps(String destIps) {
        this.destIps = destIps;
    }

    /**
     * @return the objId
     */
    public String getObjId() {
        return objId;
    }

    /**
     * @param objId the objId to set
     */
    public void setObjId(String objId) {
        this.objId = objId;
    }
}
