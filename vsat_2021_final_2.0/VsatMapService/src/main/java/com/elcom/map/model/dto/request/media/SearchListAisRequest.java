package com.elcom.map.model.dto.request.media;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchListAisRequest implements Serializable {

    private String startTime;
    private String endTime;
    private String sourceIps;
    private String destIps;
    private String mmsi;
    private Integer dataSource;
    private String vesselGroupIds;
    private Integer limit;
    private boolean searchForList;

    public SearchListAisRequest(String startTime, String endTime, String sourceIps, String destIps, String mmsi
                                , Integer dataSource, String vesselGroupIds, Integer limit) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.sourceIps = sourceIps;
        this.destIps = destIps;
        this.mmsi = mmsi;
        this.dataSource = dataSource;
        this.vesselGroupIds = vesselGroupIds;
        this.limit = limit;
    }

    public SearchListAisRequest(String mmsi, Integer limit) {
        this.mmsi = mmsi;
        this.limit = limit;
    }

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
     * @return the mmsi
     */
    public String getMmsi() {
        return mmsi;
    }

    /**
     * @param mmsi the mmsi to set
     */
    public void setMmsi(String mmsi) {
        this.mmsi = mmsi;
    }

    /**
     * @return the dataSource
     */
    public Integer getDataSource() {
        return dataSource;
    }

    /**
     * @param dataSource the dataSource to set
     */
    public void setDataSource(Integer dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @return the limit
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * @param limit the limit to set
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * @return the searchForList
     */
    public boolean isSearchForList() {
        return searchForList;
    }

    /**
     * @param searchForList the searchForList to set
     */
    public void setSearchForList(boolean searchForList) {
        this.searchForList = searchForList;
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
     * @return the vesselGroupIds
     */
    public String getVesselGroupIds() {
        return vesselGroupIds;
    }

    /**
     * @param vesselGroupIds the vesselGroupIds to set
     */
    public void setVesselGroupIds(String vesselGroupIds) {
        this.vesselGroupIds = vesselGroupIds;
    }
}
