package com.example.bikerescueusermobile.data.model.shop_services;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class ShopServicesRepository {
    private final IShopServicesService shopServices;

    @Inject
    public ShopServicesRepository(IShopServicesService shopServices) {
        this.shopServices = shopServices;
    }

    public Single<List<ShopService>> getAllServices(String token){
        return shopServices.getAllServices(token);
    }

    public Single<List<ShopService>> getTop3Services(String token){
        return shopServices.getTop3Services(token);
    }

}
