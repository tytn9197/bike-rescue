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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseFragment;
import com.example.bikerescueusermobile.data.model.request.CurrentRequest;
import com.example.bikerescueusermobile.data.model.request.MessageRequestFB;
import com.example.bikerescueusermobile.data.model.request.Request;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.ui.confirm.ConfirmViewModel;
import com.example.bikerescueusermobile.ui.create_request.RequestDetailViewModel;
import com.example.bikerescueusermobile.ui.login.UpdateLocationService;
import com.example.bikerescueusermobile.ui.tracking_map.TrackingMapActivity;
import com.example.bikerescueusermobile.util.MyInstances;
import com.example.bikerescueusermobile.util.SharedPreferenceHelper;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
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

    @BindView(R.id.btnShopHomeCancelReq)
    Button btnCancelReq;

    @BindView(R.id.txtShopHomeReqCancelReason)
    TextView txtReqCancelReason;

    @BindView(R.id.btnShopHomeTracking)
    Button btnTracking;

    @Inject
    ViewModelFactory viewModelFactory;

    private RequestDetailViewModel viewModel;

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

                if (responeReq.getMessage().equals(MyInstances.NOTI_CREATED)) {
                    sweetAlertDialog.setContentText("Bạn có một yêu cầu mới!");
                    sweetAlertDialog.setConfirmClickListener(sDialog -> {
                        sDialog.dismiss();
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
                                                viewModel.updateStatusRequest(req.getId(), false)
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
                                                viewModel.updateStatusRequest(req.getId(), true)
                                                        .subscribeOn(Schedulers.io())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe(responseDTO -> {
                                                            getActivity().getSupportFragmentManager().beginTransaction()
                                                                    .replace(R.id.frame_container, new ShopHomeFragment())
                                                                    .commit();
                                                        });
                                                btnCancelReq.setVisibility(View.VISIBLE);
                                            });
                                            sweetAlertDialog.show();
                                        });

                                    } else {
                                        SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
                                        txtNoReq.setVisibility(View.VISIBLE);
                                    }
                                });
                    });
                    sweetAlertDialog.show();
                }

                if (responeReq.getMessage().equals(MyInstances.NOTI_CANELED)) {
                    SweetAlertDialog cancelReq = new SweetAlertDialog(getBaseActivity(), SweetAlertDialog.NORMAL_TYPE);
                    cancelReq.setTitleText("Thông báo");
                    cancelReq.setConfirmText("Đóng");
                    cancelReq.setCanceledOnTouchOutside(false);
                    cancelReq.setCancelable(false);
                    cancelReq.setContentText("Khách đã hủy yêu cầu");
                    cancelReq.setConfirmClickListener(sDialog -> {
                        sDialog.dismiss();
                        txtNoReq.setVisibility(View.VISIBLE);
                        SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
                    });
                    cancelReq.show();
                }
            }

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
        if (request.trim().equals("")) {
            txtNoReq.setVisibility(View.VISIBLE);
        } else {
            txtNoReq.setVisibility(View.GONE);
            Request requestFromPref = gson.fromJson(request, Request.class);

            viewModel.getRequestById(requestFromPref.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(req -> {
                        if (req != null) {
                            btnTracking.setOnClickListener(v -> {
                                Intent intent = new Intent(getActivity(), TrackingMapActivity.class);
                                intent.putExtra("isBikerTracking", false);
                                intent.putExtra("reqId", req.getId());
                                startActivity(intent);

                                Intent serviceIntent = new Intent(getActivity(), UpdateLocationService.class);
                                CurrentRequest.getInstance().setRequestCode(req.getRequestCode());
                                CurrentUser.getInstance().setCurrentBikerId(req.getCreatedUser().getId());
                                CurrentUser.getInstance().setChosenShopOwnerId(CurrentUser.getInstance().getId());
                                getActivity().startService(serviceIntent);
                            });

                            if (req.getStatus().equals(MyInstances.STATUS_ACCEPT) || req.getStatus().equals(MyInstances.STATUS_CREATED)) {
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
                                        viewModel.updateStatusRequest(req.getId(), false)
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
                                        viewModel.updateStatusRequest(req.getId(), true)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(responseDTO -> {
                                                    getActivity().getSupportFragmentManager().beginTransaction()
                                                            .replace(R.id.frame_container, new ShopHomeFragment())
                                                            .commit();
                                                });
                                        btnCancelReq.setVisibility(View.VISIBLE);
                                    });
                                    sweetAlertDialog.show();
                                });
                            } else {
                                SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
                                txtNoReq.setVisibility(View.VISIBLE);
                            }
                        }
                    });

            btnFinish.setOnClickListener(v -> {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getBaseActivity(), SweetAlertDialog.NORMAL_TYPE);
                sweetAlertDialog.setTitleText("Thông báo");
                sweetAlertDialog.setConfirmText("Xác nhận");
                sweetAlertDialog.setCanceledOnTouchOutside(false);
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.setContentText("Xác nhận hoàn thành yêu cầu?");
                sweetAlertDialog.setCancelText("Hủy");
                sweetAlertDialog.setCancelClickListener(Dialog::dismiss);
                sweetAlertDialog.setConfirmClickListener(dialog -> {
                    dialog.dismiss();

                    viewModel.finishedRequest(requestFromPref.getId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(isSuccess -> {
                                if (isSuccess) {
                                    SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
                                    txtNoReq.setVisibility(View.VISIBLE);
                                }
                            });
                });
                sweetAlertDialog.show();
            });
        } //end of else


        btnReqImg.setOnClickListener(v -> {
            SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
        });
    }

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
        btnCancelReq.setVisibility(View.GONE);
        btnTracking.setVisibility(View.GONE);
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

        btnReqImg.setVisibility(View.GONE);

    }

    @SuppressLint("SetTextI18n")
    private void refreshStatus(String status, int reqId) {
        txtStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.core_color));
        //request moi duoc tao
        if (status.equals(MyInstances.STATUS_CREATED)) {
            hideFinishButton();
            txtStatus.setVisibility(View.GONE);
            txtNoReq.setVisibility(View.GONE);
            btnTracking.setVisibility(View.VISIBLE);
        }

        if (status.equals(MyInstances.STATUS_ACCEPT)) {
            showFinishButton();
            btnTracking.setVisibility(View.VISIBLE);
            txtStatus.setVisibility(View.VISIBLE);
            txtStatus.setText("Đã nhận ");
            btnCancelReq.setVisibility(View.VISIBLE);
            btnCancelReq.setOnClickListener(v -> {
                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View editDialogView = factory.inflate(R.layout.activity_cancel_reason, null);
                final AlertDialog editDialog = new AlertDialog.Builder(getActivity()).create();
                editDialog.setView(editDialogView);
                MaterialSpinner spinner = editDialogView.findViewById(R.id.confirm_spinner);

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

                editDialogView.findViewById(R.id.btn_confirm).setOnClickListener(confirmView -> {
                    editDialog.dismiss();
                    String reason = spinner.getText().toString();
                    viewModel.cancleRequest(reqId, false, reason)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(isSuccess -> {
                                if (isSuccess) {
                                    SweetAlertDialog notiDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
                                    notiDialog.setTitleText("Thông báo");
                                    notiDialog.setContentText("Huỷ thành công");
                                    notiDialog.setConfirmText("OK");
                                    notiDialog.setConfirmClickListener(Dialog::dismiss);
                                    notiDialog.show();
                                    txtStatus.setVisibility(View.VISIBLE);
                                    txtStatus.setText("Đã hủy");
                                    txtStatus.setTextColor(Color.RED);
                                    btnCancelReq.setVisibility(View.GONE);
                                    txtNoReq.setVisibility(View.VISIBLE);
                                    txtReqCancelReason.setVisibility(View.VISIBLE);
                                    txtReqCancelReason.setText(" Lý do hủy: " + reason);
                                    SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_BIKER_REQUEST, "");
                                }
                            }, throwable -> {
                                Log.e(TAG, "cancleRequest: " + throwable.getMessage());
                            });
                });
                editDialogView.findViewById(R.id.btn_return).setOnClickListener(v1 -> editDialog.dismiss());
                editDialog.show();
            });
        }

        if (status.equals(MyInstances.STATUS_REJECTED)) {
            txtStatus.setVisibility(View.VISIBLE);
            txtStatus.setText("Đã từ chối ");
            txtStatus.setTextColor(Color.RED);
            statusCreated.setVisibility(View.GONE);
            btnFinish.setVisibility(View.GONE);
            SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
        }

        if (status.equals(MyInstances.STATUS_CANCELED)) {
            txtNoReq.setVisibility(View.VISIBLE);
            SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
        }

        if (status.equals(MyInstances.STATUS_FINISHED)) {
            txtNoReq.setVisibility(View.VISIBLE);
            SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
        }
    }

}
