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
import com.abym.abha.Constants.AppConstants;
import com.abym.abha.Listener.ResponseListener;
import com.abym.abha.R;
import com.abym.abha.Util.DeviceInfoUtil;
import com.abym.abha.Util.PreferenceUtil;
import com.abym.abha.Util.ToastUtil;
import com.abym.abha.Util.UtilityABHA;
import com.abym.abha.Wrapper.ABHARepo;
import com.abym.abha.databinding.ActivityConfirmAdharBinding;

import org.json.JSONObject;

public class ConfirmAdharDetailsActivity extends AppCompatActivity {
    ActivityConfirmAdharBinding dataBinding;
    String profileToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_adhar);
        init();
    }

    private void init() {
        if (!PreferenceUtil.getStringPrefs(this, PreferenceUtil.ABHADATA, "").equalsIgnoreCase("")) {
            try {
                JSONObject jsonObject = new JSONObject(PreferenceUtil.getStringPrefs(this, PreferenceUtil.ABHADATA, ""));
                dataBinding.tvName.setText(jsonObject.optString("name"));
                dataBinding.tvAdhar.setText("xxxx xxxx " + getIntent().getStringExtra(AppConstants.AADHAAR));
                try {
                    if (jsonObject.has("dayOfBirth")) {
                        String date = jsonObject.optString("dayOfBirth");
                        String month = jsonObject.optString("monthOfBirth");
                        String year = jsonObject.optString("yearOfBirth");
                        if (Integer.parseInt(date) < 10) {
                            date = "0" + date;
                        }
                        if (Integer.parseInt(month) < 10) {
                            month = "0" + month;
                        }
                        dataBinding.tvDOB.setText(date + "-" + month + "-" + year);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (jsonObject.has("birthdate")) {
                    dataBinding.tvDOB.setText(jsonObject.optString("birthdate"));
                }
                try {
                    if (jsonObject.has("photo")) {
                        byte[] decodedString = Base64.decode(jsonObject.optString("photo"), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        dataBinding.ivAdharImage.setImageBitmap(decodedByte);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (jsonObject.optString("gender").equalsIgnoreCase("M")) {
                    dataBinding.tvGender.setText("Male");
                } else if (jsonObject.optString("gender").equalsIgnoreCase("F")) {
                    dataBinding.tvGender.setText("Female");
                } else {
                    dataBinding.tvGender.setText("Others");
                }
                if (getIntent().hasExtra("new")) {
                    if (getIntent().getStringExtra("new").equalsIgnoreCase("false")) {
                        profileToken = jsonObject.optJSONObject("jwtResponse").optString("token");
                        PreferenceUtil.setStringPrefs(getApplicationContext(), PreferenceUtil.XUSERTOKEN, profileToken);
                    }
                }
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
        dataBinding.btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().hasExtra("new")) {
                    if (getIntent().getStringExtra("new").equalsIgnoreCase("true")) {
                        Intent intent = new Intent(getApplicationContext(), MobileNumberActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        getProfileDetails();
                    }
                }

            }
        });
    }

    public void getProfileDetails() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("profileToken", profileToken);
            jsonObject.put("referenceId", PreferenceUtil.getStringPrefs(this, PreferenceUtil.REFERENCE_ID, ""));
            jsonObject.put("referenceType", PreferenceUtil.getStringPrefs(this, PreferenceUtil.REFERENCE_TYPE, ""));
            jsonObject.put("platform", AppConstants.MOBILE);
            jsonObject.put("platformType", AppConstants.ANDROID);
            jsonObject.put("version", DeviceInfoUtil.getAppVersion(this));
            UtilityABHA.abhaAPICall(this, dataBinding.rlProgress, jsonObject, ApiConstants.ABHA_PROFILE, new ResponseListener() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        if (jsonObject1.optString("status").equalsIgnoreCase("true")) {
                            JSONObject jsonObject2 = jsonObject1.optJSONObject("result");
                            PreferenceUtil.setStringPrefs(getApplicationContext(), PreferenceUtil.ABHADATA, jsonObject2.toString());
                            Intent intent = new Intent(getApplicationContext(), AbhaSuccessActivity.class);
                            startActivity(intent);
                            finish();
                        } else
                            ToastUtil.showToastLong(getApplicationContext(), jsonObject1.optString("message"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String response) {
                    ToastUtil.showToastLong(getApplicationContext(),response);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}