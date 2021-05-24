/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.contact.model.contact;

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
@Table(name = "vsat02.vessel_alt_info")
public class VesselAltInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "mmsi")
    private long mmsi;

    @Column(name = "operation_unit")
    private String operationUnit;

    @Column(name = "other_info")
    private String otherInfo;

    @Column(name = "owner")
    private String owner;

    @Column(name = "status")
    private String status;

    @Column(name = "unit")
    private String unit;
    
    @Column(name = "created_time")
    private Timestamp CreatedTime;

    @Column(name = "updated_time")
    private Timestamp updatedTime;

    public long getMmsi() {
        return mmsi;
    }

    public void setMmsi(long mmsi) {
        this.mmsi = mmsi;
    }

    public String getOperationUnit() {
        return operationUnit;
    }

    public void setOperationUnit(String operationUnit) {
        this.operationUnit = operationUnit;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
    
    
}
