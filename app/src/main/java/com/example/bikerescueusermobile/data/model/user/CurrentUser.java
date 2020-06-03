package com.example.bikerescueusermobile.data.model.user;


public class CurrentUser {

    private static User INSTANCE = null;

    // other instance variables can be here

    public CurrentUser(User user) {
        INSTANCE = user;
    }

    public static synchronized User getInstance() {
        if (INSTANCE == null) {

            INSTANCE = new User();

        }
        return(INSTANCE);
    }


    // other instance methods can follow
}
