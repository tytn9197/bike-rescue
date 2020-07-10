package com.example.bikerescueusermobile.data.model.shop_request;

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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getRequest_code() {
        return request_code;
    }

    public void setRequest_code(String request_code) {
        this.request_code = request_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getStatus_ic() {
        return status_ic;
    }

    public void setStatus_ic(int status_ic) {
        this.status_ic = status_ic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
