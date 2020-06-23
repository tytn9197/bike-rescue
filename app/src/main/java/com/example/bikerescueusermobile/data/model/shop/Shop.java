package com.example.bikerescueusermobile.data.model.shop;

import androidx.annotation.NonNull;

import com.example.bikerescueusermobile.data.model.user.User;
import com.google.gson.annotations.SerializedName;

public class Shop {

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

    @SerializedName("description")
    private String description;

    @SerializedName("status")
    private boolean status;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longtitude")
    private String longtitude;

//    @SerializedName("user")
    private User user;

    private String shopRatingStar;

    public Shop(int id, String shopName, String address, String shopRatingStar) {
        this.id = id;
        this.shopName = shopName;
        this.address = address;
        this.shopRatingStar = shopRatingStar;
    }

    public Shop() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getShopRatingStar() {
        return shopRatingStar;
    }

    public void setShopRatingStar(String shopRatingStar) {
        this.shopRatingStar = shopRatingStar;
    }
}

