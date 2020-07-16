package com.example.bikerescueusermobile.data.model.request;

import java.util.List;

import lombok.Data;

@Data
public class RequestDTO {
    private String description;
    private String code;
    private String latitude;
    private String longtitude;
    private String address;
    private int vehicleId;
    private int createdId;
    private int acceptedId;
    private int shopServiceId;
}
