package com.example.bikerescueusermobile.ui.shopMain;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.ui.shop_owner.shop_history.ShopHistoryFragment;
import com.example.bikerescueusermobile.ui.shop_owner.shop_home.ShopHomeFragment;
import com.example.bikerescueusermobile.ui.shop_owner.shop_profile.ShopProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;

public class ShopMainActivity extends BaseActivity {

    @BindView(R.id.bottomNav)
    BottomNavigationView bottomNavigationView;

    private Fragment fragment; // use it to change fragment
    private Fragment shopHomeFragment;
    private Fragment shopHistoryFragment;
    private Fragment shopProfileFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private void init(){


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
                    fragment = shopHomeFragment;
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




    @Override
    protected int layoutRes() {
        return R.layout.activity_main_shop;
    }
}
