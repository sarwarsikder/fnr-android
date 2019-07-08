package com.apper.sarwar.fnr.service.iservice;

import org.json.JSONObject;

public interface SubComponentDetailIService {
    void onSubComponentDetailSuccess(JSONObject subComponentDetailsListModel);

    void onSubComponentDetailFailed(JSONObject jsonObject);
}
