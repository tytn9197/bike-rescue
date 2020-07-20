package com.example.bikerescueusermobile.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.data.model.login.LoginData;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.ui.confirm.ConfirmInfoActivity;
import com.example.bikerescueusermobile.ui.main.MainActivity;
import com.example.bikerescueusermobile.ui.otp_page.LoginByPhoneNumberActivity;
import com.example.bikerescueusermobile.ui.shopMain.ShopMainActivity;
import com.example.bikerescueusermobile.util.MyInstances;
import com.example.bikerescueusermobile.util.SharedPreferenceHelper;
import com.example.bikerescueusermobile.ui.register.CreatePasswordActivity;
import com.example.bikerescueusermobile.util.SharedPreferenceHelper;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
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

    @BindView(R.id.btnForgotPassword)
    TextView btnForgotPassword;

    @Inject
    ViewModelFactory viewModelFactory;

    Gson gson;

    private LoginModel viewModel;
    private String deviceToken = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, LoginByPhoneNumberActivity.class);
            startActivity(intent);
        });

        //GET DEVICE TOKEN
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }

                    // Get new Instance ID token
                    this.deviceToken = task.getResult().getToken();
                });

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginModel.class);
        gson = new Gson();
        btnLogin.setOnClickListener(view -> {
            hideErrorText();
            LoginData loginData = new LoginData(edtName.getText().toString(), edtPass.getText().toString(), this.deviceToken);
            viewModel.login(loginData).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(user -> {
                        viewModel.setLoading(false);
                        if (user != null) {

                            String sharedPreferenceStr = gson.toJson(user);
                            SharedPreferenceHelper.setSharedPreferenceString(LoginActivity.this, MyInstances.KEY_LOGGED_IN, sharedPreferenceStr);
                            CurrentUser.getInstance().setFullName(user.getFullName());
                            CurrentUser.getInstance().setDeviceToken(this.deviceToken);
                            CurrentUser.getInstance().setAccessToken("Bearer " + user.getAccessToken());
                            CurrentUser.getInstance().setAvatarUrl(user.getAvatarUrl());
                            CurrentUser.getInstance().setId(user.getId());
                            CurrentUser.getInstance().setDeviceToken(user.getDeviceToken());
                            CurrentUser.getInstance().setAddress(user.getAddress());
                            CurrentUser.getInstance().setEmail(user.getEmail());
                            CurrentUser.getInstance().setPhoneNumber(user.getPhoneNumber());
                            CurrentUser.getInstance().setCreatedTime(user.getCreatedTime());
//                            CurrentUser.getInstance().setProvider(user.getProvider());
//                            CurrentUser.getInstance().setCreatedTime(user.getCreatedTime());
//                            CurrentUser.getInstance().setIdentifyNumber(user.getIdentifyNumber());
//                            CurrentUser.getInstance().setRoleId(user.getRoleId());

                            if(user.getRoleId() == 3){
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else if(user.getRoleId() == 2){
                                Intent intent = new Intent(LoginActivity.this, ShopMainActivity.class);
                                startActivity(intent);
                                finish();
                            } else{
                                errorTextView.setVisibility(View.VISIBLE);
                                errorTextView.setText("Số điện thoại hoặc mật khẩu không đúng!!!!");
                            }
                        }

                    }, throwable -> {
                        viewModel.setLoading(false);
                        errorTextView.setVisibility(View.VISIBLE);
                        if (throwable.getMessage().contains("No address associated with hostname") || throwable.getMessage().contains("network")) {
                            errorTextView.setText("Lỗi mạng!!");
                        } else {
                            errorTextView.setText("Số điện thoại hoặc mật khẩu không đúng!!");
                        }
                        Log.e("LoginActivity", "" + throwable.getMessage());
                    });
            viewModel.getLoading().observe(this, isLoading -> {
                if (isLoading != null) {
                    if (isLoading) {
                        edtName.setEnabled(false);
                        edtPass.setEnabled(false);
                        btnLogin.setVisibility(View.GONE);
                        viewRegister.setVisibility(View.GONE);
                        errorTextView.setVisibility(View.GONE);
                        btnForgotPassword.setVisibility(View.GONE);
                        rotateLoading.start();
                    } else {
                        edtName.setEnabled(true);
                        edtPass.setEnabled(true);
                        btnLogin.setVisibility(View.VISIBLE);
                        viewRegister.setVisibility(View.VISIBLE);
                        errorTextView.setVisibility(View.VISIBLE);
                        btnForgotPassword.setVisibility(View.VISIBLE);
                        rotateLoading.stop();
                    }
                }
            });
        });

        edtPass.setOnClickListener(v -> {
            hideErrorText();
        });

        edtName.setOnClickListener(v -> {
            hideErrorText();
        });
    }


    private void hideErrorText() {
        errorTextView.setText("");
        errorTextView.setVisibility(View.GONE);
    }

}
