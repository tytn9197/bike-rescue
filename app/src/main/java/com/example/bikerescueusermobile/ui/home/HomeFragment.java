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
import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseFragment;
import com.example.bikerescueusermobile.ui.main.MyCurrentLocation;
import com.example.bikerescueusermobile.ui.send_request.SendRequestActivity;
import com.example.bikerescueusermobile.util.MyMethods;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class HomeFragment extends BaseFragment
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMyLocationChangeListener {
    @Override
    protected int layoutRes() {
        return R.layout.biker_home_fragment;
    }

    private boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private LatLng mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    View mapView;
    private Marker mMarker;

    private Location currentLocation;



//    @BindView(R.id.edtFindLocation)
//    EditText edtFindLocation;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapHome);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mLocationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        changeMyLocationButtonPosition();
        updateLocationUI();

        if(mLocationPermissionGranted){
            getDeviceLocation();
        }

        LatLng s1 = new LatLng(10.7798827, 106.6333353);
        LatLng s2 = new LatLng(10.7798827, 106.6333353);
        LatLng s3 = new LatLng(10.7916663, 106.6351864);
        LatLng s4 = new LatLng(10.7801671, 106.6311021);
        LatLng s5 = new LatLng(10.7829047,106.6272502 );
        LatLng s6 = new LatLng(10.7597079,106.6316521 );
        List<LatLng> list = new ArrayList<>();
        list.add(s1);
        list.add(s2);
        list.add(s3);
        list.add(s4);
        list.add(s5);
        list.add(s6);

        for (int i = 0; i <list.size(); i++) {
            googleMap.addMarker(new MarkerOptions().position(list.get(i))
                    .title("Tiem " + i));
        }

//        mMarker = googleMap.addMarker(new MarkerOptions().position(list.get(0))
//                .title("Tiệm sửa xe La Thành").snippet("Địa chỉ 61 Hoàng Thiều Hoa, Hiệp Tân, Tân Phú"));
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMyLocationChangeListener(this);
        if(currentLocation != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));
        }else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(s1));
        }

        if(MyCurrentLocation.getInstance().getCurrentLocation() != null) {
            Log.e("aaaaa", "lat: " + MyCurrentLocation.getInstance().getCurrentLocation().latitude + " ----long: " + MyCurrentLocation.getInstance().getCurrentLocation().longitude);
        }else{
            Log.e("aaaa", "null");
        }
    }

    private void changeMyLocationButtonPosition(){
        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
//            // Get the button view
//            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
//            // and next place it, on bottom right (as Google Maps app)
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
//                    locationButton.getLayoutParams();
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//            layoutParams.setMargins(0, 10, 10, 10);
//            try {
//                locationButton.setBackground(Drawable.createFromXml(getResources(), getResources().getXml(R.xml.circle_button)));
//            }catch (Exception ex){
//                Log.e(TAG, "changeMyLocationButtonPosition - " + ex.getMessage());
//            }
            //change position google map logo
            View googleLogo = mapView.findViewWithTag("GoogleWatermark");
            RelativeLayout.LayoutParams glLayoutParams = (RelativeLayout.LayoutParams)googleLogo.getLayoutParams();
            googleLogo.setLayoutParams(glLayoutParams);
            googleLogo.setVisibility(View.GONE);
        }

    }

    private void initGrantAppPermission(){
        mLocationPermissionGranted = false;
        Dexter.withContext(getActivity())
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted or not
                        if (report.areAllPermissionsGranted()) {
                            mLocationPermissionGranted = true;
                            updateLocationUI();
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
                }).withErrorListener(error -> Log.e("HomeFragment", "" + error ))
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                initGrantAppPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void getDeviceLocation(){
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        try {
            if(mLocationPermissionGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d(HomeFragmentConstants.TAG,"onComplete: found location!");
                        currentLocation = (Location) task.getResult();
                        MyCurrentLocation.getInstance().setCurrentLocation(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()));
                        MyMethods.moveCamera(mMap, new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),
                                HomeFragmentConstants.DEFAULT_ZOOM_VALUE);
                    }else{
                        Log.d(HomeFragmentConstants.TAG,"onComplete: currents locaiton is null!");
                        Toast.makeText(getActivity(), "Không thể lấy vị trí này", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }catch (SecurityException e){
            Log.e(HomeFragmentConstants.TAG,"getDeviceLocation: SecurityException: "+e.getMessage());
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
//        btnSendRequest.setVisibility(View.VISIBLE);
//        homeShopDetail.setVisibility(View.VISIBLE);
//        homeShopDetailBackground.setVisibility(View.VISIBLE);

        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
//        btnSendRequest.setVisibility(View.GONE);
//        homeShopDetail.setVisibility(View.GONE);
//        homeShopDetailBackground.setVisibility(View.GONE);

    }


    @Override
    public void onMyLocationChange(Location location) {
        if(location != null){
            MyCurrentLocation.getInstance().setCurrentLocation(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()));
        }
    }


//    private void searchLocation(){
//        Log.d(HomeFragmentConstants.TAG,"searchLocation start");
//
//        String searchString = edtFindLocation.getText().toString();
//
//        Geocoder geocoder = new Geocoder(getActivity());
//        List<Address> list = new ArrayList<>();
//        try {
//            list = geocoder.getFromLocationName(searchString,1);
//        }catch (IOException e){
//            Log.e(HomeFragmentConstants.TAG,"searchLocation: get list "+e.getMessage());
//        }
//
//        if(list.size() > 0){
//            Address address = list.get(0);
//            MyMethods.moveCamera(mMap,new LatLng(address.getLatitude(),address.getLongitude()),
//                    HomeFragmentConstants.DEFAULT_ZOOM_VALUE,
//                    address.getAddressLine(0));
//            Log.d(HomeFragmentConstants.TAG,"searchLocation end");
//        }
//    }
//
//    private void hideSoftKeyboard(){
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//    }

}
