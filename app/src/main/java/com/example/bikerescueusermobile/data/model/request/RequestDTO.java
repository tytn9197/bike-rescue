package com.example.bikerescueusermobile.data.model.request;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

@Data
public class RequestDTO {
    private Integer id;
    private String status;
    private Timestamp createdDate;
    private String description;
    private String code;
    private String latitude;
    private String longtitude;
    private String address;
    private String reviewComment;
    private double reviewRating;
    private Timestamp reviewUpdateDate;
    private int vehicleId;
    private int createdId;
    private int acceptedId;
    private int shopServiceId;
}
