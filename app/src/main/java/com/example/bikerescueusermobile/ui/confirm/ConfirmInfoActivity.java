package com.example.bikerescueusermobile.ui.confirm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.ui.create_request.CreateRequestActivity;
import com.example.bikerescueusermobile.ui.main.MainActivity;
import com.example.bikerescueusermobile.ui.map.MapActivity;
import com.example.bikerescueusermobile.ui.register.CreatePasswordActivity;
import com.example.bikerescueusermobile.ui.seach_shop_service.ShopServiceViewModel;
import com.example.bikerescueusermobile.ui.send_request.SendRequestActivity;
import com.example.bikerescueusermobile.ui.update_info.UpdateInfoActivity;
import com.example.bikerescueusermobile.util.MyMethods;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ConfirmInfoActivity extends BaseActivity {

//    @BindView(R.id.spinner1)
//    Spinner problem;
//
//    @BindView(R.id.spinner2)
//    Spinner bike;
//
//    @BindView(R.id.spinner3)
//    Spinner year;

    private static final String TAG = "ConfirmInfoActivity";

    @BindView(R.id.confirm_toolbar)
    Toolbar toolbar;

    @BindView(R.id.tvProblem)
    TextView tvProblem;

    @BindView(R.id.tvBrand)
    TextView tvBrand;

    @BindView(R.id.tvYear)
    TextView tvYear;

    @BindView(R.id.btnBookService)
    TextView tvBookService;

    @BindView(R.id.btnImg)
    ImageButton btnImg;

    @BindView(R.id.edtName)
    EditText edtName;

    @BindView(R.id.edtPhone)
    EditText edtPhone;

    @BindView(R.id.edtMarker)
    EditText edtMarker;

    @BindView(R.id.tvVehicleType)
    TextView tvVehicleType;

    @Inject
    ViewModelFactory viewModelFactory;

    private ConfirmViewModel viewModel;

    @Override
    protected int layoutRes() {
        return R.layout.activity_confirm_info;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setup toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Xác nhận thông tin");

        //setup viewmodel
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ConfirmViewModel.class);

        //get all vehicle title
        viewModel.getAllConfig()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listConfig -> {
                    if(listConfig != null){
                        ArrayAdapter<String> brands = new ArrayAdapter<>(this,
                                android.R.layout.simple_list_item_1);
                        ArrayAdapter<String> years = new ArrayAdapter<>(this,
                                android.R.layout.simple_list_item_1);
                        ArrayAdapter<String> types = new ArrayAdapter<>(this,
                                android.R.layout.simple_list_item_1);

                        for (int i = 0; i < listConfig.size(); i++){
                            if (listConfig.get(i).getName().equals("brand name")){
                                brands.add(listConfig.get(i).getValue());
                            }
                            if(listConfig.get(i).getName().equals("vehicle year")){
                                years.add(listConfig.get(i).getValue());
                            }
                            if(listConfig.get(i).getName().equals("vehicle type")){
                                types.add(listConfig.get(i).getValue());
                            }
                        }
                        tvBrand.setOnClickListener(v -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmInfoActivity.this);
                            builder.setTitle("Hãng xe");
                            builder.setAdapter(brands, (dialog, which) -> {
                                tvBrand.setText(brands.getItem(which));
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        });

                        tvYear.setOnClickListener(v -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmInfoActivity.this);
                            builder.setTitle("Đời xe");
                            builder.setAdapter(years, (dialog, which) -> {
                                tvYear.setText(years.getItem(which));
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        });

                        tvVehicleType.setOnClickListener(v -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmInfoActivity.this);
                            builder.setTitle("Loại xe");
                            builder.setAdapter(types, (dialog, which) -> {
                                tvVehicleType.setText(types.getItem(which));
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        });
                    }
                });


        //test data
        edtName.setText(CurrentUser.getInstance().getFullName());
        edtPhone.setText(CurrentUser.getInstance().getPhoneNumber());
        String mPlace = getIntent().getStringExtra("placeName");
        edtMarker.setText(mPlace);

        String serviceName = getIntent().getStringExtra("serviceName");
        tvProblem.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmInfoActivity.this);
            builder.setTitle("Vấn đề của bạn");
            String[] problems = getBaseContext().getResources().getStringArray(R.array.vande);
            builder.setItems(problems, (dialog, which) -> {
                tvProblem.setText(problems[which]);
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
        if (serviceName.equals("")) {
            tvProblem.setText("Vui lòng chọn vấn đề của bạn");
        } else {
            String s = "Tôi cần " + serviceName.toLowerCase().trim() + ".";
            tvProblem.setText(s);
        }

        tvBookService.setOnClickListener(v -> {

            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ConfirmInfoActivity.this, SweetAlertDialog.NORMAL_TYPE);
            sweetAlertDialog.setTitleText("Thông báo");
            sweetAlertDialog.setContentText("Quý khách xác nhận thực hiện gọi dịch vụ lúc này?");
            sweetAlertDialog.setConfirmText("Xác nhận");
            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    Intent intent = new Intent(getApplicationContext(), CreateRequestActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            sweetAlertDialog.setCancelButton("Quay lại", sDialog -> {
                sDialog.dismissWithAnimation();
            });
            sweetAlertDialog.show();

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
}
