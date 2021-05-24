package com.elcom.contact.model.contact;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
public class VesselInfo implements Serializable {
    @Id
    @Column(name = "mmsi")
    private Long mmsi;
    
    @Column(name = "imo")
    private Long imo;
    
    @Column(name = "country_id")
    private Long country_id;

    @Column(name = "type_id")
    private Integer type_id;

    @Column(name="chieudai")
    private String chieudai;

    @Column(name="chieurong")
    private String chieurong;

    @Column(name="callsign")
    private String callsign;

    @Column(name="name")
    private String name;

    @Column(name="created_time")
    private Timestamp createdTime;

    @Column(name="updated_time")
    private Timestamp updatedTime;

    @Column(name="name_static")
    private String nameStatic;

    @Column(name="is_master")
    private Integer isMaster;

    @Column(name="image_path")
    private String imagePath;

    @Column(name="satellite_phone_code")
    private String satellitePhoneCode;

    @Column(name="year_of_build")
    private String yearOfBuild;

    @Column(name="place_of_build_code")
    private Integer placeOfBuildCode;

    @Column(name="draugth")
    private String draugth;

    @Column(name="engine_type")
    private String engineType;

    @Column(name="gross_tonnage")
    private String grossTonnage;

    @Column(name="dead_weight")
    private String deadWeight;

    @Column(name="name_place")
    private String namePlace;

    @Column(name="speed_avg")
    private Double speedAvg;

    @Column(name="speed_max")
    private Double speedMax;

    @Column(name="displacement")
    private String displacement;

    @Column(name="crew")
    private String crew;

    @Column(name="weapons")
    private String weapons;

    @Column(name="endurance")
    private String endurance;

    @Column(name="country_name")
    private String countryName;

    @Column(name="country_flag")
    private String countryFlag;

    @Column(name="vessel_type_name")
    private String vesselTypeName;

    @Column(name="vessel_type_desc")
    private String vesselTypeDesc;

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

    public Long getCountry_id() {
        return country_id;
    }

    public void setCountry_id(Long country_id) {
        this.country_id = country_id;
    }

    public Integer getType_id() {
        return type_id;
    }

    public void setType_id(Integer type_id) {
        this.type_id = type_id;
    }

    public String getChieudai() {
        return chieudai;
    }

    public void setChieudai(String chieudai) {
        this.chieudai = chieudai;
    }

    public String getChieurong() {
        return chieurong;
    }

    public void setChieurong(String chieurong) {
        this.chieurong = chieurong;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getYearOfBuild() {
        return yearOfBuild;
    }

    public void setYearOfBuild(String yearOfBuild) {
        this.yearOfBuild = yearOfBuild;
    }

    public Integer getPlaceOfBuildCode() {
        return placeOfBuildCode;
    }

    public void setPlaceOfBuildCode(Integer placeOfBuildCode) {
        this.placeOfBuildCode = placeOfBuildCode;
    }

    public String getDraugth() {
        return draugth;
    }

    public void setDraugth(String draugth) {
        this.draugth = draugth;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public String getGrossTonnage() {
        return grossTonnage;
    }

    public void setGrossTonnage(String grossTonnage) {
        this.grossTonnage = grossTonnage;
    }

    public String getDeadWeight() {
        return deadWeight;
    }

    public void setDeadWeight(String deadWeight) {
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

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryFlag() {
        return countryFlag;
    }

    public void setCountryFlag(String countryFlag) {
        this.countryFlag = countryFlag;
    }

    public String getVesselTypeName() {
        return vesselTypeName;
    }

    public void setVesselTypeName(String vesselTypeName) {
        this.vesselTypeName = vesselTypeName;
    }

    public String getVesselTypeDesc() {
        return vesselTypeDesc;
    }

    public void setVesselTypeDesc(String vesselTypeDesc) {
        this.vesselTypeDesc = vesselTypeDesc;
    }

 
}
