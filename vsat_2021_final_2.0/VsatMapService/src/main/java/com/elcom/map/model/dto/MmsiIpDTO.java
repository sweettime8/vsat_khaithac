package com.elcom.map.model.dto;

import java.io.Serializable;

/**
 *
 * @author anhdv
 */
public class MmsiIpDTO implements Serializable {
    
    private String ip;
    private String note;

    public MmsiIpDTO(String ip, String note) {
        this.ip = ip;
        this.note = note;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }
}
