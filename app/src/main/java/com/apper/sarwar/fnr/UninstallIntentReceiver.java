package com.apper.sarwar.fnr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UninstallIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // fetching package names from extras
        String[] packageNames = intent.getStringArrayExtra("android.intent.extra.PACKAGES");

        if (packageNames != null) {
            for (String packageName : packageNames) {
                if (packageName != null) {
                    new ListenActivities(context).start();
                }
            }
        }
    }
}