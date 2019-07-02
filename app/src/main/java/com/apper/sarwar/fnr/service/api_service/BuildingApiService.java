package com.apper.sarwar.fnr.service.api_service;

import android.content.Context;
import android.util.Log;

import com.apper.sarwar.fnr.config.AppConfigRemote;
import com.apper.sarwar.fnr.service.iservice.BuildingIServiceListener;
import com.apper.sarwar.fnr.service.iservice.ProjectIServiceListener;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BuildingApiService {

    private static final String TAG = "PROJECTS";
    private BuildingIServiceListener buildingIServiceListener;
    private Context context;
    private AppConfigRemote appConfigRemote;

    public BuildingApiService(Context context) {
        this.context = context;
        buildingIServiceListener = (BuildingIServiceListener) context;
        appConfigRemote = new AppConfigRemote();
    }

    public void get_building(int projectId, int pageNum) {
        String requestUrl = appConfigRemote.getBASE_URL() + "/api/project/" + projectId + "/buildings/?page=" + pageNum;


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
                buildingIServiceListener.onBuildingFailed(failResponse);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    System.out.println("onResponse()");

                    String responseBody = response.body().string();
                    JSONObject responseObject = new JSONObject(responseBody);

                    if (response.code() == 200) {
                        /*SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.urlAuthorization, authorization, this);*/
                        buildingIServiceListener.onBuildingSuccess(responseObject);

                    } else {
                        buildingIServiceListener.onBuildingFailed(responseObject);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "onResponse: " + e.getMessage());
                }


            }
        });

    }
}
