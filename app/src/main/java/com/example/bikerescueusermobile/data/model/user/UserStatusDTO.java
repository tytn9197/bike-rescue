package com.example.bikerescueusermobile.data.model.user;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class UserStatusDTO {
    @SerializedName("id")
    private int id;
    @SerializedName("status")
    private int status;
}
