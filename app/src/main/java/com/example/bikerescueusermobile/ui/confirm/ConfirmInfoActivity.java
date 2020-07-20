package com.example.bikerescueusermobile.ui.confirm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.data.model.configuration.MyConfiguaration;
import com.example.bikerescueusermobile.data.model.request.CurrentRequest;
import com.example.bikerescueusermobile.data.model.request.Request;
import com.example.bikerescueusermobile.data.model.request.RequestDTO;
import com.example.bikerescueusermobile.data.model.request.RequestShopService;
import com.example.bikerescueusermobile.data.model.shop.Shop;
import com.example.bikerescueusermobile.data.model.shop_services.ShopService;
import com.example.bikerescueusermobile.data.model.shop_services.ShopServiceTable;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.data.model.user.User;
import com.example.bikerescueusermobile.data.model.vehicle.Vehicle;
import com.example.bikerescueusermobile.ui.create_request.RequestDetailActivity;
import com.example.bikerescueusermobile.ui.login.LoginActivity;
import com.example.bikerescueusermobile.ui.main.MainActivity;
import com.example.bikerescueusermobile.ui.map.MapActivity;
import com.example.bikerescueusermobile.ui.register.CreatePasswordActivity;
import com.example.bikerescueusermobile.ui.seach_shop_service.ShopServiceViewModel;
import com.example.bikerescueusermobile.ui.send_request.SendRequestActivity;
import com.example.bikerescueusermobile.ui.update_info.UpdateInfoActivity;
import com.example.bikerescueusermobile.util.MyInstances;
import com.example.bikerescueusermobile.util.MyMethods;
import com.example.bikerescueusermobile.util.SharedPreferenceHelper;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ConfirmInfoActivity extends BaseActivity {

    private static final String TAG = "ConfirmInfoActivity";

    @BindView(R.id.confirm_toolbar)
    Toolbar toolbar;

    @BindView(R.id.tvConfirmMyBike)
    TextView tvConfirmMyBike;

    @BindView(R.id.tvProblem)
    TextView tvProblem;

    @BindView(R.id.btnBookService)
    TextView tvBookService;

    @BindView(R.id.btnImg)
    ImageButton btnImg;

    @BindView(R.id.edtName)
    EditText edtName;

    @BindView(R.id.edtPhone)
    EditText edtPhone;

    @BindView(R.id.edtMarker)
    EditText edtConfirmAddress;

    @BindView(R.id.edtConfirmDescription)
    EditText edtConfirmDescription;

    @BindView(R.id.txtConfirmShopName)
    EditText txtConfirmShopName;

    @BindView(R.id.txtConfirmShopAddress)
    EditText txtConfirmShopAddress;

    @BindView(R.id.txtConfirmProblemBadge)
    TextView txtConfirmProblemBadge;

    @Inject
    ViewModelFactory viewModelFactory;

    private ConfirmViewModel viewModel;
    private ArrayList<ShopServiceTable> listAllShopServices;
    private List<Vehicle> listUserVehicle;
    private int selectedVehicle = -1;
    private Shop selectedShop;
    private int shopServiceId = -1;
    private SweetAlertDialog errorDialog;
    private SweetAlertDialog loadingDialog;
    private int selectedService = -1;
    private Intent intentReqDetail;
    @Override
    protected int layoutRes() {
        return R.layout.activity_confirm_info;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentReqDetail = new Intent(getApplicationContext(), RequestDetailActivity.class);

        //setup process dialog
        loadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loadingDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        loadingDialog.setTitleText("Đang gửi yêu cầu...");
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);

        //setup error dialog
        errorDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        errorDialog.setTitleText("Thông báo");
        errorDialog.setConfirmText("Xác nhận");
        errorDialog.setContentText("Vui lòng kiểm tra lại thông tin!!");
        errorDialog.setConfirmClickListener(sDialog2 -> {
            sDialog2.cancel();
            errorDialog.dismiss();
        });

        //get extra from MapActivity
        listAllShopServices = getIntent().getParcelableArrayListExtra("allServices");
        selectedShop = (Shop) getIntent().getSerializableExtra("selectedShop");

        //set up thong tin shop
        txtConfirmShopName.setText(" " + selectedShop.getShopName());
        txtConfirmShopAddress.setText(" " + selectedShop.getAddress());

        //setup toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Xác nhận thông tin");

        //setup viewmodel
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ConfirmViewModel.class);

        //get all vehicle title
        viewModel.getVehicleByUserId(CurrentUser.getInstance().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listVehicles -> {
                    listUserVehicle = listVehicles;
                    if (listVehicles != null && listVehicles.size() > 0) {
                        ArrayAdapter<String> vehiclesName = new ArrayAdapter<>(this,
                                android.R.layout.simple_list_item_1);

                        for (int i = 0; i < listVehicles.size(); i++) {
                            vehiclesName.add(listVehicles.get(i).getBrand() + " " + listVehicles.get(i).getVehiclesYear());
                        }
                        tvConfirmMyBike.setText(vehiclesName.getItem(0));
                        tvConfirmMyBike.setOnClickListener(v -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmInfoActivity.this);
                            builder.setTitle("Xe của tôi");
                            builder.setAdapter(vehiclesName, (dialog, which) -> {
                                tvConfirmMyBike.setText(vehiclesName.getItem(which));
                                selectedVehicle = which;
                                dialog.dismiss();
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        });
                    } else {
                        errorDialog.show();
                    }
                });

        //setup user data
        edtName.setText(CurrentUser.getInstance().getFullName());
        edtPhone.setText(CurrentUser.getInstance().getPhoneNumber());
        String mPlace = getIntent().getStringExtra("placeName");
        edtConfirmAddress.setText(mPlace);

        String serviceName = getIntent().getStringExtra("serviceName");
        tvProblem.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmInfoActivity.this);
            builder.setTitle("Vấn đề của bạn");

            ArrayAdapter<String> problems = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1);
            for (int i = 0; i < listAllShopServices.size(); i++) {
                problems.add("Tôi cần " + listAllShopServices.get(i).getServices().getName().toLowerCase());
            }

            builder.setAdapter(problems, (dialog, which) -> {
                dialog.dismiss();
                tvProblem.setText(problems.getItem(which));
                selectedService = which;
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        if (serviceName.equals("")) {
            tvProblem.setText("Vui lòng chọn vấn đề của bạn");
        } else {
            String s = "Tôi cần " + serviceName.toLowerCase().trim() + ".";
            for (int i = 0; i < listAllShopServices.size(); i++) {
                if (serviceName.equals(listAllShopServices.get(i).getServices().getName())) {
                    selectedService = i;
                    break;
                }
            }
            tvProblem.setText(s);
        }

        tvBookService.setOnClickListener(v -> {
            txtConfirmProblemBadge.setVisibility(View.GONE);
            int serviceId = -1;
            if (selectedService != -1) {
                serviceId = listAllShopServices.get(selectedService).getServices().getId();
                viewModel.getShopServiceId(selectedShop.getId(), serviceId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(shopService -> {
                            if (shopService != null) {
                                shopServiceId = shopService.getId();
                            }
                        });

                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
                sweetAlertDialog.setTitleText("Thông báo");
                sweetAlertDialog.setConfirmText("Xác nhận");
                sweetAlertDialog.setContentText("Xác nhận thực hiện gọi dịch vụ lúc này?");
                sweetAlertDialog.setConfirmClickListener(sDialog -> {
                    if (shopServiceId != -1) {
                        sendReqAndSaveRequestToSharePref();
                        sDialog.cancel();
                    } else {
                        sDialog.cancel();
                        errorDialog.show();
                    }
                });
                sweetAlertDialog.setCancelButton("Quay lại", sDialog -> {
                    sDialog.dismissWithAnimation();
                });
                sweetAlertDialog.show();
            } else {
                errorDialog.show();
                txtConfirmProblemBadge.setVisibility(View.VISIBLE);
            }
        });

        btnImg.setOnClickListener(v -> {
            ImagePicker.Companion.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(100, 100)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //get File object from ImagePicker
            File file = ImagePicker.Companion.getFile(data);

            // set path toi' hin`h do' thanh` bitmap
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            //set image to image view
            btnImg.setImageBitmap(myBitmap);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Log.e(TAG, "ImagePicker - Get image fail: " + ImagePicker.Companion.getError(data));
        } else {
            Log.e(TAG, "ImagePicker - Task Cancelled");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendReqAndSaveRequestToSharePref() {
        //create code from current time -> seconds
        long secs = (new Date().getTime()) / 1000;
        String code = "" + String.format(Locale.getDefault(), "%013d", Integer.parseInt("" + secs));

        String address = edtConfirmAddress.getText().toString();

        String description = edtConfirmDescription.getText().toString();

        String userLat = CurrentUser.getInstance().getLatitude();
        String userLong = CurrentUser.getInstance().getLongtitude();

        int vehicleId = listUserVehicle.get(0).getId();
        if (selectedVehicle != -1) {
            vehicleId = listUserVehicle.get(selectedVehicle).getId();
        }

        RequestDTO request = new RequestDTO();
        request.setCode(code);
        request.setAddress(address);
        request.setAcceptedId(CurrentUser.getInstance().getChosenShopOwnerId());
        request.setCreatedId(CurrentUser.getInstance().getId());
        request.setDescription(description);
        request.setLatitude(userLat);
        request.setLongtitude(userLong);
        request.setVehicleId(vehicleId);
        request.setShopServiceId(shopServiceId);

        Gson gson = new Gson();

        viewModel.createRequest(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Log.e(TAG, "respone data: " + gson.toJson(response.getData()));

                    if(response!= null) {
//                        CurrentUser.getInstance().setSentReqId(response.getData().getId().intValue());
                        intentReqDetail.putExtra("reqId", response.getData().getId().intValue());

                        String sharedPreferenceStr = gson.toJson(response.getData());
                        SharedPreferenceHelper.setSharedPreferenceString(getApplicationContext(), MyInstances.KEY_BIKER_REQUEST, sharedPreferenceStr);

                        startActivity(intentReqDetail);
                        finish();
                    }
                });


    }

}
