package com.example.bikerescueusermobile.data.model.vehicle;

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

}
