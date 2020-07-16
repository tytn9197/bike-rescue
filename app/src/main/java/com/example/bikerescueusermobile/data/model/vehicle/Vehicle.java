package com.example.bikerescueusermobile.data.model.vehicle;

import com.example.bikerescueusermobile.data.model.user.User;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Vehicle {
    @SerializedName("id")
    private int id;
    @SerializedName("type")
    private String type;
    @SerializedName("brand")
    private String brand;
    @SerializedName("vehiclesYear")
    private int vehiclesYear;
    @SerializedName("status")
    private boolean status;
    @SerializedName("user")
    private User user;
}
