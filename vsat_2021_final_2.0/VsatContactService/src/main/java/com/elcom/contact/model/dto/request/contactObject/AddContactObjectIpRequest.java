package com.elcom.contact.model.dto.request.contactObject;

import java.util.UUID;

public class AddContactObjectIpRequest {
    private UUID uuid;
    private String ip_address;
    private String note;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID mmsi) {
        this.uuid = mmsi;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
