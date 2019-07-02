package com.apper.sarwar.fnr.model.building_model;

public class BuildingFlatListModel {

    private int Id;
    private String flatNumber;
    private String flatDescription;
    private String flatClientName;
    private String flatClientAddress;
    private String flatClientEmail;
    private String flatClientTel;
    private int flatTotalTask;
    private int flatTotalTaskDone;


    public BuildingFlatListModel(int id, String flatNumber, String flatDescription, String flatClientName, String flatClientAddress, String flatClientEmail, String flatClientTel, int flatTotalTask, int flatTotalTaskDone) {
        Id = id;
        this.flatNumber = flatNumber;
        this.flatDescription = flatDescription;
        this.flatClientName = flatClientName;
        this.flatClientAddress = flatClientAddress;
        this.flatClientEmail = flatClientEmail;
        this.flatClientTel = flatClientTel;
        this.flatTotalTask = flatTotalTask;
        this.flatTotalTaskDone = flatTotalTaskDone;
    }

    public BuildingFlatListModel() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public String getFlatDescription() {
        return flatDescription;
    }

    public void setFlatDescription(String flatDescription) {
        this.flatDescription = flatDescription;
    }

    public String getFlatClientName() {
        return flatClientName;
    }

    public void setFlatClientName(String flatClientName) {
        this.flatClientName = flatClientName;
    }

    public String getFlatClientAddress() {
        return flatClientAddress;
    }

    public void setFlatClientAddress(String flatClientAddress) {
        this.flatClientAddress = flatClientAddress;
    }

    public String getFlatClientEmail() {
        return flatClientEmail;
    }

    public void setFlatClientEmail(String flatClientEmail) {
        this.flatClientEmail = flatClientEmail;
    }

    public String getFlatClientTel() {
        return flatClientTel;
    }

    public void setFlatClientTel(String flatClientTel) {
        this.flatClientTel = flatClientTel;
    }

    public int getFlatTotalTask() {
        return flatTotalTask;
    }

    public void setFlatTotalTask(int flatTotalTask) {
        this.flatTotalTask = flatTotalTask;
    }

    public int getFlatTotalTaskDone() {
        return flatTotalTaskDone;
    }

    public void setFlatTotalTaskDone(int flatTotalTaskDone) {
        this.flatTotalTaskDone = flatTotalTaskDone;
    }
}
