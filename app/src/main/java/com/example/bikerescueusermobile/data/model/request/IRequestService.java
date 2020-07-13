package com.example.bikerescueusermobile.data.model.request;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface IRequestService {

    @GET("/shop/getRequestByShopId/{id}")
    Single<List<Request>> getRequestByShopId(@Header("Authorization") String token, @Path("id") int shopId);
}
