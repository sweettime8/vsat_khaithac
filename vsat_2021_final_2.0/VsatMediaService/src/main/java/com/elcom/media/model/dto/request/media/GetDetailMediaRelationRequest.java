package com.elcom.media.model.dto.request.media;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetDetailMediaRelationRequest implements Serializable {
    
    private String uuidKeyFrom;
    private String uuidKeyTo;
    private Integer partName;

    /**
     * @return the uuidKeyFrom
     */
    public String getUuidKeyFrom() {
        return uuidKeyFrom;
    }

    /**
     * @param uuidKeyFrom the uuidKeyFrom to set
     */
    public void setUuidKeyFrom(String uuidKeyFrom) {
        this.uuidKeyFrom = uuidKeyFrom;
    }

    /**
     * @return the uuidKeyTo
     */
    public String getUuidKeyTo() {
        return uuidKeyTo;
    }

    /**
     * @param uuidKeyTo the uuidKeyTo to set
     */
    public void setUuidKeyTo(String uuidKeyTo) {
        this.uuidKeyTo = uuidKeyTo;
    }

    /**
     * @return the partName
     */
    public Integer getPartName() {
        return partName;
    }

    /**
     * @param partName the partName to set
     */
    public void setPartName(Integer partName) {
        this.partName = partName;
    }
}
