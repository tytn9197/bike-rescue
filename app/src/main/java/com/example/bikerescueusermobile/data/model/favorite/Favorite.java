package com.example.bikerescueusermobile.data.model.favorite;

import com.example.bikerescueusermobile.data.model.shop.Shop;
import com.example.bikerescueusermobile.data.model.user.User;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Favorite {
    @SerializedName("id")
    private int id;

    @SerializedName("description")
    private String description;

    @SerializedName("status")
    private boolean status;

    @SerializedName("shopId")
    private int shopId;

    @SerializedName("userId")
    private int userId;
}
