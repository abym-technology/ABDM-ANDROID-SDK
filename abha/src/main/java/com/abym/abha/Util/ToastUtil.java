package com.abym.abha.Util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by rsawh on 13-Sep-17.
 */

public class ToastUtil {
    public static void showToastShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}

