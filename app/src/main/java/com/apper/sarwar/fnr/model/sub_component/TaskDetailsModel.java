package com.apper.sarwar.fnr.model.sub_component;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.util.ArrayList;

public class TaskDetailsModel {

    private int id;
    private String name;
    private String status;
    private String description;
    private String due_date = null;
    private float created_by_id;
    private float updated_by_id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp created_at;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp updated_at;
    private String assign_to = null;
    ArrayList<TaskStatusModel> status_list = new ArrayList<TaskStatusModel>();
    ArrayList<TaskDetailsCommentsModel> comments = new ArrayList<TaskDetailsCommentsModel>();
    private boolean more_comments;
    private int total_comments;


    public TaskDetailsModel(int id,
                            String name,
                            String status,
                            String description,
                            String due_date,
                            float created_by_id,
                            float updated_by_id,
                            Timestamp created_at,
                            Timestamp updated_at,
                            String assign_to,
                            ArrayList<TaskStatusModel> status_list,
                            ArrayList<TaskDetailsCommentsModel> comments,
                            boolean more_comments,
                            int total_comments) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;
        this.due_date = due_date;
        this.created_by_id = created_by_id;
        this.updated_by_id = updated_by_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.assign_to = assign_to;
        this.status_list = status_list;
        this.comments = comments;
        this.more_comments = more_comments;
        this.total_comments = total_comments;
    }

    public TaskDetailsModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public float getCreated_by_id() {
        return created_by_id;
    }

    public void setCreated_by_id(float created_by_id) {
        this.created_by_id = created_by_id;
    }

    public float getUpdated_by_id() {
        return updated_by_id;
    }

    public void setUpdated_by_id(float updated_by_id) {
        this.updated_by_id = updated_by_id;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public String getAssign_to() {
        return assign_to;
    }

    public void setAssign_to(String assign_to) {
        this.assign_to = assign_to;
    }

    public ArrayList<TaskStatusModel> getStatus_list() {
        return status_list;
    }

    public void setStatus_list(ArrayList<TaskStatusModel> status_list) {
        this.status_list = status_list;
    }

    public ArrayList<TaskDetailsCommentsModel> getComments() {
        return comments;
    }

    public void setComments(ArrayList<TaskDetailsCommentsModel> comments) {
        this.comments = comments;
    }

    public boolean isMore_comments() {
        return more_comments;
    }

    public void setMore_comments(boolean more_comments) {
        this.more_comments = more_comments;
    }

    public int getTotal_comments() {
        return total_comments;
    }

    public void setTotal_comments(int total_comments) {
        this.total_comments = total_comments;
    }
}
