package com.example.bikerescueusermobile.data.model.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class User implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("status")
    private String status;

    @SerializedName("address")
    private String address;

    @SerializedName("avtUrl")
    private String avatarUrl;

    @SerializedName("deviceToken")
    private String deviceToken;

    @SerializedName("email")
    private String email;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("password")
    private String password;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("accessToken")
    private String accessToken;

    private String latitude;

    private String longtitude;

    @SerializedName("createdDate")
    private String createdTime;

    @SerializedName("roleId")
    private int roleId;

    private int chosenShopOwnerId;

    private int currentBikerId;

    private int sentReqId;

    public User(){

    }

    public User(String password, String phoneNumber) {
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

}
