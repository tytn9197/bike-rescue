package com.example.bikerescueusermobile.ui.shop_owner.shop_profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseFragment;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.data.model.user.UserStatusDTO;
import com.example.bikerescueusermobile.ui.login.LoginActivity;
import com.example.bikerescueusermobile.ui.main.MainActivity;
import com.example.bikerescueusermobile.ui.shopMain.ShopMainActivity;
import com.example.bikerescueusermobile.ui.shop_owner.ShopUpdateInfoActivity;
import com.example.bikerescueusermobile.ui.shop_owner.ShopUpdateViewModel;
import com.example.bikerescueusermobile.ui.shop_owner.services.ManageServicesActivity;
import com.example.bikerescueusermobile.util.MyInstances;
import com.example.bikerescueusermobile.util.SharedPreferenceHelper;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;
import com.willy.ratingbar.ScaleRatingBar;

import javax.inject.Inject;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ShopProfileFragment extends BaseFragment {

    @Override
    protected int layoutRes() {
        return R.layout.shop_profile_fragment;
    }

    @BindView(R.id.manageProfile)
    TextView manageProfile;

//    @BindView(R.id.shopRating)
//    RatingBar shopRating;

    @BindView(R.id.shopLogout)
    TextView tvShopLogout;

    @BindView(R.id.booking)
    TextView booking;

    @BindView(R.id.manageService)
    TextView manageService;

    @BindView(R.id.shopProfileRating)
    ScaleRatingBar shopProfileRating;

    @BindView(R.id.full_name)
    TextView txtFullName;

    @BindView(R.id.profile_img)
    ImageView imgAvatar;

    @BindView(R.id.phone_number)
    TextView txtPhoneNumber;

    @BindView(R.id.shopOwnerStatus)
    Switch shopOwnerStatus;

    @BindView(R.id.shopProfileName)
    TextView shopProfileName;

    @BindView(R.id.shopProfileAddress)
    TextView txtShopAdd;

    @BindView(R.id.shopProfileOpenTime)
    TextView txtOpenTime;

    @Inject
    ViewModelFactory viewModelFactory;

    private ShopUpdateViewModel viewModel;

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtFullName.setText(CurrentUser.getInstance().getFullName());
        txtPhoneNumber.setText("Số điện thoại: " + CurrentUser.getInstance().getPhoneNumber());

        if(CurrentUser.getInstance().getAvatarUrl().contains("imgur")){
            Picasso.with(getActivity())
                    .load(CurrentUser.getInstance().getAvatarUrl()).placeholder(R.drawable.ic_load)
                    .into(imgAvatar);
        }

        shopProfileName.setText(CurrentUser.getInstance().getShop().getShopName());

        txtShopAdd.setText("Địa chỉ: " + CurrentUser.getInstance().getShop().getAddress());

        txtOpenTime.setText("Mở cửa từ " + CurrentUser.getInstance().getShop().getOpenTime() + "h tới " + CurrentUser.getInstance().getShop().getCloseTime() + "h");

        shopProfileRating.setStepSize((float) 0.5);
        float numOfStar = Float.parseFloat(CurrentUser.getInstance().getShop().getShopRatingStar());
        if(numOfStar < 0){
            shopProfileRating.setVisibility(View.GONE);
        }else{
            shopProfileRating.setVisibility(View.VISIBLE);
            shopProfileRating.setRating(numOfStar);
        }

        manageProfile.setOnClickListener(v -> {
            LayoutInflater factory = LayoutInflater.from(getActivity());
            final View passView = factory.inflate(R.layout.dialog_confirm_password, null);
            AlertDialog passDialog = new AlertDialog.Builder(getActivity()).create();

            EditText edtConfirmPass = passView.findViewById(R.id.edtConfirmPass);

            passDialog.setView(passView);
            passDialog.setCanceledOnTouchOutside(false);
            passDialog.setCancelable(false);

            passView.findViewById(R.id.btn_return).setOnClickListener(v1 -> passDialog.dismiss());

            passView.findViewById(R.id.btn_confirm).setOnClickListener(confirmView -> {
                passDialog.dismiss();
                Log.e("CONFIRMPASS", "edt pass: " + edtConfirmPass.getText().toString() + " -- instance: " + CurrentUser.getInstance().getPasswordLogin());
                if (edtConfirmPass.getText().toString().equals(CurrentUser.getInstance().getPasswordLogin())) {
                    Intent intent = new Intent(getContext(), ShopUpdateInfoActivity.class);
                    startActivity(intent);
                } else {
                    SweetAlertDialog cancelReq = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                    cancelReq.setTitleText("Thông báo");
                    cancelReq.setConfirmText("Đóng");
                    cancelReq.setContentText("Mật khẩu không đúng!");
                    cancelReq.setConfirmClickListener(sweetAlertDialog -> {
                        sweetAlertDialog.dismiss();
                        passDialog.show();
                    });
                    cancelReq.show();
                }
            });
            passDialog.show();
        });
        tvShopLogout.setOnClickListener(v -> {
            SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_LOGGED_IN, "");
            Intent intentLog = new Intent(getActivity(), LoginActivity.class);
            startActivity(intentLog);
            getActivity().finish();
        });

        booking.setOnClickListener(v -> {
            SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_SHOP_REQUEST, "");
        });

        manageService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ManageServicesActivity.class);
                startActivity(intent);
            }
        });

        //setup viewmodel
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopUpdateViewModel.class);

//        viewModel.getSuccessReq(CurrentUser.getInstance().getId())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(listReq -> {
//                    if(listReq != null && listReq.size() > 0){
//                        booking.setText("" + listReq.size());
//                    }else{
//                        booking.setText("0");
//                    }
//                });

        shopOwnerStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            shopOwnerStatus.setEnabled(false);

            int status = MyInstances.USER_STATUS_FREE;
            if(!isChecked){
                status = MyInstances.USER_STATUS_OFFLINE;
            }

            UserStatusDTO userStatusDTO = new UserStatusDTO();
            userStatusDTO.setId(CurrentUser.getInstance().getId());
            userStatusDTO.setStatus(status);

            viewModel.updateUserStatus(userStatusDTO)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(isSuccess -> {
                        if(isSuccess){
                            Log.e("updateUserStatus", "OK");
                            shopOwnerStatus.setEnabled(true);
                        }
                    }, throwable -> {
                        Log.e("ShopProfile", "updateUserStatus: " + throwable.getMessage());
                    });
        });
    }

}
