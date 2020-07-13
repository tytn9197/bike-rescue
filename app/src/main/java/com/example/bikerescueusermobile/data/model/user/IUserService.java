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
import retrofit2.http.Query;

public interface IUserService {

    @POST("/api/login")
    Single<User> login(@Body LoginData loginData);

    @POST("/biker/register?roleName=ROLE_BIKER")
    Single<User> register(@Body LoginData user);

    Single<User> register(User user, String roleName);

    @POST("/biker/update/{id}")
    Single<User> updateInfo(@Header("Authorization") String token, @Path("id") int userId, @Body User user);

}
