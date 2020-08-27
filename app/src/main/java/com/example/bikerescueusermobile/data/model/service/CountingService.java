package com.example.bikerescueusermobile.data.model.service;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class CountingService {
    @SerializedName("countRequest")
    private long countRequest;

    @SerializedName("serviceName")
    private String serviceName;
}
