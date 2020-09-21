package com.example.bikerescueusermobile.ui.tracking_map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.data.model.request.CurrentRequest;
import com.example.bikerescueusermobile.data.model.request.MessageRequestFB;
import com.example.bikerescueusermobile.data.model.request.ReviewRequestDTO;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.data.model.user.UserLatLong;
import com.example.bikerescueusermobile.ui.create_request.RequestDetailActivity;
import com.example.bikerescueusermobile.ui.create_request.RequestDetailViewModel;
import com.example.bikerescueusermobile.ui.login.LoginModel;
import com.example.bikerescueusermobile.ui.login.UpdateLocationService;
import com.example.bikerescueusermobile.ui.shop_owner.shop_home.ConfirmPriceRecyclerViewAdapter;
import com.example.bikerescueusermobile.ui.shop_owner.shop_home.ShopHomeFragment;
import com.example.bikerescueusermobile.util.MyInstances;
import com.example.bikerescueusermobile.util.MyMethods;
import com.example.bikerescueusermobile.util.SharedPreferenceHelper;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.willy.ratingbar.ScaleRatingBar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class TrackingMapActivity extends DaggerAppCompatActivity implements
        OnMapReadyCallback {

    protected int layoutRes() {
        return R.layout.activity_tracking_map;
    }

    private static final String TAG = "TrackingMapActivity";
    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";

    MapView mapView;
    private MapboxMap mapboxMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private MapboxDirections client;
    private DirectionsRoute currentRoute;
    private LatLng destination = new LatLng(10.8411847, 106.8092702);
    private GeoJsonSource source;

    @BindView(R.id.btnTrackingMapBack)
    Button btnBack;

    @Inject
    ViewModelFactory viewModelFactory;

    @BindView(R.id.btnTrackingArrived)
    Button btnArrived;

    @BindView(R.id.btnTrackingFinish)
    Button btnFinish;


    private LoginModel viewModel;
    private boolean isBikerTracking = false;
    private DatabaseReference mDatabase;
    private List<UserLatLong> userLatLongList = new ArrayList<>();
    private RequestDetailViewModel reqViewModel;
    private double distance = -1;
    private AlertDialog reviewDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginModel.class);
        reqViewModel = ViewModelProviders.of(this, viewModelFactory).get(RequestDetailViewModel.class);

        try {
            isBikerTracking = getIntent().getBooleanExtra("isBikerTracking", false);

            if (isBikerTracking) {
                LocalBroadcastManager.getInstance(this).registerReceiver(
                        mMessageReceiver, new IntentFilter("BikeRescueBiker"));
            }

            int reqId = getIntent().getIntExtra("reqId", -1);
            String reqStatus = "";
            reqStatus = getIntent().getStringExtra("reqStatus");

            //set destination
            if (reqId == -1) {
                Log.e(TAG, "onCreate: cannot get reqId");
            } else {
                Mapbox.getInstance(TrackingMapActivity.this, getString(R.string.mapbox_access_token));
                setContentView(layoutRes());
                ButterKnife.bind(TrackingMapActivity.this);

                mapView = findViewById(R.id.trackingMap);
                mapView.onCreate(savedInstanceState);
                mapView.getMapAsync(TrackingMapActivity.this);
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(TrackingMapActivity.this);

                if (reqStatus != null)
                    if (reqStatus.equals(MyInstances.STATUS_ARRIVED) || reqStatus.equals(MyInstances.STATUS_CREATED)) {
                        btnArrived.setVisibility(View.GONE);
                    }

                if (isBikerTracking) {
                    btnArrived.setVisibility(View.GONE);
                }

                btnBack.setOnClickListener(v -> {
                    Intent i = new Intent();
                    setResult(Activity.RESULT_OK, i);
                    finish();
                });

                mDatabase = FirebaseDatabase.getInstance().getReference(MyInstances.APP);
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                            try {
                                userLatLongList.clear();
                                for (DataSnapshot listUserLatlng : dataSnapshot.getChildren()) {
                                    Log.e(TAG, "value is biker: " + listUserLatlng.getValue().toString() + "---- getChosenShopOwnerId id:" + CurrentUser.getInstance().getChosenShopOwnerId());
                                    userLatLongList.add(listUserLatlng.getValue(UserLatLong.class));
                                }
                                if (userLatLongList.size() > 0) {
                                    if (isBikerTracking) {
                                        Log.e(TAG, "list size: " + userLatLongList.size());
                                        int pos = -1;
                                        for (int i = 0; i < userLatLongList.size(); i++) {
                                            if (userLatLongList.get(i).getId() == CurrentUser.getInstance().getChosenShopOwnerId())
                                                pos = i;
                                        }
                                        if (pos > -1) {
                                            Log.e(TAG, "pos: " + pos);
                                            UserLatLong newUser = userLatLongList.get(pos);
                                            Log.e(TAG, "onChildChanged: " + newUser.toString());
                                            destination = new LatLng(Double.parseDouble(newUser.getLatitude()), Double.parseDouble(newUser.getLongtitude()));
                                            updateRoute();
                                        }
                                    } else { // for shop owner
                                        Log.e(TAG, "list size: " + userLatLongList.size());
                                        int pos = -1;
                                        for (int i = 0; i < userLatLongList.size(); i++) {
                                            if (userLatLongList.get(i).getId() == CurrentUser.getInstance().getCurrentBikerId())
                                                pos = i;
                                        }
                                        if (pos > -1) {
                                            Log.e(TAG, "pos: " + pos);
                                            UserLatLong newUser = userLatLongList.get(pos);
                                            Log.e(TAG, "onChildChanged: " + newUser.toString());
                                            destination = new LatLng(Double.parseDouble(newUser.getLatitude()), Double.parseDouble(newUser.getLongtitude()));
                                            updateRoute();

                                            //set up btn arrive
                                            btnArrived.setOnClickListener(view -> {
                                                Log.e(TAG, "distance: " + distance + " ---- current: " + CurrentUser.getInstance().getArriveDistance());
                                                if (distance > 0 && distance < CurrentUser.getInstance().getArriveDistance()) {
                                                    reqViewModel.updateStatusRequest(reqId, MyInstances.STATUS_ARRIVED)
                                                            .subscribeOn(Schedulers.io())
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(responseDTO -> {
                                                                Intent i = new Intent();
                                                                setResult(Activity.RESULT_OK, i);
                                                                finish();
                                                            }, throwable -> {
                                                                Log.e(TAG, "updateStatusRequest: " + throwable.getMessage());
                                                            });
                                                } else {
                                                    SweetAlertDialog errorDialog = new SweetAlertDialog(TrackingMapActivity.this, SweetAlertDialog.ERROR_TYPE);
                                                    errorDialog.setTitleText("Thông báo");
                                                    errorDialog.setConfirmText("OK");
                                                    errorDialog.setContentText("Vị trí của bạn và khách quá xa nhau");
                                                    errorDialog.setConfirmClickListener(Dialog::dismiss);
                                                    errorDialog.show();
                                                }
                                            });
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "onCreate: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }//end if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
                    }//end data change

                    @Override
                    public void onCancelled(@NotNull DatabaseError error) {
                        // Failed to read value
                        Log.e(TAG, "Failed to read value." + error.toException());
                    }
                });
            }//end if (reqId == -1)
        } catch (Exception e) {
            Log.e(TAG, "onCreate: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (context != null && getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                String message = intent.getStringExtra("message");
                Gson gson = new Gson();
                MessageRequestFB responeReq = gson.fromJson(message, MessageRequestFB.class);


                if (responeReq.getMessage().equals(MyInstances.NOTI_FINISH)) {
                    SharedPreferenceHelper.setSharedPreferenceString(getApplicationContext(), MyInstances.KEY_BIKER_REQUEST, "");

                    //review reruest
                    setupReviewView(responeReq.getReqId(), responeReq.getReqCode(), responeReq.getReqPrice());
                    reviewDialog.show();
                }

                if (responeReq.getMessage().equals(MyInstances.NOTI_REJECTED) || responeReq.getMessage().equals(MyInstances.NOTI_CANELED)) {
                    SharedPreferenceHelper.setSharedPreferenceString(getApplicationContext(), MyInstances.KEY_BIKER_REQUEST, "");
                    Intent i = new Intent();
                    TrackingMapActivity.this.setResult(Activity.RESULT_OK, i);
                    finish();
                }

                if (responeReq.getMessage().equals(MyInstances.NOTI_ARRIVED)) {
                    Intent i = new Intent();
                    TrackingMapActivity.this.setResult(Activity.RESULT_OK, i);
                    finish();
                }

            }
        }
    };

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void setupReviewView(int reqId, String code, double price) {
        //set up review dialog
        LayoutInflater factory = LayoutInflater.from(this);
        final View reviewView = factory.inflate(R.layout.dialog_review_request, null);
        reviewDialog = new AlertDialog.Builder(this).create();

        ScaleRatingBar ratingBar = reviewView.findViewById(R.id.reviewRatingBar);
        EditText edtComment = reviewView.findViewById(R.id.edtCommentDetail);
        TextView txtReqCode = reviewView.findViewById(R.id.txtReviewReqCode);
        TextView txtPrice = reviewView.findViewById(R.id.txtReviewPrice);
        RecyclerView rvReviewServicePrice = reviewView.findViewById(R.id.rvReviewServicePrice);

        ViewModelProviders.of(this, viewModelFactory).get(RequestDetailViewModel.class)
                .getAllReqShopSerById(reqId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listReqShopSer -> {
                    if (listReqShopSer != null) {
                        rvReviewServicePrice.setAdapter(new ConfirmPriceRecyclerViewAdapter(listReqShopSer, true, requestShopService -> {
                            // on button click do nothing
                        }));
                        rvReviewServicePrice.setLayoutManager(new LinearLayoutManager(this));
                    }
                }, throwable -> {
                    Log.e(TAG, "setupReviewView - getRequestById: " + throwable.getMessage());
                });

        txtPrice.setText("" + MyMethods.convertMoney((float) price * 1000) + " vnd");
        txtReqCode.setText(code);

        reviewDialog.setView(reviewView);
        reviewView.findViewById(R.id.btn_confirm).setOnClickListener(confirmView -> {
            reviewDialog.dismiss();
            String comment = edtComment.getText().toString();
            double star = ratingBar.getRating();

            ReviewRequestDTO reviewDTO = new ReviewRequestDTO(comment, star);
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

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getDelegate().setContentView(layoutResID);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.LIGHT,
                style -> {
                    initLayers(style);
                    enableLocationComponent(style);
                    //Add marker
                    if (isBikerTracking) {
                        style.addImage("marker-icon-id",
                                BitmapFactory.decodeResource(
                                        this.getResources(), R.drawable.ic_shop_location));
                    } else {
                        style.addImage("marker-icon-id",
                                BitmapFactory.decodeResource(
                                        this.getResources(), R.drawable.mapbox_marker_icon_default));
                    }
                    if (destination != null) {
                        GeoJsonSource geoJsonSource = new GeoJsonSource("source-id", Feature.fromGeometry(
                                Point.fromLngLat(destination.getLongitude(), destination.getLatitude())));
                        style.addSource(geoJsonSource);
                    }
                    SymbolLayer symbolLayer = new SymbolLayer("layer-id", "source-id");
                    symbolLayer.withProperties(
                            PropertyFactory.iconImage("marker-icon-id")
                    );
                    style.addLayer(symbolLayer);
                });
    }

    private void setCameraPositon(Point point) {
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(point.latitude(), point.longitude()), 12));
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Get an instance of the component
        LocationComponent locationComponent = mapboxMap.getLocationComponent();

        // Activate with options
        locationComponent.activateLocationComponent(
                LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

        // Enable to make component visible
        locationComponent.setLocationComponentEnabled(true);

        // Set the component's camera mode
        locationComponent.setCameraMode(CameraMode.TRACKING);

        // Set the component's render mode
        locationComponent.setRenderMode(RenderMode.COMPASS);

        updateRoute(loadedMapStyle);
    }

    @SuppressWarnings({"MissingPermission"})
    private void updateRoute() {
        if (mapboxMap != null) {
            mapboxMap.setStyle(Style.LIGHT,
                    style -> {
                        initLayers(style);
                        // Get an instance of the component
                        LocationComponent locationComponent = mapboxMap.getLocationComponent();

                        // Activate with options
                        locationComponent.activateLocationComponent(
                                LocationComponentActivationOptions.builder(this, style).build());

                        // Enable to make component visible
                        locationComponent.setLocationComponentEnabled(true);

                        // Set the component's camera mode
                        locationComponent.setCameraMode(CameraMode.TRACKING);

                        // Set the component's render mode
                        locationComponent.setRenderMode(RenderMode.COMPASS);

                        if (style.isFullyLoaded())
                            updateRoute(style);
                        //Add marker
                        if (isBikerTracking) {
                            style.addImage("marker-icon-id",
                                    BitmapFactory.decodeResource(
                                            this.getResources(), R.drawable.ic_shop_location));
                        } else {
                            style.addImage("marker-icon-id",
                                    BitmapFactory.decodeResource(
                                            this.getResources(), R.drawable.mapbox_marker_icon_default));
                        }
                        if (destination != null) {
                            GeoJsonSource geoJsonSource = new GeoJsonSource("source-id", Feature.fromGeometry(
                                    Point.fromLngLat(destination.getLongitude(), destination.getLatitude())));
                            style.addSource(geoJsonSource);
                        }
                        SymbolLayer symbolLayer = new SymbolLayer("layer-id", "source-id");
                        symbolLayer.withProperties(
                                PropertyFactory.iconImage("marker-icon-id")
                        );
                        style.addLayer(symbolLayer);
                    });
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void updateRoute(@NonNull Style loadedMapStyle) {
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                Point origin = Point.fromLngLat(location.getLongitude(), location.getLatitude());
                setCameraPositon(origin);

                if (destination != null) {
                    Point destinationPoint = Point.fromLngLat(destination.getLongitude(), destination.getLatitude());
                    initSource(loadedMapStyle, origin, destinationPoint);
                    client = MapboxDirections.builder()
                            .origin(origin)
                            .destination(destinationPoint)
                            .overview(DirectionsCriteria.OVERVIEW_FULL)
                            .profile(DirectionsCriteria.PROFILE_DRIVING)
                            .accessToken(getString(R.string.mapbox_access_token))
                            .build();
                }
                client.enqueueCall(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                        if (response.body() == null) {
                            Log.e(TAG, "response.body() == null: No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "response.body().routes().size() < 1: No routes found");
                            return;
                        }

                        // Retrieve the directions route from the API response
                        currentRoute = response.body().routes().get(0);
                        if (loadedMapStyle.isFullyLoaded()) {
                            // Retrieve and update the source designated for showing the directions route
                            source = loadedMapStyle.getSourceAs(ROUTE_SOURCE_ID);

                            distance = response.body().routes().get(0).distance() / 1000;
                            // Create a LineString with the directions route's geometry and
                            // reset the GeoJSON source for the route LineLayer source
                            if (source != null) {
                                source.setGeoJson(FeatureCollection.fromFeature(
                                        Feature.fromGeometry(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6))));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "enableLocationComponent - onFailure: " + throwable.getMessage());
                    }
                });
            }
        });
    }

    /**
     * Add the route and marker sources to the map
     */
    private void initSource(@NonNull Style loadedMapStyle, Point origin, Point destination) {
        if (loadedMapStyle.isFullyLoaded()) {
            loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID,
                    FeatureCollection.fromFeatures(new Feature[]{})));

            GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[]{
                    Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude()))}));
            loadedMapStyle.addSource(iconGeoJsonSource);
        }
    }

    private void initLayers(@NonNull Style loadedMapStyle) {
        LineLayer routeLayer = new LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID);

        // Add the LineLayer to the map. This layer will display the directions route.
        routeLayer.setProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f),
                lineColor(Color.parseColor("#009688"))
        );
        loadedMapStyle.addLayer(routeLayer);


        // Add the red marker icon SymbolLayer to the map
        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                iconImage(RED_PIN_ICON_ID),
                iconIgnorePlacement(true),
                iconIgnorePlacement(true),
                iconOffset(new Float[]{0f, -4f})));
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    protected void onStart() {
        super.onStart();
        if (mapView != null)
            mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent serviceIntent = new Intent(this, UpdateLocationService.class);
        stopService(serviceIntent);
        if (mapView != null)
            mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null)
            mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent serviceIntent = new Intent(this, UpdateLocationService.class);
        stopService(serviceIntent);
        if (mapView != null)
            mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null)
            mapView.onLowMemory();
    }
}
