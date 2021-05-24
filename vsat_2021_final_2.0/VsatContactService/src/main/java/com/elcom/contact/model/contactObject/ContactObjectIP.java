package com.elcom.contact.model.contactObject;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
public class ContactObjectIP implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
//    @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
    @Column(name = "ufo_id")
    private UUID ufoId;

    @Column(name = "type")
    private int type;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "note")
    private String note;

    @Column(name="created_time")
    private Timestamp createTime;

    @Column(name="updated_time")
    private Timestamp updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUfoId() {
        return ufoId;
    }

    public void setUfoId(UUID ufoId) {
        this.ufoId = ufoId;
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
