package com.apper.sarwar.fnr.model.user_model;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileModel implements Serializable {

    private int id;
    private String username;
    private String email;
    private String first_name;
    private String last_name;
    private String is_active;
    private String is_staff;
    private String avatar;
    private String address;
    private CurrentActivityModel currentActivityModel;

    public ProfileModel(int id, String username, String email, String first_name, String last_name, String is_active, String is_staff, String avatar, String address, CurrentActivityModel currentActivityModel) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.is_active = is_active;
        this.is_staff = is_staff;
        this.avatar = avatar;
        this.address = address;
        this.currentActivityModel = currentActivityModel;
    }

    public ProfileModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getIs_staff() {
        return is_staff;
    }

    public void setIs_staff(String is_staff) {
        this.is_staff = is_staff;
    }

    public String getAvatar() {
        return avatar;
    }

    @Nullable
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    @Nullable
    public void setAddress(String address) {
        this.address = address;
    }

    public CurrentActivityModel getCurrentActivityModel() {
        return currentActivityModel;
    }

    @Nullable
    public void setCurrentActivityModel(CurrentActivityModel currentActivityModel) {
        this.currentActivityModel = currentActivityModel;
    }
}
