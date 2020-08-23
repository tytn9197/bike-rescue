package com.example.bikerescueusermobile.data.model.favorite;

import com.example.bikerescueusermobile.data.model.shop.Shop;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface FavoriteService {

    @GET("/favoriteShop/{id}")
    Single<List<Shop>> getFavoriteShopByUserId(@Path("id") int id, @Header("Authorization") String token);
}
