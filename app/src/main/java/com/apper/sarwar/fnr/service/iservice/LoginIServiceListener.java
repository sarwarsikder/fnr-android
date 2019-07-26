package com.apper.sarwar.fnr.service.iservice;

import com.apper.sarwar.fnr.model.user_model.LoginModel;

import org.json.JSONObject;

public interface LoginIServiceListener {
    void onLoginSuccess(LoginModel loginModel);
    void onLoginFailed(JSONObject jsonObject);
    void onLogOutSuccess();
    void onLogOutFailed();
}
