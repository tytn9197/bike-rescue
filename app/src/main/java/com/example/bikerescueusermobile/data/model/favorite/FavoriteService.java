package com.example.bikerescueusermobile.data.model.favorite;

import com.example.bikerescueusermobile.data.model.shop.Shop;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FavoriteService {

    @GET("/favoriteShop/{id}")
    Single<List<Shop>> getFavoriteShopByUserId(@Path("id") int id, @Header("Authorization") String token);

    @POST("/create")
    Single<Favorite> createFavorite(@Body Favorite favorite, @Query("status") boolean status, @Header("Authorization") String token);
}
