package com.elcom.contact.model.dto.request.contacLand;

public class SearchContactLandRequest {
    private String option;
    private String name;
    private String mmsi;
    private String ip;
    private String phone;
    private String callsign;
    private String country_ids;
    private String type_ids;
    private String min_mmsi;
    private String max_mmsi;
    private String min_imo;
    private String max_imo;
    private String min_height;
    private String max_height;
    private String min_width;
    private String max_width;
    private String sort_name;
    private int rows_start;
    private int rows_end;

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMmsi() {
        return mmsi;
    }

    public void setMmsi(String mmsi) {
        this.mmsi = mmsi;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public String getCountry_ids() {
        return country_ids;
    }

    public void setCountry_ids(String country_ids) {
        this.country_ids = country_ids;
    }

    public String getType_ids() {
        return type_ids;
    }

    public void setType_ids(String type_ids) {
        this.type_ids = type_ids;
    }

    public String getMin_mmsi() {
        return min_mmsi;
    }

    public void setMin_mmsi(String min_mmsi) {
        this.min_mmsi = min_mmsi;
    }

    public String getMax_mmsi() {
        return max_mmsi;
    }

    public void setMax_mmsi(String max_mmsi) {
        this.max_mmsi = max_mmsi;
    }

    public String getMin_imo() {
        return min_imo;
    }

    public void setMin_imo(String min_imo) {
        this.min_imo = min_imo;
    }

    public String getMax_imo() {
        return max_imo;
    }

    public void setMax_imo(String max_imo) {
        this.max_imo = max_imo;
    }

    public String getMin_height() {
        return min_height;
    }

    public void setMin_height(String min_height) {
        this.min_height = min_height;
    }

    public String getMax_height() {
        return max_height;
    }

    public void setMax_height(String max_height) {
        this.max_height = max_height;
    }

    public String getMin_width() {
        return min_width;
    }

    public void setMin_width(String min_width) {
        this.min_width = min_width;
    }

    public String getMax_width() {
        return max_width;
    }

    public void setMax_width(String max_width) {
        this.max_width = max_width;
    }

    public String getSort_name() {
        return sort_name;
    }

    public void setSort_name(String sort_name) {
        this.sort_name = sort_name;
    }

    public int getRows_start() {
        return rows_start;
    }

    public void setRows_start(int rows_start) {
        this.rows_start = rows_start;
    }

    public int getRows_end() {
        return rows_end;
    }

    public void setRows_end(int rows_end) {
        this.rows_end = rows_end;
    }
}
