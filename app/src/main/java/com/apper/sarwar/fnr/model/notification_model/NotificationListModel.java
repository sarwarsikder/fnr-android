package com.apper.sarwar.fnr.model.notification_model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationListModel implements Serializable {
    private int Id;
    private String status;
    private String userName;
    private String userPic;
    private String notificationText;
    private String notificationCreatedTime;

    public NotificationListModel(int id, String status, String userName, String userPic, String notificationText, String notificationCreatedTime) {
        Id = id;
        this.status = status;
        this.userName = userName;
        this.userPic = userPic;
        this.notificationText = notificationText;
        this.notificationCreatedTime = notificationCreatedTime;
    }

    public NotificationListModel() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public String getNotificationCreatedTime() {
        return notificationCreatedTime;
    }

    public void setNotificationCreatedTime(String notificationCreatedTime) {
        this.notificationCreatedTime = notificationCreatedTime;
    }
}
