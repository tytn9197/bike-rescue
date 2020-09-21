package com.example.bikerescueusermobile.data.model.shop_services;

import com.example.bikerescueusermobile.data.model.request.RequestShopService;

import java.util.ArrayList;
import java.util.List;

public class CurrentShopService {
    private static List<RequestShopService> INSTANCE = null;
    private static boolean isDelete = false;

    private CurrentShopService(List<RequestShopService> list, boolean delete) {
        INSTANCE = list;
        isDelete = delete;
    }

    public static List<RequestShopService> getInstance() {
        synchronized(CurrentShopService.class){
            if (INSTANCE == null) {
                INSTANCE = new ArrayList<>();
            }
        }
        return(INSTANCE);
    }

    public static boolean isDelete() {
        return(isDelete);
    }

    public static void setDelete(boolean delete) {
        isDelete = delete;
    }
}
