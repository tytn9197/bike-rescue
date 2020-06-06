package com.example.bikerescueusermobile.data.model.request;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
