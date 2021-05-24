/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.statistic.model.dto;

import java.io.Serializable;

/**
 *
 * @author ducnh
 */
public class DatasourceDTO implements Serializable{

    private String dataSource;  
    private String dataSourceName;  
    private String byDate;
    private Long totalData;
    private Long totalTopic;

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getByDate() {
        return byDate;
    }

    public void setByDate(String byDate) {
        this.byDate = byDate;
    }

    public Long getTotalData() {
        return totalData;
    }

    public void setTotalData(Long totalData) {
        this.totalData = totalData;
    }

    public Long getTotalTopic() {
        return totalTopic;
    }

    public void setTotalTopic(Long totalTopic) {
        this.totalTopic = totalTopic;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }
    
    
}
