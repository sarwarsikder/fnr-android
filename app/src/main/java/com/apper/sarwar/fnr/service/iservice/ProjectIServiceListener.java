package com.apper.sarwar.fnr.service.iservice;

import org.json.JSONObject;

public interface ProjectIServiceListener {
    void onProjectSuccess(JSONObject BuildingFlatListModel);

    void onProjectFailed(JSONObject jsonObject);
}
