package com.apper.sarwar.fnr.service.api_service;

import android.content.Context;
import android.util.Log;

import com.apper.sarwar.fnr.config.AppConfigRemote;
import com.apper.sarwar.fnr.model.user_model.LoginModel;
import com.apper.sarwar.fnr.service.iservice.LoginIServiceListener;
import com.apper.sarwar.fnr.utils.Loader;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginApiService {

    private static final String TAG = "USER_LOGIN";
    private LoginIServiceListener loginIServiceListener;
    private Context context;
    private AppConfigRemote appConfigRemote;
    private Loader loader;
    public LoginApiService(Context context) {
        this.context = context;
        loginIServiceListener = (LoginIServiceListener) context;
        appConfigRemote = new AppConfigRemote();
    }


    public void login(String userName, String password) {
        try {
            final MediaType JSON = MediaType.parse("application/x-www-form-urlencoded");
            String requestUrl = appConfigRemote.getBASE_URL() + "/api/auth/token/";


            RequestBody requestBody = new FormBody.Builder()
                    .add("client_id", appConfigRemote.getCLIENT_ID())
                    .add("client_secret", appConfigRemote.getCLIENT_SECRET())
                    .add("grant_type", appConfigRemote.getGRANT_TYPE())
                    .add("username", userName)
                    .add("password", password)
                    .build();

            final Request httpRequest = new Request.Builder()
                    .url(requestUrl)
                    .post(requestBody)
                    .build();

            final OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(httpRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("onFailure()");
                    call.cancel();
                    JSONObject failResponse = new JSONObject();
                    try {
                        failResponse.put("response_body", "");
                        failResponse.put("response_code", 404);
                        failResponse.put("response_message", "login failed");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    loginIServiceListener.onLoginFailed(failResponse);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        System.out.println("onResponse()");

                        String responseBody = response.body().string();

                        JSONObject responseObject = new JSONObject(responseBody);

                        if (response.code() == 200) {
                            LoginModel loginModel = new ObjectMapper().readValue(responseBody, LoginModel.class);
                            /*SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.urlAuthorization, authorization, this);*/
                            loginIServiceListener.onLoginSuccess(loginModel);
                        } else {
                            loginIServiceListener.onLoginFailed(responseObject);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
