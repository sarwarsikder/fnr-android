package com.apper.sarwar.fnr.model.building_model;

public class BuildingComponentListModel {

    private int Id;
    private String componentName;
    private int totalTask;
    private int taskDone;

    public BuildingComponentListModel(int id, String componentName, int totalTask, int taskDone) {
        Id = id;
        this.componentName = componentName;
        this.totalTask = totalTask;
        this.taskDone = taskDone;
    }

    public BuildingComponentListModel() {
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

    public int getTotalTask() {
        return totalTask;
    }

    public void setTotalTask(int totalTask) {
        this.totalTask = totalTask;
    }

    public int getTaskDone() {
        return taskDone;
    }

    public void setTaskDone(int taskDone) {
        this.taskDone = taskDone;
    }
}
