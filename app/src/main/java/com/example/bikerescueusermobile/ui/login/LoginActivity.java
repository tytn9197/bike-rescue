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
import com.example.bikerescueusermobile.data.model.user.User;
import com.example.bikerescueusermobile.data.model.user.UserLatLong;
import com.example.bikerescueusermobile.ui.confirm.ConfirmInfoActivity;
import com.example.bikerescueusermobile.ui.loading_page.LoadPageActivity;
import com.example.bikerescueusermobile.ui.main.MainActivity;
import com.example.bikerescueusermobile.ui.otp_page.LoginByPhoneNumberActivity;
import com.example.bikerescueusermobile.ui.shopMain.ShopMainActivity;
import com.example.bikerescueusermobile.util.MyInstances;
import com.example.bikerescueusermobile.util.SharedPreferenceHelper;
import com.example.bikerescueusermobile.ui.register.CreatePasswordActivity;
import com.example.bikerescueusermobile.util.SharedPreferenceHelper;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.victor.loading.rotate.RotateLoading;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {
    @Override
    protected int layoutRes() {
        return R.layout.activity_login_by_username_password;
    }

    private static final String TAG = "LoginActivity";

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

    @SuppressWarnings({"MissingPermission"})
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

                    deviceToken = task.getResult().getToken();

                    Log.e(TAG, "device token: " + task.getResult().getToken());

                    viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginModel.class);
                    gson = new Gson();
                    btnLogin.setOnClickListener(view -> {
                        hideErrorText();
                        LoginData loginData = new LoginData(edtName.getText().toString(), edtPass.getText().toString(), this.deviceToken);
                        viewModel.login(loginData)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(user -> {
                                    viewModel.setLoading(false);
                                    if (user != null) {
                                        user.setPasswordLogin(edtPass.getText().toString());
                                        String sharedPreferenceStr = gson.toJson(user);
                                        SharedPreferenceHelper.setSharedPreferenceString(LoginActivity.this, MyInstances.KEY_LOGGED_IN, sharedPreferenceStr);
                                        setDataToCurrentUser(user, edtPass.getText().toString());

                                        //login success -> set user latlong
                                        final FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

                                        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                                            if (location != null) {
                                                UserLatLong userLatln = new UserLatLong(
                                                        user.getId(),
                                                        "" + location.getLatitude(),
                                                        "" + location.getLongitude());

                                                viewModel.setUserLatLong(userLatln, "Bearer " + user.getAccessToken())
                                                        .subscribeOn(Schedulers.io())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe(uLatlng -> {
                                                            CurrentUser.getInstance().setLatitude("" + uLatlng.getLatitude());
                                                            CurrentUser.getInstance().setLongtitude("" + uLatlng.getLongtitude());
                                                        }, throwable -> {
                                                            Log.e(TAG, "setUserLatLong: " + throwable.getMessage());
                                                        });
                                            }
                                        });


                                        if (user.getRoleId() == 3) {
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else if (user.getRoleId() == 2) {
                                            if (user.getShop() != null)
                                                CurrentUser.getInstance().setShop(user.getShop());
                                            Intent intent = new Intent(LoginActivity.this, ShopMainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            errorTextView.setVisibility(View.VISIBLE);
                                            errorTextView.setText("Số điện thoại hoặc mật khẩu không đúng!!!!");
                                        }
                                    }

                                }, throwable -> {
                                    viewModel.setLoading(false);
                                    errorTextView.setVisibility(View.VISIBLE);
                                    if (throwable.getMessage().contains("address")
                                            || throwable.getMessage().contains("network")
                                            || throwable.getMessage().contains("connect")) {
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

                    String shareUser = SharedPreferenceHelper.getSharedPreferenceString(this, MyInstances.KEY_LOGGED_IN, "");

                    if (!shareUser.trim().equals("")) {
                        User loginUser = gson.fromJson(shareUser, User.class);
                        LoginData loginData = new LoginData(loginUser.getPhoneNumber(), loginUser.getPasswordLogin(), this.deviceToken);
                        viewModel.login(loginData)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(user -> {
                                    viewModel.setLoading(false);
                                    if (user != null) {
                                        setDataToCurrentUser(user, loginUser.getPasswordLogin());

                                        //login success -> set user latlong
                                        final FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

                                        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                                            if (location != null) {
                                                UserLatLong userLatln = new UserLatLong(
                                                        user.getId(),
                                                        "" + location.getLatitude(),
                                                        "" + location.getLongitude());

                                                viewModel.setUserLatLong(userLatln, "Bearer " + user.getAccessToken())
                                                        .subscribeOn(Schedulers.io())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe(uLatlng -> {
                                                            CurrentUser.getInstance().setLatitude("" + uLatlng.getLatitude());
                                                            CurrentUser.getInstance().setLongtitude("" + uLatlng.getLongtitude());
                                                        }, throwable -> {
                                                            Log.e(TAG, "setUserLatLong: " + throwable.getMessage());
                                                        });
                                            }
                                        });


                                        if (user.getRoleId() == 3) {
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else if (user.getRoleId() == 2) {
                                            if (user.getShop() != null)
                                                CurrentUser.getInstance().setShop(user.getShop());
                                            Intent intent = new Intent(LoginActivity.this, ShopMainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
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
                        });//end loading
                    }
                }); //end get token
    } //end create

    private void setDataToCurrentUser(User user, String pass) {
        CurrentUser.getInstance().setFullName(user.getFullName());
        CurrentUser.getInstance().setDeviceToken(this.deviceToken);
        CurrentUser.getInstance().setAccessToken("Bearer " + user.getAccessToken());
        CurrentUser.getInstance().setAvatarUrl(user.getAvatarUrl());
        CurrentUser.getInstance().setId(user.getId());
//        CurrentUser.getInstance().setDeviceToken(user.getDeviceToken());
        CurrentUser.getInstance().setAddress(user.getAddress());
        CurrentUser.getInstance().setEmail(user.getEmail());
        CurrentUser.getInstance().setPhoneNumber(user.getPhoneNumber());
        CurrentUser.getInstance().setCreatedTime(user.getCreatedTime());
        CurrentUser.getInstance().setStatus(user.getStatus());
        CurrentUser.getInstance().setRoleId(user.getRoleId());
        CurrentUser.getInstance().setListVehicle(user.getListVehicle());
        CurrentUser.getInstance().setRejectTime(user.getRejectTime());
        CurrentUser.getInstance().setArriveDistance(user.getArriveDistance());
        CurrentUser.getInstance().setMinimumDistance(user.getMinimumDistance());
        CurrentUser.getInstance().setPasswordLogin(pass);

        long currentTimeMillis = System.currentTimeMillis();

        String shareUser = SharedPreferenceHelper.getSharedPreferenceString(this, MyInstances.KEY_COUNT_CANCELATION, "");

        if (shareUser.equals("")) {
            CurrentUser.getInstance().setLastLogin(currentTimeMillis);
            CurrentUser.getInstance().setNumOfCancel(0);
            Log.e(TAG, "shareUser.equals(null)");
        } else {
            User userLastLogin = gson.fromJson(shareUser, User.class);
            CurrentUser.getInstance().setLastLogin(userLastLogin.getLastLogin());
            CurrentUser.getInstance().setNumOfCancel(userLastLogin.getNumOfCancel());

            if ((currentTimeMillis - CurrentUser.getInstance().getLastLogin()) > MyInstances.ONE_DAY_MILLISECONDS) {
                CurrentUser.getInstance().setNumOfCancel(0);
                CurrentUser.getInstance().setLastLogin(currentTimeMillis);
                Log.e(TAG, "set num of cancel = 0");
            }
        }
        Log.e(TAG, "last login mili sec after: " + CurrentUser.getInstance().getLastLogin());
        Log.e(TAG, "num of cancel: " + CurrentUser.getInstance().getNumOfCancel());
        String sharedPreferenceStr = gson.toJson(CurrentUser.getInstance());
        SharedPreferenceHelper.setSharedPreferenceString(LoginActivity.this, MyInstances.KEY_COUNT_CANCELATION, sharedPreferenceStr);

    }

    private void hideErrorText() {
        errorTextView.setText("");
        errorTextView.setVisibility(View.GONE);
    }

}
