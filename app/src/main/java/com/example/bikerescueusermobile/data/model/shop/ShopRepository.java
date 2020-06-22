package com.example.bikerescueusermobile.data.model.shop;

import com.example.bikerescueusermobile.data.model.shop_services.IShopServicesService;
import com.example.bikerescueusermobile.data.model.shop_services.ShopService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class ShopRepository {
    private final IShopService shopServices;

    @Inject
    public ShopRepository(IShopService shopServices) {
        this.shopServices = shopServices;
    }

    public Single<List<Shop>> getTop5Shop(String token){
        return shopServices.getTop5Shop(token);
    }

}
