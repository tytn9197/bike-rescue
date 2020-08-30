package com.example.bikerescueusermobile.data.model.request;

import com.example.bikerescueusermobile.data.model.user.User;
import com.example.bikerescueusermobile.data.model.vehicle.Vehicle;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

@Data
public class Request {
    @SerializedName("id")
    private int id;

    @SerializedName("createdDate")
    private Timestamp createdDate;

    @SerializedName("status")
    private String status;

    @SerializedName("description")
    private String description;

    @SerializedName("code")
    private String requestCode;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longtitude")
    private String longtitude;

    @SerializedName("price")
    private Double price;

    @SerializedName("address")
    private String address;

    @SerializedName("reviewComment")
    private String reviewComment;

    @SerializedName("reviewRating")
    private double reviewRating;

    @SerializedName("reviewUpdateDate")
    private Timestamp reviewUpdateDate;

    @SerializedName("created")
    private User createdUser;

    @SerializedName("accepted")
    private User acceptedUser;

    @SerializedName("vehicle")
    private Vehicle vehicle;

    @SerializedName("cancelReason")
    private String cancelReason;

    @SerializedName("listReqShopService")
    List<RequestShopService> listReqShopService;

    public Request() {
    }

    public Request(int id) {
        this.id = id;
    }
}
