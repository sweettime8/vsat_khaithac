package com.elcom.vsat.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "\"MON_HARDWARE_RAWS\"")
public class MonHardwareRaw implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "\"ID\"")
    private String ID;
    @Column(name = "\"IP\"")
    private String IP;
    @Column(name = "\"HARDWARE_NAME\"")
    private String hardwareName;
    @Column(name = "\"HDD_USED\"")
    private int hddUsed;
    @Column(name = "\"HDD_TOTAL\"")
    private int hddTotal;
    @Column(name = "\"RAM_USED\"")
    private int ramUsed;
    @Column(name = "\"RAM_TOTAL\"")
    private int ramTotal;
    @Column(name = "\"CPU_USED\"")
    private int cpuUsed;

    @Column(name = "\"RECEIVED_TIME\"")
    private Timestamp receivedTime;
    @Column(name = "\"PART_NAME\"")
    private int pathName;
    @Column(name = "\"ADD_TIME\"")
    private Timestamp addTime;

    @Column(name = "\"STATUS\"")
    private int status;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getPathName() {
        return pathName;
    }

    public void setPathName(int pathName) {
        this.pathName = pathName;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getHardwareName() {
        return hardwareName;
    }

    public void setHardwareName(String hardwareName) {
        this.hardwareName = hardwareName;
    }

    public int getHddUsed() {
        return hddUsed;
    }

    public void setHddUsed(int hddUsed) {
        this.hddUsed = hddUsed;
    }

    public int getHddTotal() {
        return hddTotal;
    }

    public void setHddTotal(int hddTotal) {
        this.hddTotal = hddTotal;
    }

    public int getRamUsed() {
        return ramUsed;
    }

    public void setRamUsed(int ramUsed) {
        this.ramUsed = ramUsed;
    }

    public int getRamTotal() {
        return ramTotal;
    }

    public void setRamTotal(int ramTotal) {
        this.ramTotal = ramTotal;
    }

    public int getCpuUsed() {
        return cpuUsed;
    }

    public void setCpuUsed(int cpuUsed) {
        this.cpuUsed = cpuUsed;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Timestamp receivedTime) {
        this.receivedTime = receivedTime;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }
}
