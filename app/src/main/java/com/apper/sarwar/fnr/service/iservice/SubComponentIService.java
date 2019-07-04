package com.apper.sarwar.fnr.service.iservice;

import org.json.JSONObject;

public interface SubComponentIService {

    void onSubComponentSuccess(JSONObject subComponentListModel);

    void onSubComponentFailed(JSONObject jsonObject);
}
