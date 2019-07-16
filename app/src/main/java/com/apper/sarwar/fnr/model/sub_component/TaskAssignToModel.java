package com.apper.sarwar.fnr.model.sub_component;

public class TaskAssignToModel {

    private String company_name;
    private String avatar;

    public TaskAssignToModel(String company_name, String avatar) {
        this.company_name = company_name;
        this.avatar = avatar;
    }

    public TaskAssignToModel() {
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
