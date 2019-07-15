package com.apper.sarwar.fnr.model.sub_component;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class TaskDetailsCommentsModel {


    private String text;
    private String type;
    private ArrayList<TaskDetailsCommentFileTypeModel> file_type;
    private CommentUser user;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp created_at;

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

    public ArrayList<TaskDetailsCommentFileTypeModel> getFile_type() {
        return file_type;
    }

    public void setFile_type(ArrayList<TaskDetailsCommentFileTypeModel> file_type) {
        this.file_type = file_type;
    }

    public CommentUser getUser() {
        return user;
    }

    public void setUser(CommentUser user) {
        this.user = user;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
