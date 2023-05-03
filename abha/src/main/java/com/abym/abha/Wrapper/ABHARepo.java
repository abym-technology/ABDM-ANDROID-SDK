package com.abym.abha.Wrapper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.abym.abha.Constants.AppConstants;
import com.abym.abha.UI.CreateABHAActivity;
import com.abym.abha.Util.PreferenceUtil;

public class ABHARepo implements ABHA {

    private static ABHARepo INSTANCE = null;
    public static ABHAListener abhaListener = null;
    public static Activity screen1, screen2, screen3, screen4, screen5, screen6, screen7;

    public static ABHARepo getInstance(Context context) {
        if (INSTANCE == null) {
            // synchronize the block to ensure only one thread can execute at a time
            synchronized (ABHARepo.class) {
                // check again if the instance is already created
                if (INSTANCE == null) {
                    // create the singleton instance
                    INSTANCE = new ABHARepo();
                }
            }
        }
        // return the singleton instance
        return INSTANCE;
    }

    @Override
    public boolean init(Context context, String mode,String clientId, String clientToken, String referenceId,String referenceType) {
        PreferenceUtil.clearpref(context);
        if ((mode.equalsIgnoreCase(AppConstants.UAT) || mode.equalsIgnoreCase(AppConstants.PROD)) &&
                !TextUtils.isEmpty(referenceId) && !TextUtils.isEmpty(referenceId)) {
            PreferenceUtil.clearpref(context);
            PreferenceUtil.setStringPrefs(context, PreferenceUtil.CLIENT_ID, clientId);
            PreferenceUtil.setStringPrefs(context, PreferenceUtil.CLIENT_TOKEN, clientToken);
            PreferenceUtil.setStringPrefs(context, PreferenceUtil.ENVIRONMENT, mode);
            PreferenceUtil.setStringPrefs(context, PreferenceUtil.REFERENCE_ID, referenceId);
            PreferenceUtil.setStringPrefs(context, PreferenceUtil.REFERENCE_TYPE, referenceType);
            return true;
        }
        return false;
    }

    @Override
    public void launchABHA(Context context, ABHAListener listener) {
        abhaListener = listener;
        context.startActivity(new Intent(context, CreateABHAActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void closeABHA() {
        if (screen1 != null) {
            screen1.finish();
        }
        if (screen2 != null) {
            screen2.finish();
        }
        if (screen3 != null) {
            screen3.finish();
        }
        if (screen4 != null) {
            screen4.finish();
        }
        if (screen5 != null) {
            screen5.finish();
        }
        if (screen6 != null) {
            screen6.finish();
        }
        if (screen7 != null) {
            screen7.finish();
        }
    }
}
