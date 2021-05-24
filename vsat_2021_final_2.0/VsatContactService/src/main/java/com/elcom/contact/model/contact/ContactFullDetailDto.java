package com.elcom.contact.model.contact;

import java.util.List;

public class ContactFullDetailDto {
    private List<Object> event;
    private List<MmsiImage> images;
    private VesselInfo info;
    private VesselAltInfo vesselAltInfo;
    private List<ContactIP> ips;
    private List<Phone> phones;
    private List<Object> voyage;

    public List<Object> getEvent() {
        return event;
    }

    public void setEvent(List<Object> event) {
        this.event = event;
    }

    public List<MmsiImage> getImages() {
        return images;
    }

    public void setImages(List<MmsiImage> images) {
        this.images = images;
    }

    public VesselInfo getInfo() {
        return info;
    }

    public void setInfo(VesselInfo info) {
        this.info = info;
    }

    public List<ContactIP> getIps() {
        return ips;
    }

    public void setIps(List<ContactIP> ips) {
        this.ips = ips;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public List<Object> getVoyage() {
        return voyage;
    }

    public void setVoyage(List<Object> voyage) {
        this.voyage = voyage;
    }

    public VesselAltInfo getVesselAltInfo() {
        return vesselAltInfo;
    }

    public void setVesselAltInfo(VesselAltInfo vesselAltInfo) {
        this.vesselAltInfo = vesselAltInfo;
    }
    
    
}
