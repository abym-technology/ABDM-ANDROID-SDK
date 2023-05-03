package com.abym.abha.UI;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.abym.abha.Constants.ApiConstants;
import com.abym.abha.Constants.AppConstants;
import com.abym.abha.Listener.ResponseListener;
import com.abym.abha.R;
import com.abym.abha.Util.PreferenceUtil;
import com.abym.abha.Util.ToastUtil;
import com.abym.abha.Util.UtilityABHA;
import com.abym.abha.Wrapper.ABHARepo;
import com.abym.abha.databinding.ActivityEnterNoBinding;

import org.json.JSONObject;


public class MobileNumberActivity extends AppCompatActivity {

    ActivityEnterNoBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_enter_no);
        ABHARepo.screen4 = this;

        init();
    }

    private void init() {
        dataBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dataBinding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateMobileOTP();
            }
        });
        dataBinding.etMobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkEmpty();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public boolean checkEmpty() {
        if (TextUtils.isEmpty(dataBinding.etMobileNo.getText().toString()) || dataBinding.etMobileNo.getText().length() < 10) {
            dataBinding.btnContinue.setBackgroundResource(R.drawable.btn_gray_bg2);
            dataBinding.btnContinue.setTextColor(getResources().getColor(R.color.black));
            dataBinding.btnContinue.setEnabled(false);
        } else {
            dataBinding.btnContinue.setBackgroundResource(R.drawable.btn_blu_bg1);
            dataBinding.btnContinue.setTextColor(getResources().getColor(R.color.white));
            dataBinding.btnContinue.setEnabled(true);
        }
        return false;
    }

    public void generateMobileOTP() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mobile", dataBinding.etMobileNo.getText().toString());
            jsonObject.put("txnId", PreferenceUtil.getStringPrefs(this,PreferenceUtil.TXNID,""));

            UtilityABHA.abhaAPICall(this, dataBinding.rlProgress, jsonObject, ApiConstants.MOBILE_OTP, new ResponseListener() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        if (jsonObject1.optString("status").equalsIgnoreCase("true")) {
                            JSONObject jsonObject2 = jsonObject1.optJSONObject("result");
                            String txnId = jsonObject2.optString("txnId");
                            String mobileLinked = jsonObject2.optString("mobileLinked");
                            PreferenceUtil.setStringPrefs(getApplicationContext(), PreferenceUtil.TXNID, txnId);
                            if(mobileLinked.equalsIgnoreCase("true")) {
                                Intent intent = new Intent(getApplicationContext(), CreateAbhaAddressActivity.class);
                                intent.putExtra(AppConstants.MOBILENO,dataBinding.etMobileNo.getText().toString());
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
                                intent.putExtra(AppConstants.MOBILENO,dataBinding.etMobileNo.getText().toString());
                                intent.putExtra(AppConstants.TYPE, "2");
                                startActivity(intent);

                            }
                            finish();
                            if (ABHARepo.screen3 != null) {
                                ABHARepo.screen3.finish();
                            }
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