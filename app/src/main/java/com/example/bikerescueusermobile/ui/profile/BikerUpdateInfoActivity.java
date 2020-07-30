package com.example.bikerescueusermobile.ui.profile;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.data.model.user.User;
import com.example.bikerescueusermobile.ui.shop_owner.ShopUpdateInfoActivity;
import com.example.bikerescueusermobile.ui.shop_owner.ShopUpdateViewModel;
import com.example.bikerescueusermobile.ui.update_info.UpdateViewModel;
import com.example.bikerescueusermobile.util.ViewModelFactory;

import javax.inject.Inject;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BikerUpdateInfoActivity extends BaseActivity {


    @BindView(R.id.bikerUpdateToolBar)
    Toolbar bikerUpdateToolBar;

    @BindView(R.id.tvUpdate)
    TextView tvUpdate;

    @BindView(R.id.bikerName)
    EditText bikerName;

    @BindView(R.id.bikerPhone)
    EditText bikerPhone;

    @BindView(R.id.bikerEmail)
    EditText bikerEmail;

    @BindView(R.id.bikerCity)
    EditText bikerCity;

    @BindView(R.id.bikerDistrict)
    EditText bikerDistrict;

    @BindView(R.id.bikerWard)
    EditText bikerWard;

    @BindView(R.id.bikerStreet)
    EditText bikerStreet;


    @Inject
    ViewModelFactory viewModelFactory;

    private UpdateViewModel viewModel;

    String TAG = "abc";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(bikerUpdateToolBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cập nhật thông tin");


        bikerName.setText(CurrentUser.getInstance().getFullName());
        bikerPhone.setText(CurrentUser.getInstance().getPhoneNumber());
        disableEditText(bikerPhone);
        bikerEmail.setText(CurrentUser.getInstance().getEmail());

        String address = CurrentUser.getInstance().getAddress();
        String[] words = address.split(",",4);
        bikerStreet.setText(words[0]);
        bikerWard.setText(words[1]);
        bikerDistrict.setText(words[2]);
        bikerCity.setText(words[3]);


        //setup viewmodel
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UpdateViewModel.class);


        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //init new Instance
                User userInfo = new User();
                userInfo.setPhoneNumber(bikerPhone.getText().toString());
                userInfo.setFullName(bikerName.getText().toString());
                userInfo.setEmail(bikerEmail.getText().toString());
                userInfo.setAddress(bikerStreet.getText().toString() + "," + bikerWard.getText().toString() + "," + bikerDistrict.getText().toString() + "," + bikerCity.getText().toString());
                //update Shop Owner Information
                Log.e(TAG, "ID: " + CurrentUser.getInstance().getId());
                Log.e(TAG, "Address: " + userInfo.getAddress());

                viewModel.updateInfo(CurrentUser.getInstance().getId(), userInfo)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user -> {
                            Log.e(TAG, "User: " + user);
                            if (user != null) {
                                CurrentUser.getInstance().setFullName(user.getFullName());
                                CurrentUser.getInstance().setAddress(user.getAddress());
                                CurrentUser.getInstance().setEmail(user.getEmail());
                                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(BikerUpdateInfoActivity.this, SweetAlertDialog.NORMAL_TYPE);
                                sweetAlertDialog.setTitleText("Thông báo");
                                sweetAlertDialog.setContentText("Bạn đã cập nhật thông tin thành công");
                                sweetAlertDialog.setConfirmText("OK");
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        finish();
                                    }
                                });
                                sweetAlertDialog.show();
                            }
                        }, throwable -> {
                            Log.e(TAG, "updateInfo: " + throwable.getMessage());
                        });
            }
        });

    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_biker_update_info;
    }
}
