package com.example.bikerescueusermobile.ui.create_request;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.data.model.request.CurrentRequest;
import com.example.bikerescueusermobile.data.model.request.Request;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.ui.confirm.ConfirmInfoActivity;
import com.example.bikerescueusermobile.ui.main.MainActivity;
import com.example.bikerescueusermobile.ui.map.MapActivity;
import com.example.bikerescueusermobile.ui.seach_shop_service.ShopServiceViewModel;
import com.example.bikerescueusermobile.util.MyInstances;
import com.example.bikerescueusermobile.util.MyMethods;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RequestDetailActivity extends BaseActivity {

    @Override
    protected int layoutRes() {
        return R.layout.activity_create_request;
    }

    private static final String TAG = "RequestDetailActivity";

    @BindView(R.id.create_toolbar)
    Toolbar create_toolbar;

    @BindView(R.id.tvCancel)
    TextView tvCancel;

    @BindView(R.id.txtReqDetailCode)
    TextView txtReqDetailCode;

    @BindView(R.id.txtReqDetailShopName)
    TextView txtReqDetailShopName;

    @BindView(R.id.txtReqDetailShopRatingStar)
    TextView txtReqDetailShopRatingStar;

    @BindView(R.id.txtReqDetailShopAddress)
    TextView txtReqDetailShopAddress;

    @BindView(R.id.txtReqDetailStatus)
    TextView txtReqDetailStatus;

    @BindView(R.id.txtReqDetailAddress)
    TextView txtReqDetailAddress;

    @BindView(R.id.txtReqDetailCreatedDate)
    TextView txtReqDetailCreatedDate;

    @BindView(R.id.txtReqDetailCreatedTime)
    TextView txtReqDetailCreatedTime;

    @BindView(R.id.txtReqDetailServiceName)
    TextView txtReqDetailServiceName;

    @BindView(R.id.imgReqDetailShopAvatar)
    ImageView imgReqDetailShopAvatar;

    @BindView(R.id.txtReqDetailVehicle)
    TextView txtReqDetailVehicle;

    @BindView(R.id.txtReqDetailReqDescription)
    TextView txtReqDetailReqDescription;

    @Inject
    ViewModelFactory viewModelFactory;

    private RequestDetailViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setup toolbar
        setSupportActionBar(create_toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chi tiết yêu cầu");

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RequestDetailViewModel.class);

        int reqId = getIntent().getIntExtra("reqId",-1);
        Log.e(TAG, "req id:" + reqId);

        viewModel.getRequestById(reqId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(req -> {
                    if(req != null) {
                        Log.e(TAG, "sent req: " + (new Gson()).toJson(req));
                        setDataToView(req);
                    }
                });

        tvCancel.setOnClickListener(v -> {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getBaseContext(), SweetAlertDialog.NORMAL_TYPE);
            sweetAlertDialog.setTitleText("Thông báo");
            sweetAlertDialog.setContentText("Quý khách xác nhận việc hủy yêu cầu lúc này?");
            sweetAlertDialog.setConfirmText("Xác nhận");
            sweetAlertDialog.setConfirmClickListener(sDialog -> finish());
            sweetAlertDialog.setCancelButton("Quay lại", sDialog -> sDialog.dismissWithAnimation());
            sweetAlertDialog.show();
        });

    }

    @SuppressLint("SetTextI18n")
    private void setDataToView(Request request){
        txtReqDetailCode.setText("Mã yêu cầu: ".concat(request.getRequestCode()));
        txtReqDetailShopName.setText(request.getListReqShopService().get(0).getShopService().getShops().getShopName());
        txtReqDetailShopRatingStar.setText(request.getListReqShopService().get(0).getShopService().getShops().getShopRatingStar() .concat("/5"));
        txtReqDetailShopAddress.setText(request.getListReqShopService().get(0).getShopService().getShops().getAddress());
        txtReqDetailAddress.setText(" " + request.getAddress());
        txtReqDetailServiceName.setText(" Vấn đề: " + request.getListReqShopService().get(0).getShopService().getServices().getName());
        txtReqDetailReqDescription.setText(" Thông tin thêm: " + request.getDescription());
//        txtReqDetailVehicle.setText(" " + request.getVehicle().getBrand() + " " + request.getVehicle().getVehiclesYear());

        if(request.getStatus().equals(MyInstances.STATUS_CREATED)){
            txtReqDetailStatus.setText("Đã gửi cho shop");
        }

        Timestamp ts = request.getCreatedDate();
        Date d = new Date(ts.getTime());
        DateFormat f = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        txtReqDetailCreatedDate.setText(" " + f.format(d));
        f = new SimpleDateFormat("hh:mm", Locale.getDefault());
        txtReqDetailCreatedTime.setText(" Vào lúc "  + f.format(d));

        if(request.getListReqShopService().get(0).getShopService().getShops().getAvatarUrl() != null){
            Picasso.with(this)
                    .load(request.getListReqShopService().get(0).getShopService().getShops().getAvatarUrl()).placeholder(R.drawable.ic_load)
                    .into(imgReqDetailShopAvatar);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            //Trở lại trang main
//            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(RequestDetailActivity.this, SweetAlertDialog.NORMAL_TYPE);
//            sweetAlertDialog.setTitleText("Thông báo");
//            sweetAlertDialog.setContentText("Quý khách xác nhận việc hủy yêu cầu lúc này?");
//            sweetAlertDialog.setConfirmText("Xác nhận");
//            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                @Override
//                public void onClick(SweetAlertDialog sDialog) {
//                    finish();
//                }
//            });
//            sweetAlertDialog.setCancelButton("Quay lại", new SweetAlertDialog.OnSweetClickListener() {
//                @Override
//                public void onClick(SweetAlertDialog sDialog) {
//                    sDialog.dismissWithAnimation();
//                }
//            });
//            sweetAlertDialog.show();
//        }
        finish();
        return super.onOptionsItemSelected(item);
    }
}
