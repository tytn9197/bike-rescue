package com.example.bikerescueusermobile.data.model.login;

import lombok.Data;

@Data
public class LoginData {

    private String phoneNumber;
    private String password;
    private String deviceToken;

    public LoginData(String phoneNumber, String password, String deviceToken) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.deviceToken = deviceToken;
    }

//    public LoginData(String username, String password) {
//        this.phoneNumber = username;
//        this.password = password;
//    }

}
