package com.example.bikerescueusermobile.data.model.user;



import com.example.bikerescueusermobile.data.model.login.LoginData;
import com.example.bikerescueusermobile.data.model.login.UpdateDevice;
import com.example.bikerescueusermobile.data.model.request.Response;
import com.example.bikerescueusermobile.data.model.shop.StatusSuccessDTO;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserRepository {
    private final IUserService userService;

    @Inject
    public UserRepository(IUserService userService) {
        this.userService = userService;
    }

    public Single<User> login(LoginData loginData){
        return userService.login(loginData);
    }

    public Single<User> register(User user, String roleName){
        return userService.register(user, roleName);
    }

    public Single<User> updateInfo(String token, int userId, User user){
        return userService.updateInfo(token, userId, user);
    }

    public Single<UserLatLong> setUserLatLong(UserLatLong input, String token){
        return userService.setUserLatLong(input, token);
    }

    public Single<UserLatLong> getUserLatLongByReqId(int userId, boolean isBikerTracking){
        return userService.getUserLatLongByReqId(userId, isBikerTracking, CurrentUser.getInstance().getAccessToken());
    }

    public Single<List<StatusSuccessDTO>> getSuccessReq(int id){
        return userService.getSuccessReq(id, CurrentUser.getInstance().getAccessToken());
    }

    public Single<Double> getNumOfStarByShopOwnerId(int id){
        return userService.getNumOfStarByShopOwnerId(id, CurrentUser.getInstance().getAccessToken());
    }

    public Single<Boolean> updateUserStatus(UserStatusDTO userStatusDTO){
        return userService.updateUserStatus(userStatusDTO, CurrentUser.getInstance().getAccessToken());
    }
}
