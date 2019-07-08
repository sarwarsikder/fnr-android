package com.apper.sarwar.fnr.model.sub_component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubComponentDetailsModel implements Serializable {

    private int id;
    private String name;
    private String status;
    private String description;
    private String due_date;
    private String created_by_id;
    private String updated_by_id;
    private String created_at;
    private Timestamp updated_at;
    private Timestamp assign_to;

    public SubComponentDetailsModel(int id, String name, String status, String description, String due_date, String created_by_id, String updated_by_id, String created_at, Timestamp updated_at, Timestamp assign_to) {
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
    }

    public SubComponentDetailsModel() {
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

    public String getCreated_by_id() {
        return created_by_id;
    }

    public void setCreated_by_id(String created_by_id) {
        this.created_by_id = created_by_id;
    }

    public String getUpdated_by_id() {
        return updated_by_id;
    }

    public void setUpdated_by_id(String updated_by_id) {
        this.updated_by_id = updated_by_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public Timestamp getAssign_to() {
        return assign_to;
    }

    public void setAssign_to(Timestamp assign_to) {
        this.assign_to = assign_to;
    }
}
