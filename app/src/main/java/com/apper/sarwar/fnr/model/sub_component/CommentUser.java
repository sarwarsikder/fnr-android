package com.apper.sarwar.fnr.model.sub_component;

public class CommentUser {
    private String name;
    private String avatar;

    public CommentUser(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    public CommentUser() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}