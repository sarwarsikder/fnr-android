package com.apper.sarwar.fnr.service.api_service;

import android.content.Context;
import android.util.Log;

import com.apper.sarwar.fnr.config.AppConfigRemote;
import com.apper.sarwar.fnr.service.iservice.LoginIServiceListener;
import com.apper.sarwar.fnr.service.iservice.ProfileIService;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

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

public class ProfileApiService {

    private static final String TAG = "PROJECTS";
    private ProfileIService profileIService;
    private LoginIServiceListener loginIServiceListener;
    private Context context;
    private AppConfigRemote appConfigRemote;

    public ProfileApiService(Context context) {
        this.context = context;
        profileIService = (ProfileIService) context;
        loginIServiceListener = (LoginIServiceListener) context;
        appConfigRemote = new AppConfigRemote();
    }

    public void get_profile() {
        String requestUrl = appConfigRemote.getBASE_URL() + "/api/user-profile/";


        String authorization = "Bearer " + SharedPreferenceUtil.getDefaults("access_token", context);
        Request httpRequest = new Request.Builder()
                .header("Authorization", authorization)
                .url(requestUrl)
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
                profileIService.onProfileFailed(failResponse);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    System.out.println("onResponse()");

                    String responseBody = response.body().string();
                    JSONObject responseObject = new JSONObject(responseBody);

                    if (response.code() == 200) {
                        /*SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.urlAuthorization, authorization, this);*/
                        profileIService.onProfileSuccess(responseObject);

                    } else {
                        profileIService.onProfileFailed(responseObject);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "onResponse: " + e.getMessage());
                }


            }
        });

    }

    public void log_out() {
        try {
            final MediaType JSON = MediaType.parse("application/x-www-form-urlencoded");
            String requestUrl = appConfigRemote.getBASE_URL() + "/api/auth/revoke_token/";


            RequestBody requestBody = new FormBody.Builder()
                    .add("client_id", appConfigRemote.getCLIENT_ID())
                    .add("token", SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.access_token, context))
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
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    loginIServiceListener.onLogOutFailed();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        System.out.println("onResponse()");
                        if (response.code() == 200) {
                            loginIServiceListener.onLogOutSuccess();
                        } else {
                            loginIServiceListener.onLogOutFailed();
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
