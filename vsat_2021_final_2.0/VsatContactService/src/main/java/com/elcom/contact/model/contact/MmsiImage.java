/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.contact.model.contact;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "vsat02.mmsi_image")
public class MmsiImage implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "mmsi")
    private long mmsi;
    
    @Column(name = "type")
    private int type;           //0- ảnh của tàu , 1- ảnh của bờ, 2- ảnh của sở chỉ huy
        
    @Column(name = "image_path")
    private String imagePath;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
  
    public long getMmsi() {
        return mmsi;
    }

    public void setMmsi(long mmsi) {
        this.mmsi = mmsi;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    
}
