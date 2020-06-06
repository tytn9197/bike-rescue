package com.example.bikerescueusermobile.ui.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.ui.favorite.FavoriteShopFragment;
import com.example.bikerescueusermobile.ui.history.HistoryFragment;
import com.example.bikerescueusermobile.ui.home.HomeFragment;
import com.example.bikerescueusermobile.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.victor.loading.rotate.RotateLoading;

import java.util.List;

import butterknife.BindView;


public class MainActivity extends BaseActivity {

    @BindView(R.id.drawer)
    DrawerLayout drawer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nav_view)
    NavigationView navigation;

    @BindView(R.id.mainRotateloading)
    RotateLoading rotateLoading;

    private Fragment fragment; // use it to change fragment
    private Fragment homeFragment;
    private Fragment historyFragment;
    private Fragment profileFragment;

    @BindView(R.id.bottomNav)
    BottomNavigationView bottomNavigationView;

    @Override
    protected int layoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initGrantAppPermission();
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnBottomNavigationItemSelectedListener);
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
                            fragment = new HomeFragment();
                            replaceFragment();
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
                }).withErrorListener(error -> Toast.makeText(this.getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show())
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
        homeFragment = new HomeFragment();
        historyFragment = new HistoryFragment();
        profileFragment = new ProfileFragment();
        //init a home page when login
        fragment = homeFragment;
        replaceFragment();
        replaceFragment();

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navigation.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        View header = navigation.getHeaderView(0);
//        fullname = header.findViewById(R.id.txtName);
////        avatar = header.findViewById(R.id.head_avatar);
////        Picasso.with(this).load(CurrentUser.getInstance().getAvatarUrl()).placeholder(R.mipmap.user).into(avatar);
//        avatar = header.findViewById(R.id.head_avatar);
//        Picasso.with(this).load(CurrentUser.getInstance().getAvatarUrl()).placeholder(R.mipmap.user).into(avatar);
//        switchCompat = header.findViewById(R.id.is_online);
//        fullname.setText(CurrentUser.getInstance().getFullname());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnBottomNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    fragment = new HomeFragment();
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
//                    closeNotification();
                    switch (item.getItemId()) {
                        case R.id.nav_feed:
                            fragment = new HomeFragment();
                            replaceFragment();
                            drawer.close();
                            return true;
                        case R.id.nav_editPro:
                            fragment = new ProfileFragment();
                            replaceFragment();
                            drawer.close();
                            return true;
                        case R.id.nav_logout:
//                            SharedPreferenceHelper.setSharedPreferenceString(MainActivity.this, "user", "");
//                            Intent intentLog = new Intent(MainActivity.this, LoginActivity.class);
//                            startActivity(intentLog);
//                            goesOffline();
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
