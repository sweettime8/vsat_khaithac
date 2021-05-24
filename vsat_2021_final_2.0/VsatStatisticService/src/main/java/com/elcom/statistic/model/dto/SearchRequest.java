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
public class SearchRequest {
    private Integer optionDate;   //theo ngày , theo tuần , theo tháng
    private String startDate;
    private String endDate;
    private String startWeek;
    private String endWeek;
    private String startMonth;
    private String endMonth;
    private String dataSource;
    private String sort;
    private int currentPage;
    private int rowsPerPage;
    private String keyword;

    public Integer getOptionDate() {
        return optionDate;
    }

    public void setOptionDate(Integer optionDate) {
        this.optionDate = optionDate;
    }
   
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(String startWeek) {
        this.startWeek = startWeek;
    }

    public String getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(String endWeek) {
        this.endWeek = endWeek;
    }

    public String getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(String startMonth) {
        this.startMonth = startMonth;
    }

    public String getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(String endMonth) {
        this.endMonth = endMonth;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    
}
