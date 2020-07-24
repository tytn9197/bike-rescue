package com.example.bikerescueusermobile.ui.shop_owner.shop_profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseFragment;
import com.example.bikerescueusermobile.ui.login.LoginActivity;
import com.example.bikerescueusermobile.ui.main.MainActivity;
import com.example.bikerescueusermobile.ui.shop_owner.ShopUpdateInfoActivity;
import com.example.bikerescueusermobile.util.MyInstances;
import com.example.bikerescueusermobile.util.SharedPreferenceHelper;

import butterknife.BindView;

public class ShopProfileFragment extends BaseFragment {

    @Override
    protected int layoutRes() {
        return R.layout.shop_profile_fragment;
    }

    @BindView(R.id.manageProfile)
    TextView manageProfile;

    @BindView(R.id.shopLogout)
    TextView tvShopLogout;

    @BindView(R.id.booking)
    TextView booking;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        manageProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ShopUpdateInfoActivity.class);
            startActivity(intent);
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
    }

}
