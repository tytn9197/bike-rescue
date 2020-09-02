package com.example.bikerescueusermobile.data.model.complain;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ComplainService {
    @Multipart
    @POST("/biker/send-complain")
    Single<Boolean> sendComplain(@Part("complainDTO") Complain complain, @Part List<MultipartBody.Part> listImg, @Header("Authorization") String token);
}
