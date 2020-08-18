package com.example.bikerescueusermobile.data.model.shop;

import androidx.annotation.NonNull;

import com.example.bikerescueusermobile.data.model.user.User;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class Shop implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("shopName")
    private String shopName;

    @SerializedName("address")
    private String address;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("email")
    private String email;

    @SerializedName("avatarUrl")
    private String avatarUrl;

    @SerializedName("avtUrl")
    private String avtUrl;

    @SerializedName("description")
    private String description;

    @SerializedName("status")
    private boolean status;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longtitude")
    private String longtitude;

    @SerializedName("userInfoDTO")
    private User user;

    @SerializedName("user")
    private User userNameOnly;

    @SerializedName("numOfStar")
    private String shopRatingStar;

    @SerializedName("openTime")
    private String openTime;

    @SerializedName("closeTime")
    private String closeTime;

    private double score;
    /**
     * distance from biker to shop address in km
     */
    private double distanceFromUser;

    /**
     * time from biker to shop address in minutes
     */
    private double durationToBiker;

    public Shop(int id, String shopName, String address, String shopRatingStar) {
        this.id = id;
        this.shopName = shopName;
        this.address = address;
        this.shopRatingStar = shopRatingStar;
    }

    public Shop() {
    }

}

