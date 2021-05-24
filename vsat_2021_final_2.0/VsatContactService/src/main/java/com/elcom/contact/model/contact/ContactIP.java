package com.elcom.contact.model.contact;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
public class ContactIP implements Serializable {
    @Id
    @Column(name = "mmsi")
    private int mmsi;

    @Column(name = "type")
    private int type;

    @Id
    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "note")
    private String note;

    @Column(name="created_time")
    private Timestamp createTime;

    @Column(name="updated_time")
    private Timestamp updateTime;

    public int getMmsi() {
        return mmsi;
    }

    public void setMmsi(int mmsi) {
        this.mmsi = mmsi;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
