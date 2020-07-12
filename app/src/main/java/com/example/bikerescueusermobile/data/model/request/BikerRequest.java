package com.example.bikerescueusermobile.data.model.request;

import lombok.Data;

@Data
public class BikerRequest {
    private int id;
    private String name;
    private String createdTime;
    private int status;

    public BikerRequest(int id, String name, String createdTime, int status) {
        this.id = id;
        this.name = name;
        this.createdTime = createdTime;
        this.status = status;
    }

}
