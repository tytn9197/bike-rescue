package com.example.bikerescueusermobile.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseFragment;
import com.example.bikerescueusermobile.data.model.shop.Shop;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.ui.map.MapActivity;
import com.example.bikerescueusermobile.ui.seach_shop_service.ShopServiceViewModel;
import com.example.bikerescueusermobile.util.MyMethods;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private final List<Shop> shops = new ArrayList<>();

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.mapHome);
            mapFragment.getMapAsync(this);
            viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ShopServiceViewModel.class);
        }
    }

    @SuppressWarnings({"MissingPermission"})
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (getActivity() != null) {
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
                                for (int i = 0; i < listShop.size(); i++) {
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(Double.parseDouble(listShop.get(i).getLatitude()), Double.parseDouble(listShop.get(i).getLongtitude())))
                                            .title(listShop.get(i).getShopName()));
                                    final int j = i;

                                    Point destination = Point.fromLngLat(
                                            Double.parseDouble(listShop.get(i).getLongtitude()),
                                            Double.parseDouble(listShop.get(i).getLatitude()));

                                    final FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

                                    mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
                                        if (location != null) {
                                            //move camera to current location
                                            LatLng hcm = new LatLng(location.getLatitude(), location.getLongitude());
                                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcm, 11));

                                            CurrentUser.getInstance().setLatitude("" + location.getLatitude());
                                            CurrentUser.getInstance().setLongtitude("" + location.getLongitude());

                                            Point origin = Point.fromLngLat(location.getLongitude(), location.getLatitude());

                                            MapboxDirections client = MapboxDirections.builder()
                                                    .origin(origin)
                                                    .destination(destination)
                                                    .overview(DirectionsCriteria.OVERVIEW_FULL)
                                                    .profile(DirectionsCriteria.PROFILE_DRIVING)
                                                    .accessToken(getString(R.string.mapbox_access_token))
                                                    .build();

                                            client.enqueueCall(new Callback<DirectionsResponse>() {
                                                @Override
                                                public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                                                    if (response.body() == null) {
                                                        Log.e(TAG, "getDistance & Duration - response.body() == null: No routes found, make sure you set the right user and access token.");
                                                        return;
                                                    } else if (response.body().routes().size() < 1) {
                                                        Log.e(TAG, "getDistance & Duration - response.body().routes().size() < 1: No routes found");
                                                        return;
                                                    }
                                                    listShop.get(j).setDistanceFromUser(response.body().routes().get(0).distance()/1000);
                                                    listShop.get(j).setDurationToBiker(response.body().routes().get(0).duration()/60);
                                                    shops.add(listShop.get(j));
                                                }

                                                @Override
                                                public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                                                    Log.e(TAG, "getDistance & Duration - enableLocationComponent - onFailure: " + throwable.getMessage());
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        }, throwable -> {
                            Log.e(TAG, "getShopByServiceName: " + throwable.getMessage());
                        });
            }

            googleMap.setOnMarkerClickListener(this);
            googleMap.setOnMapClickListener(this);
            googleMap.setOnMyLocationChangeListener(this);
//            LatLng hcm = new LatLng(10.8229002, 106.7048471); //view from ho chi minh
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcm, 10));
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
        if (getActivity() != null) {
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
        int position = -1;
        for (int i = 0; i < shops.size(); i++) {
            if (Double.parseDouble(shops.get(i).getLatitude()) == marker.getPosition().latitude
                    && Double.parseDouble(shops.get(i).getLongtitude()) == marker.getPosition().longitude) {
                position = i;
                break;
            }
        }
        if (position != -1)
            ((MapActivity) getActivity()).setShopDetailToMapbox(shops.get(position), shops.get(position).getUserNameOnly().getId());
        else
            Log.e(TAG, "position == -1");
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
