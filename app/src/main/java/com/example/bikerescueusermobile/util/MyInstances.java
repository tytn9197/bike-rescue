package com.example.bikerescueusermobile.util;

public class MyInstances {

    public static final String APP = "BikeRescue";

    //user status
    public static final int USER_STATUS_DEACTIVE = 0;
    public static final int USER_STATUS_FREE = 1;
    public static final int USER_STATUS_BUSY = 2;
    public static final int USER_STATUS_OFFLINE = 3;

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
    public static final String KEY_COUNT_CANCELATION= "KEY_COUNT_CANCELATION";
    public static final String KEY_COUNT_DOWN_TIME= "KEY_COUNT_DOWN_TIME";

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

    // algorithm
    public static final double DISTANCE_WEIGHT = 0.35;
    public static final double PRICE_WEIGHT = 0.27;
    public static final double RATING_WEIGHT = 0.15;
    public static final double COMPLETED_REQ_WEIGHT = 0.23;

    public static final long ONE_DAY_MILLISECONDS = 86400000;

//    //for biker's request detail
//    public static final String SENT_FROM_HISTORY = "SENT_FROM_HISTORY";
//    public static final String SENT_FROM_CONFIRM = "SENT_FROM_CONFIRM";


}
