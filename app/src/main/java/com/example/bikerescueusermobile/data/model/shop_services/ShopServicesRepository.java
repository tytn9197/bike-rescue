package com.example.bikerescueusermobile.data.model.shop_services;

import com.example.bikerescueusermobile.data.model.user.CurrentUser;

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

    public Single<List<ShopServiceTable>> getShopServiceByShopId(int shopId,String token){
        return shopServices.getShopServiceByShopId(shopId, token);
    }

    public Single<ShopServiceTable> getShopServiceId(int shopId, int serivceId){
        return shopServices.getShopServiceId(CurrentUser.getInstance().getAccessToken(), shopId, serivceId);
    }

    public Single<List<ShopServiceTable>> getShopServiceByShopOwnerId(int id,String token){
        return shopServices.getShopServiceByShopOwnerId(id, token);
    }
}
