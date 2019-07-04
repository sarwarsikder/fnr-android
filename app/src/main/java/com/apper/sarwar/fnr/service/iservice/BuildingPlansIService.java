package com.apper.sarwar.fnr.service.iservice;

import org.json.JSONObject;

public interface BuildingPlansIService {

    void onBuildingPlanSuccess(JSONObject buildingPlansListModel);

    void onBuildingPlanFailed(JSONObject jsonObject);
}
