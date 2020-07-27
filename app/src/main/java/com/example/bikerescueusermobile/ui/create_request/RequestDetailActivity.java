package com.example.bikerescueusermobile.ui.create_request;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.data.model.request.CurrentRequest;
import com.example.bikerescueusermobile.data.model.request.MessageRequestFB;
import com.example.bikerescueusermobile.data.model.request.Request;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.ui.confirm.ConfirmInfoActivity;
import com.example.bikerescueusermobile.ui.main.MainActivity;
import com.example.bikerescueusermobile.ui.map.MapActivity;
import com.example.bikerescueusermobile.ui.seach_shop_service.ShopServiceViewModel;
import com.example.bikerescueusermobile.util.MyInstances;
import com.example.bikerescueusermobile.util.MyMethods;
import com.example.bikerescueusermobile.util.SharedPreferenceHelper;
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

    @BindView(R.id.btnReqDetailCancel)
    TextView btnReqDetailCancel;

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
    private SweetAlertDialog notiDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("BikeRescueBiker"));

        //setup dialog
        notiDialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
        notiDialog.setTitleText("Thông báo");
        notiDialog.setContentText("Huỷ thành công");
        notiDialog.setConfirmText("Xác nhận");
        notiDialog.setConfirmClickListener(Dialog::dismiss);

        //setup toolbar
        setSupportActionBar(create_toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chi tiết yêu cầu");

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RequestDetailViewModel.class);

        int reqId = getIntent().getIntExtra("reqId", -1);

        if(reqId != -1) {
            viewModel.getRequestById(reqId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(req -> {
                        if (req != null && CurrentUser.getInstance().getRoleId() == 3) {
                            setDataToView(req);
                        } else
                            setDataToViewShop(req);
                    });

            btnReqDetailCancel.setOnClickListener(v -> {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
                sweetAlertDialog.setTitleText("Thông báo");
                sweetAlertDialog.setContentText("Xác nhận hủy yêu cầu?");
                sweetAlertDialog.setConfirmText("Xác nhận");
                sweetAlertDialog.setCanceledOnTouchOutside(false);
                sweetAlertDialog.setConfirmClickListener(sDialog -> {
                    sDialog.dismiss();
                    viewModel.cancleRequest(reqId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(isSuccess -> {
                                if (isSuccess) {
                                    notiDialog.show();
                                    btnReqDetailCancel.setVisibility(View.GONE);
                                    txtReqDetailStatus.setText("Đã hủy");
                                    txtReqDetailStatus.setTextColor(Color.RED);
                                    SharedPreferenceHelper.setSharedPreferenceString(getApplicationContext(), MyInstances.KEY_BIKER_REQUEST, "");
                                }
                            });
                });
                sweetAlertDialog.setCancelButton("Từ chối", Dialog::dismiss);
                sweetAlertDialog.show();
            });


            //do it when show clicked finish button
            txtReqDetailCreatedTime.setOnClickListener(v -> {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
                sweetAlertDialog.setTitleText("Thông báo");
                sweetAlertDialog.setContentText("Hoàn thành yêu cầu");
                sweetAlertDialog.setConfirmText("Xác nhận");
                sweetAlertDialog.setConfirmClickListener(sDialog -> {
                    finish();
                });
                sweetAlertDialog.show();

            });
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Gson gson = new Gson();
            MessageRequestFB responeReq = gson.fromJson(message, MessageRequestFB.class);

            if (responeReq.getMessage().equals(MyInstances.NOTI_ACCEPT)) {
                txtReqDetailStatus.setText("Cửa hàng đã nhận");
                txtReqDetailStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.core_color));
                btnReqDetailCancel.setVisibility(View.VISIBLE);
            }

            if (responeReq.getMessage().equals(MyInstances.NOTI_REJECTED)) {
                txtReqDetailStatus.setText("Cửa hàng đã từ chối");
                txtReqDetailStatus.setTextColor(Color.RED);
                btnReqDetailCancel.setVisibility(View.GONE);
                SharedPreferenceHelper.setSharedPreferenceString(getApplicationContext(), MyInstances.KEY_BIKER_REQUEST, "");
            }

            if (responeReq.getMessage().equals(MyInstances.NOTI_FINISH)) {
                txtReqDetailStatus.setText("Yêu cầu đã hoàn thành");
                txtReqDetailStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.core_color));
                btnReqDetailCancel.setVisibility(View.GONE);
                SharedPreferenceHelper.setSharedPreferenceString(getApplicationContext(), MyInstances.KEY_BIKER_REQUEST, "");
            }
        }
    };

    @SuppressLint("SetTextI18n")
    private void setDataToView(Request request) {
        txtReqDetailCode.setText("Mã yêu cầu: ".concat(request.getRequestCode()));
        txtReqDetailShopName.setText(request.getListReqShopService().get(0).getShopService().getShops().getShopName());
        txtReqDetailShopRatingStar.setText(request.getListReqShopService().get(0).getShopService().getShops().getShopRatingStar().concat("/5"));
        txtReqDetailShopAddress.setText(request.getListReqShopService().get(0).getShopService().getShops().getAddress());
        txtReqDetailAddress.setText(" " + request.getAddress());
        txtReqDetailServiceName.setText(" Vấn đề: " + request.getListReqShopService().get(0).getShopService().getServices().getName());
        txtReqDetailReqDescription.setText(" Thông tin thêm: " + request.getDescription());
        txtReqDetailVehicle.setText("Thông tin xe: " + request.getVehicle().getBrand() + " " + request.getVehicle().getVehiclesYear());

        refreshStatus(request.getStatus());

        txtReqDetailCreatedDate.setText(" " + MyMethods.convertTimeStampToDate(request.getCreatedDate()));
        txtReqDetailCreatedTime.setText(" Vào lúc " + MyMethods.convertTimeStampToTime(request.getCreatedDate()));

        if (request.getListReqShopService().get(0).getShopService().getShops().getAvatarUrl() != null) {
            Picasso.with(this)
                    .load(request.getListReqShopService().get(0).getShopService().getShops().getAvatarUrl()).placeholder(R.drawable.ic_load)
                    .into(imgReqDetailShopAvatar);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setDataToViewShop(Request request) {
        txtReqDetailCode.setText("Mã yêu cầu: ".concat(request.getRequestCode()));
        txtReqDetailShopName.setText(request.getCreatedUser().getFullName());
        txtReqDetailShopRatingStar.setText(request.getCreatedUser().getPhoneNumber());
        txtReqDetailShopRatingStar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_phone_24, 0, 0, 0);
        txtReqDetailShopAddress.setText(request.getCreatedUser().getEmail());
        txtReqDetailShopAddress.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_email_24, 0, 0, 0);
        txtReqDetailAddress.setText(" " + request.getAddress());
        txtReqDetailServiceName.setText(" Vấn đề: " + request.getListReqShopService().get(0).getShopService().getServices().getName());
        txtReqDetailReqDescription.setText(" Thông tin thêm: " + request.getDescription());
        txtReqDetailVehicle.setText("Thông tin xe: " + request.getVehicle().getBrand() + " " + request.getVehicle().getVehiclesYear());

        refreshStatus(request.getStatus());

        txtReqDetailCreatedDate.setText(" " + MyMethods.convertTimeStampToDate(request.getCreatedDate()));
        txtReqDetailCreatedTime.setText(" Vào lúc " + MyMethods.convertTimeStampToTime(request.getCreatedDate()));

        if (request.getCreatedUser().getAvatarUrl() != null) {
            Picasso.with(this)
                    .load(request.getCreatedUser().getAvatarUrl()).placeholder(R.drawable.ic_load)
                    .into(imgReqDetailShopAvatar);
        }
    }

    private void refreshStatus(String status) {
        txtReqDetailStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.core_color));

        if (status.equals(MyInstances.STATUS_CREATED)) {
            txtReqDetailStatus.setText("Đã gửi");
            btnReqDetailCancel.setVisibility(View.VISIBLE);
        }

        if (status.equals(MyInstances.STATUS_CANCELED)) {
            txtReqDetailStatus.setText("Đã hủy");
            txtReqDetailStatus.setTextColor(Color.RED);
            btnReqDetailCancel.setVisibility(View.GONE);
        }

        if (status.equals(MyInstances.STATUS_REJECTED)) {
            txtReqDetailStatus.setText("Cửa hàng đã từ chối");
            txtReqDetailStatus.setTextColor(Color.RED);
            btnReqDetailCancel.setVisibility(View.GONE);
        }

        if (status.equals(MyInstances.STATUS_ACCEPT)) {
            txtReqDetailStatus.setText("Cửa hàng đã nhận");
            btnReqDetailCancel.setVisibility(View.VISIBLE);
        }

        if (status.equals(MyInstances.STATUS_FINISHED)) {
            txtReqDetailStatus.setText("Yêu cầu đã hoàn thành");
            btnReqDetailCancel.setVisibility(View.GONE);
        }
    }

    //click back button on toolbar => finish activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
