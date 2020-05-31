package com.example.bikerescueusermobile.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyMethods {
    public static int exchangeFromDpToPx(Context context, int dpValue){
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dpValue,
                r.getDisplayMetrics()
        );
        return px;
    }

    public static int exchangeFromPxToDp(Context context, int pxValue){
        Resources r = context.getResources();
        int dp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX,
                pxValue,
                r.getDisplayMetrics()
        );
        return dp;
    }

    public static void moveCamera(GoogleMap map, LatLng latLng, float zoom){
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }

    public static void moveCamera(GoogleMap map, LatLng latLng, float zoom, String markerTitle){
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        if(!markerTitle.equals("My Location")) {
            MarkerOptions options = new MarkerOptions().position(latLng).title(markerTitle);
            map.addMarker(options);
        }
    }

}
