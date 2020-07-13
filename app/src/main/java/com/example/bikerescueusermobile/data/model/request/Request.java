package com.example.bikerescueusermobile.data.model.request;

import com.example.bikerescueusermobile.data.model.user.User;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Request {
    @SerializedName("id")
    private int id;

    @SerializedName("createdDate")
    private Timestamp time;

    @SerializedName("status")
    private String status;

    @SerializedName("description")
    private String description;

    @SerializedName("code")
    private String request_code;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longtitude")
    private String longtitude;

    @SerializedName("address")
    private String address;

    @SerializedName("reviewComment")
    private String reviewComment;

    @SerializedName("reviewRating")
    private double reviewRating;

    @SerializedName("reviewUpdateDate")
    private Timestamp reviewUpdateDate;

    @SerializedName("created")
    private User created;

    @SerializedName("accepted")
    private User accepted;

    @SerializedName("vehiclesBrand")
    private String vehiclesBrand;

    @SerializedName("vehiclesType")
    private String vehiclesType;

    @SerializedName("vehiclesYear")
    private int vehiclesYear;

    public Request() {
    }
}
