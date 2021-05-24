package com.elcom.rule.model.media;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "\"MEDIA_TYPE\"")
public class MediaType implements Serializable {
    @Id
    @Column(name = "\"ID\"")
    private long ID;
    @Column(name = "\"NAME\"")
    private String name;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
