package com.example.bikerescueusermobile.data.model.role;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Role {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("status")
    private boolean status;

    public Role(int id, String name, boolean status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

}
