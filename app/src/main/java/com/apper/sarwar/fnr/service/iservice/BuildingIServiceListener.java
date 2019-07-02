package com.apper.sarwar.fnr.service.iservice;

import org.json.JSONObject;

public interface BuildingIServiceListener {

    void onBuildingSuccess(JSONObject projectListModel);

    void onBuildingFailed(JSONObject jsonObject);
}
