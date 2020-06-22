package com.example.bikerescueusermobile.data.model.shop_services;

import com.example.bikerescueusermobile.data.model.shop_services.ShopService;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface IShopServicesService {
    @GET("/shop/getAllService")
    Single<List<ShopService>> getAllServices(@Header("Authorization") String token);

    @GET("/shop/getTop3Service")
    Single<List<ShopService>> getTop3Services(@Header("Authorization") String token);

}
