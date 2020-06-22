package com.example.bikerescueusermobile.ui.seach_shop_service;

import androidx.lifecycle.ViewModel;

import com.example.bikerescueusermobile.data.model.shop.Shop;
import com.example.bikerescueusermobile.data.model.shop_services.ShopService;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.data.model.shop.ShopRepository;
import com.example.bikerescueusermobile.data.model.shop_services.ShopServicesRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class ShopServiceViewModel extends ViewModel {

    private final ShopServicesRepository shopServicesRepository;
    private final ShopRepository shopRepository;

    @Inject
    public ShopServiceViewModel(ShopServicesRepository shopServicesRepository, ShopRepository shopRepository) {
        this.shopServicesRepository = shopServicesRepository;
        this.shopRepository = shopRepository;
    }

    public Single<List<ShopService>> getAllServices() {
        return shopServicesRepository.getAllServices(CurrentUser.getInstance().getToken());
    }

    public Single<List<ShopService>> getTop3Services(){
        return shopServicesRepository.getTop3Services(CurrentUser.getInstance().getToken());
    }

    public Single<List<Shop>> getTop5Shop(){
        return shopRepository.getTop5Shop(CurrentUser.getInstance().getToken());
    }

}
