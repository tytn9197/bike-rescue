package com.example.bikerescueusermobile.ui.confirm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bikerescueusermobile.data.model.configuration.ConfigurationRepository;
import com.example.bikerescueusermobile.data.model.configuration.MyConfiguaration;
import com.example.bikerescueusermobile.data.model.request.RequestDTO;
import com.example.bikerescueusermobile.data.model.request.RequestRepository;
import com.example.bikerescueusermobile.data.model.request.Response;
import com.example.bikerescueusermobile.data.model.shop_services.ShopServiceTable;
import com.example.bikerescueusermobile.data.model.shop_services.ShopServicesRepository;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.data.model.vehicle.Vehicle;
import com.example.bikerescueusermobile.data.model.vehicle.VehicleDTO;
import com.example.bikerescueusermobile.data.model.vehicle.VehicleRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class ConfirmViewModel extends ViewModel {
    private final ConfigurationRepository configurationRepository;
    private final RequestRepository requestRepository;
    private final VehicleRepository vehicleRepository;
    private final ShopServicesRepository shopServicesRepository;
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public void setLoading(boolean isLoad){
        loading.setValue(isLoad);
    }

    @Inject
    public ConfirmViewModel(ConfigurationRepository configurationRepository, RequestRepository requestRepository, VehicleRepository vehicleRepository, ShopServicesRepository shopServicesRepository) {
        this.requestRepository = requestRepository;
        this.vehicleRepository = vehicleRepository;
        this.shopServicesRepository = shopServicesRepository;
        loading.setValue(true);
        this.configurationRepository = configurationRepository;
    }

    public Single<List<MyConfiguaration>> getAllConfig() {
        loading.setValue(true);
        return configurationRepository.getAllConfig(CurrentUser.getInstance().getAccessToken());
    }

    public Single<List<Vehicle>> getAllVehicles(){
        loading.setValue(true);
        return vehicleRepository.getAllVehicles(CurrentUser.getInstance().getAccessToken());
    }

    public Single<Response<RequestDTO>> createRequest(RequestDTO request){
        loading.setValue(true);
        return requestRepository.createRequest(request);
    }

    public Single<List<Vehicle>> getVehicleByUserId(int id){
        loading.setValue(true);
        return vehicleRepository.getVehicleByUserId(CurrentUser.getInstance().getAccessToken(), id);
    }

    public Single<ShopServiceTable> getShopServiceId(int shopId, int serivceID){
        loading.setValue(true);
        return shopServicesRepository.getShopServiceId(shopId,serivceID);
    }

    public Single<Vehicle> createVehicle(VehicleDTO vehicle){
        loading.setValue(true);
        return vehicleRepository.createVehicle(vehicle);
    }

    public Single<Vehicle> updateVehicle(VehicleDTO vehicle, int vehicleID){
        loading.setValue(true);
        return vehicleRepository.updateVehicle(vehicle, vehicleID);
    }
}
