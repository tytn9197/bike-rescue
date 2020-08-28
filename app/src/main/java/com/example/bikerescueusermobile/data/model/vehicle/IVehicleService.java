package com.example.bikerescueusermobile.data.model.vehicle;

import com.example.bikerescueusermobile.data.model.user.CurrentUser;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IVehicleService {
    @GET("/vehicles")
    Single<List<Vehicle>> getAllVehicles(@Header("Authorization") String token);

    @GET("/vehicles/getVehiclesByUserId")
    Single<List<Vehicle>> getVehicleByUserId(@Header("Authorization") String token,@Query("id") int id);

    @GET("/vehicles/getVehiclesByUserIdStatusTrue")
    Single<List<Vehicle>> getVehicleByUserIdStatusTrue(@Header("Authorization") String token,@Query("id") int id);

    @POST("/vehicles")
    Single<Vehicle> createVehicle(@Body VehicleDTO vehicle, @Header("Authorization") String token);

    @PUT("/vehicles/{id}")
    Single<Vehicle> updateVehicle(@Body VehicleDTO vehicle, @Path("id") int vehicleId, @Header("Authorization") String token);
}
