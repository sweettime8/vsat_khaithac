package com.elcom.ais.model.dto.request.media;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchListMediaRequest implements Serializable {
    private Date start_time;
    private Date end_time;
    private String source_name;
    private String dest_name;
    private String source_mmsi;
    private String dest_mmsi;
    private String source_country;
    private String dest_country;
    private String source_ip;
    private String dest_ip;
    private String comment;
    private String status;
    private String hagtag;
    private String sort_name;
    private String rows_start;
    private String rows_end;
    private String user_name;
    private String media_type;
    private String source_phone;
    private String dest_phone;
    private String data_source;
    private String source_not_ip;
    private String dest_not_ip;
    private String type_file;
    private String note;
    private String flrl;
    private String seq;
    private String ipClient;

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getDest_name() {
        return dest_name;
    }

    public void setDest_name(String dest_name) {
        this.dest_name = dest_name;
    }

    public String getSource_mmsi() {
        return source_mmsi;
    }

    public void setSource_mmsi(String source_mmsi) {
        this.source_mmsi = source_mmsi;
    }

    public String getDest_mmsi() {
        return dest_mmsi;
    }

    public void setDest_mmsi(String dest_mmsi) {
        this.dest_mmsi = dest_mmsi;
    }

    public String getSource_country() {
        return source_country;
    }

    public void setSource_country(String source_country) {
        this.source_country = source_country;
    }

    public String getDest_country() {
        return dest_country;
    }

    public void setDest_country(String dest_country) {
        this.dest_country = dest_country;
    }

    public String getSource_ip() {
        return source_ip;
    }

    public void setSource_ip(String source_ip) {
        this.source_ip = source_ip;
    }

    public String getDest_ip() {
        return dest_ip;
    }

    public void setDest_ip(String dest_ip) {
        this.dest_ip = dest_ip;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHagtag() {
        return hagtag;
    }

    public void setHagtag(String hagtag) {
        this.hagtag = hagtag;
    }

    public String getSort_name() {
        return sort_name;
    }

    public void setSort_name(String sort_name) {
        this.sort_name = sort_name;
    }

    public String getRows_start() {
        return rows_start;
    }

    public void setRows_start(String rows_start) {
        this.rows_start = rows_start;
    }

    public String getRows_end() {
        return rows_end;
    }

    public void setRows_end(String rows_end) {
        this.rows_end = rows_end;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getSource_phone() {
        return source_phone;
    }

    public void setSource_phone(String source_phone) {
        this.source_phone = source_phone;
    }

    public String getDest_phone() {
        return dest_phone;
    }

    public void setDest_phone(String dest_phone) {
        this.dest_phone = dest_phone;
    }

    public String getData_source() {
        return data_source;
    }

    public void setData_source(String data_source) {
        this.data_source = data_source;
    }

    public String getSource_not_ip() {
        return source_not_ip;
    }

    public void setSource_not_ip(String source_not_ip) {
        this.source_not_ip = source_not_ip;
    }

    public String getDest_not_ip() {
        return dest_not_ip;
    }

    public void setDest_not_ip(String dest_not_ip) {
        this.dest_not_ip = dest_not_ip;
    }

    public String getType_file() {
        return type_file;
    }

    public void setType_file(String type_file) {
        this.type_file = type_file;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFlrl() {
        return flrl;
    }

    public void setFlrl(String flrl) {
        this.flrl = flrl;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getIpClient() {
        return ipClient;
    }

    public void setIpClient(String ipClient) {
        this.ipClient = ipClient;
    }
}
