package com.example.bikerescueusermobile.data.model.category;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class Category implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("categoryName")
    private String categoryName;

    @SerializedName("description")
    private String description;

    @SerializedName("status")
    private boolean status;

    public Category(int id, String categoryName, String description, boolean status) {
        this.id = id;
        this.categoryName = categoryName;
        this.description = description;
        this.status = status;
    }
}
