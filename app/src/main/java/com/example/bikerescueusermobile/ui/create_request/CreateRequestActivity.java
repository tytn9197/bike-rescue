package com.example.bikerescueusermobile.ui.create_request;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.ui.confirm.ConfirmInfoActivity;
import com.example.bikerescueusermobile.ui.main.MainActivity;
import com.example.bikerescueusermobile.ui.map.MapActivity;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class CreateRequestActivity extends BaseActivity {


    @BindView(R.id.create_toolbar)
    Toolbar create_toolbar;

    @BindView(R.id.tvCancel)
    TextView tvCancel;

    @Override
    protected int layoutRes() {
        return R.layout.activity_create_request;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(create_toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chi tiết yêu cầu");


        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(CreateRequestActivity.this, SweetAlertDialog.NORMAL_TYPE);
                sweetAlertDialog.setTitleText("Thông báo");
                sweetAlertDialog.setContentText("Quý khách xác nhận việc hủy yêu cầu lúc này?");
                sweetAlertDialog.setConfirmText("Xác nhận");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        finish();
                    }
                });
                sweetAlertDialog.setCancelButton("Quay lại", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                });
                sweetAlertDialog.show();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Trở lại trang main
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(CreateRequestActivity.this, SweetAlertDialog.NORMAL_TYPE);
            sweetAlertDialog.setTitleText("Thông báo");
            sweetAlertDialog.setContentText("Quý khách xác nhận việc hủy yêu cầu lúc này?");
            sweetAlertDialog.setConfirmText("Xác nhận");
            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    finish();
                }
            });
            sweetAlertDialog.setCancelButton("Quay lại", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismissWithAnimation();
                }
            });
            sweetAlertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
