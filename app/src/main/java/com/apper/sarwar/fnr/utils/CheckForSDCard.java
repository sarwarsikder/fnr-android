package com.apper.sarwar.fnr.utils;

import android.os.Environment;

public class CheckForSDCard {

    //Method to Check If SD Card is mounted or not
    public static boolean isSDCardPresent() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
}
