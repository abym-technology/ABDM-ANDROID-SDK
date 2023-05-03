package com.abym.abha.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 24-Jun-17.
 */

public class PreferenceUtil {

    public static final String PREFERENCE_NAME = "abhaSdk";
    public static final String PREFERENCE_NAME1 = "abhaSdk";

    public static String HEALTH_ACCESSTOKEN = "haccesstoken";
    public static String ENVIRONMENT = "environment";
    public static String PUBLICKEY = "pkey";
    public static String HEALTH_REFRESHTOKEN = "hrefreshtoken";
    public static String TXNID = "txnId";
    public static String USERTOKEN = "user-token";
    public static String XUSERTOKEN = "x-user-token";
    public static String ACCESSTOKEN = "accesstoken";
    public static String REFRESHTOKEN = "refreshtoken";
    public static String ABHALOGIN = "abhalogin";
    public static String PHRTXNID = "phrtxnId";
    public static String XUSERTOKEN_PHR = "x-user-token_phr";
    public static String LANGUAGE = "language";
    public static String AUTHTOKEN = "authtoken";
    public static String IS_VERIFIED = "is_verified";
    public static String CLIENT_ID = "clientId";
    public static String CLIENT_TOKEN = "clientSecret";
    public static String ABHADATA = "abhaData";
    public static String REFERENCE_ID = "referenceId";
    public static String REFERENCE_TYPE = "referenceType";

    private static SharedPreferences getSharedPrefs(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences getSharedPrefs1(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME1, Context.MODE_PRIVATE);
    }


    public static void setBooleanPrefs(Context context, String param, boolean value) {
        getSharedPrefs(context).edit().putBoolean(param, value).commit();
    }

    public static boolean getBooleanPrefs(Context context, String param, boolean defaultvalue) {
        return getSharedPrefs(context).getBoolean(param, defaultvalue);
    }


    public static void setStringPrefs(Context context, String param, String value) {
        getSharedPrefs(context).edit().putString(param, value).commit();
    }

    public static void setStringPrefs1(Context context, String param, String value) {
        getSharedPrefs1(context).edit().putString(param, value).commit();
    }

    public static String getStringPrefs(Context context, String param, String defaultvalue) {
        return getSharedPrefs(context).getString(param, defaultvalue);
    }

    public static String getStringPrefs1(Context context, String param, String defaultvalue) {
        return getSharedPrefs1(context).getString(param, defaultvalue);
    }


    public static void setIntPrefs(Context context, String param, int value) {
        getSharedPrefs(context).edit().putInt(param, value).commit();
    }

    public static int getIntPrefs(Context context, String param, int defaultvalue) {
        return getSharedPrefs(context).getInt(param, defaultvalue);
    }

    public static void clearpref(Context context) {
        getSharedPrefs(context).edit().clear().commit();
    }

}
