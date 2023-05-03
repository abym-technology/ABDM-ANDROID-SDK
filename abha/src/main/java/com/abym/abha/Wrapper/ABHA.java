package com.abym.abha.Wrapper;

import android.app.Activity;
import android.content.Context;

import org.json.JSONObject;

public interface ABHA {

    public boolean init(Context context, String mode,String clientId, String clientToken, String referenceId, String referenceType);

    public void launchABHA(Context context, ABHAListener listener);

}
