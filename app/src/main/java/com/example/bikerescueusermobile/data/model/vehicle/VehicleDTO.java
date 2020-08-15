package com.example.bikerescueusermobile.data.model.vehicle;


import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class VehicleDTO {
    @SerializedName("id")
    int id;
    @SerializedName("type")
    private String type;
    @SerializedName("brand")
    private String brand;
    @SerializedName("vehiclesYear")
    private int vehiclesYear;
    @SerializedName("status")
    private boolean status;
    @SerializedName("userId")
    private int userId;
}
