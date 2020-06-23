package com.example.bikerescueusermobile.ui.register;

import androidx.lifecycle.ViewModel;

import com.example.bikerescueusermobile.data.model.login.LoginData;
import com.example.bikerescueusermobile.data.model.user.User;
import com.example.bikerescueusermobile.data.model.user.UserRepository;

import javax.inject.Inject;

import io.reactivex.Single;

public class RegisterViewModel extends ViewModel {
    private final UserRepository userRepository;

    @Inject
    public RegisterViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Single<User> register(User user, String roleName){
        return userRepository.register(user, roleName);
    }

}
