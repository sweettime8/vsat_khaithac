/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.statistic.model.dto;

/**
 *
 * @author Admin
 */
public class StatisticMediaDTO {
    private String dataSource;   //theo ngày , theo tuần , theo tháng
    private String dataSourceName;   //theo ngày , theo tuần , theo tháng
    private String byDate;
    private String direction;
    private Long fileSize;

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

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }
    
     
}
