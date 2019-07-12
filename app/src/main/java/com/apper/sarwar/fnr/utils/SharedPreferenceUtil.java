package com.apper.sarwar.fnr.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {

    private static final int MODE_PRIVATE = 0x0000;
    public static final String userId = "userId";
    public static final String userName = "userName";
    public static final String access_token = "access_token";
    private static final String AUTH_USER_PREF = "AUTH_USER_PREF";
    public static final String urlAuthorization = "urlAuthorization";
    public static final String currentProjectId = "currentProject";
    public static final String currentBuildingId = "currentBuilding";
    public static final String currentFlatId = "currentFlat";
    public static final String currentComponentId = "currentComponent";
    public static final String currentSubComponentId = "currentSubComponent";
    public static final String currentState = "currentState";


    public static void setDefaults(String key, String value, Context context) {

        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(SharedPreferenceUtil.AUTH_USER_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setDefaultsId(String key, int value, Context context) {

        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(SharedPreferenceUtil.AUTH_USER_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(SharedPreferenceUtil.AUTH_USER_PREF, MODE_PRIVATE);
        String value = pref.getString(key, null);
        return value;
    }

    public static int getDefaultsId(String key, Context context) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(SharedPreferenceUtil.AUTH_USER_PREF, MODE_PRIVATE);
        int value = pref.getInt(key, 0);
        return value;
    }

    public static boolean isLoggedIn(Context context) {
        String access_token = SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.access_token, context);
        if (access_token != null && !access_token.isEmpty()) {
            return true;
        }
        return false;
    }

    public static void logOut(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SharedPreferenceUtil.AUTH_USER_PREF, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }


}
