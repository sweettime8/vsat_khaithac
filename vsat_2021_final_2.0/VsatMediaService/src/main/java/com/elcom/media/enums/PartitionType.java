package com.elcom.media.enums;

/**
 *
 * @author anhdv
 */
public enum PartitionType {
    
    PARTITION_TYPE_MONTH(1),
    PARTITION_TYPE_DAY(2);
    
    private final int value;
    
    PartitionType(int value) {
        this.value = value;
    }
    
    public int toVal() {
        return value;
    }
}
