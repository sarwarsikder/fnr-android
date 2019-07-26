package com.apper.sarwar.fnr.service.iservice;

import org.json.JSONObject;

public interface BuildingIServiceListener {

    void onBuildingSuccess(JSONObject BuildingFlatListModel);

    void onBuildingFailed(JSONObject jsonObject);
}
