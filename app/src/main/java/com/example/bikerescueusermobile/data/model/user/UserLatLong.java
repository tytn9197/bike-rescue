package com.example.bikerescueusermobile.data.model.user;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class UserLatLong {

    @SerializedName("id")
    private int id;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longtitude")
    private String longtitude;

    public UserLatLong(int id, String latitude, String longtitude) {
        this.id = id;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

}
