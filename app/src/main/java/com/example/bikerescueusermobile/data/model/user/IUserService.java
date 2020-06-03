package com.example.bikerescueusermobile.data.model.user;

import com.example.bikerescueusermobile.data.model.login.LoginData;
import com.example.bikerescueusermobile.data.model.login.UpdateDevice;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface IUserService {

    @POST("/api/login")
    Single<User> login(@Body LoginData loginData);

    @POST("/api/users/{userId}/refresh-token")
    Single<User> updateFCM(@Header("Authorization") String token, @Path("userId") int userId, @Body UpdateDevice updateDevice);

    @Multipart
    @PUT("/api/users/{userId}")
    Single<User> updateUser(@Header("Authorization") String token, @Path("userId") int userId, @Part MultipartBody.Part file,
                            @Part("fullname") RequestBody fullname,
                            @Part("email") RequestBody email,
                            @Part("address") RequestBody address,
                            @Part("phoneNumber") RequestBody phoneNumber
    );

//    @GET("/api/users/{id}/createdCases")
//    Single<List<Case>> getHistoryCaseByUserId(@Path("id") int userId);

    @Multipart
    @POST("/api/users")
    Single<User> createUser(@Part MultipartBody.Part file,
                            @Part("username") RequestBody username,
                            @Part("password") RequestBody password,
                            @Part("fullname") RequestBody fullname,
//                            @Part("email") RequestBody email,
//                            @Part("address") RequestBody address,
                            @Part("phoneNumber") RequestBody phoneNumber
    );

}
