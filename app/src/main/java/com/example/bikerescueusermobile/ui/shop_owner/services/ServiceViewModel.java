package com.example.bikerescueusermobile.ui.shop_owner.services;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bikerescueusermobile.data.model.service.CountingService;
import com.example.bikerescueusermobile.data.model.service.Service;
import com.example.bikerescueusermobile.data.model.shop.Shop;
import com.example.bikerescueusermobile.data.model.shop.ShopRepository;
import com.example.bikerescueusermobile.data.model.shop_services.ShopServiceTable;
import com.example.bikerescueusermobile.data.model.shop_services.ShopServicesRepository;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class ServiceViewModel extends ViewModel {
    private final ShopServicesRepository shopServicesRepository;
    private final ShopRepository shopRepository;
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    @Inject
    public ServiceViewModel(ShopServicesRepository shopServicesRepository, ShopRepository shopRepository) {
        this.shopServicesRepository = shopServicesRepository;
        this.shopRepository = shopRepository;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public Single<List<ShopServiceTable>> getShopServiceByShopId(int shopId) {
        loading.setValue(true);
        return shopServicesRepository.getShopServiceByShopId(shopId, CurrentUser.getInstance().getAccessToken());
    }

    public Single<List<ShopServiceTable>> getShopServiceByShopOwnerId(int shopOwnerId){
        return shopServicesRepository.getShopServiceByShopOwnerId(shopOwnerId, CurrentUser.getInstance().getAccessToken());
    }

    public Single<Integer> countAllByAccepted(int shopOwnerId, String from, String to){
        return shopRepository.countAllByAccepted(shopOwnerId, from, to);
    }

    public Single<Integer> countAllByAcceptedOnAlgo(int shopOwnerId){
        return shopRepository.countAllByAcceptedOnAlgo(shopOwnerId);
    }

    public Single<List<CountingService>> getAllCountService(int shopId, String from, String to){
        return shopRepository.getAllCountService(shopId, from, to);
    }

    public Single<List<Service>> getAllService(){
        return shopRepository.getAllService();
    }

    public Single<Double> sumPriceRequestFromTo(int shopOwnerId, String from, String to){
        return shopRepository.sumPriceRequestFromTo(shopOwnerId, from, to);
    }
}
