package com.example.bikerescueusermobile.data.model.user;


public class CurrentUser {

    private static User INSTANCE = null;

    private CurrentUser(User user) {
        INSTANCE = user;
    }

    public static User getInstance() {
        synchronized(CurrentUser.class){
            if (INSTANCE == null) {
                INSTANCE = new User();
            }
        }
        return(INSTANCE);
    }

}
