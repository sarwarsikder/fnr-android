package com.apper.sarwar.fnr.model.building_model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildingPlanModel implements Serializable {

    private int Id;
    private String planName;
    private String planFile;
    private String fileType;

    public BuildingPlanModel(int id, String planName, String planFile, String fileType) {
        Id = id;
        this.planName = planName;
        this.planFile = planFile;
        this.fileType = fileType;
    }

    public BuildingPlanModel() {

    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanFile() {
        return planFile;
    }

    public void setPlanFile(String planFile) {
        this.planFile = planFile;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
