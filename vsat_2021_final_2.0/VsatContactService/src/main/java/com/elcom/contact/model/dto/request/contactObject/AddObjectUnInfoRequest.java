package com.elcom.contact.model.dto.request.contactObject;
import java.sql.Timestamp;
import java.util.UUID;

public class AddObjectUnInfoRequest {
    private UUID uuid;
    private long country_id;
    private int dimA;
    private int dimB;
    private int dimC;
    private int dimD;
    private int chieudai;
    private int chieurong;
    private String name;
    private Timestamp createdTime;
    private Timestamp updatedTime;
    private String imagePath;
    private String userUpdate;
    private String sourceIP;
    private int sourcePort;
    private String destIP;
    private int destPort;

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

    public int getDimA() {
        return dimA;
    }

    public void setDimA(int dimA) {
        this.dimA = dimA;
    }

    public int getDimB() {
        return dimB;
    }

    public void setDimB(int dimB) {
        this.dimB = dimB;
    }

    public int getDimC() {
        return dimC;
    }

    public void setDimC(int dimC) {
        this.dimC = dimC;
    }

    public int getDimD() {
        return dimD;
    }

    public void setDimD(int dimD) {
        this.dimD = dimD;
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

    public String getUserUpdate() {
        return userUpdate;
    }

    public void setUserUpdate(String userUpdate) {
        this.userUpdate = userUpdate;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(int sourcePort) {
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

    


    public int getDestPort() {
        return destPort;
    }

    public void setDestPort(int destPort) {
        this.destPort = destPort;
    }

    public int getChieudai() {
        return chieudai;
    }

    public void setChieudai(int chieudai) {
        this.chieudai = chieudai;
    }

    public int getChieurong() {
        return chieurong;
    }

    public void setChieurong(int chieurong) {
        this.chieurong = chieurong;
    }
    
    
}
