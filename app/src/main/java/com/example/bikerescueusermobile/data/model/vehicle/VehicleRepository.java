package com.example.bikerescueusermobile.data.model.vehicle;

import com.example.bikerescueusermobile.data.model.user.CurrentUser;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class VehicleRepository {
    private final IVehicleService vehicleService;

    @Inject
    public VehicleRepository(IVehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public Single<List<Vehicle>> getAllVehicles(String token){
        return vehicleService.getAllVehicles(token);
    }

    public Single<List<Vehicle>> getVehicleByUserId(String token, int id){
        return vehicleService.getVehicleByUserId(token,id);
    }

    public Single<List<Vehicle>> getVehicleByUserIdStatusTrue(String token, int id){
        return vehicleService.getVehicleByUserIdStatusTrue(token,id);
    }

    public Single<Vehicle> createVehicle(VehicleDTO vehicle){
        return vehicleService.createVehicle(vehicle, CurrentUser.getInstance().getAccessToken());
    }

    public Single<Vehicle> updateVehicle(VehicleDTO vehicle, int vehicleID){
        return vehicleService.updateVehicle(vehicle, vehicleID, CurrentUser.getInstance().getAccessToken());
    }
}
