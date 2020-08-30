package com.example.bikerescueusermobile.data.model.shop;

import com.example.bikerescueusermobile.data.model.service.CountingService;
import com.example.bikerescueusermobile.data.model.service.Service;

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

    @GET("/shop/countRequestFromTo/{id}")
    Single<Integer> countAllByAccepted(@Path("id") int shopOwnerId,
                                       @Query("from") String from, @Query("to") String to,
                                       @Header("Authorization") String token);

    @GET("/shop/countAllServiceFromTo/{id}")
    Single<List<CountingService>> getAllCountService(@Path("id")int shopId,
                                                     @Query("from") String from, @Query("to") String to,
                                                     @Header("Authorization") String token);

    @GET("/shop/getAllService")
    Single<List<Service>> getAllService(@Header("Authorization") String token);

    @GET("/shop/sumPriceRequestFromTo/{id}")
    Single<Double> sumPriceRequestFromTo(@Path("id") int shopOwnerId,
                                          @Query("from") String from, @Query("to") String to,
                                          @Header("Authorization") String token);
}
