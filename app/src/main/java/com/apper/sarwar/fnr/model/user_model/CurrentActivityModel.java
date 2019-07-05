package com.apper.sarwar.fnr.model.user_model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentActivityModel implements Serializable {
    private int project_id;
    private String project_name;
    private int building_id;
    private String building_number;
    private int flat_id;
    private int flat_number;

    public CurrentActivityModel(int project_id, String project_name, int building_id, String building_number, int flat_id, int flat_number) {
        this.project_id = project_id;
        this.project_name = project_name;
        this.building_id = building_id;
        this.building_number = building_number;
        this.flat_id = flat_id;
        this.flat_number = flat_number;
    }

    public CurrentActivityModel() {
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public int getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(int building_id) {
        this.building_id = building_id;
    }

    public String getBuilding_number() {
        return building_number;
    }

    public void setBuilding_number(String building_number) {
        this.building_number = building_number;
    }

    public int getFlat_id() {
        return flat_id;
    }

    public void setFlat_id(int flat_id) {
        this.flat_id = flat_id;
    }

    public int getFlat_number() {
        return flat_number;
    }

    public void setFlat_number(int flat_number) {
        this.flat_number = flat_number;
    }
}
