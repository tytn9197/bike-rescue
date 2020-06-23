package com.example.bikerescueusermobile.ui.map;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.ui.confirm.ConfirmInfoActivity;
import com.example.bikerescueusermobile.ui.home.HomeFragmentConstants;
import com.example.bikerescueusermobile.ui.send_request.SendRequestActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Scanner;
import java.util.List;

import com.mapbox.mapboxsdk.location.LocationComponent;

import butterknife.BindView;
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


public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback, MapboxMap.OnMapClickListener, PermissionsListener {

    private static final String TAG = "MapActivity";
    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";

    private MapView mapView;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private FusedLocationProviderClient mFusedLocationClient;
    MapboxDirections client;
    private DirectionsRoute currentRoute;
    private LatLng shopLocation;

    Button btnSendRequest;
    Button btnMapBack;
    RelativeLayout homeShopDetail;
    RelativeLayout homeShopDetailBackground;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.mapboxView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        shopLocation = new LatLng( 10.8615381,106.7955985);
        btnSendRequest = findViewById(R.id.btnSendRequest);
        homeShopDetail = findViewById(R.id.homeShopDetail);
        homeShopDetailBackground = findViewById(R.id.homeShopDetailBackground);
        btnMapBack = findViewById(R.id.btnMapBack);

        btnSendRequest.setOnClickListener(v -> {
            Intent intent = new Intent(this, ConfirmInfoActivity.class);
            startActivity(intent);
            finish();
        });

        btnMapBack.setOnClickListener(v->{
            this.finish();
        });


    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {

        return false;
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.LIGHT,
                style -> {
                    initLayers(style);
                    enableLocationComponent(style);
                    //Add marker
                    style.addImage("marker-icon-id",
                            BitmapFactory.decodeResource(
                                    this.getResources(), R.drawable.mapbox_marker_icon_default));

                    GeoJsonSource geoJsonSource = new GeoJsonSource("source-id", Feature.fromGeometry(
                            Point.fromLngLat(shopLocation.getLongitude(), shopLocation.getLatitude())));
                    style.addSource(geoJsonSource);
                    SymbolLayer symbolLayer = new SymbolLayer("layer-id", "source-id");
                    symbolLayer.withProperties(
                            PropertyFactory.iconImage("marker-icon-id")
                    );
                    style.addLayer(symbolLayer);
                });
//        new LoadGeoJson(MapActivity.this).execute();
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
                iconOffset(new Float[] {0f, -4f})));
    }

    /**
     * Add the route and marker sources to the map
     */
    private void initSource(@NonNull Style loadedMapStyle, Point origin, Point destination) {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID,
                FeatureCollection.fromFeatures(new Feature[] {})));

        GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[] {
                Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude()))}));
        loadedMapStyle.addSource(iconGeoJsonSource);
    }

    private void setCameraPositon(Location location) {
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

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

            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    Point origin = Point.fromLngLat(location.getLongitude(), location.getLatitude());
                    Point destination = Point.fromLngLat(shopLocation.getLongitude(), shopLocation.getLatitude());
                    initSource(loadedMapStyle,origin,destination);
                    client = MapboxDirections.builder()
                            .origin(origin)
                            .destination(destination)
                            .overview(DirectionsCriteria.OVERVIEW_FULL)
                            .profile(DirectionsCriteria.PROFILE_DRIVING)
                            .accessToken(getString(R.string.mapbox_access_token))
                            .build();

                    client.enqueueCall(new Callback<DirectionsResponse>() {
                        @Override public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                            if (response.body() == null) {
                                Log.e("call","No routes found, make sure you set the right user and access token.");
                                return;
                            } else if (response.body().routes().size() < 1) {
                                Log.e("call","No routes found");
                                return;
                            }

                            // Retrieve the directions route from the API response
                            currentRoute = response.body().routes().get(0);
                            if (loadedMapStyle.isFullyLoaded()) {
                                // Retrieve and update the source designated for showing the directions route
                                GeoJsonSource source = loadedMapStyle.getSourceAs(ROUTE_SOURCE_ID);

                                // Create a LineString with the directions route's geometry and
                                // reset the GeoJSON source for the route LineLayer source
                                if (source != null) {
                                    Log.e("route","onResponse: source != null");
                                    source.setGeoJson(FeatureCollection.fromFeature(
                                            Feature.fromGeometry(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6))));
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {

                            Log.e("error","Error: " + throwable.getMessage());

                        }
                    });
                }
            });
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
//        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
//            Toast.makeText(kthis, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            Log.e(TAG, "Khong co quyen truy cap");
            finish();
        }
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
