package com.elcom.contact.model.contactObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
public class ObjectUnInfoDto implements Serializable {
    @Id
    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "country_id")
    private long country_id;

    @Column(name="chieudai")
    private String chieudai;

    @Column(name="chieurong")
    private String chieurong;


    @Column(name="name")
    private String name;

    @Column(name="created_time")
    private Timestamp createdTime;

    @Column(name="updated_time")
    private Timestamp updatedTime;


    @Column(name="image_path")
    private String imagePath;
    
    @Column(name="user_update")
    private String userUpdate;
    
    @Column(name="source_ip")
    private String sourceIP;    
    
    @Column(name="source_port")
    private Integer sourcePort;   
    
    @Column(name="dest_ip")
    private String destIP;    
    
    @Column(name="dest_port")
    private Integer destPort;      
    
    @Column(name="country_name")
    private String countryName;

    @Column(name="country_flag")
    private String countryFlag;
    
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public long getCountry_id() {
        return country_id;
    }

    public void setCountry_id(long country_id) {
        this.country_id = country_id;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    public String getUserUpdate() {
        return userUpdate;
    }

    public void setUserUpdate(String userUpdate) {
        this.userUpdate = userUpdate;
    }

    public Integer getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(Integer sourcePort) {
        this.sourcePort = sourcePort;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public void setSourceIP(String sourceIP) {
        this.sourceIP = sourceIP;
    }

    public String getDestIP() {
        return destIP;
    }

    public void setDestIP(String destIP) {
        this.destIP = destIP;
    }


    public Integer getDestPort() {
        return destPort;
    }

    public void setDestPort(Integer destPort) {
        this.destPort = destPort;
    }
    
    
}
