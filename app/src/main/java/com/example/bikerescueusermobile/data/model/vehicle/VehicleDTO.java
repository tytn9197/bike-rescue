package com.example.bikerescueusermobile.data.model.vehicle;


import lombok.Data;

@Data
public class VehicleDTO {
    int id;
    private String type;
    private String brand;
    private int vehiclesYear;
    private boolean status;
    private int userId;
}
