package com.example.bikerescueusermobile.data.model.service;

import com.example.bikerescueusermobile.data.model.category.Category;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class Service implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("serviceName")
    private String serviceName;

    @SerializedName("description")
    private String description;

    @SerializedName("unit")
    private String unit;

    @SerializedName("status")
    private boolean status;

    @SerializedName("category")
    private Category category;


    public Service(int id, String serviceName, String description, String unit, boolean status) {
        this.id = id;
        this.serviceName = serviceName;
        this.description = description;
        this.unit = unit;
        this.status = status;
    }

}
