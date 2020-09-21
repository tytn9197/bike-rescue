package com.example.bikerescueusermobile.ui.shop_owner;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.data.model.user.User;
import com.example.bikerescueusermobile.ui.confirm.ConfirmInfoActivity;
import com.example.bikerescueusermobile.ui.create_request.RequestDetailActivity;
import com.example.bikerescueusermobile.ui.main.MainActivity;
import com.example.bikerescueusermobile.ui.shopMain.ShopMainActivity;
import com.example.bikerescueusermobile.ui.shop_owner.shop_profile.ShopProfileFragment;
import com.example.bikerescueusermobile.ui.update_info.UpdateViewModel;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ShopUpdateInfoActivity extends BaseActivity {

    @Override
    protected int layoutRes() {
        return R.layout.activity_shop_update_info;
    }

    @BindView(R.id.shopUpdateToolBar)
    Toolbar shopUpdateToolBar;

    @BindView(R.id.tvRegister)
    TextView tvRegister;

    @BindView(R.id.shopAva)
    TextView shopAva;

    @BindView(R.id.boss_name)
    EditText fullName;

    @BindView(R.id.phone_number)
    EditText phoneNumber;

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.city)
    EditText city;

    @BindView(R.id.district)
    EditText district;

    @BindView(R.id.ward)
    EditText ward;

    @BindView(R.id.street)
    EditText street;

    @BindView(R.id.updateProfileAva)
    ImageView updateProfileAva;


    @Inject
    ViewModelFactory viewModelFactory;

    private ShopUpdateViewModel viewModel;

    String TAG = "ShopUpdate";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(shopUpdateToolBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cập nhật thông tin");


        fullName.setText(CurrentUser.getInstance().getFullName());
        phoneNumber.setText(CurrentUser.getInstance().getPhoneNumber());
        disableEditText(phoneNumber);
        email.setText(CurrentUser.getInstance().getEmail());

        if(CurrentUser.getInstance().getAvatarUrl().contains("imgur")){
            Picasso.with(this)
                    .load(CurrentUser.getInstance().getAvatarUrl()).placeholder(R.drawable.ic_load)
                    .into(updateProfileAva);
        }

        String address = CurrentUser.getInstance().getAddress();
//        String[] words = address.split(",",4);
//        street.setText(words[0]);
//        ward.setText(words[1]);
//        district.setText(words[2]);
//        city.setText(words[3]);


        //setup viewmodel
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopUpdateViewModel.class);

        //ava
        shopAva.setOnClickListener(v -> {
            ImagePicker.Companion.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });


        tvRegister.setOnClickListener(v -> {
            //init new Instance
            User userInfo = new User();
            userInfo.setPhoneNumber(phoneNumber.getText().toString());
            userInfo.setFullName(fullName.getText().toString());
            userInfo.setEmail(email.getText().toString());
//            userInfo.setAddress(street.getText().toString() + "," + ward.getText().toString() + "," + district.getText().toString() + "," + city.getText().toString());
            userInfo.setAddress(address);
            //update Shop Owner Information
            Log.e(TAG, "ID: " + CurrentUser.getInstance().getId());
            Log.e(TAG, "Address: " + userInfo.getAddress());

            viewModel.updateInfo(CurrentUser.getInstance().getId(), userInfo)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(user -> {
                        Log.e(TAG, "User: " + user);
                        if (user != null) {
                            CurrentUser.getInstance().setFullName(user.getFullName());
                            CurrentUser.getInstance().setAddress(user.getAddress());
                            CurrentUser.getInstance().setEmail(user.getEmail());
                            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ShopUpdateInfoActivity.this, SweetAlertDialog.NORMAL_TYPE);
                            sweetAlertDialog.setTitleText("Thông báo");
                            sweetAlertDialog.setContentText("Bạn đã cập nhật thông tin thành công");
                            sweetAlertDialog.setConfirmText("OK");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    finish();
                                }
                            });
                            sweetAlertDialog.show();
                        }
                    }, throwable -> {
                        Log.e(TAG, "updateInfo: " + throwable.getMessage());
                    });
        });

    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
