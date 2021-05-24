package com.elcom.media.model.dto.request.media;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetDetailMediaRequest implements Serializable {
    
    private String filePathLocal;

    /**
     * @return the filePathLocal
     */
    public String getFilePathLocal() {
        return filePathLocal;
    }

    /**
     * @param filePathLocal the filePathLocal to set
     */
    public void setFilePathLocal(String filePathLocal) {
        this.filePathLocal = filePathLocal;
    }
}
