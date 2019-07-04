package com.apper.sarwar.fnr.service.iservice;

import org.json.JSONObject;

public interface ProfileIService {

    void onProjectSuccess(JSONObject projectListModel);

    void onProjectFailed(JSONObject jsonObject);
}
