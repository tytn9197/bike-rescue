package com.example.bikerescueusermobile.data.model.shop_services;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ShopServiceDTO {
    @SerializedName("id")
    private int id;
    @SerializedName("description")
    private String description;
    @SerializedName("price")
    private Double price;
    @SerializedName("serviceAvatar")
    private String serviceAvatar;
    @SerializedName("status")
    private boolean status;
    @SerializedName("serviceId")
    private int serviceId;
    @SerializedName("shopId")
    private int shopId;
}
