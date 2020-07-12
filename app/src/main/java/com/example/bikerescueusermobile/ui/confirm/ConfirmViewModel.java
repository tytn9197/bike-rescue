package com.example.bikerescueusermobile.ui.confirm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bikerescueusermobile.data.model.configuration.ConfigurationRepository;
import com.example.bikerescueusermobile.data.model.configuration.MyConfiguaration;
import com.example.bikerescueusermobile.data.model.shop.ShopRepository;
import com.example.bikerescueusermobile.data.model.shop_services.ShopService;
import com.example.bikerescueusermobile.data.model.shop_services.ShopServicesRepository;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class ConfirmViewModel extends ViewModel {
    private final ConfigurationRepository configurationRepository;
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public void setLoading(boolean isLoad){
        loading.setValue(isLoad);
    }

    @Inject
    public ConfirmViewModel(ConfigurationRepository configurationRepository) {
        loading.setValue(true);
        this.configurationRepository = configurationRepository;
    }

    public Single<List<MyConfiguaration>> getAllConfig() {
        loading.setValue(true);
        return configurationRepository.getAllConfig(CurrentUser.getInstance().getAccessToken());
    }
}
