package com.example.bikerescueusermobile.data.model.user;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("userId")
    private int id;

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

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("accessToken")
    private String token;

    public User(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
