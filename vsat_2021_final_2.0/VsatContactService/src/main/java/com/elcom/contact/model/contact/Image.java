package com.elcom.contact.model.contact;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
@Entity
public class Image implements Serializable {
    @Id
    @Column(name="mmsi")
    private int mmsi;

    @Column(name="image_name")
    private String imageName;

}
