package com.elcom.media.model.dto.request.media;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetListlMediaByIdRelationRequest implements Serializable {
    
    private List<String> uuidLst;
    private Set<Long> partNameLst;

    /**
     * @return the uuidLst
     */
    public List<String> getUuidLst() {
        return uuidLst;
    }

    /**
     * @param uuidLst the uuidLst to set
     */
    public void setUuidLst(List<String> uuidLst) {
        this.uuidLst = uuidLst;
    }

    /**
     * @return the partNameLst
     */
    public Set<Long> getPartNameLst() {
        return partNameLst;
    }

    /**
     * @param partNameLst the partNameLst to set
     */
    public void setPartNameLst(Set<Long> partNameLst) {
        this.partNameLst = partNameLst;
    }
}
