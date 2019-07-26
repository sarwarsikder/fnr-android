package com.apper.sarwar.fnr.service.iservice;

import org.json.JSONObject;

public interface SubComponentDetailIService {
    void onSubComponentDetailSuccess(JSONObject subComponentDetailsListModel);

    void onSubComponentDetailFailed(JSONObject jsonObject);

    void OnCommentCreateSuccess(JSONObject subComponentModel);

    void OnCommentCreateFailed(JSONObject jsonObject);


    void OnDateChangedSuccess(String dateStr);

    void OnDateChangedFailed();

    void OnStatusChangedSuccess(JSONObject jsonObject);

    void OnStatusChangedFailed();

    void OnGetCommentSuccess(JSONObject subCommentModel);

    void OnGetCommentFailed(JSONObject jsonObject);


}
