package com.elcom.contact.model.contactLand;

import com.elcom.contact.model.contact.ContactIP;
import com.elcom.contact.model.contact.Phone;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

@Entity
public class HeadQuarterDto implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "country_id")
    private int countryId;
    @Column(name = "country_name")
    private String countryName;
    @Column(name = "longitude")
    private float longitude;
    @Column(name = "latitude")
    private float latitude;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private int status;

    @Column(name = "user_updated")
    private String username;
    @Column(name = "updated_time")
    private String updatedTime;
    @Column(name = "vai_tro")
    private String vaiTro;
    @Column(name = "chuc_nang")
    private String chucNang;
    @Column(name = "to_chuc")
    private String to_chuc;
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public String getChucNang() {
        return chucNang;
    }

    public void setChucNang(String chucNang) {
        this.chucNang = chucNang;
    }

    public String getTo_chuc() {
        return to_chuc;
    }

    public void setTo_chuc(String to_chuc) {
        this.to_chuc = to_chuc;
    }  
}
