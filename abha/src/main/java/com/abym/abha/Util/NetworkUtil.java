package com.abym.abha.Util;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtil {
    public static boolean checkInternetConnection(Context context) {
        try {
            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            // ARE WE CONNECTED TO THE NET
            if (conMgr.getActiveNetworkInfo() != null

                    && conMgr.getActiveNetworkInfo().isAvailable()

                    && conMgr.getActiveNetworkInfo().isConnected()) {

                return true;

            } else {
               // ToastUtil.showToastLong(context, context.getResources().getString(R.string.no_internet));
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
