package com.apper.sarwar.fnr.service.iservice;

import org.json.JSONObject;

public interface FlatComponentIService {

    void onFlatComponentSuccess(JSONObject buildingFlatListModel);

    void onFlatComponentFailed(JSONObject jsonObject);
}
