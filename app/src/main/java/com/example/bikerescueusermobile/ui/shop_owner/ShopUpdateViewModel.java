package com.example.bikerescueusermobile.ui.shop_owner;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bikerescueusermobile.data.model.request.Response;
import com.example.bikerescueusermobile.data.model.shop.StatusSuccessDTO;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.data.model.user.User;
import com.example.bikerescueusermobile.data.model.user.UserRepository;
import com.example.bikerescueusermobile.data.model.user.UserStatusDTO;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class ShopUpdateViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    @Inject
    public ShopUpdateViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }


    public Single<User> updateInfo(int id, User user) {
        loading.setValue(true);
        return userRepository.updateInfo(CurrentUser.getInstance().getAccessToken(), id, user);
    }

    public Single<List<StatusSuccessDTO>> getSuccessReq(int id, String from, String to){
        return userRepository.getSuccessReq(id, from, to);
    }

    public Single<List<StatusSuccessDTO>> getSuccessReqOnAlgo(int id){
        return userRepository.getSuccessReqOnAlgo(id);
    }

    public Single<Double> getNumOfStarByShopOwnerId(int id){
        return userRepository.getNumOfStarByShopOwnerId(id);
    }

    public Single<Boolean> updateUserStatus(UserStatusDTO userStatusDTO){
        return userRepository.updateUserStatus(userStatusDTO);
    }
}
