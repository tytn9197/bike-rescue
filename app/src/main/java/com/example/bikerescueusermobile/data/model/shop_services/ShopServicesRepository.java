package com.example.bikerescueusermobile.data.model.shop_services;

import com.example.bikerescueusermobile.data.model.request.ReviewRequestDTO;
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

    public Single<ShopServiceTable> getByServiceNameAndShopsId(String serviceName, int shopId){
        return shopServices.getByServiceNameAndShopsId(serviceName, shopId, CurrentUser.getInstance().getAccessToken());
    }

    public Single<List<ReviewRequestDTO>> getReviewCommentByShopId(int shopId){
        return shopServices.getReviewCommentByShopId(shopId, CurrentUser.getInstance().getAccessToken());
    }

    public Single<List<ShopServiceTable>> getAllShopServiceByShopId(int shopId,String token){
        return shopServices.getAllShopServiceByShopId(shopId, token);
    }

    public Single<ShopServiceTable> updateShopService(int shopServiceId, ShopServiceTable shopServiceTable, String token){
        return shopServices.updateShopService(shopServiceId, shopServiceTable, token);
    }

    public Single<ShopServiceDTO> createShopService(ShopServiceDTO shopServiceDTO, String token){
        return shopServices.createShopService(shopServiceDTO, token);
    }
}
