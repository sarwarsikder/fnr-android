package com.apper.sarwar.fnr.model.project_model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectListModel implements Serializable {

    private int id;
    private String address;
    private String description;
    private String city;
    private String type;
    private String energetic_standard;
    private int total_tasks;
    private int tasks_done;

    public ProjectListModel(int id, String address, String description, String city, String type, String energetic_standard, int total_tasks, int tasks_done) {
        this.id = id;
        this.address = address;
        this.description = description;
        this.city = city;
        this.type = type;
        this.energetic_standard = energetic_standard;
        this.total_tasks = total_tasks;
        this.tasks_done = tasks_done;
    }

    public ProjectListModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEnergetic_standard() {
        return energetic_standard;
    }

    public void setEnergetic_standard(String energetic_standard) {
        this.energetic_standard = energetic_standard;
    }

    public int getTotal_tasks() {
        return total_tasks;
    }

    public void setTotal_tasks(int total_tasks) {
        this.total_tasks = total_tasks;
    }

    public int getTasks_done() {
        return tasks_done;
    }

    public void setTasks_done(int tasks_done) {
        this.tasks_done = tasks_done;
    }
}
