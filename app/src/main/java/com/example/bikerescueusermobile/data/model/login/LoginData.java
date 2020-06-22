package com.example.bikerescueusermobile.data.model.login;

public class LoginData {

    private String phoneNumber;
    private String password;

    public LoginData(String username, String password) {
        this.phoneNumber = username;
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
