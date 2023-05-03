package com.abym.abha.Listener;

public interface ResponseListener {
    public void onSuccess(String response);
    public void onFailure(String response);
}
