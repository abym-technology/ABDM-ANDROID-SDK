package com.abym.abha.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import com.abym.abha.Constants.ApiConstants;
import com.abym.abha.Listener.ResponseListener;
import com.abym.abha.R;
import com.abym.abha.Util.LogUtil;
import com.abym.abha.Util.PreferenceUtil;
import com.abym.abha.Util.ToastUtil;
import com.abym.abha.Util.UtilityABHA;
import com.abym.abha.Wrapper.ABHAListener;
import com.abym.abha.Wrapper.ABHARepo;
import com.abym.abha.databinding.ActivityAbhaSuccessfullBinding;

import org.json.JSONObject;

import pl.droidsonroids.gif.GifDrawable;

public class AbhaSuccessActivity extends AppCompatActivity {
    ActivityAbhaSuccessfullBinding dataBinding;
    String profileToken = "";
    JSONObject finalJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_abha_successfull);
        ((GifDrawable) dataBinding.gifImage.getDrawable()).setLoopCount(1);
        init();
    }

    private void init() {
        if (!PreferenceUtil.getStringPrefs(this, PreferenceUtil.ABHADATA, "").equalsIgnoreCase("")) {
            try {
                JSONObject jsonObject = new JSONObject(PreferenceUtil.getStringPrefs(this, PreferenceUtil.ABHADATA, ""));
                finalJSON = jsonObject;
                profileToken = PreferenceUtil.getStringPrefs(getApplicationContext(), PreferenceUtil.XUSERTOKEN, "");
                String healthId = finalJSON.optString("healthId") + "";
                if (healthId.equalsIgnoreCase("") || healthId.equalsIgnoreCase("null")) {
                    dataBinding.tvABHAAddress.setText(finalJSON.optString("healthIdNumber"));
                } else
                    dataBinding.tvABHAAddress.setText(finalJSON.optString("healthId"));
                getABHACard();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dataBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dataBinding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.showErrorLog("response#####", finalJSON.toString());
                ABHARepo.abhaListener.onSuccess(finalJSON);
                ABHARepo.closeABHA();
                finish();
            }
        });
    }

    public void getABHACard() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("profileToken", profileToken);

            UtilityABHA.abhaAPICall(this, dataBinding.rlProgress, jsonObject, ApiConstants.ABHA_CARD, new ResponseListener() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        byte[] responseBody = Base64.decode(jsonObject1.optString("result"), Base64.DEFAULT);
                        //  finalJSON.put("cardImage", response);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                        dataBinding.ivCard.setImageBitmap(bitmap);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String response) {
                    ToastUtil.showToastLong(getApplicationContext(), response);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}