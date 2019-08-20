package com.apper.sarwar.fnr.model.sub_component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)

public class SubComponentModel implements Serializable {
    private int Id;
    private String componentName;
    private String componentDescription;
    private String CreatedTime;
    private Date due_date;
    private String status;


    public SubComponentModel(int id, String componentName, String componentDescription, String createdTime, Date due_date, String status) {
        Id = id;
        this.componentName = componentName;
        this.componentDescription = componentDescription;
        CreatedTime = createdTime;
        this.due_date = due_date;
        this.status = status;
    }

    public SubComponentModel() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentDescription() {
        return componentDescription;
    }

    public void setComponentDescription(String componentDescription) {
        this.componentDescription = componentDescription;
    }

    public Date getDue_date() {
        return due_date;
    }

    public void setDue_date(Date due_date) {
        this.due_date = due_date;
    }

    public String getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(String createdTime) {
        CreatedTime = createdTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
