package com.abym.abha.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.abym.abha.R;
import com.abym.abha.Wrapper.ABHARepo;
import com.abym.abha.databinding.ActivityCreateAbhaactivityBinding;

public class CreateABHAActivity extends AppCompatActivity {
    ActivityCreateAbhaactivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_abhaactivity);
        ABHARepo.screen1 = this;
        init();
    }

    public void init() {

        binding.btnCreateId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdharCardVerifyActivity.class);
                startActivity(intent);
            }
        });
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}