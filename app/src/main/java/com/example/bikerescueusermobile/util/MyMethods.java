package com.example.bikerescueusermobile.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;


import com.example.bikerescueusermobile.data.model.request.CurrentRequest;
import com.example.bikerescueusermobile.data.model.request.Request;
import com.example.bikerescueusermobile.data.model.request.RequestDTO;
import com.example.bikerescueusermobile.data.model.shop.Shop;
import com.example.bikerescueusermobile.data.model.shop_services.ShopServiceTable;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.data.model.vehicle.Vehicle;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyMethods {
    public static int exchangeFromDpToPx(Context context, int dpValue) {
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dpValue,
                r.getDisplayMetrics()
        );
        return px;
    }

    public static int exchangeFromPxToDp(Context context, int pxValue) {
        Resources r = context.getResources();
        int dp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX,
                pxValue,
                r.getDisplayMetrics()
        );
        return dp;
    }

    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

    public static Bitmap stringToBitmap(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",") + 1),
                Base64.DEFAULT
        );
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static double getDistanceBetweenShopAndUser(String userLat, String userLong, String shopLat, String shopLong) {
        LatLng user = new LatLng(Double.parseDouble(userLat), Double.parseDouble(userLong));
        LatLng shop = new LatLng(Double.parseDouble(shopLat), Double.parseDouble(shopLong));
        return user.distanceTo(shop) / 1000;
    }

    public static void setDistance(List<Shop> list) {
        double distance = -1;
        for (int i = 0; i < list.size(); i++) {
            distance = MyMethods.getDistanceBetweenShopAndUser(
                    CurrentUser.getInstance().getLatitude(),
                    CurrentUser.getInstance().getLongtitude(),
                    list.get(i).getLatitude(),
                    list.get(i).getLongtitude());
            list.get(i).setDistanceFromUser(distance);
        }
    }

    public static String convertLatLngToAddress(Context context, double lat, double lng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            return address;
        } catch (Exception e) {
            Log.e("MyMethods", "convertLatLngToAddress: " + e.getMessage());
            return "";
        }
    }

    public static LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            Log.e("MyMethods", "convertLatLngToAddress: " + e.getMessage());
        }
        return p1;

    }

    public static void setRequestToCurrentRequest(Request request) {
        CurrentRequest.getInstance().setId(request.getId());
        CurrentRequest.getInstance().setCreatedDate(request.getCreatedDate());
        CurrentRequest.getInstance().setStatus(request.getStatus());
        CurrentRequest.getInstance().setRequestCode(request.getRequestCode());
        CurrentRequest.getInstance().setLatitude(request.getLatitude());
        CurrentRequest.getInstance().setLongtitude(request.getLongtitude());
        CurrentRequest.getInstance().setAddress(request.getAddress());
        CurrentRequest.getInstance().setReviewComment(request.getReviewComment());
        CurrentRequest.getInstance().setReviewRating(request.getReviewRating());
        CurrentRequest.getInstance().setReviewUpdateDate(request.getReviewUpdateDate());
        CurrentRequest.getInstance().setVehicle(request.getVehicle());
        CurrentRequest.getInstance().setCreatedUser(request.getCreatedUser());
        CurrentRequest.getInstance().setAcceptedUser(request.getAcceptedUser());
        CurrentRequest.getInstance().setListReqShopService(request.getListReqShopService());
    }

    public static void setRequestToCurrentRequest(RequestDTO request) {
        CurrentRequest.getInstance().setId(request.getId());
        CurrentRequest.getInstance().setCreatedDate(request.getCreatedDate());
        CurrentRequest.getInstance().setStatus(request.getStatus());
        CurrentRequest.getInstance().setRequestCode(request.getCode());
        CurrentRequest.getInstance().setLatitude(request.getLatitude());
        CurrentRequest.getInstance().setLongtitude(request.getLongtitude());
        CurrentRequest.getInstance().setAddress(request.getAddress());
        CurrentRequest.getInstance().setReviewComment(request.getReviewComment());
        CurrentRequest.getInstance().setReviewRating(request.getReviewRating());
        CurrentRequest.getInstance().setReviewUpdateDate(request.getReviewUpdateDate());
    }

    public static String convertTimeStampToDate(Timestamp ts) {
        Date d = new Date(ts.getTime());
        DateFormat f = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return f.format(d);
    }

    public static String convertTimeStampToTime(Timestamp ts) {
        Date d = new Date(ts.getTime());
        DateFormat f = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return f.format(d);
    }

    public static Double findMaxPrice(List<ShopServiceTable> arr) {
        // Initialize maximum element
        double max = arr.get(0).getPrice();

        // Traverse array elements from second and
        // compare every element with current max
        for (int i = 1; i < arr.size(); i++)
            if (arr.get(i).getPrice() > 0)
                if (arr.get(i).getPrice() > max)
                    max = arr.get(i).getPrice();

        return max;
    }

    public static Double findMinPrice(List<ShopServiceTable> arr) {
        // Initialize maximum element
        double min = arr.get(0).getPrice();

        // Traverse array elements from second and
        // compare every element with current max
        for (int i = 1; i < arr.size(); i++)
            if (arr.get(i).getPrice() > 0)
                if (arr.get(i).getPrice() < min)
                    min = arr.get(i).getPrice();

        return min;
    }
}
