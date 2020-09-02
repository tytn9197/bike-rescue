package com.example.bikerescueusermobile.ui.complain;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.data.model.complain.Complain;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.ui.confirm.ConfirmViewModel;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ComplainActivity extends BaseActivity {
    @Override
    protected int layoutRes() {
        return R.layout.activity_complain;
    }

    private static final String TAG = "ComplainActivity";

    @BindView(R.id.complainToolbar)
    Toolbar complainToolbar;

    @BindView(R.id.txtComplainDetail)
    EditText txtComplainDetail;

    @BindView(R.id.imgComplain)
    ImageButton imgComplain;

    @BindView(R.id.btnSendComplain)
    TextView btnSendComplain;

    @Inject
    ViewModelFactory viewModelFactory;
    private ConfirmViewModel viewModel;
    private List<MultipartBody.Part> images = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(complainToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int reqId = getIntent().getIntExtra("reqId", -1);

        SweetAlertDialog errorDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        errorDialog.setTitleText("Thông báo");
        errorDialog.setConfirmText("OK");

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ConfirmViewModel.class);
        btnSendComplain.setOnClickListener(v -> {
            Complain complain = new Complain();
            complain.setCreatedId(CurrentUser.getInstance().getId());
            if (reqId > 0) {
                complain.setRequestId(reqId);
                String strComplainDetail = txtComplainDetail.getText().toString();
                if (strComplainDetail.equals("")) {
                    errorDialog.setContentText("Vui lòng nhập chi tiết khiếu nại!");
                    errorDialog.setConfirmClickListener(Dialog::dismiss);
                    errorDialog.show();
                } else {
                    complain.setComplainReason(strComplainDetail);
                    viewModel.sendComplain(complain, images)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(isSuccess -> {
                                if (isSuccess) {
                                    SweetAlertDialog success = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
                                    success.setTitleText("Thông báo");
                                    success.setConfirmText("OK");
                                    success.setContentText("Gửi thành công! Xin vui lòng đợi trong vòng 24h làm việc!");
                                    success.setConfirmClickListener(sweetAlertDialog -> {
                                        sweetAlertDialog.dismiss();
                                        finish();
                                    });
                                    success.show();
                                }
                            }, throwable -> {
                                Log.e(TAG, "sendComplain: " + throwable.getMessage());
                            });
                }
            } else {
                errorDialog.setContentText("Vui lòng thử lại sau!");
                errorDialog.setConfirmClickListener(Dialog::dismiss);
                errorDialog.show();
            }
        });

        imgComplain.setOnClickListener(v -> {
            ImagePicker.Companion.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024 * 2)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(200, 200)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //get File object from ImagePicker
            File file = ImagePicker.Companion.getFile(data);
            if (file != null) {
                // set path toi' hin`h do' thanh` bitmap
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                if (images == null) {
                    images = new ArrayList<>();
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part formData = MultipartBody.Part.createFormData("listImg", file.getName(), requestBody);
                    images.add(formData);
                }

                //set image to image view
                imgComplain.setImageBitmap(myBitmap);
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Log.e(TAG, "ImagePicker - Get image fail: " + ImagePicker.Companion.getError(data));
        } else {
            Log.e(TAG, "ImagePicker - Task Cancelled");
        }
    }
}
