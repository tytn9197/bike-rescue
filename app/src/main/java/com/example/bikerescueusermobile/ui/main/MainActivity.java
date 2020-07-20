package com.example.bikerescueusermobile.ui.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.ui.favorite.FavoriteShopFragment;
import com.example.bikerescueusermobile.ui.history.HistoryFragment;
import com.example.bikerescueusermobile.ui.home.HomeFragment;
import com.example.bikerescueusermobile.ui.login.LoginActivity;
import com.example.bikerescueusermobile.ui.profile.ProfileFragment;
import com.example.bikerescueusermobile.ui.seach_shop_service.SearchShopServiceFragment;
import com.example.bikerescueusermobile.util.MyInstances;
import com.example.bikerescueusermobile.util.MyMethods;
import com.example.bikerescueusermobile.util.SharedPreferenceHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initGrantAppPermission();
        getCurrentLocation();
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

    @SuppressWarnings({"MissingPermission"})
    private void getCurrentLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        CurrentUser.getInstance().setLatitude(""+location.getLatitude());
                        CurrentUser.getInstance().setLongtitude(""+location.getLongitude());
                    }
                });
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
}
