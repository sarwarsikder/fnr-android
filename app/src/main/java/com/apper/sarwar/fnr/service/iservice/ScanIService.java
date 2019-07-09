package com.apper.sarwar.fnr.service.iservice;

import org.json.JSONObject;

public interface ScanIService {
    void onScanSuccess(JSONObject subScanListModel);

    void onScanFailed(JSONObject jsonObject);
}
