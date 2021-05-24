package com.elcom.contact.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "source")
public class Source implements Serializable {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "source_name")
    private String sourceName;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "note")
    private String note;

    @Column(name = "band")
    private String band;

    @Column(name = "location")
    private String location;

    @Column(name = "frequencys")
    private String frequencys;

    @Column(name = "polarized")
    private String polarized;

    @Column(name = "vendor")
    private String vendor;

    @Column(name = "system")
    private String system;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFrequencys() {
        return frequencys;
    }

    public void setFrequencys(String frequencys) {
        this.frequencys = frequencys;
    }

    public String getPolarized() {
        return polarized;
    }

    public void setPolarized(String polarized) {
        this.polarized = polarized;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }
}
