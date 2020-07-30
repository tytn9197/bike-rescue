package com.example.bikerescueusermobile.ui.seach_shop_service;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bikerescueusermobile.data.model.shop.Shop;
import com.example.bikerescueusermobile.data.model.shop_services.ShopService;
import com.example.bikerescueusermobile.data.model.shop_services.ShopServiceTable;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.data.model.shop.ShopRepository;
import com.example.bikerescueusermobile.data.model.shop_services.ShopServicesRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class ShopServiceViewModel extends ViewModel {

    private final ShopServicesRepository shopServicesRepository;
    private final ShopRepository shopRepository;
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public void setLoading(boolean isLoad){
        loading.setValue(isLoad);
    }

    @Inject
    public ShopServiceViewModel(ShopServicesRepository shopServicesRepository, ShopRepository shopRepository) {
        loading.setValue(true);
        this.shopServicesRepository = shopServicesRepository;
        this.shopRepository = shopRepository;
    }

    public Single<List<ShopService>> getAllServices() {
        loading.setValue(true);
        return shopServicesRepository.getAllServices(CurrentUser.getInstance().getAccessToken());
    }

    public Single<List<ShopService>> getTop3Services(){
        loading.setValue(true);
        return shopServicesRepository.getTop3Services(CurrentUser.getInstance().getAccessToken());
    }

    public Single<List<Shop>> getTop5Shop(){
        loading.setValue(true);
        return shopRepository.getTop5Shop(CurrentUser.getInstance().getAccessToken());
    }

    public Single<List<Shop>> getAllShop(){
        loading.setValue(true);
        return shopRepository.getAllShop(CurrentUser.getInstance().getAccessToken());
    }

    public Single<List<Shop>> getShopByServiceName(String serviceName){
        loading.setValue(true);
        return shopRepository.getShopByServiceName(CurrentUser.getInstance().getAccessToken(), serviceName);
    }

    public Single<List<ShopServiceTable>> getShopServiceByShopId(int shopId){
        loading.setValue(true);
        return shopServicesRepository.getShopServiceByShopId(shopId, CurrentUser.getInstance().getAccessToken());
    }

    public Single<ShopServiceTable> getByServiceNameAndShopsId(String serviceName, int shopId){
        loading.setValue(true);
        return shopServicesRepository.getByServiceNameAndShopsId(serviceName, shopId);
    }
}
