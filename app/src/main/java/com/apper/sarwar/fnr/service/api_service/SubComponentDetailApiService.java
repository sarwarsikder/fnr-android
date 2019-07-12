package com.apper.sarwar.fnr.service.api_service;

import android.content.Context;
import android.util.Log;

import com.apper.sarwar.fnr.config.AppConfigRemote;
import com.apper.sarwar.fnr.service.iservice.SubComponentDetailIService;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SubComponentDetailApiService {
    private static final String TAG = "SubComponentDetailApi";
    private SubComponentDetailIService subComponentDetailIService;
    private Context context;
    private AppConfigRemote appConfigRemote;

    public SubComponentDetailApiService(Context context) {
        this.context = context;
        this.subComponentDetailIService = (SubComponentDetailIService) context;
        appConfigRemote = new AppConfigRemote();
    }

    public void getData(String x) {
        Log.i("data", x);

    }

    public void get_sub_component_details(int componentId) {
        String requestUrl = appConfigRemote.getBASE_URL() + "/api/task/" + componentId + "/";


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
                subComponentDetailIService.onSubComponentDetailFailed(failResponse);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    System.out.println("onResponse()");

                    String responseBody = response.body().string();
                    JSONObject responseObject = new JSONObject(responseBody);

                    if (response.code() == 200) {
                        /*SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.urlAuthorization, authorization, this);*/
                        subComponentDetailIService.onSubComponentDetailSuccess(responseObject);

                    } else {
                        subComponentDetailIService.onSubComponentDetailFailed(responseObject);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "onResponse: " + e.getMessage());
                }


            }
        });

    }


    public void create_comment(int componentId, String commentTxt, String fileText) {
        try {

            String authorization = "Bearer " + SharedPreferenceUtil.getDefaults("access_token", context);

            String requestUrl = appConfigRemote.getBASE_URL() + "/api/task/" + componentId + "/comments/";


            RequestBody requestBody = new FormBody.Builder()
                    .add("text", commentTxt)
                    .build();

            Request httpRequest = new Request.Builder()
                    .header("Authorization", authorization)
                    .url(requestUrl)
                    .post(requestBody)
                    .build();


            final OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(httpRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("onFailure()");
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        System.out.println("onResponse()");
                        String responseBody = response.body().string();
                        JSONObject responseObject = new JSONObject(responseBody);
                        if (response.code() == 200) {

                            subComponentDetailIService.OnCommentCreateSuccess(responseObject);

                        } else {
                            subComponentDetailIService.OnCommentCreateFailed(responseObject);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "signUp: " + e.getMessage());
        }
    }


}
