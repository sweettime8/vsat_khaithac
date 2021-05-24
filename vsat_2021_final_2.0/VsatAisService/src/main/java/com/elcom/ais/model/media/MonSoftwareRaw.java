package com.elcom.ais.model.media;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "\"MON_SOFTWARE_RAWS\"")
public class MonSoftwareRaw implements Serializable {
    @Id
    @Column(name = "\"ID\"")
    private String ID;
    @Column(name = "\"IP\"")
    private String IP;
    @Column(name = "\"MODULE_NAME\"")
    private String moduleName;
    @Column(name = "\"RECEIVED_TIME\"")
    private Date receivedTime;
    @Column(name = "\"PART_NAME\"")
    private long partName;
    @Column(name = "\"ADD_TIME\"")
    private Date addTime;
    @Column(name = "\"STATUS\"")
    private int status;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Date getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Date receivedTime) {
        this.receivedTime = receivedTime;
    }

    public long getPartName() {
        return partName;
    }

    public void setPartName(long partName) {
        this.partName = partName;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
