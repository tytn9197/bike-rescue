package com.example.bikerescueusermobile.data.model.request;

import com.example.bikerescueusermobile.data.model.shop_services.ShopServiceQuantity;

import java.util.List;

import lombok.Data;

@Data
public class ReqShopSerDTO {
    private Integer reqId;
    private Double price;
    private List<ShopServiceQuantity> listShopService;
}
