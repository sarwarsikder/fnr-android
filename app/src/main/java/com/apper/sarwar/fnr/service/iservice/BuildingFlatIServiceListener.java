package com.apper.sarwar.fnr.service.iservice;

import org.json.JSONObject;

public interface BuildingFlatIServiceListener {
    void onBuildingFlatSuccess(JSONObject buildingFlatListModel);

    void onBuildingFlatFailed(JSONObject jsonObject);
}
