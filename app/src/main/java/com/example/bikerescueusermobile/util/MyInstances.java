package com.example.bikerescueusermobile.util;

public class MyInstances {

    public static final String APP = "BikeRescue";

    //user status
    public static final int USER_STATUS_DEACTIVE = 0;
    public static final int USER_STATUS_FREE = 1;
    public static final int USER_STATUS_BUSY = 2;

    //request status
    public static final String STATUS_CANCELED="CANCELED";
    public static final String STATUS_CREATED ="CREATED";
    public static final String STATUS_ACCEPT = "ACCEPT";
    public static final String STATUS_REJECTED = "REJECTED";
    public static final String STATUS_FINISHED = "FINISHED";
    public static final String STATUS_ARRIVED = "ARRIVED";

    // role name
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_SHOP = "ROLE_SHOP";
    public static final String ROLE_BIKER = "ROLE_BIKER";

    //share preference key
    public static final String KEY_BIKER_REQUEST= "bikerReq";
    public static final String KEY_SHOP_REQUEST= "shopReq";
    public static final String KEY_LOGGED_IN= "user";

    //for noti message
    public static final String NOTI_CREATED = "NOTI_CREATED";
    public static final String NOTI_CANELED = "NOTI_CANELED";
    public static final String NOTI_REJECTED = "NOTI_REJECTED";
    public static final String NOTI_ACCEPT = "NOTI_ACCEPT";
    public static final String NOTI_FINISH = "NOTI_FINISH";
    public static final String TITTLE_SHOP = "TITTLE_SHOP";
    public static final String TITTLE_BIKER = "TITTLE_BIKER";
    public static final String NOTI_AUTO_REJECTED = "NOTI_AUTO_REJECTED";
    public static final String NOTI_ARRIVED = "NOTI_ARRIVED";

    //for shop
    public static final int SHOP_RESULT_CODE = 111;

    // error code
    public static final int ERROR_DOXANG_PRICE = 2;
    public static final int ERROR_VANDEKHAC = 3;
    public static final int ERROR_VEHICLE_TYPE = 1;


//    //for biker's request detail
//    public static final String SENT_FROM_HISTORY = "SENT_FROM_HISTORY";
//    public static final String SENT_FROM_CONFIRM = "SENT_FROM_CONFIRM";


}
