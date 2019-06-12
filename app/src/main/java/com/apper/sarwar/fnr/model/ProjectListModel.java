package com.apper.sarwar.fnr.model;

public class ProjectListModel {

    private int projectId;
    private String projectTitle;
    private String projectLocation;
    private String projectProgressCount;
    private int projectProgress;

    public ProjectListModel(int projectId, String projectTitle, String projectLocation, String projectProgressCount, int projectProgress) {
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.projectLocation = projectLocation;
        this.projectProgressCount = projectProgressCount;
        this.projectProgress = projectProgress;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectLocation() {
        return projectLocation;
    }

    public void setProjectLocation(String projectLocation) {
        this.projectLocation = projectLocation;
    }

    public String getProjectProgressCount() {
        return projectProgressCount;
    }

    public void setProjectProgressCount(String projectProgressCount) {
        this.projectProgressCount = projectProgressCount;
    }

    public int getGetProjectProgress() {
        return projectProgress;
    }

    public void setGetProjectProgress(int projectProgress) {
        this.projectProgress = projectProgress;
    }
}
