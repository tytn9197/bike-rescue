package com.example.bikerescueusermobile.ui.profile;

import com.example.bikerescueusermobile.data.model.vehicle.Vehicle;

public interface VehicleSelectedListener {
    void onDetailSelected(Vehicle vehicle);
    void onLongClick(Vehicle vehicle);
}
