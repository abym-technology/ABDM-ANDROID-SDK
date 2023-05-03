package com.abym.abha.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.abym.abha.Constants.ApiConstants;
import com.abym.abha.Constants.AppConstants;
import com.abym.abha.Listener.ResponseListener;
import com.abym.abha.R;
import com.abym.abha.Util.PreferenceUtil;
import com.abym.abha.Util.ToastUtil;
import com.abym.abha.Util.UtilityABHA;
import com.abym.abha.Wrapper.ABHARepo;
import com.abym.abha.databinding.ActivityOtpactivityBinding;

import org.json.JSONObject;

public class OTPActivity extends AppCompatActivity {
    ActivityOtpactivityBinding binding;
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otpactivity);
        ABHARepo.screen3 = this;
        init();
    }

    public void OTPTimer() {
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                String time = (millisUntilFinished / 1000) + "";
                if (time.length() > 1) {
                    binding.tvTimer.setText("00:" + time);
                } else {
                    binding.tvTimer.setText("00:0" + time);
                }
                binding.tvTimer.setVisibility(View.VISIBLE);
                binding.tvResend.setTextColor(getResources().getColor(R.color.light_gray));
                binding.tvResend.setEnabled(false);
            }

            public void onFinish() {
                binding.tvTimer.setVisibility(View.GONE);
                binding.tvResend.setTextColor(getResources().getColor(R.color.text_blue));
                binding.tvResend.setEnabled(true);
            }
        }.start();
    }

    private void init() {
        if (getIntent().hasExtra(AppConstants.MOBILENO)) {
            binding.tvMobileNumber.setText("+91 " + getIntent().getStringExtra(AppConstants.MOBILENO));
        }
        if (getIntent().hasExtra(AppConstants.TYPE)) {
            if (getIntent().getStringExtra(AppConstants.TYPE).equalsIgnoreCase("1")) {
                type = "1";
            } else {
                type = "2";
            }
        }
        OTPTimer();
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.otpView.getText().toString().length() < 6) {
                    Toast.makeText(getApplicationContext(), getString(R.string.entervalidotp), Toast.LENGTH_SHORT).show();
                } else {
                    if (type.equalsIgnoreCase("1"))
                        verifyAdharOTP();
                    else
                        verifyMobileOTP();
                }
            }
        });
        binding.otpView.addTextChangedListener(new TextWatcher() {
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
        binding.tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendAdharOTP();
            }
        });
    }


    public boolean checkEmpty() {
        if (TextUtils.isEmpty(binding.otpView.getText().toString()) || binding.otpView.getText().length() < 4) {
            binding.btnVerify.setBackgroundResource(R.drawable.btn_gray_bg2);
            binding.btnVerify.setTextColor(getResources().getColor(R.color.black));
            binding.btnVerify.setEnabled(false);
        } else {
            binding.btnVerify.setBackgroundResource(R.drawable.btn_blu_bg1);
            binding.btnVerify.setTextColor(getResources().getColor(R.color.white));
            binding.btnVerify.setEnabled(true);
        }
        return false;
    }

    public void verifyAdharOTP() {
        try {
            String otp = binding.otpView.getText().toString();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("otp", otp);
            jsonObject.put("txnId", PreferenceUtil.getStringPrefs(this, PreferenceUtil.TXNID, ""));

            UtilityABHA.abhaAPICall(this, binding.rlProgress, jsonObject, ApiConstants.VERIFY_AADHAR_OTP, new ResponseListener() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        if (jsonObject1.optString("status").equalsIgnoreCase("true")) {
                            JSONObject jsonObject2 = jsonObject1.optJSONObject("result");
                            String txnId = jsonObject2.optString("txnId");
                            PreferenceUtil.setStringPrefs(getApplicationContext(), PreferenceUtil.ABHADATA, jsonObject2.toString());
                            Intent intent = new Intent(getApplicationContext(), ConfirmAdharDetailsActivity.class);
                            intent.putExtra(AppConstants.AADHAAR, getIntent().getStringExtra(AppConstants.AADHAAR));
                            if (jsonObject2.optString("new").equalsIgnoreCase("true")) {
                                PreferenceUtil.setStringPrefs(getApplicationContext(), PreferenceUtil.TXNID, txnId);
                                intent.putExtra("new", "true");
                                startActivity(intent);
                            } else {
                                intent.putExtra("new", "false");
                                startActivity(intent);
                            }
                            finish();
                            if (ABHARepo.screen2 != null) {
                                ABHARepo.screen2.finish();
                            }
                        } else
                            ToastUtil.showToastLong(getApplicationContext(), jsonObject1.optString("message"));
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

    public void resendAdharOTP() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("txnId", PreferenceUtil.getStringPrefs(this, PreferenceUtil.TXNID, ""));

            UtilityABHA.abhaAPICall(this, binding.rlProgress, jsonObject, ApiConstants.RESEND_AADHAR_OTP, new ResponseListener() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        if (jsonObject1.optString("status").equalsIgnoreCase("true")) {
                            JSONObject jsonObject2 = jsonObject1.optJSONObject("result");
                            String txnId = jsonObject2.optString("txnId");
                            PreferenceUtil.setStringPrefs(getApplicationContext(), PreferenceUtil.TXNID, txnId);
                            ToastUtil.showToastLong(getApplicationContext(), getString(R.string.otpresent));
                            OTPTimer();
                        } else
                            ToastUtil.showToastLong(getApplicationContext(), jsonObject1.optString("message"));
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

    public void verifyMobileOTP() {
        try {
            String otp = binding.otpView.getText().toString();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mobile", getIntent().getStringExtra(AppConstants.MOBILENO));
            jsonObject.put("otp", otp);
            jsonObject.put("txnId", PreferenceUtil.getStringPrefs(this, PreferenceUtil.TXNID, ""));

            UtilityABHA.abhaAPICall(this, binding.rlProgress, jsonObject, ApiConstants.VERIFY_MOBILE_OTP, new ResponseListener() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        if (jsonObject1.optString("status").equalsIgnoreCase("true")) {
                            JSONObject jsonObject2 = jsonObject1.optJSONObject("result");
                            String txnId = jsonObject2.optString("txnId");
                            PreferenceUtil.setStringPrefs(getApplicationContext(), PreferenceUtil.TXNID, txnId);
                            Intent intent = new Intent(getApplicationContext(), CreateAbhaAddressActivity.class);
                            intent.putExtra(AppConstants.MOBILENO, getIntent().getStringExtra(AppConstants.MOBILENO));
                            startActivity(intent);
                            finish();
                            if (ABHARepo.screen4 != null) {
                                ABHARepo.screen4.finish();
                            }
                        } else
                            ToastUtil.showToastLong(getApplicationContext(), jsonObject1.optString("message"));
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