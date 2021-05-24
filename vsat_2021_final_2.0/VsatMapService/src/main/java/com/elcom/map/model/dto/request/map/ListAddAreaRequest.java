package com.elcom.map.model.dto.request.map;

import java.io.Serializable;
import java.util.List;

public class ListAddAreaRequest implements Serializable {
    private List<AddAreaRequest> listAddAreaRequest;

    public List<AddAreaRequest> getListAddAreaRequest() {
        return listAddAreaRequest;
    }

    public void setListAddAreaRequest(List<AddAreaRequest> listAddAreaRequest) {
        this.listAddAreaRequest = listAddAreaRequest;
    }
}
