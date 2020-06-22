package com.example.bikerescueusermobile.ui.main;

import com.google.android.gms.maps.model.LatLng;

public class MyCurrentLocation {
    private static MyCurrentLocation INSTANCE;

    private MyCurrentLocation(){}

    private LatLng currentLocation;
    public static MyCurrentLocation getInstance(){
        if(INSTANCE == null){
            INSTANCE = new MyCurrentLocation();
        }
        return INSTANCE;
    }

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LatLng currentLocation) {
        this.currentLocation = currentLocation;
    }

}
