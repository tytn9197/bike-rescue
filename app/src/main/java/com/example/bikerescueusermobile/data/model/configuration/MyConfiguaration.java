package com.example.bikerescueusermobile.data.model.configuration;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class MyConfiguaration {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("value")
    private String value;

    @SerializedName("status")
    private boolean status;

    public MyConfiguaration() {

    }
}
