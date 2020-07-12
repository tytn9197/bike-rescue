package com.example.bikerescueusermobile.data.model.shop_services;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class ShopService {

    @SerializedName("id")
    private int id;

    @SerializedName("serviceName")
    private String name;

    @SerializedName("serviceUrl")
    private String serviceUrl;

    @SerializedName("description")
    private String description;

    @SerializedName("status")
    private boolean status;

    public ShopService(int id, String name, boolean status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public ShopService(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public ShopService() {
    }

}
