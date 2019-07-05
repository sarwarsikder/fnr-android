package com.apper.sarwar.fnr.service.iservice;

import org.json.JSONObject;

public interface NotificationIService {

    void onNotificationSuccess(JSONObject notificationListModel);

    void onNotificationFailed(JSONObject jsonObject);
}
