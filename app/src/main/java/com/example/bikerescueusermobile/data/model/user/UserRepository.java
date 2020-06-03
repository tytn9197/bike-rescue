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

    public Single<User> updateFCM(String token, int userId, UpdateDevice updateDevice){
        return userService.updateFCM(token,userId,updateDevice);
    }

//    public Single<User> updateUser(String token, int userId, MultipartBody.Part file){
//        return  userService.updateUser(token,userId,file);
//    }

    public Single<User> updateUser(String token, int userId, MultipartBody.Part file, RequestBody fullname, RequestBody email, RequestBody address, RequestBody phoneNumber){
        return  userService.updateUser(token,userId,file,fullname,email,address,phoneNumber);
    }


    public Single<User> createUser(MultipartBody.Part file, RequestBody username, RequestBody password, RequestBody fullname, RequestBody phoneNumber){
        return  userService.createUser(file,username,password,fullname,phoneNumber);
    }

//    public Single<List<Case>> getHistoryCasesByUserId(int userId){
//        return userService.getHistoryCaseByUserId(userId);
//    }
}
