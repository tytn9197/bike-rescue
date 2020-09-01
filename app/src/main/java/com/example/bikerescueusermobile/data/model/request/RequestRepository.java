package com.example.bikerescueusermobile.data.model.request;

import com.example.bikerescueusermobile.data.model.user.CurrentUser;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import okhttp3.MultipartBody;

public class RequestRepository {
    private final IRequestService iRequestService;

    @Inject
    public RequestRepository(IRequestService iRequestService) {
        this.iRequestService = iRequestService;
    }

    //function
    public Single<List<Request>> getRequestByShopId(String token, int shopId, String from, String to, String status) {
        return iRequestService.getRequestByShopId(token, shopId, from, to, status);
    }

    public Single<Response<RequestDTO>> createRequest(RequestDTO request, List<MultipartBody.Part> listImg) {
        return iRequestService.createRequest(CurrentUser.getInstance().getAccessToken(), request,  listImg);
    }

    public Single<Request> getRequestById(int reqId) {
        return iRequestService.getRequestById(reqId, CurrentUser.getInstance().getAccessToken());
    }

    public Single<Boolean> cancleRequest(int reqID, boolean isSendToShop, String reason) {
        return iRequestService.cancleRequest(reqID, isSendToShop, reason, CurrentUser.getInstance().getAccessToken());
    }

    public Single<Response<RequestDTO>> updateStatusRequest(int reqId, String status) {
        return iRequestService.updateStatusRequest(reqId, status, CurrentUser.getInstance().getAccessToken());
    }

    public Single<Boolean> finishedRequest(int reqID, double price) {
        return iRequestService.finishedRequest(reqID, price, CurrentUser.getInstance().getAccessToken());
    }

    public Single<List<Request>> getRequestByBikerId(int bikerId, String from, String to, String status) {
        return iRequestService.getRequestByBikerId(bikerId, from, to, status, CurrentUser.getInstance().getAccessToken());
    }

    public Single<ReviewRequestDTO> reviewRequest(int requestId, ReviewRequestDTO reviewRequestDTO){
        return iRequestService.reviewRequest(requestId, reviewRequestDTO, CurrentUser.getInstance().getAccessToken());
    }

    public Single<List<RequestImg>> getReqImgByReqId(int reqId){
        return iRequestService.getReqImgByReqId(reqId, CurrentUser.getInstance().getAccessToken());
    }

    public Single<Boolean> rejectRequest(int reqId, String reason){
        return iRequestService.rejectRequest(reqId, reason, CurrentUser.getInstance().getAccessToken());
    }
}
