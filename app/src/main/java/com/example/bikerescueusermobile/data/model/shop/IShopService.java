package com.example.bikerescueusermobile.data.model.shop;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryName;

public interface IShopService {
    @GET("/shop/getTop5Shop")
    Single<List<Shop>> getTop5Shop(@Header("Authorization") String token);

    @GET("/shop/shops")
    Single<List<Shop>> getAllShop(@Header("Authorization") String token);

    @GET("/shop/getShopByService")
    Single<List<Shop>> getShopByServiceName(@Header("Authorization") String token, @Query("name") String serviceName);

    @GET("/shop/getShopByShopOwnerId/{shopOwnerId}")
    Single<Shop> getShopByShopOwnerId(@Header("Authorization") String token, @Path("shopOwnerId") int shopOwnerId);
}
