package com.abym.abha.Network;

/**
 * Created by admin on 04-Oct-17.
 */

public interface IScreen {
        void handleResponse(String responseStr, String api);
        void handleErrorMessage(String response, String api);
}
