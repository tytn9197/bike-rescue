package com.example.bikerescueusermobile.data.model.vehicle;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface IVehicleService {
    @GET("/vehicles")
    Single<List<Vehicle>> getAllVehicles(@Header("Authorization") String token);
    @GET("/vehicles/getVehiclesByUserId")
    Single<List<Vehicle>> getVehicleByUserId(@Header("Authorization") String token,@Query("id") int id);
}
