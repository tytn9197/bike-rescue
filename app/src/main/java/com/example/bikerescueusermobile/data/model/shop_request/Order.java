package com.example.bikerescueusermobile.data.model.shop_request;

import lombok.Data;

@Data
public class Order {
    int image;
    String request_code;
    String name;
    String address;
    String field;
    String time;
    int status_ic;
    String status;

    public Order(int image, String request_code, String name, String address, String field, String time, int status_ic, String status) {
        this.image = image;
        this.request_code = request_code;
        this.name = name;
        this.address = address;
        this.field = field;
        this.time = time;
        this.status_ic = status_ic;
        this.status = status;
    }


}
