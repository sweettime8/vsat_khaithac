package com.elcom.contact.model.dto.request.media;

import java.io.Serializable;

public class AddCommentRequest implements Serializable {
    private int cmt_type;
    private String category_id;
    private String hagtag;
    private String comment;

    public int getCmt_type() {
        return cmt_type;
    }

    public void setCmt_type(int cmt_type) {
        this.cmt_type = cmt_type;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getHagtag() {
        return hagtag;
    }

    public void setHagtag(String hagtag) {
        this.hagtag = hagtag;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
