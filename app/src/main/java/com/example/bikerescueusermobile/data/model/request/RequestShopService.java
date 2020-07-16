package com.example.bikerescueusermobile.data.model.request;

import com.example.bikerescueusermobile.data.model.shop_services.ShopServiceTable;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class RequestShopService {
    @SerializedName("id")
    private int id;

    @SerializedName("request")
    private Request request;

    @SerializedName("shopService")
    private ShopServiceTable shopService;

}
