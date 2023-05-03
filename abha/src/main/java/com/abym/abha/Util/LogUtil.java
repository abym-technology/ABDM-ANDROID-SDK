package com.abym.abha.Util;

import android.util.Log;

import com.abym.abha.BuildConfig;


/**
 * Created by rsawh on 13-Sep-17.
 */

public class LogUtil {
    public static void showErrorLog(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void showVerboseLog(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }
}

