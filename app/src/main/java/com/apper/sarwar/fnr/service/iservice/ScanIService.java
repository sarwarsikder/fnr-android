package com.apper.sarwar.fnr.service.iservice;

import org.json.JSONObject;

public interface ScanIService {
    void onScanSuccess(JSONObject subComponentDetailsListModel);

    void onScanFailed(JSONObject jsonObject);
}
