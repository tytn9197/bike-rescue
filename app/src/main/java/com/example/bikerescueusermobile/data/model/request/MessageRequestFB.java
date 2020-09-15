package com.example.bikerescueusermobile.data.model.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class MessageRequestFB implements Serializable {
    @SerializedName("reqId")
    private int reqId;
    @SerializedName("message")
    private String message;
    @SerializedName("reason")
    private String reason;
    @SerializedName("acceptedId")
    private int acceptedId;
    @SerializedName("reqCode")
    private String reqCode;
    @SerializedName("reqPrice")
    private double reqPrice;
}
