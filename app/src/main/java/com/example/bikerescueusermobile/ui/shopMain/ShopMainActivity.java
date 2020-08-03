package com.example.bikerescueusermobile.ui.shopMain;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.data.model.request.MessageRequestFB;
import com.example.bikerescueusermobile.data.model.request.Request;
import com.example.bikerescueusermobile.ui.shop_owner.shop_history.ShopHistoryFragment;
import com.example.bikerescueusermobile.ui.shop_owner.shop_home.ShopHomeFragment;
import com.example.bikerescueusermobile.ui.shop_owner.shop_profile.ShopProfileFragment;
import com.example.bikerescueusermobile.util.MyInstances;
import com.example.bikerescueusermobile.util.SharedPreferenceHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import butterknife.BindView;

public class ShopMainActivity extends BaseActivity {

    @Override
    protected int layoutRes() {
        return R.layout.activity_main_shop;
    }

    @BindView(R.id.bottomNav)
    BottomNavigationView bottomNavigationView;

    private Fragment fragment; // use it to change fragment
    private Fragment shopHomeFragment;
    private Fragment shopHistoryFragment;
    private Fragment shopProfileFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("BikeRescueShop"));

        init();
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(context != null) {
                String message = intent.getStringExtra("message");
                Gson gson = new Gson();

                MessageRequestFB responeReq = gson.fromJson(message, MessageRequestFB.class);

                if (responeReq.getMessage().equals(MyInstances.NOTI_CREATED)) {
                    String sharedPreferenceStr = gson.toJson(new Request(responeReq.getReqId()));
                    SharedPreferenceHelper.setSharedPreferenceString(context, MyInstances.KEY_SHOP_REQUEST, sharedPreferenceStr);
                }

                if (responeReq.getMessage().equals(MyInstances.NOTI_CANELED)) {
                    SharedPreferenceHelper.setSharedPreferenceString(context, MyInstances.KEY_SHOP_REQUEST, "");
                }
            }
        }
    };

    private void init() {

        //Shop Owner
        shopHomeFragment = new ShopHomeFragment();
        shopHistoryFragment = new ShopHistoryFragment();
        shopProfileFragment = new ShopProfileFragment();

        //init a home page when login
        fragment = new ShopHomeFragment();
        replaceFragment();
    }

    // replace fragment when select new navigation
    private void replaceFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    fragment = new ShopHomeFragment();
                    replaceFragment();
                    return true;
                case R.id.nav_history:
                    fragment = shopHistoryFragment;
                    replaceFragment();
                    return true;
                case R.id.nav_profile:
                    fragment = shopProfileFragment;
                    replaceFragment();
                    return true;
            }
            return false;
        }
    };

}
