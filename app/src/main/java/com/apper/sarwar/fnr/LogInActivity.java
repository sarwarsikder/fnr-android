package com.apper.sarwar.fnr;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apper.sarwar.fnr.model.user_model.LoginModel;
import com.apper.sarwar.fnr.service.api_service.LoginApiService;
import com.apper.sarwar.fnr.service.api_service.ProfileApiService;
import com.apper.sarwar.fnr.service.iservice.LoginIServiceListener;
import com.apper.sarwar.fnr.service.iservice.ProfileIService;
import com.apper.sarwar.fnr.utils.Loader;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;
import com.google.android.gms.common.internal.Constants;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

public class LogInActivity extends AppCompatActivity implements LoginIServiceListener, ProfileIService {

    private static final String TAG = "LogInActivity";
    TextView go_sign_up_button, sign_in;
    Loader loader;
    LoginApiService loginApiService;
    ProfileApiService profileApiService;
    EditText user_name;
    EditText password;
    String text_user_name, text_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        loginApiService = new LoginApiService(this);
        profileApiService = new ProfileApiService(this);

        sign_in = (Button) findViewById(R.id.sign_in);
        user_name = (EditText) findViewById(R.id.user_full_name);
        password = (EditText) findViewById(R.id.password);


        try {
            sign_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    text_user_name = user_name.getText().toString().trim();
                    text_password = password.getText().toString().trim();

                    if (isValidForm(text_user_name, text_password)) {
                        loader.startLoading(view.getContext());
                        loginApiService.login(text_user_name, text_password);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public boolean isValidForm(String nameTxt, String passwordTxt) {
        boolean validName = !nameTxt.isEmpty();

        if (!validName) {
            user_name.setError("Enter valid user name");
        }

        boolean validPass = !passwordTxt.isEmpty();
        if (!validPass) {
            password.setError("Enter valid password");
        }

        return validName && validPass;
    }

    @Override
    public void onLoginSuccess(final LoginModel loginModel) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    loader.stopLoading();
                    Log.d(TAG, "onLoginSuccess: " + loginModel.getAccess_token());
                    SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.access_token, loginModel.getAccess_token(), getApplicationContext());
                    SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.refresh_token, loginModel.getRefresh_token(), getApplicationContext());
                    profileApiService.get_profile();

                    /*String device = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    String token = FirebaseInstanceId.getInstance().getToken();
                    loginApiService.device_notification_insert(device, token);*/

                    Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }

    @Override
    public void onLoginFailed(JSONObject jsonObject) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              Toast.makeText(getApplicationContext(), "Incorrect credentials!", Toast.LENGTH_SHORT).show();
                              loader.stopLoading();
                          }
                      }
        );
    }

    @Override
    public void onLogOutSuccess() {

    }

    @Override
    public void onLogOutFailed() {

    }

    @Override
    public void onProfileSuccess(final JSONObject profileListModel) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    boolean is_staff = (boolean) profileListModel.get("is_staff");
                    SharedPreferenceUtil.setDefaultsId(SharedPreferenceUtil.isStaff, is_staff, getApplicationContext());
                    int x = 0;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onProfileFailed(JSONObject jsonObject) {

    }
}
