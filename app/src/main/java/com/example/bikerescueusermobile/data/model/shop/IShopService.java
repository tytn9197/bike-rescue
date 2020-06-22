package com.example.bikerescueusermobile.data.model.shop;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface IShopService {
    @GET("/shop/getTop5Shop")
    Single<List<Shop>> getTop5Shop(@Header("Authorization") String token);
}
