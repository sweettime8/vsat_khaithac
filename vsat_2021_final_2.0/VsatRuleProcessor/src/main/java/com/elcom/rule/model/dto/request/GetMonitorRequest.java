package com.elcom.rule.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetMonitorRequest implements Serializable {
    private List<String> ip;

    public List<String> getIp() {
        return ip;
    }

    public void setIp(List<String> ip) {
        this.ip = ip;
    }
}
