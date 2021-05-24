package com.elcom.map.model.dto.request.map;

import java.io.Serializable;
import java.util.Date;

public class GetListAreaRequest implements Serializable {
    private Date startTime;
    private Date endTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
