package com.elcom.rule.model.dto;

import java.io.Serializable;

/**
 *
 * @author anhdv
 */
public class MmsiPhoneDTO implements Serializable {
    
    private String phone;
    private String note;

    public MmsiPhoneDTO(String phone, String note) {
        this.phone = phone;
        this.note = note;
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

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
