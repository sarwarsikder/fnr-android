package com.apper.sarwar.fnr.model.sub_component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)

public class SubComponentModel implements Serializable {
    private int Id;
    private String componentName;
    private String componentDescription;
    private String CreatedTime;


    public SubComponentModel(int id, String componentName, String componentDescription, String createdTime) {
        Id = id;
        this.componentName = componentName;
        this.componentDescription = componentDescription;
        CreatedTime = createdTime;
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

    public String getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(String createdTime) {
        CreatedTime = createdTime;
    }
}
