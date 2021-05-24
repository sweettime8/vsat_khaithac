package com.elcom.ais.model.dto;

/**
 *
 * @author anhdv
 */
public class UpsertProcessResult {
    
    private boolean success;
    private String message;

    public UpsertProcessResult(boolean success) {
        this.success = success;
    }
    
    public UpsertProcessResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @param success the success to set
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
