package com.example.bikerescueusermobile.data.model.login;

public class UpdateDevice {
    private String deviceToken;

    public UpdateDevice(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
