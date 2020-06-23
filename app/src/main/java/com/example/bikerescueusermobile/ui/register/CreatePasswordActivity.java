package com.example.bikerescueusermobile.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.data.model.login.LoginData;
import com.example.bikerescueusermobile.data.model.user.User;
import com.example.bikerescueusermobile.ui.login.LoginActivity;
import com.example.bikerescueusermobile.ui.main.MainActivity;
import com.example.bikerescueusermobile.ui.seach_shop_service.ShopServiceListViewAdapter;
import com.example.bikerescueusermobile.ui.seach_shop_service.ShopServiceViewModel;
import com.example.bikerescueusermobile.ui.update_info.UpdateInfoActivity;
import com.example.bikerescueusermobile.util.ViewModelFactory;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CreatePasswordActivity extends BaseActivity {

    @BindView(R.id.newPassword)
    EditText newPassword;

    @BindView(R.id.confirmPassword)
    EditText confirmPassword;

    @BindView(R.id.clearNewPw)
    ImageView clearNewPw;

    @BindView(R.id.clearConfirmPw)
    ImageView clearConfirmPw;

    @BindView(R.id.phoneNumber)
    TextView phoneNumber;

    @BindView(R.id.tvConfirm)
    TextView tvConfirm;

    @Inject
    ViewModelFactory viewModelFactory;

    private RegisterViewModel viewModel;

    private static final String TAG = "CreatePasswordActivity";

    private static final String ROLE_NAME = "ROLE_BIKER";

    @Override
    protected int layoutRes() {
        return R.layout.activity_create_pasword;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        Intent intent = getIntent();
        String sdt = intent.getStringExtra("phonenumber");
        phoneNumber.setText(sdt);
        //
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RegisterViewModel.class);


        tvConfirm.setOnClickListener(v -> {
//            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(CreatePasswordActivity.this, SweetAlertDialog.NORMAL_TYPE);
//            sweetAlertDialog.setTitleText("Thông báo");
//            sweetAlertDialog.setContentText("Quý khách xác nhận thực hiện tạo mật khẩu này?");
//            sweetAlertDialog.setConfirmText("Xác nhận");
//            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                @Override
//                public void onClick(SweetAlertDialog sDialog) {
//
//
//                }
//            });
//            sweetAlertDialog.setCancelButton("Quay lại", new SweetAlertDialog.OnSweetClickListener() {
//                @Override
//                public void onClick(SweetAlertDialog sDialog) {
//                    sDialog.dismissWithAnimation();
//                }
//            });
//            sweetAlertDialog.show();

            //LoginData user = new LoginData(phoneNumber.getText().toString(), newPassword.getText().toString());
            User user = new User();
            user.setPassword("123");
            user.setPhoneNumber("0909123456");
            viewModel.register(user, ROLE_NAME)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(newUser -> {
                        if (newUser != null) {
                            Toast.makeText(CreatePasswordActivity.this, "OK", Toast.LENGTH_SHORT);
                            Intent ok = new Intent(CreatePasswordActivity.this, LoginActivity.class);
                            startActivity(ok);
                            finish();
                        }
                    }, throwable -> {
                        Log.e(TAG, "register Biker: " + throwable.getMessage());
                    });
        });


    }

    public void init() {
        newPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                clearNewPw.setVisibility(View.VISIBLE);
                clearConfirmPw.setVisibility(View.GONE);
            }
        });


        confirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                clearConfirmPw.setVisibility(View.VISIBLE);
                clearNewPw.setVisibility(View.GONE);
            }
        });

        clearNewPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPassword.setText("");
            }
        });

        clearConfirmPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPassword.setText("");
            }
        });
    }
}
