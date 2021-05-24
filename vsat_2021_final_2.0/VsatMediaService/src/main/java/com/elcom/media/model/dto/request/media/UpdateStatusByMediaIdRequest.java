package com.elcom.media.model.dto.request.media;


import java.io.Serializable;

public class UpdateStatusByMediaIdRequest implements Serializable {
    
    private String uuidKey;

    /**
     * @return the uuidKey
     */
    public String getUuidKey() {
        return uuidKey;
    }

    /**
     * @param uuidKey the uuidKey to set
     */
    public void setUuidKey(String uuidKey) {
        this.uuidKey = uuidKey;
    }
}
