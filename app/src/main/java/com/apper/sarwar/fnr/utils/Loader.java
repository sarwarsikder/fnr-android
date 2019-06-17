package com.apper.sarwar.fnr.utils;

import android.content.Context;

import com.apper.sarwar.fnr.R;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

public class Loader {

   public static SimpleArcDialog mDialog;

    public static void startLoading(Context context) {

        int[] loaderColor = context.getResources().getIntArray(R.array.rainbow);

        mDialog = new SimpleArcDialog(context);
        ArcConfiguration configuration = new ArcConfiguration(context);
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText("Please wait..");
        configuration.setColors(loaderColor);
        mDialog.setConfiguration(configuration);
        mDialog.show();
    }

    public static void stopLoading() {
        mDialog.cancel();
    }


}
