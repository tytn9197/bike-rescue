package com.example.bikerescueusermobile.data.model.user;

import com.google.gson.annotations.SerializedName;

public class User {
    public String name;

    @SerializedName("id")
    public int id;

    @SerializedName("roleId")
    public int roleId;

    @SerializedName("avatarUrl")
    public String avatarUrl;

    @SerializedName("username")
    public String username;

    @SerializedName("password")
    public String password;

    @SerializedName("createdTime")
    public String createdTime;

    @SerializedName("identifyNumber")
    public String identifyNumber;

    @SerializedName("fullname")
    public String fullname;

    @SerializedName("email")
    public String email;

    @SerializedName("address")
    public  String address;

    @SerializedName("phoneNumber")
    public String phoneNumber;

    @SerializedName("provider")
    public String provider;

    @SerializedName("accessToken")
    public String accessToken;

    @SerializedName("deviceToken")
    public String deviceToken;

    @SerializedName("Authorization")
    public String token;

    public User(String username, int id, int roleId, String avatarUrl, String provider, String accessToken, String deviceToken, String token) {
        this.username = username;
        this.id = id;
        this.roleId = roleId;
        this.avatarUrl = avatarUrl;
        this.provider = provider;
        this.accessToken = accessToken;
        this.deviceToken = deviceToken;
        this.token = token;
    }

    public User( String avatarUrl, String username, String email, String address, String phoneNumber) {
        this.avatarUrl = avatarUrl;
        this.username = username;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public User(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int  getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getIdentifyNumber() {
        return identifyNumber;
    }

    public void setIdentifyNumber(String identifyNumber) {
        this.identifyNumber = identifyNumber;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
