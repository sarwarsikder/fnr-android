package com.apper.sarwar.fnr.model;

public class BuildingFlatListModel {

    private int Id;
    private String flatName;
    private String flatComponent;

    public BuildingFlatListModel(int id, String flatName, String flatComponent) {
        Id = id;
        this.flatName = flatName;
        this.flatComponent = flatComponent;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getFlatName() {
        return flatName;
    }

    public void setFlatName(String flatName) {
        this.flatName = flatName;
    }

    public String getFlatComponent() {
        return flatComponent;
    }

    public void setFlatComponent(String flatComponent) {
        this.flatComponent = flatComponent;
    }
}
