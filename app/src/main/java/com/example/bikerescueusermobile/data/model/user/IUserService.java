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


}
