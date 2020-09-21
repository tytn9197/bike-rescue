package com.example.bikerescueusermobile.ui.shopMain;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.data.model.request.MessageRequestFB;
import com.example.bikerescueusermobile.data.model.request.Request;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.data.model.user.UserStatusDTO;
import com.example.bikerescueusermobile.ui.login.LoginActivity;
import com.example.bikerescueusermobile.ui.main.MainActivity;
import com.example.bikerescueusermobile.ui.profile.ProfileFragment;
import com.example.bikerescueusermobile.ui.seach_shop_service.SearchShopServiceFragment;
import com.example.bikerescueusermobile.ui.shop_owner.ShopUpdateViewModel;
import com.example.bikerescueusermobile.ui.shop_owner.services.ManageServicesActivity;
import com.example.bikerescueusermobile.ui.shop_owner.shop_chart.ShopChartFragment;
import com.example.bikerescueusermobile.ui.shop_owner.shop_history.ShopHistoryFragment;
import com.example.bikerescueusermobile.ui.shop_owner.shop_home.ShopHomeFragment;
import com.example.bikerescueusermobile.ui.shop_owner.shop_profile.ShopProfileFragment;
import com.example.bikerescueusermobile.util.MyInstances;
import com.example.bikerescueusermobile.util.SharedPreferenceHelper;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ShopMainActivity extends BaseActivity {

    @Override
    protected int layoutRes() {
        return R.layout.activity_main_shop;
    }

    @BindView(R.id.drawer)
    DrawerLayout drawer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nav_view)
    NavigationView navigation;

    @BindView(R.id.bottomNav)
    BottomNavigationView bottomNavigationView;

    @Inject
    ViewModelFactory viewModelFactory;

    private Fragment fragment; // use it to change fragment
    private boolean isHomeFragmentRunning = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("BikeRescueShop"));

        init();
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // set header image & name
        View header = navigation.getHeaderView(0);

        CircleImageView head_avatar = header.findViewById(R.id.head_avatar);
        TextView txtHeaderName = header.findViewById(R.id.txtName);
        TextView txtPhone = header.findViewById(R.id.txtPhoneHead);
        Switch shopOwnerStatus = header.findViewById(R.id.shopOwnerStatus);

        txtHeaderName.setText(CurrentUser.getInstance().getFullName());

        txtPhone.setText(CurrentUser.getInstance().getPhoneNumber());

        if (CurrentUser.getInstance().getAvatarUrl().contains("imgur")) {
            Picasso.with(this).load(CurrentUser.getInstance().getAvatarUrl()).placeholder(R.drawable.ic_load).into(head_avatar);
        }

        shopOwnerStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            shopOwnerStatus.setEnabled(false);

            int status = MyInstances.USER_STATUS_FREE;
            if (!isChecked) {
                status = MyInstances.USER_STATUS_OFFLINE;
            }

            UserStatusDTO userStatusDTO = new UserStatusDTO();
            userStatusDTO.setId(CurrentUser.getInstance().getId());
            userStatusDTO.setStatus(status);

            ViewModelProviders.of(this, viewModelFactory).get(ShopUpdateViewModel.class).updateUserStatus(userStatusDTO)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(isSuccess -> {
                        if (isSuccess) {
                            Log.e("updateUserStatus", "OK");
                            shopOwnerStatus.setEnabled(true);
                        }
                    }, throwable -> {
                        Log.e("ShopMainActivity", "updateUserStatus: " + throwable.getMessage());
                    });
        });
    }

    public NavigationView.OnNavigationItemSelectedListener mOnSideNavigationItemSelectedListener =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    isHomeFragmentRunning = false;
                    switch (item.getItemId()) {
                        case R.id.nav_feed:
                            fragment = new ShopChartFragment();
                            replaceFragment();
                            drawer.close();
                            return true;
                        case R.id.nav_editPro:
                            fragment = new ShopProfileFragment();
                            replaceFragment();
                            drawer.close();
                            return true;
                        case R.id.nav_shop_service:
                            LayoutInflater factory = LayoutInflater.from(ShopMainActivity.this);
                            final View passView = factory.inflate(R.layout.dialog_confirm_password, null);
                            AlertDialog passDialog = new AlertDialog.Builder(ShopMainActivity.this).create();

                            EditText edtConfirmPass = passView.findViewById(R.id.edtConfirmPass);

                            passDialog.setView(passView);
                            passDialog.setCanceledOnTouchOutside(false);
                            passDialog.setCancelable(false);

                            passView.findViewById(R.id.btn_return).setOnClickListener(v1 -> passDialog.dismiss());

                            passView.findViewById(R.id.btn_confirm).setOnClickListener(confirmView -> {
                                passDialog.dismiss();
                                Log.e("CONFIRMPASS", "edt pass: " + edtConfirmPass.getText().toString() + " -- instance: " + CurrentUser.getInstance().getPasswordLogin());
                                if (edtConfirmPass.getText().toString().equals(CurrentUser.getInstance().getPasswordLogin())) {
                                    Intent intent = new Intent(ShopMainActivity.this, ManageServicesActivity.class);
                                    startActivity(intent);
                                } else {
                                    SweetAlertDialog cancelReq = new SweetAlertDialog(ShopMainActivity.this, SweetAlertDialog.ERROR_TYPE);
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
                            drawer.close();
                            return true;
                        case R.id.nav_logout:
                            SharedPreferenceHelper.setSharedPreferenceString(ShopMainActivity.this, MyInstances.KEY_LOGGED_IN, "");
                            Intent intentLog = new Intent(ShopMainActivity.this, LoginActivity.class);
                            startActivity(intentLog);
                            finish();
                            return true;
                    }
                    return false;
                }
            };

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (context != null && getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                String message = intent.getStringExtra("message");
                Gson gson = new Gson();

                MessageRequestFB responeReq = gson.fromJson(message, MessageRequestFB.class);
                if (!isHomeFragmentRunning) {
                    if (responeReq.getMessage().equals(MyInstances.NOTI_CREATED)) {
                        String sharedPreferenceStr = gson.toJson(new Request(responeReq.getReqId()));
                        SharedPreferenceHelper.setSharedPreferenceString(ShopMainActivity.this, MyInstances.KEY_SHOP_REQUEST, sharedPreferenceStr);
                        SweetAlertDialog noti = new SweetAlertDialog(ShopMainActivity.this, SweetAlertDialog.NORMAL_TYPE);
                        noti.setTitleText("Thông báo");
                        noti.setConfirmText("Đóng");
                        noti.setContentText("Bạn có yêu cầu mới!");
                        noti.setConfirmText("Xem thông báo");
                        noti.setConfirmClickListener(dialog -> {
                            dialog.dismiss();
                            SharedPreferenceHelper.setSharedPreferenceString
                                    (ShopMainActivity.this, MyInstances.KEY_COUNT_DOWN_TIME, "" + System.currentTimeMillis());
                            fragment = new ShopHomeFragment();
                            isHomeFragmentRunning = true;
                            replaceFragment();
                        });
                        noti.show();
                    }

                    if (responeReq.getMessage().equals(MyInstances.NOTI_CANELED)) {
                        SharedPreferenceHelper.setSharedPreferenceString(ShopMainActivity.this, MyInstances.KEY_SHOP_REQUEST, "");
                        SharedPreferenceHelper.setSharedPreferenceString(ShopMainActivity.this, MyInstances.KEY_COUNT_DOWN_TIME, "");
                        SweetAlertDialog cancelReq = new SweetAlertDialog(ShopMainActivity.this, SweetAlertDialog.NORMAL_TYPE);
                        cancelReq.setTitleText("Thông báo");
                        cancelReq.setConfirmText("Đóng");
                        cancelReq.setContentText("Khách đã hủy yêu cầu." + "\nLý do hủy: " + responeReq.getReason());
                        cancelReq.setConfirmClickListener(Dialog::dismiss);
                        cancelReq.show();
                    }
                }
            }
        }
    };

    private void init() {
        //init a home page when login
        fragment = new ShopChartFragment();
        replaceFragment();

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navigation.setNavigationItemSelectedListener(mOnSideNavigationItemSelectedListener);
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
            isHomeFragmentRunning = false;
            switch (item.getItemId()) {
                case R.id.nav_chart:
                    fragment = new ShopChartFragment();
                    replaceFragment();
                    return true;
                case R.id.nav_home:
                    fragment = new ShopHomeFragment();
                    isHomeFragmentRunning = true;
                    replaceFragment();
                    return true;
                case R.id.nav_history:
                    fragment = new ShopHistoryFragment();
                    replaceFragment();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, new ShopHomeFragment())
                    .commit();
            isHomeFragmentRunning = true;
        }
    }
}
