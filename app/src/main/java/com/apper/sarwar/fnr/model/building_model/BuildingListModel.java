package com.apper.sarwar.fnr.model.building_model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildingListModel implements Serializable {
    private int Id;
    private String houseNumber;
    private String displayNumber;
    private int totalTasks;
    private int tasksDone;
    private int totalFlats;

    public BuildingListModel(int id, String houseNumber, String displayNumber, int totalTasks, int tasksDone, int totalFlats) {
        Id = id;
        this.houseNumber = houseNumber;
        this.displayNumber = displayNumber;
        this.totalTasks = totalTasks;
        this.tasksDone = tasksDone;
        this.totalFlats = totalFlats;
    }

    public BuildingListModel() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getDisplayNumber() {
        return displayNumber;
    }

    public void setDisplayNumber(String displayNumber) {
        this.displayNumber = displayNumber;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public int getTasksDone() {
        return tasksDone;
    }

    public void setTasksDone(int tasksDone) {
        this.tasksDone = tasksDone;
    }

    public int getTotalFlats() {
        return totalFlats;
    }

    public void setTotalFlats(int totalFlats) {
        this.totalFlats = totalFlats;
    }
}
