package com.example.bikerescueusermobile.data.model.user;

import com.example.bikerescueusermobile.data.model.login.LoginData;
import com.example.bikerescueusermobile.data.model.login.UpdateDevice;
import com.example.bikerescueusermobile.data.model.request.Response;
import com.example.bikerescueusermobile.data.model.shop.StatusSuccessDTO;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IUserService {

    @POST("/api/login")
    Single<User> login(@Body LoginData loginData);

//    @POST("/biker/register?roleName=ROLE_BIKER")
//    Single<User> register(@Body LoginData user);

    @POST("biker/register")
    Single<User> register(@Body User user, @Query("roleName") String roleName);

    @POST("/biker/update/{id}")
    Single<User> updateInfo(@Header("Authorization") String token, @Path("id") int userId, @Body User user);

    @PUT("/biker/userLatLong")
    Single<UserLatLong> setUserLatLong(@Body UserLatLong input, @Header("Authorization") String token);

    @GET("/biker/getLatLongFromReqId/{id}")
    Single<UserLatLong> getUserLatLongByReqId(@Path("id") int userId,
                                              @Query("isBikerTracking") boolean isBikerTracking,
                                              @Header("Authorization") String token);

    @GET("/shop/requestSuccessFromTo/{id}")
    Single<List<StatusSuccessDTO>> getSuccessReq(@Path("id") int id, @Query("from") String from, @Query("to") String to, @Header("Authorization") String token);

    @GET("/shop/requestSuccess/{id}")
    Single<List<StatusSuccessDTO>> getSuccessReqOnAlgo(@Path("id") int id, @Header("Authorization") String token);


    @GET("/shop/getNumOfStarByShopOwnerId/{id}")
    Single<Double> getNumOfStarByShopOwnerId(@Path("id") int id, @Header("Authorization") String token);

    @PUT("/api/updateStatusById")
    Single<Boolean> updateUserStatus(@Body UserStatusDTO userStatusDTO, @Header("Authorization") String token);
}
