package com.elcom.media.enums;

/**
 *
 * @author anhdv
 */
public enum MediaStreamFileType {
    
    TS(".ts"),
    M3U8(".m3u8");
    
    private final String value;
    
    MediaStreamFileType(String value) {
        this.value = value;
    }
    
    public String toVal() {
        return value;
    }
}
