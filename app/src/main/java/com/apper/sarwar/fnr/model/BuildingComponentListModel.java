package com.apper.sarwar.fnr.model;

public class BuildingComponentListModel {

    private int Id;
    private String componentName;
    private String componentCount;
    private int componentProgress;

    public BuildingComponentListModel(int id, String componentName, String componentCount, int componentProgress) {
        Id = id;
        this.componentName = componentName;
        this.componentCount = componentCount;
        this.componentProgress = componentProgress;
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

    public String getComponentCount() {
        return componentCount;
    }

    public void setComponentCount(String componentCount) {
        this.componentCount = componentCount;
    }

    public int getComponentProgress() {
        return componentProgress;
    }

    public void setComponentProgress(int componentProgress) {
        this.componentProgress = componentProgress;
    }
}
