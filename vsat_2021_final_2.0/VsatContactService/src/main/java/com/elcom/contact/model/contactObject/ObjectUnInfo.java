package com.elcom.contact.model.contactObject;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;


@Entity
@Table(name = "\"object_undefined_info\"")
public class ObjectUnInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "uuid")
    @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID uuid;

    @Column(name = "country_id")
    private long country_id;

    @Column(name="dim_a")
    private int dimA;

    @Column(name="dim_b")
    private int dimB;

    @Column(name="dim_c")
    private int dimC;

    @Column(name="dim_d")
    private int dimD;

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
    private String sourceIp;

    @Column(name="source_port")
    private int sourcePort;

    @Column(name="dest_ip")
    private String destIp;

    @Column(name="dest_port")
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

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(int sourcePort) {
        this.sourcePort = sourcePort;
    }

    public String getDestIp() {
        return destIp;
    }

    public void setDestIp(String destIp) {
        this.destIp = destIp;
    }

    public int getDestPort() {
        return destPort;
    }

    public void setDestPort(int destPort) {
        this.destPort = destPort;
    }
}
