package com.example.bikerescueusermobile.data.model.complain;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Complain {
    @SerializedName("id")
    private int id;

    @SerializedName("complainReason")
    private String complainReason;

    @SerializedName("description")
    private String description;

    @SerializedName("status")
    private Boolean status;

    @SerializedName("image")
    private String image;

    @SerializedName("createdDate")
    private Timestamp createdDate;

    @SerializedName("finishedDate")
    private Timestamp finishedDate;

    @SerializedName("requestId")
    private int requestId;

    @SerializedName("createdId")
    private int createdId;
}
