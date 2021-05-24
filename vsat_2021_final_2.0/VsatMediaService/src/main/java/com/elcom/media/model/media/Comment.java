package com.elcom.media.model.media;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "comments")
public class Comment implements Serializable {
    
    @Id
    @Column(name = "id")
    private BigDecimal id;
    
    @Column(name = "comment_type_id")
    private Integer commentTypeId;
    
    @Column(name = "ref_id")
    private String refId;

    @Column(name = "v_comment")
    private String content;
    
    @Column(name = "v_hagtag")
    private String hagtag;
    
    @Column(name = "create_user")
    private String createUser;
    
    @Column(name = "update_user")
    private String updateUser;
    
    @Column(name = "created_time")
    private Timestamp createdTime;
    
    @Column(name = "updated_time")
    private Timestamp updatedTime;

    /**
     * @return the id
     */
    public BigDecimal getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(BigDecimal id) {
        this.id = id;
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

    /**
     * @return the updateUser
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * @param updateUser the updateUser to set
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * @return the createdTime
     */
    public Timestamp getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime the createdTime to set
     */
    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return the updatedTime
     */
    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime the updatedTime to set
     */
    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }
}
