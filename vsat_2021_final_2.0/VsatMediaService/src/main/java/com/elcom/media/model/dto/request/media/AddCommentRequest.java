package com.elcom.media.model.dto.request.media;

import java.io.Serializable;

public class AddCommentRequest implements Serializable {
    
    private Integer commentTypeId;
    private String refId;
    private String hagtag;
    private String content;
    private String createUser;

    public AddCommentRequest(Integer commentTypeId, String refId, String hagtag, String content, String createUser) {
        this.commentTypeId = commentTypeId;
        this.refId = refId;
        this.hagtag = hagtag;
        this.content = content;
        this.createUser = createUser;
    }

    /**
     * @return the refId
     */
    public String getRefId() {
        return refId;
    }

    /**
     * @param refId the refId to set
     */
    public void setRefId(String refId) {
        this.refId = refId;
    }

    /**
     * @return the hagtag
     */
    public String getHagtag() {
        return hagtag;
    }

    /**
     * @param hagtag the hagtag to set
     */
    public void setHagtag(String hagtag) {
        this.hagtag = hagtag;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the commentTypeId
     */
    public Integer getCommentTypeId() {
        return commentTypeId;
    }

    /**
     * @param commentTypeId the commentTypeId to set
     */
    public void setCommentTypeId(Integer commentTypeId) {
        this.commentTypeId = commentTypeId;
    }

    /**
     * @return the createUser
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser the createUser to set
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
}
