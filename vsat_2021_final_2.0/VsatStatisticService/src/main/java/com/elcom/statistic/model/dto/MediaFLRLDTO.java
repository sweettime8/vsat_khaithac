/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.statistic.model.dto;

import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class MediaFLRLDTO implements Serializable{
    private String dataSource;   //theo ngày , theo tuần , theo tháng
    private String byDate;
    private Long totalUndef;
    private Long totalFL;
    private Long totalRL;
    private Long totalALL;

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

    public Long getTotalUndef() {
        return totalUndef;
    }

    public void setTotalUndef(Long totalUndef) {
        this.totalUndef = totalUndef;
    }

    public Long getTotalFL() {
        return totalFL;
    }

    public void setTotalFL(Long totalFL) {
        this.totalFL = totalFL;
    }

    public Long getTotalRL() {
        return totalRL;
    }

    public void setTotalRL(Long totalRL) {
        this.totalRL = totalRL;
    }

    public Long getTotalALL() {
        return totalALL;
    }

    public void setTotalALL(Long totalALL) {
        this.totalALL = totalALL;
    }


    
}
