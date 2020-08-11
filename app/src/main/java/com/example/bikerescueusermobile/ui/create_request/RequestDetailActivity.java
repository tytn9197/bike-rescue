package com.example.bikerescueusermobile.ui.create_request;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.data.model.request.CurrentRequest;
import com.example.bikerescueusermobile.data.model.request.MessageRequestFB;
import com.example.bikerescueusermobile.data.model.request.Request;
import com.example.bikerescueusermobile.data.model.request.ReviewRequestDTO;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.ui.confirm.ConfirmInfoActivity;
import com.example.bikerescueusermobile.ui.confirm.ConfirmViewModel;
import com.example.bikerescueusermobile.ui.login.UpdateLocationService;
import com.example.bikerescueusermobile.ui.shop_owner.shop_home.ShopHomeFragment;
import com.example.bikerescueusermobile.ui.tracking_map.TrackingMapActivity;
import com.example.bikerescueusermobile.util.MyInstances;
import com.example.bikerescueusermobile.util.MyMethods;
import com.example.bikerescueusermobile.util.SharedPreferenceHelper;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;
import com.willy.ratingbar.ScaleRatingBar;

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

    @BindView(R.id.btnComplain)
    TextView btnComplain;

    @BindView(R.id.txtReqDetailCancelReason)
    TextView txtReqDetailCancelReason;

    @BindView(R.id.btnReqDetailTracking)
    Button btnReqDetailTracking;

    @BindView(R.id.btnReqDetailCallShop)
    Button btnReqDetailCallShop;

    @Inject
    ViewModelFactory viewModelFactory;

    private RequestDetailViewModel viewModel;
    private SweetAlertDialog notiDialog;
    private AlertDialog reviewDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("BikeRescueBiker"));

        //setup dialog
        notiDialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
        notiDialog.setTitleText("Thông báo");
        notiDialog.setContentText("Huỷ thành công");
        notiDialog.setConfirmText("OK");
        notiDialog.setConfirmClickListener(Dialog::dismiss);

        //setup toolbar
        setSupportActionBar(create_toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chi tiết yêu cầu");

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RequestDetailViewModel.class);

        int reqId = getIntent().getIntExtra("reqId", -1);

        if (reqId != -1) {
            viewModel.getRequestById(reqId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(req -> {
                        if (req != null) {
                            if (CurrentUser.getInstance().getRoleId() == 3) {
                                setDataToView(req);
                            } else {
                                setDataToViewShop(req);
                            }
                        }
                    }, throwable -> {
                        Log.e(TAG, "getRequestById: " + throwable.getMessage());
                    });
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (context != null && getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                String message = intent.getStringExtra("message");
                Gson gson = new Gson();
                MessageRequestFB responeReq = gson.fromJson(message, MessageRequestFB.class);

                btnReqDetailTracking.setVisibility(View.GONE);
                txtReqDetailStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.core_color));

                if (responeReq.getMessage().equals(MyInstances.NOTI_ACCEPT)) {
                    txtReqDetailStatus.setText("Cửa hàng đã nhận");
                    btnReqDetailCancel.setVisibility(View.VISIBLE);
                    btnReqDetailTracking.setVisibility(View.VISIBLE);

                    //start service and go to tracking map
                    Intent serviceIntent = new Intent(context, UpdateLocationService.class);
                    CurrentUser.getInstance().setCurrentBikerId(CurrentUser.getInstance().getId());
                    CurrentUser.getInstance().setChosenShopOwnerId(responeReq.getAcceptedId());
                    startService(serviceIntent);

                    Intent maptracking = new Intent(context, TrackingMapActivity.class);
                    maptracking.putExtra("isBikerTracking", true);
                    maptracking.putExtra("reqId", responeReq.getReqId());
                    startActivity(maptracking);
                }

                if (responeReq.getMessage().equals(MyInstances.NOTI_REJECTED)) {
                    txtReqDetailStatus.setText("Cửa hàng đã từ chối");
                    txtReqDetailStatus.setTextColor(Color.RED);
                    btnReqDetailCancel.setVisibility(View.GONE);
                    txtReqDetailCancelReason.setVisibility(View.VISIBLE);
                    if (responeReq.getReason() != null && !responeReq.getReason().equals("")) {
                        txtReqDetailCancelReason.setText(" Lý do hủy: " + responeReq.getReason());
                    }
                    SharedPreferenceHelper.setSharedPreferenceString(getApplicationContext(), MyInstances.KEY_BIKER_REQUEST, "");
                }

                if (responeReq.getMessage().equals(MyInstances.NOTI_FINISH)) {
                    txtReqDetailStatus.setText("Yêu cầu đã hoàn thành");
                    btnComplain.setVisibility(View.VISIBLE);
                    btnReqDetailCancel.setVisibility(View.GONE);
                    SharedPreferenceHelper.setSharedPreferenceString(getApplicationContext(), MyInstances.KEY_BIKER_REQUEST, "");

                    //review reruest
                    setupReviewView(responeReq.getReqId(), responeReq.getReqCode(), responeReq.getReqPrice());
                    reviewDialog.show();
                }

                if (responeReq.getMessage().equals(MyInstances.NOTI_CANELED)) {
                    txtReqDetailStatus.setText("Cửa hàng đã hủy");
                    txtReqDetailStatus.setTextColor(Color.RED);
                    btnReqDetailCancel.setVisibility(View.GONE);
                    txtReqDetailCancelReason.setVisibility(View.VISIBLE);
                    if (responeReq.getReason() != null) {
                        txtReqDetailCancelReason.setText(" Lý do hủy: " + responeReq.getReason());
                    }
                    SharedPreferenceHelper.setSharedPreferenceString(getApplicationContext(), MyInstances.KEY_BIKER_REQUEST, "");

                }

                if (responeReq.getMessage().equals(MyInstances.NOTI_ARRIVED)) {
                    txtReqDetailStatus.setText("Thợ sửa xe đã đến nơi");
                    btnReqDetailCancel.setVisibility(View.VISIBLE);
                    btnReqDetailTracking.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @SuppressLint("DefaultLocale")
    private void setupReviewView(int reqId, String code, double price){
        //set up review dialog
        LayoutInflater factory = LayoutInflater.from(this);
        final View reviewView = factory.inflate(R.layout.dialog_review_request, null);
        reviewDialog = new AlertDialog.Builder(this).create();

        ScaleRatingBar ratingBar = reviewView.findViewById(R.id.reviewRatingBar);
        EditText edtComment = reviewView.findViewById(R.id.edtCommentDetail);
        TextView txtReqCode = reviewView.findViewById(R.id.txtReviewReqCode);
        TextView txtPrice = reviewView.findViewById(R.id.txtReviewPrice);

        txtReqCode.setText(code);
        txtPrice.setText(String.format("Giá: %1.0f k VND", price));

        reviewDialog.setView(reviewView);
        reviewView.findViewById(R.id.btn_confirm).setOnClickListener(confirmView -> {
            reviewDialog.dismiss();

            String comment = edtComment.getText().toString();
            double star = ratingBar.getRating();

            ReviewRequestDTO reviewDTO = new ReviewRequestDTO(comment, star);
            Log.e(TAG, "review: " + reviewDTO.toString());

            viewModel.reviewRequest(reqId, reviewDTO)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(respone -> {
                        if (respone != null) {
                            SweetAlertDialog notiDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
                            notiDialog.setTitleText("Thông báo");
                            notiDialog.setContentText("Đánh giá thành công");
                            notiDialog.setConfirmText("OK");
                            notiDialog.setConfirmClickListener(Dialog::dismiss);
                            notiDialog.show();
                        }
                    }, throwable -> {
                        Log.e(TAG, "reviewRequest: " + throwable.getMessage());
                    });
        });
        reviewView.findViewById(R.id.btn_return).setOnClickListener(v1 -> reviewDialog.dismiss());
    }

    @SuppressLint("SetTextI18n")
    private void setDataToView(Request request) {
        txtReqDetailCancelReason.setVisibility(View.GONE);
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

        if (request.getCancelReason() != null) {
            if (request.getStatus().equals(MyInstances.STATUS_CANCELED) || request.getStatus().equals(MyInstances.STATUS_REJECTED))
                txtReqDetailCancelReason.setText(" Lý do hủy: " + request.getCancelReason());
            txtReqDetailCancelReason.setVisibility(View.VISIBLE);
        }

        setCancelButtonClick(request.getId(), true);
        btnReqDetailTracking.setOnClickListener(v -> {

            //mac dinh la thang biker vao activity nay
            Intent serviceIntent = new Intent(this, UpdateLocationService.class);
            CurrentUser.getInstance().setCurrentBikerId(CurrentUser.getInstance().getId());
            CurrentUser.getInstance().setChosenShopOwnerId(request.getAcceptedUser().getId());
            startService(serviceIntent);

            Intent intent = new Intent(this, TrackingMapActivity.class);
            intent.putExtra("isBikerTracking", true);
            intent.putExtra("reqId", request.getId());
            startActivity(intent);
        });

        btnReqDetailCallShop.setVisibility(View.VISIBLE);
        btnReqDetailCallShop.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + Uri.encode(request.getAcceptedUser().getPhoneNumber())));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(callIntent);
        });
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
        btnComplain.setVisibility(View.GONE);
        if (request.getCancelReason() != null && request.getStatus().equals(MyInstances.STATUS_CANCELED)) {
            txtReqDetailCancelReason.setText(" Lý do hủy: " + request.getCancelReason());
            txtReqDetailCancelReason.setVisibility(View.VISIBLE);
        }

        setCancelButtonClick(request.getId(), false);

        txtReqDetailCancelReason.setVisibility(View.GONE);
        if (request.getStatus().equals(MyInstances.STATUS_CANCELED) || request.getStatus().equals(MyInstances.STATUS_REJECTED)) {
            txtReqDetailCancelReason.setVisibility(View.VISIBLE);
            txtReqDetailCancelReason.setText(" Lý do hủy: " + request.getCancelReason());
        }
    }

    @SuppressLint("SetTextI18n")
    private void setCancelButtonClick(int reqId, boolean isSendToShop) {
        btnReqDetailCancel.setOnClickListener(v -> {
            LayoutInflater factory = LayoutInflater.from(this);
            final View editDialogView = factory.inflate(R.layout.activity_cancel_reason, null);
            final AlertDialog editDialog = new AlertDialog.Builder(this).create();
            editDialog.setView(editDialogView);
            MaterialSpinner spinner = editDialogView.findViewById(R.id.confirm_spinner);
            EditText txtReasonDetail = editDialogView.findViewById(R.id.txtReasonDetail);
            spinner.setDropdownHeight(100);
            ConfirmViewModel confirmViewModel = ViewModelProviders.of(this, viewModelFactory).get(ConfirmViewModel.class);
            confirmViewModel.getAllConfig()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listConfig -> {
                        if (listConfig != null) {
                            ArrayAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
                            for (int i = 0; i < listConfig.size(); i++) {
                                if (listConfig.get(i).getName().equals("biker cancel reason"))
                                    listAdapter.add(listConfig.get(i).getValue());
                            }
                            spinner.setAdapter(listAdapter);
                        }
                    }, throwable -> {
                        Log.e(TAG, "getAllConfig: " + throwable.getMessage());
                    });


            spinner.setOnItemSelectedListener((view, position, id, item) -> {
                if (spinner.getText().toString().equals("Lý do khác")) {
                    txtReasonDetail.setVisibility(View.VISIBLE);
                }
            });

            editDialogView.findViewById(R.id.btn_confirm).setOnClickListener(confirmView -> {
                editDialog.dismiss();
                String reason = spinner.getText().toString();
                if (reason.equals("Lý do khác")) {
                    reason = txtReasonDetail.getText().toString();
                }

                final String finalReason = reason;
                viewModel.cancleRequest(reqId, isSendToShop, reason)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isSuccess -> {
                            if (isSuccess) {
                                btnReqDetailTracking.setVisibility(View.GONE);
                                notiDialog.show();
                                btnReqDetailCancel.setVisibility(View.GONE);
                                txtReqDetailStatus.setText("Đã hủy");
                                txtReqDetailStatus.setTextColor(Color.RED);
                                txtReqDetailCancelReason.setVisibility(View.VISIBLE);
                                txtReqDetailCancelReason.setText(" Lý do hủy: " + finalReason);
                                SharedPreferenceHelper.setSharedPreferenceString(getApplicationContext(), MyInstances.KEY_BIKER_REQUEST, "");
                            }
                        }, throwable -> {
                            Log.e(TAG, "cancleRequest: " + throwable.getMessage());
                        });
            });
            editDialogView.findViewById(R.id.btn_return).setOnClickListener(v1 -> editDialog.dismiss());
            editDialog.show();
        });
    }

    private void refreshStatus(String status) {
        txtReqDetailStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.core_color));
        btnComplain.setVisibility(View.GONE);
        btnReqDetailTracking.setVisibility(View.GONE);

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
            btnReqDetailTracking.setVisibility(View.VISIBLE);
        }

        if (status.equals(MyInstances.STATUS_FINISHED)) {
            txtReqDetailStatus.setText("Yêu cầu đã hoàn thành");
            btnReqDetailCancel.setVisibility(View.GONE);
            btnComplain.setVisibility(View.VISIBLE);
        }

        if (status.equals(MyInstances.STATUS_ARRIVED)) {
            txtReqDetailStatus.setText("Đã đến");
            btnReqDetailCancel.setVisibility(View.VISIBLE);
            btnReqDetailTracking.setVisibility(View.VISIBLE);
        }
    }

    //click back button on toolbar => finish activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
