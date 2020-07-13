package com.example.bikerescueusermobile.ui.update_info;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.data.model.user.User;
import com.example.bikerescueusermobile.data.model.user.UserRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class UpdateViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    @Inject
    public UpdateViewModel(UserRepository userRepository) {
        loading.setValue(true);
        this.userRepository = userRepository;
    }

    public Single<User> updateInfo(int id, User user) {
        loading.setValue(true);
        return userRepository.updateInfo(CurrentUser.getInstance().getAccessToken(), id, user);
    }
}
