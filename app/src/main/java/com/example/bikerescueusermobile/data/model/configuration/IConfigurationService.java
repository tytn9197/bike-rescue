package com.example.bikerescueusermobile.data.model.configuration;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface IConfigurationService {
    @GET("/configuration/{name}")
    Single<List<MyConfiguaration>> getConfiguarationByName(@Path("name") String configName, @Header("Authorization") String token);

    @GET("/configuation")
    Single<List<MyConfiguaration>> getAllConfig(@Header("Authorization") String token);
}
