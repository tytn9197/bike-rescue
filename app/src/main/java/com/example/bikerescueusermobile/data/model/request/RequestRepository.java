package com.example.bikerescueusermobile.data.model.request;

import com.example.bikerescueusermobile.data.model.user.CurrentUser;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class RequestRepository {
    private final IRequestService iRequestService;

    @Inject
    public RequestRepository(IRequestService iRequestService) {
        this.iRequestService = iRequestService;
    }

    //function
    public Single<List<Request>> getRequestByShopId(String token, int shopId){
        return iRequestService.getRequestByShopId(token, shopId);
    }

    public Single<Response<RequestDTO>> createRequest(RequestDTO request){
        return iRequestService.createRequest(CurrentUser.getInstance().getAccessToken(), request);
    }

}
