package com.example.bikerescueusermobile.data.model.user;



import com.example.bikerescueusermobile.data.model.login.LoginData;
import com.example.bikerescueusermobile.data.model.login.UpdateDevice;

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
}
