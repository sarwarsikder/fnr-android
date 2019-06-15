package com.apper.sarwar.fnr.model.building_model;

public class BuildingListModel {
    private int Id;
    private String buildingName;
    private String buildingTask;
    private String buildingFlat;

    public BuildingListModel(int id, String buildingName, String buildingTask, String buildingFlat) {
        Id = id;
        this.buildingName = buildingName;
        this.buildingTask = buildingTask;
        this.buildingFlat = buildingFlat;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingTask() {
        return buildingTask;
    }

    public void setBuildingTask(String buildingTask) {
        this.buildingTask = buildingTask;
    }

    public String getBuildingFlat() {
        return buildingFlat;
    }

    public void setBuildingFlat(String buildingFlat) {
        this.buildingFlat = buildingFlat;
    }
}
