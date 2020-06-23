package com.example.bikerescueusermobile.ui.loading_page;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.data.model.user.User;
import com.example.bikerescueusermobile.ui.login.LoginActivity;
import com.example.bikerescueusermobile.ui.otp_page.LoginByPhoneNumberActivity;
import com.example.bikerescueusermobile.ui.register.CreatePasswordActivity;
import com.google.gson.Gson;

public class LoadPageActivity extends BaseActivity {

    private Gson gson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        Thread loadTime = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1200);
//                    String user = SharedPreferenceHelper.getSharedPreferenceString(LoadPageActivity.this, "user", "");
//                    if (user.trim().equals("")) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
//                    } else {
//                        User fromReference = gson.fromJson(user, User.class);
////                        Log.e("sharedreference",user);
//                        CurrentUser.getInstance().setFullname(fromReference.getFullname());
//                        CurrentUser.getInstance().setUsername(fromReference.getUsername());
//                        CurrentUser.getInstance().setToken(fromReference.getToken());
//                        CurrentUser.getInstance().setAccessToken(fromReference.getAccessToken());
//                        CurrentUser.getInstance().setAvatarUrl(fromReference.getAvatarUrl());
//                        CurrentUser.getInstance().setId(fromReference.getId());
//                        CurrentUser.getInstance().setProvider(fromReference.getProvider());
//                        CurrentUser.getInstance().setRoleId(fromReference.getRoleId());
//                        CurrentUser.getInstance().setCreatedTime(fromReference.getCreatedTime());
//                        CurrentUser.getInstance().setAddress(fromReference.getAddress());
//                        CurrentUser.getInstance().setEmail(fromReference.getEmail());
//                        CurrentUser.getInstance().setIdentifyNumber(fromReference.getIdentifyNumber());
//                        CurrentUser.getInstance().setPhoneNumber(fromReference.getPhoneNumber());
//
//                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
////                        startActivity(intent);
////                        finish();
//                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        loadTime.start();
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_load_page;
    }
}
