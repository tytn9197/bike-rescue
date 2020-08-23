package com.example.bikerescueusermobile.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bikerescueusermobile.data.model.login.LoginData;
import com.example.bikerescueusermobile.data.model.user.User;
import com.example.bikerescueusermobile.data.model.user.UserLatLong;
import com.example.bikerescueusermobile.data.model.user.UserRepository;

import javax.inject.Inject;

import io.reactivex.Single;

public class LoginModel extends ViewModel {

    private final UserRepository userRepository;

    private final MutableLiveData<User> userLivedata = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginLoadError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    LiveData<User> getUser() {
        return userLivedata;
    }
    LiveData<Boolean> getError() {
        return loginLoadError;
    }
    LiveData<Boolean> getLoading() {
        return loading;
    }

    @Inject
    public LoginModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setLoading(boolean isLoad){
        loading.setValue(isLoad);
    }

    public Single<User> login(LoginData loginData) {
        loading.setValue(true);
        return userRepository.login(loginData);
    }

    public Single<User> loginWithoutLoad(LoginData loginData) {
        return userRepository.login(loginData);
    }

    public Single<UserLatLong> setUserLatLong(UserLatLong input, String token){
        return userRepository.setUserLatLong(input, token);
    }

    public Single<UserLatLong> getUserLatLongByReqId(int reqId, boolean isBikerTracking){
        return userRepository.getUserLatLongByReqId(reqId, isBikerTracking);
    }
}
