package com.elcom.rule.model.dto.request.media;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchListAisRequest implements Serializable {

    private String startTime;
    private String endTime;
    private String sourceIps;
    private String destIps;
    private String mmsi;
    private Integer dataSource; // Nguồn thu
    private Integer typeId; // Loại tàu
    private Integer countryId; // Quốc gia
    private Integer isUfo; // Lọc theo tàu hay đối tượng ko xác định
    private List<Integer> areaIds; // Vùng
    private List<Integer> groupIds; // Nhóm tàu
    private Integer limit;
    private boolean searchForList; // Tìm kiếm cho màn hình danh sách(tổng thể) hay màn hình chi tiết(tập trung)

    public SearchListAisRequest(String startTime, String endTime, String sourceIps, String destIps, String mmsi
                                , Integer dataSource, Integer typeId, Integer countryId, Integer isUfo
                                , List<Integer> areaIds, List<Integer> groupIds, Integer limit) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.sourceIps = sourceIps;
        this.destIps = destIps;
        this.mmsi = mmsi;
        this.dataSource = dataSource;
        this.typeId = typeId;
        this.countryId = countryId;
        this.isUfo = isUfo;
        this.areaIds = areaIds;
        this.groupIds = groupIds;
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
     * @return the areaIds
     */
    public List<Integer> getAreaIds() {
        return areaIds;
    }

    /**
     * @param areaIds the areaIds to set
     */
    public void setAreaIds(List<Integer> areaIds) {
        this.areaIds = areaIds;
    }

    /**
     * @return the groupIds
     */
    public List<Integer> getGroupIds() {
        return groupIds;
    }

    /**
     * @param groupIds the groupIds to set
     */
    public void setGroupIds(List<Integer> groupIds) {
        this.groupIds = groupIds;
    }

    /**
     * @return the typeId
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * @param typeId the typeId to set
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    /**
     * @return the countryId
     */
    public Integer getCountryId() {
        return countryId;
    }

    /**
     * @param countryId the countryId to set
     */
    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    /**
     * @return the isUfo
     */
    public Integer getIsUfo() {
        return isUfo;
    }

    /**
     * @param isUfo the isUfo to set
     */
    public void setIsUfo(Integer isUfo) {
        this.isUfo = isUfo;
    }
}
