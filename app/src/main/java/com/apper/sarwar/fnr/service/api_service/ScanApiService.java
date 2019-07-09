package com.apper.sarwar.fnr.service.api_service;

import android.content.Context;
import android.util.Log;

import com.apper.sarwar.fnr.config.AppConfigRemote;
import com.apper.sarwar.fnr.service.iservice.ScanIService;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ScanApiService {

    private static final String TAG = "ScanApiService";
    private ScanIService scanIService;
    private Context context;
    private AppConfigRemote appConfigRemote;

    public ScanApiService(Context context) {
        this.context = context;
        scanIService = (ScanIService) context;
        appConfigRemote = new AppConfigRemote();
    }

    public void get_scan_data(String ScanString) {
        String requestUrl = appConfigRemote.getBASE_URL() + "/api/scan/?unique_key=" + ScanString;


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
                scanIService.onScanFailed(failResponse);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    System.out.println("onResponse()");

                    String responseBody = response.body().string();
                    JSONObject responseObject = new JSONObject(responseBody);

                    if (response.code() == 200) {
                        scanIService.onScanSuccess(responseObject);
                    } else {
                        scanIService.onScanFailed(responseObject);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "onResponse: " + e.getMessage());
                }


            }
        });

    }

}
