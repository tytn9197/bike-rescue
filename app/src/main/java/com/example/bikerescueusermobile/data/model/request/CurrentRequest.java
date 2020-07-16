package com.example.bikerescueusermobile.data.model.request;

public class CurrentRequest {
    private static Request INSTANCE = null;

    // other instance variables can be here

    public CurrentRequest(Request request) {
        INSTANCE = request;
    }

    public static synchronized Request getInstance() {
        if (INSTANCE == null) {

            INSTANCE = new Request();

        }
        return(INSTANCE);
    }
}
