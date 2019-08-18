package com.apper.sarwar.fnr.service.iservice;

import com.apper.sarwar.fnr.model.user_model.LoginModel;

import org.json.JSONObject;

public interface RefreshTokenListener {

    void onRefreshTokenSuccess( LoginModel loginModel);

    void onRefreshTokenFailed(JSONObject jsonObject);
}