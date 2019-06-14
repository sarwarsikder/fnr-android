package com.apper.sarwar.fnr.model;

public class BuildingPlanModel {

    private int Id;
    private String planName;

    public BuildingPlanModel(int id, String planName) {
        Id = id;
        this.planName = planName;
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
}
