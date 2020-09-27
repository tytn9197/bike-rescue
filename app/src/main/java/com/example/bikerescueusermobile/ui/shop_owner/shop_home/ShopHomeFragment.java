package com.example.bikerescueusermobile.ui.shop_owner.shop_home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseFragment;
import com.example.bikerescueusermobile.data.model.request.MessageRequestFB;
import com.example.bikerescueusermobile.data.model.request.ReqShopSerDTO;
import com.example.bikerescueusermobile.data.model.request.Request;
import com.example.bikerescueusermobile.data.model.request.RequestShopService;
import com.example.bikerescueusermobile.data.model.shop_services.CurrentShopService;
import com.example.bikerescueusermobile.data.model.shop_services.ShopServiceQuantity;
import com.example.bikerescueusermobile.data.model.shop_services.ShopServiceTable;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.ui.confirm.ConfirmViewModel;
import com.example.bikerescueusermobile.ui.create_request.RequestDetailViewModel;
import com.example.bikerescueusermobile.ui.favorite.FavoriteRecyclerViewAdapter;
import com.example.bikerescueusermobile.ui.login.UpdateLocationService;
import com.example.bikerescueusermobile.ui.seach_shop_service.ShopServiceViewModel;
import com.example.bikerescueusermobile.ui.tracking_map.TrackingMapActivity;
import com.example.bikerescueusermobile.util.MyInstances;
import com.example.bikerescueusermobile.util.MyMethods;
import com.example.bikerescueusermobile.util.SharedPreferenceHelper;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mingle.sweetpick.CustomDelegate;
import com.mingle.sweetpick.SweetSheet;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

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

    @BindView(R.id.txtShopHomeCountdown)
    TextView txtCountdown;

    @BindView(R.id.btnShopHomeArrived)
    Button btnArrived;

    @BindView(R.id.shopHomeRelativeLayout)
    RelativeLayout shopHomeRelativeLayout;

    @BindView(R.id.btnAddShopService)
    Button btnAddShopService;

    @Inject
    ViewModelFactory viewModelFactory;

    private RequestDetailViewModel viewModel;
    private CountDownTimer countDownTimer;
    private double distance = -1;
    private SweetSheet mSweetSheet;
    private ArrayList<ShopServiceTable> listAllShopServices;
    private int vanDeKhacPos = -1;
    private int quantity = 1;
    private float serPrice = -1;
    private ShopServiceTable selectedShopSer;
    private List<RequestShopService> listReqShopSer = new ArrayList<>();
    private double totalPrice = 0;

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

                                    listReqShopSer = new ArrayList<>();
                                    CurrentShopService.getInstance().clear();
                                    CurrentShopService.setDelete(false);
                                    if (!req.getListReqShopService().get(0).getShopService().getServices().getName().equals("Vấn đề khác")) {
                                        RequestShopService requestShopService = new RequestShopService();
                                        requestShopService.setShopService(req.getListReqShopService().get(0).getShopService());
                                        requestShopService.setQuantity(1);
                                        listReqShopSer.add(requestShopService);
                                        CurrentShopService.getInstance().add(requestShopService);
                                    }

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
                                        rejectReq(req.getId());
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
                                                        SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_COUNT_DOWN_TIME, "");

                                                        Intent serviceIntent = new Intent(getActivity(), UpdateLocationService.class);
                                                        CurrentUser.getInstance().setCurrentBikerId(req.getCreatedUser().getId());
                                                        CurrentUser.getInstance().setChosenShopOwnerId(CurrentUser.getInstance().getId());
                                                        getActivity().startService(serviceIntent);

                                                        Intent tracking = new Intent(getActivity(), TrackingMapActivity.class);
                                                        tracking.putExtra("isBikerTracking", false);
                                                        tracking.putExtra("reqId", req.getId());
                                                        tracking.putExtra("reqStatus", MyInstances.STATUS_ACCEPT);
                                                        startActivityForResult(tracking, MyInstances.SHOP_RESULT_CODE);
                                                    }, throwable -> {
                                                        Log.e(TAG, "updateStatusRequest: " + throwable.getMessage());
                                                    });
                                            btnCancelReq.setVisibility(View.VISIBLE);
//                                            btnArrived.setVisibility(View.VISIBLE);
                                        });
                                        sweetAlertDialog.show();
                                    });

                                    //run cown down timer first time
                                    if (getActivity() != null)
                                        SharedPreferenceHelper.setSharedPreferenceString
                                                (getActivity(), MyInstances.KEY_COUNT_DOWN_TIME, "" + System.currentTimeMillis());
                                    runCountDownTimer(req.getId(), CurrentUser.getInstance().getRejectTime());
                                } else {
                                    SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
                                    SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_COUNT_DOWN_TIME, "");
                                    txtNoReq.setVisibility(View.VISIBLE);
                                }
                            }, throwable -> {
                                Log.e(TAG, "getRequestById: " + throwable.getMessage());
                            });
                }

                if (responeReq.getMessage().equals(MyInstances.NOTI_CANELED)) {
                    txtNoReq.setVisibility(View.VISIBLE);
                    SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
                    SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_COUNT_DOWN_TIME, "");

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

        if(CurrentShopService.getInstance().size() > 0){
            listReqShopSer = new ArrayList<>();
            listReqShopSer.addAll(CurrentShopService.getInstance());
        }
        Log.e(TAG, "onViewCreated start - list shop service size: " + CurrentShopService.getInstance().size());

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mMessageReceiver, new IntentFilter("BikeRescueShop"));

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RequestDetailViewModel.class);
        String request = SharedPreferenceHelper.getSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");

        txtNoReq.setVisibility(View.VISIBLE);

        //get shop services
        ViewModelProviders.of(this, viewModelFactory).get(ShopServiceViewModel.class)
                .getShopServiceByShopId(CurrentUser.getInstance().getShop().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listServices -> {
                    if (listServices != null) {
                        listAllShopServices = new ArrayList<>();
                        listAllShopServices.addAll(listServices);
                    }
                }, throwable -> {
                    Log.e(TAG, "getShopServiceByShopId: " + throwable.getMessage());
                });

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

                                    listReqShopSer = new ArrayList<>();
                                    if (!req.getListReqShopService().get(0).getShopService().getServices().getName().equals("Vấn đề khác")) {
                                        RequestShopService requestShopService = new RequestShopService();
                                        requestShopService.setShopService(req.getListReqShopService().get(0).getShopService());
                                        requestShopService.setQuantity(1);
                                        listReqShopSer.add(requestShopService);
                                    }

                                    if(CurrentShopService.getInstance().size() > 0){
                                        listReqShopSer = new ArrayList<>();
                                        listReqShopSer.addAll(CurrentShopService.getInstance());
                                    }

                                    if(CurrentShopService.isDelete()){
                                        listReqShopSer = new ArrayList<>();
                                    }
                                    //get count down timer and check
                                    String shareCountDown = SharedPreferenceHelper
                                            .getSharedPreferenceString(getActivity(), MyInstances.KEY_COUNT_DOWN_TIME, "");
                                    if (!shareCountDown.equals("")) {
                                        long seconds = Long.parseLong(shareCountDown);
                                        if ((System.currentTimeMillis() - seconds) < 60 * 1000) {
                                            Log.e(TAG, "con count down timer ---- time: " + (System.currentTimeMillis() - seconds));
                                            runCountDownTimer(req.getId(),
                                                    (CurrentUser.getInstance().getRejectTime() - (System.currentTimeMillis() - seconds) / 1000));
                                        } else {
                                            Log.e(TAG, "xoa count down share ref");
                                            SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_COUNT_DOWN_TIME, "");
                                        }
                                    }
                                    String sharedPreferenceStr = gson.toJson(req);
                                    SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, sharedPreferenceStr);

                                    btnCallBiker.setOnClickListener(v -> {
                                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                        callIntent.setData(Uri.parse("tel:" + Uri.encode(req.getCreatedUser().getPhoneNumber())));
                                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(callIntent);
                                    });

                                    btnDecline.setOnClickListener(v -> {
                                        rejectReq(req.getId());
                                    });

                                    btnAccept.setOnClickListener(v -> {
                                        sweetAlertDialog.setContentText("Chấp nhận yêu cầu này?");
                                        sweetAlertDialog.setConfirmClickListener(d -> {
                                            d.dismiss();
                                            viewModel.updateStatusRequest(req.getId(), MyInstances.STATUS_ACCEPT)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(responseDTO -> {
                                                        SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_COUNT_DOWN_TIME, "");
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
                                                        tracking.putExtra("reqStatus", MyInstances.STATUS_ACCEPT);
                                                        startActivityForResult(tracking, MyInstances.SHOP_RESULT_CODE);
                                                    }, throwable -> {
                                                        Log.e(TAG, "updateStatusRequest: " + throwable.getMessage());
                                                    });
                                            btnCancelReq.setVisibility(View.VISIBLE);
//                                            btnArrived.setVisibility(View.VISIBLE);
                                        });
                                        sweetAlertDialog.show();
                                    });
                                } else {
                                    SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
                                    SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_COUNT_DOWN_TIME, "");
                                    txtNoReq.setVisibility(View.VISIBLE);
                                }
                            }
//                            setBtnArrived(req.getId());
                        }// req ->
                    }, throwable -> {
                        Log.e(TAG, "oncreate - getRequestById: " + throwable.getMessage());
                    });
        }//end of else
    }

    @SuppressLint("SetTextI18n")
    private void setDataToView(Request request) {
        allButtonGone();

        btnReqImg.setOnClickListener(v -> {
            mSweetSheet = new SweetSheet(shopHomeRelativeLayout);
            CustomDelegate customDelegate = new CustomDelegate(true,
                    CustomDelegate.AnimationType.DuangLayoutAnimation);
            View mView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_show_image, null, false);
            customDelegate.setCustomView(mView);
            mSweetSheet.setDelegate(customDelegate);
            viewModel.getReqImgByReqId(request.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listImgs -> {
                        if (listImgs != null && listImgs.size() > 0) {
                            LinearLayout imageLayout = mView.findViewById(R.id.imageLayout);

                            for (int i = 0; i < listImgs.size(); i++) {
                                ImageView image = new ImageView(getActivity());
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
                                int marginValue = 10;
                                params.setMargins(marginValue, marginValue, marginValue, marginValue);
                                Picasso.with(getActivity())
                                        .load(listImgs.get(0).getImage()).placeholder(R.drawable.ic_load)
                                        .into(image);
                                image.setLayoutParams(params);
                                imageLayout.addView(image);
                            }

                            mSweetSheet.show();
                        }
                    }, throwable -> {
                        Log.e(TAG, "getReqImgByReqId: " + throwable.getMessage());
                    });
        });

        btnAddShopService.setOnClickListener(v -> {
            selectedShopSer = null;
            LayoutInflater factory = LayoutInflater.from(getActivity());
            final View priceView = factory.inflate(R.layout.dialog_add_shop_service, null);
            AlertDialog priceDialog = new AlertDialog.Builder(getActivity()).create();

            TextView spinnerSerName = priceView.findViewById(R.id.spinnerSerName);
            LinearLayout layoutQuantity = priceView.findViewById(R.id.layoutQuantity);
            Spinner spinnerQuantity = priceView.findViewById(R.id.spinnerQuantity);
            TextView txtUnitPrice = priceView.findViewById(R.id.txtUnitPrice);
            TextView txtPriceSum = priceView.findViewById(R.id.txtPriceSum);

            //moi tao => ẩn hết, chỉ hiện nút chọn dịch vụ
            spinnerSerName.setText("Vui lòng chọn một dịch vụ");
            layoutQuantity.setVisibility(View.GONE);
            txtUnitPrice.setVisibility(View.GONE);
            txtPriceSum.setVisibility(View.GONE);

            // init so luong tu 1 -> 3
            spinnerQuantity.setPrompt("1");
            quantity = 1;
            ArrayAdapter<String> quantityNumberAdapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item);
            for (int i = 1; i < 4; i++) {
                quantityNumberAdapter.add("" + i);
            }
            spinnerQuantity.setAdapter(quantityNumberAdapter);

            spinnerQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    quantity = position + 1;
                    txtPriceSum.setText("Tổng tiền: " + MyMethods.convertMoney(serPrice * quantity) + " vnd");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //set spinner của dịch vụ
            spinnerSerName.setOnClickListener(v1 -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(request.getListReqShopService().get(0).getShopService().getServices().getName());

                ArrayAdapter<String> problems = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_list_item_1);
                for (int i = 0; i < listAllShopServices.size(); i++) {
                    if (!listAllShopServices.get(i).getServices().getName().equals("Vấn đề khác")) {
                        problems.add(listAllShopServices.get(i).getServices().getName());
                    } else {
                        vanDeKhacPos = i;
                    }
                }

                builder.setAdapter(problems, (dialog, which) -> {
                    dialog.dismiss();
                    int serId = -1;
                    if (which < vanDeKhacPos) {
                        spinnerSerName.setText(listAllShopServices.get(which).getServices().getName());
                        serId = listAllShopServices.get(which).getServices().getId();
                    } else {
                        spinnerSerName.setText(listAllShopServices.get(which + 1).getServices().getName());
                        serId = listAllShopServices.get(which + 1).getServices().getId();
                    }

                    //get lai service price cua shop do
                    ViewModelProviders.of(this, viewModelFactory).get(ConfirmViewModel.class)
                            .getShopServiceId(CurrentUser.getInstance().getShop().getId(), serId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(shopService -> {
                                if (shopService != null) {
                                    selectedShopSer = shopService;
                                    serPrice = shopService.getPrice().floatValue() * 1000;
                                    txtUnitPrice.setText("Giá: " +
                                            MyMethods.convertMoney(serPrice) +
                                            " vnd / " + shopService.getServices().getUnit());

                                    txtPriceSum.setText("Tổng tiền: " + MyMethods.convertMoney(serPrice * quantity) + " vnd");

                                    //set lai view khi chon 1 dich vu
                                    spinnerSerName.setText(shopService.getServices().getName());
                                    layoutQuantity.setVisibility(View.VISIBLE);
                                    txtUnitPrice.setVisibility(View.VISIBLE);
                                    txtPriceSum.setVisibility(View.VISIBLE);
                                }
                            }, throwable -> {
                                Log.e(TAG, "getShopServiceId: " + throwable.getMessage());
                            });
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            });

            //set thêm dịch vụ button
            priceDialog.setView(priceView);
            priceView.findViewById(R.id.btn_confirm).setOnClickListener(confirmView -> {
                SweetAlertDialog error = new SweetAlertDialog(getBaseActivity(), SweetAlertDialog.ERROR_TYPE);
                error.setTitleText("Thông báo");
                error.setConfirmText("Đóng");
                error.setConfirmClickListener(Dialog::dismiss);
                boolean isHasService = false;
                if (selectedShopSer != null) {
                    for (int i = 0; i < listReqShopSer.size(); i++) {
                        if (listReqShopSer.get(i).getShopService().getId() == selectedShopSer.getId()) {
                            isHasService = true;
                            break;
                        }
                    }

                    if (isHasService) {
                        error.setContentText("Đã chọn dịch vụ này");
                        error.show();
                    } else {
                        RequestShopService requestShopService = new RequestShopService();
                        requestShopService.setQuantity(quantity);
                        requestShopService.setShopService(selectedShopSer);
                        requestShopService.setRequest(request);
                        listReqShopSer.add(requestShopService);

                        error.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        error.setContentText("Thêm thành công");
                        error.show();

                        if(CurrentShopService.isDelete()){
                            CurrentShopService.setDelete(false);
                        }
                        priceDialog.dismiss();
                        Log.e(TAG, "add service success: " + requestShopService.toString());
                    }
                } else {
                    error.setContentText("Vui lòng chọn một dịch vụ");
                    error.show();
                }
            });

            priceView.findViewById(R.id.btn_return).setOnClickListener(v1 -> priceDialog.dismiss());
            priceDialog.show();
        }); //end button add service onclick

        btnFinish.setOnClickListener(v -> {
            SweetAlertDialog error = new SweetAlertDialog(getBaseActivity(), SweetAlertDialog.ERROR_TYPE);
            error.setTitleText("Thông báo");
            error.setConfirmText("Đóng");
            error.setConfirmClickListener(Dialog::dismiss);

            if (listReqShopSer.size() == 0) {
                error.setContentText("Vui lòng thêm một dịch vụ");
                error.show();
            } else {
                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View priceView = factory.inflate(R.layout.dialog_confirm_price, null);
                AlertDialog priceDialog = new AlertDialog.Builder(getActivity()).create();

                TextView txtTotalPrice = priceView.findViewById(R.id.txtTotalPrice);
                RecyclerView rvConfirmPrice = priceView.findViewById(R.id.rvConfirmPrice);
                totalPrice = 0;
                List<ShopServiceQuantity> listQuantity = new ArrayList<>();
                for (int i = 0; i < listReqShopSer.size(); i++) {
                    double price = listReqShopSer.get(i).getShopService().getPrice() * listReqShopSer.get(i).getQuantity();
                    ShopServiceQuantity shopServiceQuantity = new ShopServiceQuantity();
                    shopServiceQuantity.setQuantity(listReqShopSer.get(i).getQuantity());
                    shopServiceQuantity.setShopServiceId(listReqShopSer.get(i).getShopService().getId());
                    listQuantity.add(shopServiceQuantity);
                    totalPrice += price;
                }

                rvConfirmPrice.setAdapter(new ConfirmPriceRecyclerViewAdapter(listReqShopSer, false, new ConfirmPriceSelectedListener() {
                    @Override
                    public void onDeleteClick(RequestShopService requestShopService) {
                        Log.e(TAG, "onDeleteClick");
                        listReqShopSer.remove(requestShopService);
                        priceDialog.dismiss();
                        if(listReqShopSer.size() == 0) {
                            CurrentShopService.getInstance().clear();
                            CurrentShopService.setDelete(true);
                        }
                        btnFinish.callOnClick();
                    }

                    @Override
                    public void onChangeQuantityClick(RequestShopService requestShopService) {
                        Log.e(TAG, "onChangeQuantityClick");

                        ArrayAdapter<String> quantityNumberAdapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item);
                        for (int i = 1; i < 4; i++) {
                            quantityNumberAdapter.add("" + i);
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Vui lòng chọn số lượng");
                        builder.setAdapter(quantityNumberAdapter, (dialog, which) -> {
                            for (int i = 0; i < listReqShopSer.size(); i++) {
                                if(listReqShopSer.get(i).equals(requestShopService)){
                                    listReqShopSer.get(i).setQuantity(which + 1);
                                    break;
                                }
                            }
                            dialog.dismiss();
                            priceDialog.dismiss();
                            btnFinish.callOnClick();
                        });
                        AlertDialog quantityDialog = builder.create();
                        quantityDialog.show();
                    }
                }));
                rvConfirmPrice.setLayoutManager(new LinearLayoutManager(getActivity()));

                txtTotalPrice.setText(MyMethods.convertMoney((float) totalPrice * 1000) + " vnd");

                priceDialog.setView(priceView);
                priceView.findViewById(R.id.btn_confirm).setOnClickListener(confirmView -> {
                    ReqShopSerDTO reqShopSerDTO = new ReqShopSerDTO();
                    reqShopSerDTO.setReqId(request.getId());
                    reqShopSerDTO.setPrice(totalPrice);
                    reqShopSerDTO.setListShopService(listQuantity);
                    viewModel.finishedRequest(reqShopSerDTO)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(isSuccess -> {
                                if (isSuccess) {
                                    SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
                                    txtNoReq.setVisibility(View.VISIBLE);
                                    CurrentShopService.getInstance().clear();
                                    listReqShopSer.clear();
                                    if(CurrentShopService.isDelete()){
                                        CurrentShopService.setDelete(false);
                                    }
                                    priceDialog.dismiss();
                                }
                            }, throwable -> {
                                Log.e(TAG, "finishedRequest: " + throwable.toString());
                            });
                });
                priceView.findViewById(R.id.btn_return).setOnClickListener(v1 -> priceDialog.dismiss());
                priceDialog.show();
            }
        }); // end button finish on click

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
                } else {
                    txtReasonDetail.setVisibility(View.GONE);
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
                                    CurrentShopService.getInstance().clear();
                                    listReqShopSer.clear();
                                    if(CurrentShopService.isDelete()){
                                        CurrentShopService.setDelete(false);
                                    }
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
            btnAddShopService.setVisibility(View.VISIBLE);
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

    private void allButtonGone() {
        btnCancelReq.setVisibility(View.GONE);
//        btnReqImg.setVisibility(View.GONE);
        statusCreated.setVisibility(View.GONE);
        btnFinish.setVisibility(View.GONE);
        btnAddShopService.setVisibility(View.GONE);
        btnArrived.setVisibility(View.GONE);
        btnTracking.setVisibility(View.GONE);
        txtReqCancelReason.setVisibility(View.GONE);
        txtStatus.setVisibility(View.GONE);
    }

    private void rejectReq(int reqId) {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View editDialogView = factory.inflate(R.layout.activity_cancel_reason, null);
        final AlertDialog editDialog = new AlertDialog.Builder(getActivity()).create();
        editDialog.setView(editDialogView);

        MaterialSpinner spinner = editDialogView.findViewById(R.id.confirm_spinner);
        EditText txtReasonDetail = editDialogView.findViewById(R.id.txtReasonDetail);
        TextView tittle = editDialogView.findViewById(R.id.select_content);
        tittle.setText("Chọn lý do từ chối yêu cầu:");

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
            } else {
                txtReasonDetail.setVisibility(View.GONE);
            }
        });

        editDialogView.findViewById(R.id.btn_confirm).setOnClickListener(confirmView -> {
            editDialog.dismiss();
            String reason = spinner.getText().toString();
            if (reason.equals("Lý do khác")) {
                reason = txtReasonDetail.getText().toString();
            }
            viewModel.rejectRequest(reqId, reason)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(isSuccess -> {
                        if (isSuccess) {
                            SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_BIKER_REQUEST, "");
                            SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_COUNT_DOWN_TIME, "");
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frame_container, new ShopHomeFragment())
                                    .commit();
                        }
                    }, throwable -> {
                        Log.e(TAG, "rejectRequest: " + throwable.getMessage());
                    });
        });
        editDialogView.findViewById(R.id.btn_return).setOnClickListener(v1 -> editDialog.dismiss());
        editDialog.show();
    }

    private void runCountDownTimer(int reqId, long seconds) {
        countDownTimer = new CountDownTimer(seconds * 1000, 1000) {

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
                    Log.e(TAG, "count down onFinish");
                    countDownTimer.cancel();
                    SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_COUNT_DOWN_TIME, "");
                    txtCountdown.setText("");
                    viewModel.updateStatusRequest(reqId, MyInstances.NOTI_AUTO_REJECTED)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(responseDTO -> {
                                SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.frame_container, new ShopHomeFragment())
                                        .commit();
                            }, throwable -> {
                                Log.e(TAG, "updateStatusRequest: " + throwable.getMessage());
                            });
                }
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if(listReqShopSer.size() > 0) {
            CurrentShopService.getInstance().clear();
            CurrentShopService.getInstance().addAll(listReqShopSer);
        }
    }

}
