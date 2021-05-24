package com.elcom.rule.model;

import java.io.Serializable;
import java.util.Date;

public class DataRawJDBC implements Serializable {
    private String uuid;
    private Date event_time;
    private Date proc_time;
    private int media_type;
    private String media_type_name;
    private String source_ip;
    private String src_mmsi;
    private String src_is_hq;
    private int src_country_id;
    private int src_type_id;
    private String src_name;
    private String src_extra;
    private String source_port;
    private String source_phone;
    private String dest_ip;
    private String dest_mmsi;
    private long dest_is_hq;
    private int dest_country_id;
    private int dest_type_id;
    private String dest_name;
    private String dest_extra;
    private String dest_port;
    private String dest_phone;
    private String file_path;
    private long fileSize;
    private int data_source;
    private int part_name;
    private int date_key;
    private Date ingest_time;
    private int status;
    private int direction;
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getEvent_time() {
        return event_time;
    }

    public void setEvent_time(Date event_time) {
        this.event_time = event_time;
    }

    public Date getProc_time() {
        return proc_time;
    }

    public void setProc_time(Date proc_time) {
        this.proc_time = proc_time;
    }

    public int getMedia_type() {
        return media_type;
    }

    public void setMedia_type(int media_type) {
        this.media_type = media_type;
    }

    public String getMedia_type_name() {
        return media_type_name;
    }

    public void setMedia_type_name(String media_type_name) {
        this.media_type_name = media_type_name;
    }

    public String getSource_ip() {
        return source_ip;
    }

    public void setSource_ip(String source_ip) {
        this.source_ip = source_ip;
    }

    public String getSrc_mmsi() {
        return src_mmsi;
    }

    public void setSrc_mmsi(String src_mmsi) {
        this.src_mmsi = src_mmsi;
    }

    public String getSrc_is_hq() {
        return src_is_hq;
    }

    public void setSrc_is_hq(String src_is_hq) {
        this.src_is_hq = src_is_hq;
    }

    public int getSrc_country_id() {
        return src_country_id;
    }

    public void setSrc_country_id(int src_country_id) {
        this.src_country_id = src_country_id;
    }

    public int getSrc_type_id() {
        return src_type_id;
    }

    public void setSrc_type_id(int src_type_id) {
        this.src_type_id = src_type_id;
    }

    public String getSrc_name() {
        return src_name;
    }

    public void setSrc_name(String src_name) {
        this.src_name = src_name;
    }

    public String getSrc_extra() {
        return src_extra;
    }

    public void setSrc_extra(String src_extra) {
        this.src_extra = src_extra;
    }

    public String getSource_port() {
        return source_port;
    }

    public void setSource_port(String source_port) {
        this.source_port = source_port;
    }

    public String getSource_phone() {
        return source_phone;
    }

    public void setSource_phone(String source_phone) {
        this.source_phone = source_phone;
    }

    public String getDest_ip() {
        return dest_ip;
    }

    public void setDest_ip(String dest_ip) {
        this.dest_ip = dest_ip;
    }

    public String getDest_mmsi() {
        return dest_mmsi;
    }

    public void setDest_mmsi(String dest_mmsi) {
        this.dest_mmsi = dest_mmsi;
    }

    public long getDest_is_hq() {
        return dest_is_hq;
    }

    public void setDest_is_hq(long dest_is_hq) {
        this.dest_is_hq = dest_is_hq;
    }

    public int getDest_country_id() {
        return dest_country_id;
    }

    public void setDest_country_id(int dest_country_id) {
        this.dest_country_id = dest_country_id;
    }

    public int getDest_type_id() {
        return dest_type_id;
    }

    public void setDest_type_id(int dest_type_id) {
        this.dest_type_id = dest_type_id;
    }

    public String getDest_name() {
        return dest_name;
    }

    public void setDest_name(String dest_name) {
        this.dest_name = dest_name;
    }

    public String getDest_extra() {
        return dest_extra;
    }

    public void setDest_extra(String dest_extra) {
        this.dest_extra = dest_extra;
    }

    public String getDest_port() {
        return dest_port;
    }

    public void setDest_port(String dest_port) {
        this.dest_port = dest_port;
    }

    public String getDest_phone() {
        return dest_phone;
    }

    public void setDest_phone(String dest_phone) {
        this.dest_phone = dest_phone;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getData_source() {
        return data_source;
    }

    public void setData_source(int data_source) {
        this.data_source = data_source;
    }

    public int getPart_name() {
        return part_name;
    }

    public void setPart_name(int part_name) {
        this.part_name = part_name;
    }

    public int getDate_key() {
        return date_key;
    }

    public void setDate_key(int date_key) {
        this.date_key = date_key;
    }

    public Date getIngest_time() {
        return ingest_time;
    }

    public void setIngest_time(Date ingest_time) {
        this.ingest_time = ingest_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
