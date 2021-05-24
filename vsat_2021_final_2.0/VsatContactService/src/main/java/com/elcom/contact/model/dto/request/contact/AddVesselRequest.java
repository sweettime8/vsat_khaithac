/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.contact.model.dto.request.contact;

import java.sql.Timestamp;

/**
 *
 * @author Admin
 */
public class AddVesselRequest {
    private Long mmsi;
    private String vesselName;
    private Integer vesselType;
    private String vesselTypeName;
    private Long imo;
    private String crew;                //đội tàu
    private String satellitePhoneCode;
    private Long countryId;
    private String countryName;
    private Integer typeId;
    private Integer height;
    private Integer width;
    private String callsign;
    private String imagePath;
    private Integer yearOfBuild;
    private Integer placeOfBuildCode;
    private Integer draugth;
    private String engineType;
    private Integer grossTonnage;
    private Integer deadWeight;
    private String namePlace;
    private String owner;
    private String otherInfo;
    private String status;        
    private String unit;
    private String operationUnit;
    private Double speedAvg;
    private Double speedMax;
    private String displacement;
    private String weapons;
    private String endurance;
    private String userUpdate;
    private String nameStatic;
    
    private Timestamp createdTime;
    private Timestamp updatedTime;

    private String sort;
    private Integer currentPage;
    private Integer rowsPerPage;
    private String keyword;

    public Long getMmsi() {
        return mmsi;
    }

    public void setMmsi(Long mmsi) {
        this.mmsi = mmsi;
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public Integer getVesselType() {
        return vesselType;
    }

    public void setVesselType(Integer vesselType) {
        this.vesselType = vesselType;
    }

    public String getVesselTypeName() {
        return vesselTypeName;
    }

    public void setVesselTypeName(String vesselTypeName) {
        this.vesselTypeName = vesselTypeName;
    }

    public Long getImo() {
        return imo;
    }

    public void setImo(Long imo) {
        this.imo = imo;
    }

    public String getCrew() {
        return crew;
    }

    public void setCrew(String crew) {
        this.crew = crew;
    }

    public String getSatellitePhoneCode() {
        return satellitePhoneCode;
    }

    public void setSatellitePhoneCode(String satellitePhoneCode) {
        this.satellitePhoneCode = satellitePhoneCode;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getYearOfBuild() {
        return yearOfBuild;
    }

    public void setYearOfBuild(Integer yearOfBuild) {
        this.yearOfBuild = yearOfBuild;
    }

    public Integer getPlaceOfBuildCode() {
        return placeOfBuildCode;
    }

    public void setPlaceOfBuildCode(Integer placeOfBuildCode) {
        this.placeOfBuildCode = placeOfBuildCode;
    }

    public Integer getDraugth() {
        return draugth;
    }

    public void setDraugth(Integer draugth) {
        this.draugth = draugth;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public Integer getGrossTonnage() {
        return grossTonnage;
    }

    public void setGrossTonnage(Integer grossTonnage) {
        this.grossTonnage = grossTonnage;
    }

    public Integer getDeadWeight() {
        return deadWeight;
    }

    public void setDeadWeight(Integer deadWeight) {
        this.deadWeight = deadWeight;
    }

    public String getNamePlace() {
        return namePlace;
    }

    public void setNamePlace(String namePlace) {
        this.namePlace = namePlace;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getOperationUnit() {
        return operationUnit;
    }

    public void setOperationUnit(String operationUnit) {
        this.operationUnit = operationUnit;
    }

    public Double getSpeedAvg() {
        return speedAvg;
    }

    public void setSpeedAvg(Double speedAvg) {
        this.speedAvg = speedAvg;
    }

    public Double getSpeedMax() {
        return speedMax;
    }

    public void setSpeedMax(Double speedMax) {
        this.speedMax = speedMax;
    }

    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    public String getWeapons() {
        return weapons;
    }

    public void setWeapons(String weapons) {
        this.weapons = weapons;
    }

    public String getEndurance() {
        return endurance;
    }

    public void setEndurance(String endurance) {
        this.endurance = endurance;
    }

    public String getUserUpdate() {
        return userUpdate;
    }

    public void setUserUpdate(String userUpdate) {
        this.userUpdate = userUpdate;
    }

    public String getNameStatic() {
        return nameStatic;
    }

    public void setNameStatic(String nameStatic) {
        this.nameStatic = nameStatic;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(Integer rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    
}
