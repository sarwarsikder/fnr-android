package com.apper.sarwar.fnr.service.api_service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.apper.sarwar.fnr.config.AppConfigRemote;
import com.apper.sarwar.fnr.service.iservice.SubComponentDetailIService;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
                        //SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.urlAuthorization, authorization, this);
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

    public void uploadImageFile(int componentId, List<String> paths, String text) {
        try {
            final ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("Uploading");
            pd.show();
            String authorization = "Bearer " + SharedPreferenceUtil.getDefaults("access_token", context);
            String requestUrl = appConfigRemote.getBASE_URL() + "/api/task/" + componentId + "/comments/";
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            for (String path : paths) {
                builder.addFormDataPart("files", new File(path).getName(), RequestBody.create(MediaType.parse("File/*"), new File(path)));
            }
            if (text != null && text.trim().length() > 0) {
                builder.addFormDataPart("text", text);
            }
            RequestBody requestBody = builder.build();
            Request httpRequest = new Request.Builder()
                    .header("Authorization", authorization)
                    .url(requestUrl)
                    .post(requestBody)
                    .build();
            final OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(httpRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    pd.cancel();
                    try {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.show();
                                if (e.getMessage().contains("timeout")) {
                                    builder.setMessage("Server Timeout! try again");
                                } else {
                                    builder.setMessage(e.getMessage());
                                }
                            }
                        });
                    } catch (Exception ex) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(context, "Server Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    e.printStackTrace();
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    pd.cancel();
                    try {
                        JSONObject responseObject = new JSONObject(response.body().string());
                        if (response.code() >= 200 && response.code() < 300) {
                            subComponentDetailIService.OnCommentCreateSuccess(responseObject);
                        } else {
                            new AlertDialog.Builder(context).setMessage("Try Again").show();
                            subComponentDetailIService.OnCommentCreateFailed(responseObject);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "signUp: " + e.getMessage());
        }
    }

    public void sub_component_date_change(int componentId, String text) {

        try {
            String authorization = "Bearer " + SharedPreferenceUtil.getDefaults("access_token", context);

            String requestUrl = appConfigRemote.getBASE_URL() + "/api/task/" + componentId + "/change-due-date/";


            RequestBody requestBody = new FormBody.Builder()
                    .add("due_date", text)
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
                            subComponentDetailIService.OnDateChangedSuccess(responseBody);
                        } else {
                            subComponentDetailIService.OnDateChangedFailed();
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
