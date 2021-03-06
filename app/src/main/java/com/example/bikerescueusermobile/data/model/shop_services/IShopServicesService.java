package com.example.bikerescueusermobile.data.model.shop_services;

import com.example.bikerescueusermobile.data.model.request.ReviewRequestDTO;
import com.example.bikerescueusermobile.data.model.shop_services.ShopService;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IShopServicesService {
    @GET("/shop/getAllService")
    Single<List<ShopService>> getAllServices(@Header("Authorization") String token);

    @GET("/shop/getTop3Service")
    Single<List<ShopService>> getTop3Services(@Header("Authorization") String token);

    @GET("/shop/getShopServiceByShopId")
    Single<List<ShopServiceTable>> getShopServiceByShopId(@Query("shopId") int shopId, @Header("Authorization") String token);

    @GET("/shop/getShopIdAndServiceId")
    Single<ShopServiceTable> getShopServiceId(@Header("Authorization") String token, @Query("shopId") int shopId, @Query("serviceId") int serivceId);

    @GET("/shop/getShopServiceByShopOwnerId/{shopOwnerId}")
    Single<List<ShopServiceTable>> getShopServiceByShopOwnerId(@Path("shopOwnerId") int shopOwnerId, @Header("Authorization") String token);

    @GET("/shop/getByServiceNameAndShopsId")
    Single<ShopServiceTable> getByServiceNameAndShopsId(@Query("serviceName") String serviceName, @Query("shopsId") int shopId, @Header("Authorization") String token);

    @GET("/shop/reviewComment/{shopId}")
    Single<List<ReviewRequestDTO>> getReviewCommentByShopId(@Path("shopId") int shopId, @Header("Authorization") String token);

    @GET("/shop/getAllShopServiceByShopId")
    Single<List<ShopServiceTable>> getAllShopServiceByShopId(@Query("shopId") int shopId, @Header("Authorization") String token);

    @PUT("/shop/shopService/{id}")
    Single<ShopServiceTable> updateShopService(@Path("id") int shopServiceId, @Body ShopServiceTable shopServiceTable, @Header("Authorization") String token);

    @POST("/shop/shopService/create")
    Single<ShopServiceDTO> createShopService(@Body ShopServiceDTO shopServiceDTO, @Header("Authorization") String token);
}
