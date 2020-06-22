package com.example.bikerescueusermobile.data.model.shop_services;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
