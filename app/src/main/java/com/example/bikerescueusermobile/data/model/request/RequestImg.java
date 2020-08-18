package com.example.bikerescueusermobile.data.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class RequestImg {
    @SerializedName("id")
    private Integer id;

    @SerializedName("image")
    private String image;
//    private RequestEntity requestEntity;
}
