package com.example.bikerescueusermobile.data.model.complain;

import com.example.bikerescueusermobile.data.model.user.CurrentUser;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import okhttp3.MultipartBody;

public class ComplainRepository {
    private final ComplainService complainService;

    @Inject
    public ComplainRepository(ComplainService complainService) {
        this.complainService = complainService;
    }

    public Single<Boolean> sendComplain(Complain complain, List<MultipartBody.Part> listImg){
        return complainService.sendComplain(complain, listImg, CurrentUser.getInstance().getAccessToken());
    }
}
