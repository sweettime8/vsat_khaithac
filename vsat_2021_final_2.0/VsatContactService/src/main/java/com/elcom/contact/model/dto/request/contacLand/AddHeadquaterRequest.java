package com.elcom.contact.model.dto.request.contacLand;

import java.sql.Timestamp;

public class AddHeadquaterRequest {
    private String id;
    private String name;
    private int country;
    private float longitude;
    private float latitude;
    private String vai_tro;
    private String chuc_nang;
    private String to_chuc;
    private String note;
    private int status;
    private String username;
    private String updateTime;

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVai_tro() {
        return vai_tro;
    }

    public void setVai_tro(String vai_tro) {
        this.vai_tro = vai_tro;
    }

    public String getChuc_nang() {
        return chuc_nang;
    }

    public void setChuc_nang(String chuc_nang) {
        this.chuc_nang = chuc_nang;
    }

    public String getTo_chuc() {
        return to_chuc;
    }

    public void setTo_chuc(String to_chuc) {
        this.to_chuc = to_chuc;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    

    
}
