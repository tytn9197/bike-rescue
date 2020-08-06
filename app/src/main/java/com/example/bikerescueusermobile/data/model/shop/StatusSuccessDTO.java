package com.example.bikerescueusermobile.data.model.shop;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class StatusSuccessDTO {
    @SerializedName("id")
    private int id;

    @SerializedName("status")
    private String status;

    @SerializedName("acceptedId")
    private int acceptedId;
}
