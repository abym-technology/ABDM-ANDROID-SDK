package com.abym.abha.Wrapper;

import android.content.Context;

import org.json.JSONObject;

public interface ABHAListener {
    public void onSuccess(JSONObject response);
    public void onFailure(String response);
}
