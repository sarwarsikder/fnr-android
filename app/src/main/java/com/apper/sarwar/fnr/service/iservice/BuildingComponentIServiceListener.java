package com.apper.sarwar.fnr.service.iservice;

import org.json.JSONObject;

public interface BuildingComponentIServiceListener {
    void onBuildingComponentSuccess(JSONObject buildingFlatListModel);

    void onBuildingComponentFailed(JSONObject jsonObject);
}
