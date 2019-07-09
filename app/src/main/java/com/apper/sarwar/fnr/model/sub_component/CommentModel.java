package com.apper.sarwar.fnr.model.sub_component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)

public class CommentModel implements Serializable {

    private String text;
    private String type;
    private String file_type;
    private CommentUser commentUser;

    public CommentModel(String text, String type, String file_type, CommentUser commentUser) {
        this.text = text;
        this.type = type;
        this.file_type = file_type;
        this.commentUser = commentUser;
    }

    public CommentModel() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public CommentUser getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(CommentUser commentUser) {
        this.commentUser = commentUser;
    }

}



