package com.example.bikerescueusermobile.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
import com.example.bikerescueusermobile.data.model.request.ReviewRequestDTO;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.ui.create_request.RequestDetailViewModel;
import com.example.bikerescueusermobile.ui.favorite.FavoriteShopFragment;
import com.example.bikerescueusermobile.ui.history.HistoryFragment;
import com.example.bikerescueusermobile.ui.home.HomeFragment;
import com.example.bikerescueusermobile.ui.login.LoginActivity;
import com.example.bikerescueusermobile.ui.profile.ProfileFragment;
import com.example.bikerescueusermobile.ui.seach_shop_service.SearchShopServiceFragment;
import com.example.bikerescueusermobile.util.MyInstances;
import com.example.bikerescueusermobile.util.MyMethods;
import com.example.bikerescueusermobile.util.SharedPreferenceHelper;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends BaseActivity {

    @Override
    protected int layoutRes() {
        return R.layout.activity_main;
    }

    private static final String TAG = "MainActivity";

    @BindView(R.id.drawer)
    DrawerLayout drawer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nav_view)
    NavigationView navigation;

    @BindView(R.id.mainRotateloading)
    RotateLoading rotateLoading;

    private Fragment fragment; // use it to change fragment
    private SearchShopServiceFragment searchShopServiceFragment;

    @BindView(R.id.bottomNav)
    BottomNavigationView bottomNavigationView;

    private CircleImageView head_avatar;
    private TextView txtHeaderName;
    private FusedLocationProviderClient fusedLocationClient;
    private AlertDialog reviewDialog;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initGrantAppPermission();

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("BikeRescueBiker"));

        searchShopServiceFragment = new SearchShopServiceFragment();
        // set header image & name
        View header =navigation.getHeaderView(0);
        head_avatar = header.findViewById(R.id.head_avatar);
        txtHeaderName = header.findViewById(R.id.txtName);
        txtHeaderName.setText(CurrentUser.getInstance().getFullName());
        if(CurrentUser.getInstance().getAvatarUrl().contains("imgur")) {
            Picasso.with(this).load(CurrentUser.getInstance().getAvatarUrl()).placeholder(R.drawable.ic_load).into(head_avatar);
        }

//        UpdateDevice device = new UpdateDevice(token);
//        viewModel.updateFcm(CurrentUser.getInstance().getToken(), CurrentUser.getInstance().getId(), device);
//        CurrentUser.getInstance().setDeviceToken(token);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("profileTag");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initGrantAppPermission(){
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted or not
                        if (report.areAllPermissionsGranted()) {
                            Log.d(TAG, "onPermissionsChecked: " + "all permissions are granted");
                        }
                        // check for permanent denial of any permission show alert dialog
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // open Settings activity
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(error -> Log.e("Main", "" + error ))
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.message_need_permission));
        builder.setMessage(getString(R.string.message_permission));
        builder.setPositiveButton(getString(R.string.title_go_to_setting), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void init(){
        //init a home page when login
        fragment = new SearchShopServiceFragment();
        replaceFragment();

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navigation.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnBottomNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnBottomNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    fragment = searchShopServiceFragment;
                    replaceFragment();
                    return true;
                case R.id.nav_history:
                    fragment = new HistoryFragment();
                    replaceFragment();
                    return true;
                case R.id.nav_profile:
                    fragment = new FavoriteShopFragment();
                    replaceFragment();
                    return true;
            }
            return false;
        }
    };

    public NavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_feed:
                            fragment = new SearchShopServiceFragment();
                            replaceFragment();
                            drawer.close();
                            return true;
                        case R.id.nav_editPro:
                            fragment = new ProfileFragment();
//                            replaceFragment();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.add(R.id.frame_container,fragment,"profileTag");
                            fragmentTransaction.commit();
                            drawer.close();
                            return true;
                        case R.id.nav_introduce:
//                            fragment = new HomeFragment();
//                            replaceFragment();
                            SharedPreferenceHelper.setSharedPreferenceString(getApplicationContext(), MyInstances.KEY_BIKER_REQUEST, "");
                            drawer.close();
                            return true;
                        case R.id.nav_logout:
                            SharedPreferenceHelper.setSharedPreferenceString(MainActivity.this, MyInstances.KEY_LOGGED_IN, "");
                            Intent intentLog = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intentLog);
                            finish();
                            return true;
                    }
                    return false;
                }
            };

    // replace fragment when select new navigation
    private void replaceFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (context != null && getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                String message = intent.getStringExtra("message");
                Gson gson = new Gson();
                MessageRequestFB responeReq = gson.fromJson(message, MessageRequestFB.class);

                SweetAlertDialog notiDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.NORMAL_TYPE);
                notiDialog.setTitleText("Thông báo");
                notiDialog.setConfirmText("OK");
                notiDialog.setConfirmClickListener(Dialog::dismiss);

                if (responeReq.getMessage().equals(MyInstances.NOTI_ACCEPT)) {
                    notiDialog.setContentText("Cửa hàng đã nhận yêu cầu của bạn");
                    notiDialog.show();
                }

                if (responeReq.getMessage().equals(MyInstances.NOTI_REJECTED)) {
                    notiDialog.setContentText("Cửa hàng đã từ chối yêu cầu của bạn");
                    SharedPreferenceHelper.setSharedPreferenceString(getApplicationContext(), MyInstances.KEY_BIKER_REQUEST, "");
                    notiDialog.show();
                }

                if (responeReq.getMessage().equals(MyInstances.NOTI_FINISH)) {
                    SharedPreferenceHelper.setSharedPreferenceString(getApplicationContext(), MyInstances.KEY_BIKER_REQUEST, "");

                    //review reruest
                    setupReviewView(responeReq.getReqId(), responeReq.getReqCode(), responeReq.getReqPrice());
                    reviewDialog.show();
                }

                if (responeReq.getMessage().equals(MyInstances.NOTI_CANELED)) {
                    notiDialog.setContentText("Cửa hàng đã hủy yêu cầu của bạn");
                    SharedPreferenceHelper.setSharedPreferenceString(getApplicationContext(), MyInstances.KEY_BIKER_REQUEST, "");
                    notiDialog.show();
                }

                if (responeReq.getMessage().equals(MyInstances.NOTI_ARRIVED)) {
                    notiDialog.setContentText("Thợ sửa xe đã đến nơi");
                    notiDialog.show();
                }

            }
        }
    };

    @SuppressLint("DefaultLocale")
    private void setupReviewView(int reqId, String code, double price){
        //set up review dialog
        LayoutInflater factory = LayoutInflater.from(this);
        final View reviewView = factory.inflate(R.layout.dialog_review_request, null);
        reviewDialog = new AlertDialog.Builder(this).create();

        ScaleRatingBar ratingBar = reviewView.findViewById(R.id.reviewRatingBar);
        EditText edtComment = reviewView.findViewById(R.id.edtCommentDetail);
        TextView txtReqCode = reviewView.findViewById(R.id.txtReviewReqCode);
        TextView txtPrice = reviewView.findViewById(R.id.txtReviewPrice);

        txtReqCode.setText(code);
        txtPrice.setText("Giá: " + MyMethods.convertMoney((float)price*1000) + " vnd");

        reviewDialog.setView(reviewView);
        reviewView.findViewById(R.id.btn_confirm).setOnClickListener(confirmView -> {
            reviewDialog.dismiss();

            String comment = edtComment.getText().toString();
            double star = ratingBar.getRating();

            ReviewRequestDTO reviewDTO = new ReviewRequestDTO(comment, star);
            Log.e(TAG, "review: " + reviewDTO.toString());

            ViewModelProviders.of(this, viewModelFactory).get(RequestDetailViewModel.class).reviewRequest(reqId, reviewDTO)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(respone -> {
                        if (respone != null) {
                            SweetAlertDialog notiDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
                            notiDialog.setTitleText("Thông báo");
                            notiDialog.setContentText("Đánh giá thành công");
                            notiDialog.setConfirmText("OK");
                            notiDialog.setConfirmClickListener(Dialog::dismiss);
                            notiDialog.show();
                        }
                    }, throwable -> {
                        Log.e(TAG, "reviewRequest: " + throwable.getMessage());
                    });
        });
        reviewView.findViewById(R.id.btn_return).setOnClickListener(v1 -> reviewDialog.dismiss());
    }
}
