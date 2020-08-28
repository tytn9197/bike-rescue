package com.example.bikerescueusermobile.ui.confirm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.data.model.request.RequestDTO;
import com.example.bikerescueusermobile.data.model.shop.Shop;
import com.example.bikerescueusermobile.data.model.shop_services.ShopServiceTable;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.data.model.vehicle.Vehicle;
import com.example.bikerescueusermobile.ui.create_request.RequestDetailActivity;
import com.example.bikerescueusermobile.ui.profile.VehicleActivity;
import com.example.bikerescueusermobile.ui.seach_shop_service.ShopServiceViewModel;
import com.example.bikerescueusermobile.ui.shopMain.ShopMainActivity;
import com.example.bikerescueusermobile.util.MyInstances;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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

    @BindView(R.id.txtConfirmInfoPrice)
    TextView txtConfirmInfoPrice;

    @BindView(R.id.edtVanDeKhac)
    EditText edtVanDeKhac;

    @BindView(R.id.edtDoXang)
    EditText edtDoXang;

    @BindView(R.id.imageLayout)
    LinearLayout imageLayout;

    @BindView(R.id.image_scroll)
    HorizontalScrollView imgScroll;

    @Inject
    ViewModelFactory viewModelFactory;

    private ConfirmViewModel viewModel;
    private ArrayList<ShopServiceTable> listAllShopServices;
    private List<Vehicle> listUserVehicle;
    private int selectedVehicle = 0;
    private Shop selectedShop;
    private int shopServiceId = -1;
    private SweetAlertDialog errorDialog;
    private SweetAlertDialog loadingDialog;
    private int selectedService = -1;
    private String serviceName = "";
    private List<MultipartBody.Part> images = null;

    @Override
    protected int layoutRes() {
        return R.layout.activity_confirm_info;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setup viewmodel
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ConfirmViewModel.class);

        //setup process dialog
        loadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loadingDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        loadingDialog.setTitleText("Đang gửi yêu cầu...");
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);

        //setup error dialog
        errorDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        errorDialog.setTitleText("Thông báo");
        errorDialog.setConfirmText("OK");
        errorDialog.setContentText("Vui lòng kiểm tra lại thông tin!!");
        errorDialog.setConfirmClickListener(sDialog2 -> {
            sDialog2.cancel();
            errorDialog.dismiss();
        });

        //get extra from MapActivity
//        listAllShopServices = getIntent().getParcelableArrayListExtra("allServices");
        selectedShop = (Shop) getIntent().getSerializableExtra("selectedShop");

        //set up thong tin shop
        txtConfirmShopName.setText(" " + selectedShop.getShopName());
        txtConfirmShopAddress.setText(" " + selectedShop.getAddress());

        ShopServiceViewModel vm = ViewModelProviders.of(this, viewModelFactory).get(ShopServiceViewModel.class);
        vm.getShopServiceByShopId(selectedShop.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listServices -> {
                    if (listServices != null && listServices.size() > 0) {
                        listAllShopServices = new ArrayList<>();
                        listAllShopServices.addAll(listServices);

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
                                txtConfirmInfoPrice.setVisibility(View.VISIBLE);

                                if (listAllShopServices.get(which).getPrice().intValue() < 0) {
                                    txtConfirmInfoPrice.setText("Giá: liên hệ");
                                } else {
                                    txtConfirmInfoPrice.setText("Giá: " + listAllShopServices.get(which).getPrice().intValue() + "k vnd");
                                }

                                if (problems.getItem(which).contains("Đổ xăng".toLowerCase())) {
                                    edtDoXang.setVisibility(View.VISIBLE);
                                    txtConfirmInfoPrice.setVisibility(View.GONE);
                                } else {
                                    edtDoXang.setVisibility(View.GONE);
                                    txtConfirmInfoPrice.setVisibility(View.VISIBLE);
                                }

                                if (problems.getItem(which).contains("Vấn đề khác".toLowerCase())) {
                                    edtVanDeKhac.setVisibility(View.VISIBLE);
                                } else {
                                    edtVanDeKhac.setVisibility(View.GONE);
                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        });

                        if (serviceName.equals("")) {
                            tvProblem.setText("Vui lòng chọn vấn đề của bạn");
                        } else {
                            String s = "Tôi cần " + serviceName.toLowerCase().trim();
                            for (int i = 0; i < listAllShopServices.size(); i++) {
                                if (serviceName.equals(listAllShopServices.get(i).getServices().getName())) {
                                    selectedService = i;
                                    break;
                                }
                            }
                            tvProblem.setText(s);

                            if (serviceName.equals("Đổ xăng")) {
                                edtDoXang.setVisibility(View.VISIBLE);
                            }

                            if (serviceName.equals("Vấn đề khác")) {
                                edtVanDeKhac.setVisibility(View.VISIBLE);
                            }
                        }

                        tvBookService.setOnClickListener(v -> {
                            Log.e(TAG, "num of cancel: " + CurrentUser.getInstance().getNumOfCancel());
                            if (CurrentUser.getInstance().getNumOfCancel() < 4) {
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
                                                    this.serviceName = shopService.getServices().getName();
                                                }
                                            }, throwable -> {
                                                Log.e(TAG, "getShopServiceId: " + throwable.getMessage());
                                            });

                                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
                                    sweetAlertDialog.setTitleText("Thông báo");
                                    sweetAlertDialog.setConfirmText("Xác nhận");
                                    sweetAlertDialog.setContentText("Xác nhận thực hiện đặt dịch vụ?");
                                    sweetAlertDialog.setConfirmClickListener(sDialog -> {
                                        sDialog.dismiss();
                                        if (shopServiceId != -1) {
                                            int returnCode = sendReqAndSaveRequestToSharePref();

                                            SweetAlertDialog returnDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
                                            returnDialog.setTitleText("Thông báo");
                                            returnDialog.setConfirmText("OK");
                                            returnDialog.setConfirmClickListener(Dialog::dismiss);

                                            if (returnCode == MyInstances.ERROR_VEHICLE_TYPE) {
                                                returnDialog.setContentText("Xe bạn chọn và dịch vụ bạn chọn không tương đồng!");
                                                returnDialog.show();
                                                Log.e(TAG, "!isSucces: check vehicle va service name khong khop");
                                            }

                                            if (returnCode == MyInstances.ERROR_VANDEKHAC) {
                                                returnDialog.setContentText("Vui lòng nhập chi tiết vấn đề!");
                                                edtVanDeKhac.requestFocus();
                                                returnDialog.show();
                                            }

                                            if (returnCode == MyInstances.ERROR_DOXANG_PRICE) {
                                                returnDialog.setContentText("Vui lòng nhập giá tiền đổ xăng!");
                                                edtDoXang.requestFocus();
                                                returnDialog.show();
                                            }
                                        } else {
                                            errorDialog.show();
                                            Log.e(TAG, "shopServiceId != -1");
                                        }
                                    });
                                    sweetAlertDialog.setCancelButton("Hủy", SweetAlertDialog::dismissWithAnimation);
                                    sweetAlertDialog.show();

                                } else { // else khi chua chon service name
                                    errorDialog.show();
                                    txtConfirmProblemBadge.setVisibility(View.VISIBLE);
                                    Log.e(TAG, "selectedService == -1: chua chon dich vu");
                                }
                            } else {
                                SweetAlertDialog err = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
                                err.setTitleText("Thông báo");
                                err.setConfirmText("Đóng");
                                err.setContentText("Bạn đã hủy yêu cầu quá " +
                                        CurrentUser.getInstance().getNumOfCancel() +
                                        " lần, xin vui lòng thử lại sau 24 giờ.");
                                err.setConfirmClickListener(Dialog::dismiss);
                                err.show();
                            }
                        }); //end btn confirm request
                    }
                }, throwable -> {
                    Log.e(TAG, "getShopServiceByShopId: " + throwable.getMessage());
                });
        //setup toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Xác nhận thông tin");

        //get all vehicle title
        viewModel.getVehicleByUserIdStatusTrue(CurrentUser.getInstance().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listVehicles -> {
                    if (listVehicles != null && listVehicles.size() > 0) {
                        listUserVehicle = listVehicles;
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
                        SweetAlertDialog errorDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
                        errorDialog.setTitleText("Thông báo");
                        errorDialog.setConfirmText("OK");
                        errorDialog.setContentText("Vui lòng cập nhập thông tin xe của bạn!");
                        errorDialog.setConfirmClickListener(sweetAlertDialog -> {
                            sweetAlertDialog.dismiss();
                            Intent intent = new Intent(this, VehicleActivity.class);
                            startActivity(intent);
                            finish();
                        });
                        errorDialog.show();
                        Log.e(TAG, "listVehicles != null && listVehicles.size() > 0  -> false");
                    }
                }, throwable -> {
                    Log.e(TAG, "getVehicleByUserIdStatusTrue: " + throwable.getMessage());
                });

        //setup user data
        edtName.setText(CurrentUser.getInstance().getFullName());
        edtPhone.setText(CurrentUser.getInstance().getPhoneNumber());
        String mPlace = getIntent().getStringExtra("placeName");
        edtConfirmAddress.setText(mPlace);

        btnImg.setOnClickListener(v -> {
            ImagePicker.Companion.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024 * 2)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(200, 200)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //get File object from ImagePicker
            File file = ImagePicker.Companion.getFile(data);
//            for (int i = 0; i < 10; i++) {
//                ImageView image = new ImageView(this);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(btnImg.getWidth(), btnImg.getHeight());
//                int marginValue = 10;
//                params.setMargins(marginValue, marginValue, marginValue, marginValue);
//                image.setImageResource(R.drawable.ic_add_photo_70);
//                image.setLayoutParams(params);
//                image.setAdjustViewBounds(true);
//
//                imageLayout.addView(image);
            imgScroll.setVisibility(View.VISIBLE);
//            }
            if (file != null) {
                // set path toi' hin`h do' thanh` bitmap
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                if (images == null) {
                    Log.e(TAG, "image == null");
                    images = new ArrayList<>();
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part formData = MultipartBody.Part.createFormData("listImg", file.getName(), requestBody);
                    images.add(formData);
                }

                //set image to image view
                btnImg.setImageBitmap(myBitmap);
            }
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

    private int sendReqAndSaveRequestToSharePref() {
        //create code from current time -> seconds
        long secs = (new Date().getTime()) / 1000;
        String code = "" + String.format(Locale.getDefault(), "%013d", Integer.parseInt("" + secs));

        String address = edtConfirmAddress.getText().toString();

        String description = edtConfirmDescription.getText().toString();

        String userLat = CurrentUser.getInstance().getLatitude();
        String userLong = CurrentUser.getInstance().getLongtitude();
        String doXangPrice = edtDoXang.getText().toString();
        int doXangPriceInt = -1;

        if (serviceName.equals("Đổ xăng")) {
            if (doXangPrice.equals("")) {
                return MyInstances.ERROR_DOXANG_PRICE;
            } else {
                try {
                    doXangPriceInt = Integer.parseInt(doXangPrice);
                    description = String.format(Locale.getDefault(), "Tôi cần đổ %d k vnd tiền xăng.\n " + description, doXangPriceInt);
                } catch (Exception e) {
                    Log.e(TAG, "Khong the parse gia tien do xang thanh int: " + e.getMessage());
                    return MyInstances.ERROR_DOXANG_PRICE;
                }
            }
        }

        String problemDetail = edtVanDeKhac.getText().toString();
        if (serviceName.equals("Vấn đề khác")) {
            if (problemDetail.equals("")) {
                return MyInstances.ERROR_VANDEKHAC;
            } else {
                description = "Chi tiết vấn đề: " + problemDetail + "\n " + description;
            }
        }

        int vehicleId = listUserVehicle.get(0).getId();
        if (selectedVehicle != 0) {
            vehicleId = listUserVehicle.get(selectedVehicle).getId();
        }

        if (!serviceName.contains(listUserVehicle.get(selectedVehicle).getType().toLowerCase().trim()))
            if (!serviceName.equals("Đổ xăng"))
                if (!serviceName.equals("Vấn đề khác"))
                    return MyInstances.ERROR_VEHICLE_TYPE;

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

        viewModel.createRequest(request, images)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null) {
                        if (response.isStatus()) {
                            Intent intentReqDetail = new Intent(getApplicationContext(), RequestDetailActivity.class);
                            intentReqDetail.putExtra("reqId", response.getData().getId().intValue());

                            String sharedPreferenceStr = gson.toJson(response.getData());
                            SharedPreferenceHelper.setSharedPreferenceString(getApplicationContext(), MyInstances.KEY_BIKER_REQUEST, sharedPreferenceStr);

                            startActivity(intentReqDetail);
                            finish();
                        } else {
                            SweetAlertDialog notiDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
                            notiDialog.setTitleText("Thông báo");
                            notiDialog.setContentText("Shop này hiện đang bận");
                            notiDialog.setConfirmText("OK");
                            notiDialog.setConfirmClickListener(Dialog::dismiss);
                            notiDialog.show();
                        }
                    }
                }, throwable -> {
                    Log.e(TAG, "createRequest: " + throwable.getMessage());
                });

        return 0;
    }

}
