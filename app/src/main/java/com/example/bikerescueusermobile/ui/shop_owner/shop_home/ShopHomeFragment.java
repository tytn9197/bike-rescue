package com.example.bikerescueusermobile.ui.shop_owner.shop_home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseFragment;
import com.example.bikerescueusermobile.data.model.adapter.CustomAdapter;
import com.example.bikerescueusermobile.data.model.request.Request;
import com.example.bikerescueusermobile.data.model.request.RequestDTO;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.ui.create_request.RequestDetailViewModel;
import com.example.bikerescueusermobile.util.MyInstances;
import com.example.bikerescueusermobile.util.SharedPreferenceHelper;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ShopHomeFragment extends BaseFragment {

    @Override
    protected int layoutRes() {
        return R.layout.shop_home_fragment;
    }

    private final String TAG = "ShopHomeFragment";

    private Gson gson = new Gson();

    @BindView(R.id.txtShopHomeNoReq)
    TextView txtNoReq;

    @BindView(R.id.txtShopHomeCode)
    TextView txtCode;

    @BindView(R.id.imgShopHomeBikerAvatar)
    ImageView imgBikerAvatar;

    @BindView(R.id.txtShopHomeBikeName)
    TextView txtBikeName;

    @BindView(R.id.txtShopHomeBikerPhone)
    TextView txtBikerPhone;

    @BindView(R.id.btnShopHomeCallBiker)
    Button btnCallBiker;

    @BindView(R.id.txtShopHomeAddress)
    TextView txtAddress;

    @BindView(R.id.txtShopHomeStatus)
    TextView txtStatus;

    @BindView(R.id.txtShopHomeVehicle)
    TextView txtVehicle;

    @BindView(R.id.txtShopHomeCreatedDate)
    TextView txtCreatedDate;

    @BindView(R.id.txtShopHomeCreatedTime)
    TextView txtCreatedTime;

    @BindView(R.id.txtShopHomeServiceName)
    TextView txtServiceName;

    @BindView(R.id.txtShopHomeReqDescription)
    TextView txtReqDescription;

    @BindView(R.id.btnShopHomeReqImg)
    Button btnReqImg;

    @BindView(R.id.btnShopHomeAccept)
    Button btnAccept;

    @BindView(R.id.btnShopHomeDecline)
    Button btnDecline;

    @BindView(R.id.btnShopHomeFinish)
    Button btnFinish;

    @BindView(R.id.statusCreated)
    LinearLayout statusCreated;

    @Inject
    ViewModelFactory viewModelFactory;

    private RequestDetailViewModel viewModel;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
            sweetAlertDialog.setTitleText("Thông báo");
            sweetAlertDialog.setConfirmText("Xác nhận");
            sweetAlertDialog.setContentText("Bạn có một yêu cầu mới!");
            sweetAlertDialog.setConfirmClickListener(sDialog -> {
                sDialog.dismiss();
                txtNoReq.setVisibility(View.GONE);

                // Get extra data included in the Intent
                String id = intent.getStringExtra("mess");

                viewModel.getRequestById(Integer.parseInt(id))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(req -> {
                            if (req != null) {
                                setDataToView(req);
                                String sharedPreferenceStr = gson.toJson(req);
                                SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, sharedPreferenceStr);
                                btnCallBiker.setOnClickListener(v -> {
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                    callIntent.setData(Uri.parse("tel:" + Uri.encode(req.getCreatedUser().getPhoneNumber())));
                                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(callIntent);
                                });
                            }
                        });
            });
            sweetAlertDialog.show();

        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mMessageReceiver, new IntentFilter("BikeRescue"));

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RequestDetailViewModel.class);

        String request = SharedPreferenceHelper.getSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
        if (request.trim().equals("")) {
            txtNoReq.setVisibility(View.VISIBLE);
        } else {
            txtNoReq.setVisibility(View.GONE);
            Request requestFromPref =gson.fromJson(request, Request.class);
            setDataToView(requestFromPref);
        }

        btnFinish.setOnClickListener(onBtnFinishClick);
        btnAccept.setOnClickListener(onBtnAcceptClick);
        btnDecline.setOnClickListener(onBtnDeclineClick);
        btnReqImg.setOnClickListener(onBtnReqImgClick);
    }

    private View.OnClickListener onBtnFinishClick = v -> {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
        sweetAlertDialog.setTitleText("Thông báo");
        sweetAlertDialog.setConfirmText("Xác nhận");
        sweetAlertDialog.setContentText("Xác nhận hoàn thành yêu cầu?");
        sweetAlertDialog.setConfirmClickListener(sDialog -> {
            sDialog.dismiss();
            SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
            txtNoReq.setVisibility(View.VISIBLE);

        });
        sweetAlertDialog.setCancelText("Hủy");
        sweetAlertDialog.setCancelClickListener(Dialog::dismiss);
        sweetAlertDialog.show();
    };

    private View.OnClickListener onBtnAcceptClick = v -> {
        showFinishButton();
    };

    private View.OnClickListener onBtnDeclineClick = v -> {
        showFinishButton();
    };

    private View.OnClickListener onBtnReqImgClick = v -> {
        SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
    };

    private void showFinishButton() {
        statusCreated.setVisibility(View.GONE);
        btnFinish.setVisibility(View.VISIBLE);
    }

    private void hideFinishButton() {
        statusCreated.setVisibility(View.VISIBLE);
        btnFinish.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    private void setDataToView(Request request) {
        txtCode.setText("Mã yêu cầu: ".concat(request.getRequestCode()));
        txtBikeName.setText(request.getCreatedUser().getFullName());
        txtBikerPhone.setText(request.getCreatedUser().getPhoneNumber());
        if (request.getCreatedUser().getAvatarUrl().contains("imgur")) {
            Picasso.with(getActivity())
                    .load(request.getCreatedUser().getAvatarUrl()).placeholder(R.drawable.ic_load)
                    .into(imgBikerAvatar);
        }
        txtAddress.setText(" " + request.getAddress());
        txtServiceName.setText(" Vấn đề: " + request.getListReqShopService().get(0).getShopService().getServices().getName());
        txtReqDescription.setText(" Thông tin thêm: " + request.getDescription());
//        txtVehicle.setText(" " + request.getVehicle().getBrand() + " " + request.getVehicle().getVehiclesYear());

        //set date time
        Timestamp ts = request.getCreatedDate();
        Date d = new Date(ts.getTime());
        DateFormat f = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        txtCreatedDate.setText(" " + f.format(d));
        f = new SimpleDateFormat("hh:mm", Locale.getDefault());
        txtCreatedTime.setText(" Vào lúc " + f.format(d));

        //request moi duoc tao
        if (request.getStatus().equals(MyInstances.STATUS_CREATED)) {
            hideFinishButton();
        } else {
            showFinishButton();
        }

        txtStatus.setVisibility(View.GONE);
    }

}
