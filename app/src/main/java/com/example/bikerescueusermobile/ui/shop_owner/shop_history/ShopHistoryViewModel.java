package com.example.bikerescueusermobile.ui.shop_owner.shop_history;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bikerescueusermobile.data.model.request.RequestRepository;
import com.example.bikerescueusermobile.data.model.request.Request;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;


import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class ShopHistoryViewModel extends ViewModel {
    private final RequestRepository requestRepository;
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    @Inject
    public ShopHistoryViewModel(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public Single<List<Request>> getRequestByShopId(int shopId, String from, String to) {
        loading.setValue(true);
        return requestRepository.getRequestByShopId(CurrentUser.getInstance().getAccessToken(), shopId, from, to);
    }
}
