package com.example.bikerescueusermobile.ui.shop_owner.shop_home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseFragment;
import com.example.bikerescueusermobile.data.model.request.CurrentRequest;
import com.example.bikerescueusermobile.data.model.request.MessageRequestFB;
import com.example.bikerescueusermobile.data.model.request.Request;
import com.example.bikerescueusermobile.data.model.request.ReviewRequestDTO;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.data.model.user.UserLatLong;
import com.example.bikerescueusermobile.ui.confirm.ConfirmViewModel;
import com.example.bikerescueusermobile.ui.create_request.RequestDetailViewModel;
import com.example.bikerescueusermobile.ui.login.UpdateLocationService;
import com.example.bikerescueusermobile.ui.tracking_map.TrackingMapActivity;
import com.example.bikerescueusermobile.util.MyInstances;
import com.example.bikerescueusermobile.util.SharedPreferenceHelper;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.squareup.picasso.Picasso;
import com.willy.ratingbar.ScaleRatingBar;

import org.jetbrains.annotations.NotNull;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    @BindView(R.id.btnShopHomeCancelReq)
    Button btnCancelReq;

    @BindView(R.id.txtShopHomeReqCancelReason)
    TextView txtReqCancelReason;

    @BindView(R.id.btnShopHomeTracking)
    Button btnTracking;

    @BindView(R.id.txtShopHomeCountdown)
    TextView txtCountdown;

    @BindView(R.id.btnShopHomeArrived)
    Button btnArrived;

    @Inject
    ViewModelFactory viewModelFactory;

    private RequestDetailViewModel viewModel;
    private CountDownTimer countDownTimer;
    private double distance = -1;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getActivity() != null) {
                String message = intent.getStringExtra("message");
                Gson gson = new Gson();

                MessageRequestFB responeReq = gson.fromJson(message, MessageRequestFB.class);

                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getBaseActivity(), SweetAlertDialog.NORMAL_TYPE);
                sweetAlertDialog.setTitleText("Thông báo");
                sweetAlertDialog.setConfirmText("Xem thông báo");
                sweetAlertDialog.setCanceledOnTouchOutside(false);
                sweetAlertDialog.setCancelable(false);

                allButtonGone();

                if (responeReq.getMessage().equals(MyInstances.NOTI_CREATED)) {
                    SweetAlertDialog noti = new SweetAlertDialog(getBaseActivity(), SweetAlertDialog.NORMAL_TYPE);
                    noti.setTitleText("Thông báo");
                    noti.setConfirmText("Đóng");
                    noti.setContentText("Bạn có yêu cầu mới!");
                    noti.setCanceledOnTouchOutside(false);
                    noti.setCancelable(false);
                    noti.setConfirmText("Xem thông báo");
                    noti.setConfirmClickListener(Dialog::dismiss);
                    noti.show();

                    //set view
                    txtNoReq.setVisibility(View.GONE);
                    viewModel.getRequestById(responeReq.getReqId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(req -> {
                                if (req != null) {
                                    setDataToView(req);

                                    String sharedPreferenceStr = gson.toJson(req);
                                    SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, sharedPreferenceStr);

                                    sweetAlertDialog.setCancelText("Hủy");
                                    sweetAlertDialog.setConfirmText("OK");
                                    sweetAlertDialog.setCancelClickListener(Dialog::dismiss);

                                    btnCallBiker.setOnClickListener(v -> {
                                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                        callIntent.setData(Uri.parse("tel:" + Uri.encode(req.getCreatedUser().getPhoneNumber())));
                                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(callIntent);
                                    });

                                    btnDecline.setOnClickListener(v -> {
                                        sweetAlertDialog.setContentText("Từ chối yêu cầu này?");
                                        sweetAlertDialog.setConfirmClickListener(d -> {
                                            d.dismiss();
                                            viewModel.updateStatusRequest(req.getId(), MyInstances.STATUS_REJECTED)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(responseDTO -> {
                                                        SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
                                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                                .replace(R.id.frame_container, new ShopHomeFragment())
                                                                .commit();
                                                    });
                                        });
                                        sweetAlertDialog.show();
                                    });

                                    btnAccept.setOnClickListener(v -> {
                                        sweetAlertDialog.setContentText("Chấp nhận yêu cầu này?");
                                        sweetAlertDialog.setConfirmClickListener(d -> {
                                            d.dismiss();
                                            viewModel.updateStatusRequest(req.getId(), MyInstances.STATUS_ACCEPT)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(responseDTO -> {
                                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                                .replace(R.id.frame_container, new ShopHomeFragment())
                                                                .commit();

                                                        Intent serviceIntent = new Intent(getActivity(), UpdateLocationService.class);
                                                        CurrentUser.getInstance().setCurrentBikerId(req.getCreatedUser().getId());
                                                        CurrentUser.getInstance().setChosenShopOwnerId(CurrentUser.getInstance().getId());
                                                        getActivity().startService(serviceIntent);

                                                        Intent tracking = new Intent(getActivity(), TrackingMapActivity.class);
                                                        tracking.putExtra("isBikerTracking", false);
                                                        tracking.putExtra("reqId", req.getId());
                                                        tracking.putExtra("reqStatus", req.getStatus());
                                                        startActivityForResult(tracking, MyInstances.SHOP_RESULT_CODE);
                                                    });
                                            btnCancelReq.setVisibility(View.VISIBLE);
//                                            btnArrived.setVisibility(View.VISIBLE);
                                        });
                                        sweetAlertDialog.show();
                                    });

                                    //run cown down timer
                                    countDownTimer = new CountDownTimer(CurrentUser.getInstance().getRejectTime() * 1000, 1000) {

                                        @SuppressLint("DefaultLocale")
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            if (millisUntilFinished != 0) {
                                                txtCountdown.setText(String.format(" Hệ thống sẽ từ chối yêu cầu sau %d giây", millisUntilFinished / 1000));
                                            }
                                        }

                                        @Override
                                        public void onFinish() {
                                            if (countDownTimer != null) {
                                                countDownTimer.cancel();
                                                txtCountdown.setText("");
                                                viewModel.updateStatusRequest(req.getId(), MyInstances.NOTI_AUTO_REJECTED)
                                                        .subscribeOn(Schedulers.io())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe(responseDTO -> {
                                                            SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
                                                            getActivity().getSupportFragmentManager().beginTransaction()
                                                                    .replace(R.id.frame_container, new ShopHomeFragment())
                                                                    .commit();
                                                        });
                                            }
                                        }
                                    };
                                    countDownTimer.start();
                                } else {
                                    SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
                                    txtNoReq.setVisibility(View.VISIBLE);
                                }
                            });
                }

                if (responeReq.getMessage().equals(MyInstances.NOTI_CANELED)) {
                    txtNoReq.setVisibility(View.VISIBLE);
                    SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");

                    //show dialog
                    SweetAlertDialog cancelReq = new SweetAlertDialog(getBaseActivity(), SweetAlertDialog.NORMAL_TYPE);
                    cancelReq.setTitleText("Thông báo");
                    cancelReq.setConfirmText("Đóng");
                    cancelReq.setCanceledOnTouchOutside(false);
                    cancelReq.setCancelable(false);
                    cancelReq.setContentText("Khách đã hủy yêu cầu." + "\nLý do hủy: " + responeReq.getReason());
                    cancelReq.setConfirmClickListener(Dialog::dismiss);
                    cancelReq.show();
                }
            }//end getactivity != null
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.e(TAG, "onViewCreated start");

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mMessageReceiver, new IntentFilter("BikeRescueShop"));

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RequestDetailViewModel.class);
        String request = SharedPreferenceHelper.getSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");

        txtNoReq.setVisibility(View.VISIBLE);

        if (!request.trim().equals("")) {
            Request requestFromPref = gson.fromJson(request, Request.class);

            viewModel.getRequestById(requestFromPref.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(req -> {
                        if (req != null && getActivity() != null) {
                            if (req.getAcceptedUser().getId() == CurrentUser.getInstance().getId()) {
                                txtNoReq.setVisibility(View.GONE);

                                if (req.getStatus().equals(MyInstances.STATUS_ACCEPT)
                                        || req.getStatus().equals(MyInstances.STATUS_CREATED)
                                        || req.getStatus().equals(MyInstances.STATUS_ARRIVED)) {
                                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getBaseActivity(), SweetAlertDialog.NORMAL_TYPE);
                                    sweetAlertDialog.setTitleText("Thông báo");
                                    sweetAlertDialog.setConfirmText("Xem thông báo");
                                    sweetAlertDialog.setCanceledOnTouchOutside(false);
                                    sweetAlertDialog.setCancelable(false);
                                    sweetAlertDialog.setCancelText("Hủy");
                                    sweetAlertDialog.setConfirmText("OK");
                                    sweetAlertDialog.setCancelClickListener(Dialog::dismiss);

                                    setDataToView(req);

                                    String sharedPreferenceStr = gson.toJson(req);
                                    SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, sharedPreferenceStr);

                                    btnCallBiker.setOnClickListener(v -> {
                                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                        callIntent.setData(Uri.parse("tel:" + Uri.encode(req.getCreatedUser().getPhoneNumber())));
                                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(callIntent);
                                    });

                                    btnDecline.setOnClickListener(v -> {
                                        sweetAlertDialog.setContentText("Từ chối yêu cầu này?");
                                        sweetAlertDialog.setConfirmClickListener(d -> {
                                            d.dismiss();
                                            viewModel.updateStatusRequest(req.getId(), MyInstances.STATUS_REJECTED)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(responseDTO -> {
                                                        SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
                                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                                .replace(R.id.frame_container, new ShopHomeFragment())
                                                                .commit();
                                                    });
                                        });
                                        sweetAlertDialog.show();
                                    });

                                    btnAccept.setOnClickListener(v -> {
                                        sweetAlertDialog.setContentText("Chấp nhận yêu cầu này?");
                                        sweetAlertDialog.setConfirmClickListener(d -> {
                                            d.dismiss();
                                            viewModel.updateStatusRequest(req.getId(), MyInstances.STATUS_ACCEPT)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(responseDTO -> {
                                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                                .replace(R.id.frame_container, new ShopHomeFragment())
                                                                .commit();
                                                        Intent serviceIntent = new Intent(getActivity(), UpdateLocationService.class);
                                                        CurrentUser.getInstance().setCurrentBikerId(req.getCreatedUser().getId());
                                                        CurrentUser.getInstance().setChosenShopOwnerId(CurrentUser.getInstance().getId());
                                                        getActivity().startService(serviceIntent);

                                                        Intent tracking = new Intent(getActivity(), TrackingMapActivity.class);
                                                        tracking.putExtra("isBikerTracking", false);
                                                        tracking.putExtra("reqId", req.getId());
                                                        tracking.putExtra("reqStatus", req.getStatus());
                                                        startActivityForResult(tracking, MyInstances.SHOP_RESULT_CODE);
                                                    });
                                            btnCancelReq.setVisibility(View.VISIBLE);
//                                            btnArrived.setVisibility(View.VISIBLE);
                                        });
                                        sweetAlertDialog.show();
                                    });
                                } else {
                                    SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
                                    txtNoReq.setVisibility(View.VISIBLE);
                                }
                            }
//                            setBtnArrived(req.getId());
                        }// req ->
                    });
        }//end of else

        btnReqImg.setOnClickListener(v -> {
            SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
        });
    }

    @SuppressLint("SetTextI18n")
    private void setDataToView(Request request) {
        allButtonGone();

        btnFinish.setOnClickListener(v -> {

            LayoutInflater factory = LayoutInflater.from(getActivity());
            final View priceView = factory.inflate(R.layout.dialog_confirm_price, null);
            AlertDialog priceDialog = new AlertDialog.Builder(getActivity()).create();

            TextView txtPrice = priceView.findViewById(R.id.txtConfirmPrice);
            TextView txtServiceName = priceView.findViewById(R.id.txtConfirmSerName);

            if (request.getListReqShopService().get(0).getShopService().getPrice() > 0) {
                txtPrice.setText("" + request.getListReqShopService().get(0).getShopService().getPrice().intValue());
            }

            txtServiceName.setText("Tên dịch vụ: " + request.getListReqShopService().get(0).getShopService().getServices().getName());

            priceDialog.setView(priceView);
            priceView.findViewById(R.id.btn_confirm).setOnClickListener(confirmView -> {
                priceDialog.dismiss();
                SweetAlertDialog error = new SweetAlertDialog(getBaseActivity(), SweetAlertDialog.ERROR_TYPE);
                error.setTitleText("Thông báo");
                error.setConfirmText("Đóng");
                error.setContentText("Gía tiền không hợp lệ, vui lòng kiểm tra lại");
                error.setConfirmClickListener(Dialog::dismiss);
                try {
                    double price = Double.parseDouble(txtPrice.getText().toString());

                    if (price >= 0) {
                        viewModel.finishedRequest(request.getId(), price)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(isSuccess -> {
                                    if (isSuccess) {
                                        SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
                                        txtNoReq.setVisibility(View.VISIBLE);
                                    }
                                });
                    } else {
                        error.show();
                    }
                } catch (Exception e) {
                    error.show();
                    Log.e(TAG, "can't parse double: " + e.getMessage());
                }
            });
            priceView.findViewById(R.id.btn_return).setOnClickListener(v1 -> priceDialog.dismiss());
            priceDialog.show();
        });

        btnCancelReq.setOnClickListener(v -> {
            LayoutInflater factory = LayoutInflater.from(getActivity());
            final View editDialogView = factory.inflate(R.layout.activity_cancel_reason, null);
            final AlertDialog editDialog = new AlertDialog.Builder(getActivity()).create();
            editDialog.setView(editDialogView);
            MaterialSpinner spinner = editDialogView.findViewById(R.id.confirm_spinner);
            EditText txtReasonDetail = editDialogView.findViewById(R.id.txtReasonDetail);

            ConfirmViewModel confirmViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ConfirmViewModel.class);
            confirmViewModel.getAllConfig()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listConfig -> {
                        if (listConfig != null) {
                            ArrayAdapter listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
                            for (int i = 0; i < listConfig.size(); i++) {
                                if (listConfig.get(i).getName().equals("shop cancel reason"))
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
                viewModel.cancleRequest(request.getId(), false, reason)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isSuccess -> {
                            if (isSuccess) {
                                SweetAlertDialog notiDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
                                notiDialog.setTitleText("Thông báo");
                                notiDialog.setContentText("Huỷ thành công");
                                notiDialog.setConfirmText("OK");
                                notiDialog.setConfirmClickListener(sweetAlertDialog -> {
                                    sweetAlertDialog.dismiss();
                                    SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_BIKER_REQUEST, "");
                                    txtNoReq.setVisibility(View.VISIBLE);
                                });
                                notiDialog.show();
                            }
                        }, throwable -> {
                            Log.e(TAG, "cancleRequest: " + throwable.getMessage());
                        });
            });
            editDialogView.findViewById(R.id.btn_return).setOnClickListener(v1 -> editDialog.dismiss());
            editDialog.show();
        });

        btnTracking.setOnClickListener(v -> {
            Intent serviceIntent = new Intent(getActivity(), UpdateLocationService.class);
            CurrentUser.getInstance().setCurrentBikerId(request.getCreatedUser().getId());
            CurrentUser.getInstance().setChosenShopOwnerId(CurrentUser.getInstance().getId());
            getActivity().startService(serviceIntent);

            Intent intent = new Intent(getActivity(), TrackingMapActivity.class);
            intent.putExtra("isBikerTracking", false);
            intent.putExtra("reqId", request.getId());
            intent.putExtra("reqStatus", request.getStatus());
            startActivityForResult(intent, MyInstances.SHOP_RESULT_CODE);
        });

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
        txtVehicle.setText(" Thông tin xe: " + request.getVehicle().getBrand() + " " + request.getVehicle().getVehiclesYear());

        //set date time
        Timestamp ts = request.getCreatedDate();
        Date d = new Date(ts.getTime());
        DateFormat f = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        txtCreatedDate.setText(" " + f.format(d));
        f = new SimpleDateFormat("HH:mm", Locale.getDefault());
        txtCreatedTime.setText(" Vào lúc " + f.format(d));

        refreshStatus(request.getStatus(), request.getId());

        if (request.getStatus().equals(MyInstances.STATUS_CANCELED) || request.getStatus().equals(MyInstances.STATUS_REJECTED)) {
            txtReqCancelReason.setVisibility(View.VISIBLE);
            txtReqCancelReason.setText(" Lý do hủy: " + request.getCancelReason());
        }
    }

    @SuppressLint("SetTextI18n")
    private void refreshStatus(String status, int reqId) {
        txtStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.core_color));
        //request moi duoc tao
        if (status.equals(MyInstances.STATUS_CREATED)) {
            statusCreated.setVisibility(View.VISIBLE);
            btnTracking.setVisibility(View.VISIBLE);
        }

        if (status.equals(MyInstances.STATUS_ARRIVED)) {
            btnFinish.setVisibility(View.VISIBLE);
            btnTracking.setVisibility(View.VISIBLE);
            btnCancelReq.setVisibility(View.VISIBLE);
            txtStatus.setVisibility(View.VISIBLE);
            txtStatus.setText("Đã đến nơi ");
        }

        if (status.equals(MyInstances.STATUS_ACCEPT)) {
            btnTracking.setVisibility(View.VISIBLE);
//            btnArrived.setVisibility(View.VISIBLE);
            txtStatus.setVisibility(View.VISIBLE);
            btnCancelReq.setVisibility(View.VISIBLE);

            txtStatus.setText("Đã nhận ");
        }

        if (status.equals(MyInstances.STATUS_CANCELED) || status.equals(MyInstances.STATUS_REJECTED) || status.equals(MyInstances.STATUS_FINISHED)) {
            txtNoReq.setVisibility(View.VISIBLE);
            SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void allButtonGone() {
        btnCancelReq.setVisibility(View.GONE);
        btnReqImg.setVisibility(View.GONE);
        statusCreated.setVisibility(View.GONE);
        btnFinish.setVisibility(View.GONE);
        btnArrived.setVisibility(View.GONE);
        btnTracking.setVisibility(View.GONE);
        txtReqCancelReason.setVisibility(View.GONE);
        txtStatus.setVisibility(View.GONE);
    }

//    @SuppressWarnings({"MissingPermission"})
//    private void setBtnArrived(int reqId){
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(MyInstances.APP);
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
//                List<UserLatLong> list = new ArrayList<>();
//                for (DataSnapshot listUserLatlng : dataSnapshot.getChildren()) {
//                    list.add(listUserLatlng.getValue(UserLatLong.class));
//                }
//                if (list.size() > 0) {
//                    int pos = -1;
//                    for (int i = 0; i < list.size(); i++) {
//                        if (list.get(i).getId() == CurrentUser.getInstance().getCurrentBikerId())
//                            pos = i;
//                    }
//                    if (pos > -1) {
//                        UserLatLong newUser = list.get(pos);
//                        final FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
//
//                        mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
//                            if (location != null) {
//                                Point origin = Point.fromLngLat(location.getLongitude(), location.getLatitude());
//
//                                MapboxDirections client = MapboxDirections.builder()
//                                        .origin(origin)
//                                        .destination(Point.fromLngLat(Double.parseDouble(newUser.getLongtitude()), Double.parseDouble(newUser.getLatitude())))
//                                        .overview(DirectionsCriteria.OVERVIEW_FULL)
//                                        .profile(DirectionsCriteria.PROFILE_DRIVING)
//                                        .accessToken(getString(R.string.mapbox_access_token))
//                                        .build();
//
//                                client.enqueueCall(new Callback<DirectionsResponse>() {
//                                    @Override
//                                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
//                                        if (response.body() == null) {
//                                            Log.e(TAG, "getDistance & Duration - response.body() == null: No routes found, make sure you set the right user and access token.");
//                                            return;
//                                        } else if (response.body().routes().size() < 1) {
//                                            Log.e(TAG, "getDistance & Duration - response.body().routes().size() < 1: No routes found");
//                                            return;
//                                        }
//
//                                        double distance = response.body().routes().get(0).distance() / 1000;
//                                        Log.e(TAG, "distance 11: " + distance);
//                                        btnArrived.setOnClickListener(view -> {
//                                            if (distance < 0.2 && distance > 0) { // 0.5 km
//                                                viewModel.updateStatusRequest(reqId, MyInstances.STATUS_ARRIVED)
//                                                        .subscribeOn(Schedulers.io())
//                                                        .observeOn(AndroidSchedulers.mainThread())
//                                                        .subscribe(responseDTO -> {
//                                                            btnArrived.setVisibility(View.GONE);
//                                                            btnFinish.setVisibility(View.VISIBLE);
//                                                            txtStatus.setText("Đã đến nơi ");
//                                                        });
//                                            } else {
//                                                SweetAlertDialog errorDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
//                                                errorDialog.setTitleText("Thông báo");
//                                                errorDialog.setConfirmText("OK");
//                                                errorDialog.setContentText("Vị trí của bạn và khách quá xa nhau");
//                                                errorDialog.setConfirmClickListener(Dialog::dismiss);
//                                                errorDialog.show();
//                                            }
//                                        });
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
//                                        Log.e(TAG, "getDistance & Duration - enableLocationComponent - onFailure: " + throwable.getMessage());
//                                    }
//                                });
//                            }
//                        });
//                    }
//                }
//            }//end data change
//
//            @Override
//            public void onCancelled(@NotNull DatabaseError error) {
//                // Failed to read value
//                Log.e(TAG, "Failed to read value." + error.toException());
//            }
//        });
//    }
}
