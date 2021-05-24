/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.statistic.model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "vsat02.ais_info")
public class AisInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "mmsi")
    private Long mmsi;

    @Column(name = "imo")
    private Long imo;

    @Column(name = "country_id")
    private Long countryId;

    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "dim_a")
    private Integer dimA;

    @Column(name = "dim_b")
    private Integer dimB;

    @Column(name = "dim_c")
    private Integer dimC;

    @Column(name = "dim_d")
    private Integer dimD;

    @Column(name = "callsign_bak")
    private String callsignBaK;

    @Column(name = "name")
    private String name;

    @Column(name = "created_time")
    private Timestamp CreatedTime;

    @Column(name = "updated_time")
    private Timestamp updatedTime;

    @Column(name = "name_static")
    private String nameStatic;

    @Column(name = "is_master")
    private Integer isMaster;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "satellite_phone_code")
    private String satellitePhoneCode;

    @Column(name = "year_of_build")
    private Integer yearOfBuild;

    @Column(name = "place_of_build_code")
    private Integer placeOfBuildCode;

    @Column(name = "draugth")
    private Integer draught;

    @Column(name = "engine_type")
    private String engineType;

    @Column(name = "gross_tonnage")
    private Integer grossTonnage;

    @Column(name = "dead_weight")
    private Integer deadWeight;

    @Column(name = "name_place")
    private String namePlace;

    @Column(name = "speed_avg")
    private Double speedAvg;

    @Column(name = "speed_max")
    private Double speedMax;

    @Column(name = "displacement")
    private String displacement;
    
    @Column(name = "crew")
    private String crew;
    
    @Column(name = "weapons")
    private String weapons;
    
    @Column(name = "endurance")
    private String endurance;
    
    @Column(name = "user_update")
    private String userUpdate;
    
    @Column(name = "callsign")
    private String callsign;
    
    @Column(name = "source_ip")
    private String sourceIp;
    
    @Column(name = "source_port")
    private Integer sourcePort;
    
    @Column(name = "dest_ip")
    private String destIp;
    
    @Column(name = "dest_port")
    private Integer destPort;
    
    @Column(name = "is_ufo")
    private boolean isUfo;

    public Long getMmsi() {
        return mmsi;
    }

    public void setMmsi(Long mmsi) {
        this.mmsi = mmsi;
    }

    public Long getImo() {
        return imo;
    }

    public void setImo(Long imo) {
        this.imo = imo;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getDimA() {
        return dimA;
    }

    public void setDimA(Integer dimA) {
        this.dimA = dimA;
    }

    public Integer getDimB() {
        return dimB;
    }

    public void setDimB(Integer dimB) {
        this.dimB = dimB;
    }

    public Integer getDimC() {
        return dimC;
    }

    public void setDimC(Integer dimC) {
        this.dimC = dimC;
    }

    public Integer getDimD() {
        return dimD;
    }

    public void setDimD(Integer dimD) {
        this.dimD = dimD;
    }

    public String getCallsignBaK() {
        return callsignBaK;
    }

    public void setCallsignBaK(String callsignBaK) {
        this.callsignBaK = callsignBaK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(Timestamp CreatedTime) {
        this.CreatedTime = CreatedTime;
    }

    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getNameStatic() {
        return nameStatic;
    }

    public void setNameStatic(String nameStatic) {
        this.nameStatic = nameStatic;
    }

    public Integer getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(Integer isMaster) {
        this.isMaster = isMaster;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getSatellitePhoneCode() {
        return satellitePhoneCode;
    }

    public void setSatellitePhoneCode(String satellitePhoneCode) {
        this.satellitePhoneCode = satellitePhoneCode;
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

    public Integer getDraught() {
        return draught;
    }

    public void setDraught(Integer draught) {
        this.draught = draught;
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

    public String getCrew() {
        return crew;
    }

    public void setCrew(String crew) {
        this.crew = crew;
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

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public Integer getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(Integer sourcePort) {
        this.sourcePort = sourcePort;
    }

    public String getDestIp() {
        return destIp;
    }

    public void setDestIp(String destIp) {
        this.destIp = destIp;
    }

    public Integer getDestPort() {
        return destPort;
    }

    public void setDestPort(Integer destPort) {
        this.destPort = destPort;
    }

    public boolean isIsUfo() {
        return isUfo;
    }

    public void setIsUfo(boolean isUfo) {
        this.isUfo = isUfo;
    }

    
}
