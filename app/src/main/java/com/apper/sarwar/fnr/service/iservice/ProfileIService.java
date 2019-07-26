package com.apper.sarwar.fnr.service.iservice;

import org.json.JSONObject;

public interface ProfileIService {

    void onProfileSuccess(JSONObject profileListModel);

    void onProfileFailed(JSONObject jsonObject);

}
