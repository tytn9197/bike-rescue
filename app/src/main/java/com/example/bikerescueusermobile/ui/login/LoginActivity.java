package com.example.bikerescueusermobile.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.data.model.login.LoginData;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.ui.main.MainActivity;
import com.example.bikerescueusermobile.ui.otp_page.LoginByPhoneNumberActivity;
import com.example.bikerescueusermobile.util.SharedPreferenceHelper;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.google.gson.Gson;
import com.victor.loading.rotate.RotateLoading;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {
    @Override
    protected int layoutRes() {
        return R.layout.activity_login_by_username_password;
    }

    @BindView(R.id.viewRegister)
    TextView viewRegister;

    @BindView(R.id.btn_login)
    Button btnLogin;

    @BindView(R.id.edtName)
    EditText edtName;

    @BindView(R.id.edtPass)
    EditText edtPass;

    @BindView(R.id.mainRotateloading)
    RotateLoading rotateLoading;

    @BindView(R.id.errorText)
    TextView errorTextView;

    @Inject
    ViewModelFactory viewModelFactory;

    Gson gson;

    private LoginModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewRegister.setOnClickListener(view -> {
                Intent intent = new Intent(LoginActivity.this, LoginByPhoneNumberActivity.class);
                startActivity(intent);
        });

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginModel.class);
        gson = new Gson();
        btnLogin.setOnClickListener(view -> {
            LoginData loginData = new LoginData(edtName.getText().toString(),edtPass.getText().toString());
            viewModel.login(loginData).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(user -> {
                        viewModel.setLoading(false);
                        if(user != null){
                            String sharedPreferenceStr = gson.toJson(user);
                            SharedPreferenceHelper.setSharedPreferenceString(LoginActivity.this,"user",sharedPreferenceStr);
//                            CurrentUser.getInstance().setUsername(user.getUsername());
//                            CurrentUser.getInstance().setFullname(user.getFullname());
                            CurrentUser.getInstance().setToken("Bearer " + user.getToken());
//                            CurrentUser.getInstance().setAccessToken(user.getAccessToken());
//                            CurrentUser.getInstance().setAvatarUrl(user.getAvatarUrl());
                            CurrentUser.getInstance().setId(user.getId());
//                            CurrentUser.getInstance().setProvider(user.getProvider());
//                            CurrentUser.getInstance().setCreatedTime(user.getCreatedTime());
//                            CurrentUser.getInstance().setAddress(user.getAddress());
//                            CurrentUser.getInstance().setEmail(user.getEmail());
//                            CurrentUser.getInstance().setIdentifyNumber(user.getIdentifyNumber());
                            CurrentUser.getInstance().setPhoneNumber(user.getPhoneNumber());
//                            CurrentUser.getInstance().setRoleId(user.getRoleId());
//                            if(user.getRoleId() == 2) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
//                            }else{
//                                errorTextView.setVisibility(View.VISIBLE);
//                                errorTextView.setText("Lỗi ! Tài khoản hoặc mật khẩu không đúng. ");
//                            }
                        }

                    },throwable -> {
                        viewModel.setLoading(false);
                        errorTextView.setVisibility(View.VISIBLE);
                        errorTextView.setText("Số điện thoại hoặc mật khẩu không đúng!!");
                        Log.e("LoginActivity","" + throwable.getMessage());
                    });
        });

    }
}
