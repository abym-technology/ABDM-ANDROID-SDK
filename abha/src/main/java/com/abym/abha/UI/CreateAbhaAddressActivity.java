package com.abym.abha.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abym.abha.Constants.ApiConstants;
import com.abym.abha.Constants.AppConstants;
import com.abym.abha.Listener.ResponseListener;
import com.abym.abha.R;
import com.abym.abha.Util.DeviceInfoUtil;
import com.abym.abha.Util.PreferenceUtil;
import com.abym.abha.Util.ToastUtil;
import com.abym.abha.Util.UtilityABHA;
import com.abym.abha.Wrapper.ABHARepo;
import com.abym.abha.databinding.ActivityCreateAbhaAddressBinding;

import org.json.JSONObject;

public class CreateAbhaAddressActivity extends AppCompatActivity {
    ActivityCreateAbhaAddressBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_abha_address);
        init();
    }

    private void init() {
        generateSuggestions();
        if (PreferenceUtil.getStringPrefs(this, PreferenceUtil.ENVIRONMENT, "").equalsIgnoreCase(AppConstants.UAT)) {
            dataBinding.tvPostfix.setText("@sbx");
        } else if (PreferenceUtil.getStringPrefs(this, PreferenceUtil.ENVIRONMENT, "").equalsIgnoreCase(AppConstants.PROD)) {
            dataBinding.tvPostfix.setText("@abdm");
        }

        dataBinding.etAbhaAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 4) {
                    checkABHAAddress();
                } else checkEmpty(1);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dataBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dataBinding.btnCreateId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createABHAAddress();
            }
        });
    }


    public boolean checkEmpty(int flag) {
        if (flag == 1) {
            dataBinding.btnCreateId.setBackgroundResource(R.drawable.btn_gray_bg2);
            dataBinding.btnCreateId.setTextColor(getResources().getColor(R.color.black));
            dataBinding.btnCreateId.setEnabled(false);
        } else {
            dataBinding.btnCreateId.setBackgroundResource(R.drawable.btn_blu_bg1);
            dataBinding.btnCreateId.setTextColor(getResources().getColor(R.color.white));
            dataBinding.btnCreateId.setEnabled(true);
        }
        return false;
    }


    public void checkABHAAddress() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("healthId", dataBinding.etAbhaAddress.getText().toString());

            UtilityABHA.abhaAPICall(this, null, jsonObject, ApiConstants.CHECK_PHR_AVAIL, new ResponseListener() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        if (jsonObject1.optString("status").equalsIgnoreCase("true")) {
                            JSONObject jsonObject2 = jsonObject1.optJSONObject("result");
                            if (jsonObject2.optString("status").equalsIgnoreCase("false")) {
                                checkEmpty(2);
                            } else {
                                checkEmpty(1);
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


    public void createABHAAddress() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("healthId", dataBinding.etAbhaAddress.getText().toString());
            jsonObject.put("txnId", PreferenceUtil.getStringPrefs(this, PreferenceUtil.TXNID, ""));
            jsonObject.put("referenceId", PreferenceUtil.getStringPrefs(this, PreferenceUtil.REFERENCE_ID, ""));
            jsonObject.put("referenceType", PreferenceUtil.getStringPrefs(this, PreferenceUtil.REFERENCE_TYPE, ""));
            jsonObject.put("platform", AppConstants.MOBILE);
            jsonObject.put("platformType", AppConstants.ANDROID);
            jsonObject.put("version", DeviceInfoUtil.getAppVersion(this));

            UtilityABHA.abhaAPICall(this, null, jsonObject, ApiConstants.CREATE_ABHA, new ResponseListener() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        if (jsonObject1.optString("status").equalsIgnoreCase("true")) {
                            JSONObject jsonObject2 = jsonObject1.optJSONObject("result");
                            PreferenceUtil.setStringPrefs(getApplicationContext(), PreferenceUtil.XUSERTOKEN, jsonObject2.optString("token"));
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
                    ToastUtil.showToastLong(getApplicationContext(), response);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSuggestView(String phr) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_phr, null);
        TextView tvPHR = view.findViewById(R.id.tvPHR);
        RelativeLayout rlPHR = view.findViewById(R.id.rlPHR);

        tvPHR.setText(phr);
        rlPHR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dataBinding.etAbhaAddress.setText(phr);
                    dataBinding.etAbhaAddress.setSelection(dataBinding.etAbhaAddress.getText().toString().length());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dataBinding.flexLayout.addView(view);
    }


    public void generateSuggestions() {
        if (!PreferenceUtil.getStringPrefs(this, PreferenceUtil.ABHADATA, "").equalsIgnoreCase("")) {
            try {
                JSONObject jsonObject = new JSONObject(PreferenceUtil.getStringPrefs(this, PreferenceUtil.ABHADATA, ""));
                String name = jsonObject.optString("name");
                if (name.contains(" ")) {
                    String[] names = name.split(" ");
                    name = names[0];
                }
                if (getIntent().hasExtra(AppConstants.MOBILENO)) {
                    String phr1 = name + getIntent().getStringExtra(AppConstants.MOBILENO).substring(7);
                    addSuggestView(phr1);
                    phr1 = name + getIntent().getStringExtra(AppConstants.MOBILENO).substring(6);
                    addSuggestView(phr1);
                }
                try {
                    if (jsonObject.has("dayOfBirth")) {
                        String year = jsonObject.optString("yearOfBirth");
                        String phr1 = name + year;
                        addSuggestView(phr1);
                    } else if (jsonObject.has("birthdate")) {
                        String year = jsonObject.optString("birthdate").substring(6, 10);
                        String phr1 = name + year;
                        addSuggestView(phr1);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}