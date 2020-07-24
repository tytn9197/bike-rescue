package com.example.bikerescueusermobile.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseFragment;
import com.example.bikerescueusermobile.data.model.shop.Shop;
import com.example.bikerescueusermobile.ui.map.MapActivity;
import com.example.bikerescueusermobile.ui.seach_shop_service.ShopServiceViewModel;
import com.example.bikerescueusermobile.ui.seach_shop_service.TopShopRecyclerViewAdapter;
import com.example.bikerescueusermobile.ui.send_request.SendRequestActivity;
import com.example.bikerescueusermobile.util.MyMethods;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.compat.GeoDataClient;
import com.google.android.libraries.places.compat.PlaceDetectionClient;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class HomeFragment extends BaseFragment
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMyLocationChangeListener, PermissionsListener {
    @Override
    protected int layoutRes() {
        return R.layout.biker_home_fragment;
    }

    private static final String TAG = "HomeFragment";

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location currentLocation;
    private PermissionsManager permissionsManager;
    private ShopServiceViewModel viewModel;

    private List<Shop> listShop = new ArrayList<>();

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getActivity() != null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.mapHome);
            mapFragment.getMapAsync(this);
            viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ShopServiceViewModel.class);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(getActivity() != null) {
            mMap = googleMap;
            if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                getDeviceLocation();
            } else {
                permissionsManager = new PermissionsManager(this);
                permissionsManager.requestLocationPermissions(getActivity());
            }
            updateLocationUI();

            // get shop by service name then set shop's marker
            String serviceName = getActivity().getIntent().getStringExtra("serviceName");
            if (!serviceName.equals("")) {
                viewModel.getShopByServiceName(serviceName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(listShop -> {
                            if (listShop != null) {
                                this.listShop.addAll(listShop);
                                for (int i = 0; i < listShop.size(); i++) {
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(Double.parseDouble(listShop.get(i).getLatitude()), Double.parseDouble(listShop.get(i).getLongtitude())))
                                            .title(listShop.get(i).getShopName()));
                                }
                                MyMethods.setDistance(listShop);
                            }
                        }, throwable -> {
                            Log.e("SearchShopService", "getShopByServiceName: " + throwable.getMessage());
                        });
            }

            googleMap.setOnMarkerClickListener(this);
            googleMap.setOnMapClickListener(this);
            googleMap.setOnMyLocationChangeListener(this);
            LatLng hcm = new LatLng(10.8229002, 106.7048471);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcm, 10));
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @SuppressWarnings({"MissingPermission"})
    public void getDeviceLocation() {
        if(getActivity() != null) {
            if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {
                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                try {
                    Task location = mFusedLocationProviderClient.getLastLocation();
                    location.addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            currentLocation = (Location) task.getResult();
                        } else {
                            Toast.makeText(getActivity(), "Không thể lấy vị trí này", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (SecurityException e) {
                    Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
                }
            } else {
                permissionsManager = new PermissionsManager(this);
                permissionsManager.requestLocationPermissions(getActivity());
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for (int i = 0; i < listShop.size(); i++) {
            if (Double.parseDouble(listShop.get(i).getLatitude()) == marker.getPosition().latitude
                    && Double.parseDouble(listShop.get(i).getLongtitude()) == marker.getPosition().longitude) {
                ((MapActivity) getActivity()).setShopDetailToMapbox(listShop.get(i), listShop.get(i).getUserNameOnly().getId());
            }
        }
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMyLocationChange(Location location) {

    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Log.e(TAG, "onExplanationNeeded: " + permissionsToExplain.toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            updateLocationUI();
            getDeviceLocation();
        } else {
            Log.e(TAG, "onPermissionResult: Khong co quyen truy cap");
        }
    }
}
