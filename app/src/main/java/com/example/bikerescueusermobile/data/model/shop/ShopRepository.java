package com.example.bikerescueusermobile.data.model.shop;

import com.example.bikerescueusermobile.data.model.service.CountingService;
import com.example.bikerescueusermobile.data.model.shop_services.IShopServicesService;
import com.example.bikerescueusermobile.data.model.shop_services.ShopService;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;

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

    public Single<List<Shop>> getAllShop(String token){
        return shopServices.getAllShop(token);
    }

    public Single<List<Shop>> getShopByServiceName(String token, String serviceName){
        return shopServices.getShopByServiceName(token, serviceName);
    }
    public Single<Shop> getShopByShopOwnerId(String token, int shopOwnerId){
        return shopServices.getShopByShopOwnerId(token, shopOwnerId);
    }

    public Single<Integer> countAllByAccepted(int shopOwnerId){
        return shopServices.countAllByAccepted(shopOwnerId, CurrentUser.getInstance().getAccessToken());
    }

    public Single<List<CountingService>> getAllCountService(int shopId){
        return shopServices.getAllCountService(shopId, CurrentUser.getInstance().getAccessToken());
    }
}
