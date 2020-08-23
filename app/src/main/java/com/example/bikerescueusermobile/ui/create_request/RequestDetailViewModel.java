package com.example.bikerescueusermobile.ui.create_request;

import androidx.lifecycle.ViewModel;

import com.example.bikerescueusermobile.data.model.request.Request;
import com.example.bikerescueusermobile.data.model.request.RequestDTO;
import com.example.bikerescueusermobile.data.model.request.RequestImg;
import com.example.bikerescueusermobile.data.model.request.RequestRepository;
import com.example.bikerescueusermobile.data.model.request.Response;
import com.example.bikerescueusermobile.data.model.request.ReviewRequestDTO;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class RequestDetailViewModel extends ViewModel {
    private final RequestRepository requestRepository;

    @Inject
    public RequestDetailViewModel(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public Single<Request> getRequestById(int reqId) {
        return requestRepository.getRequestById(reqId);
    }

    public Single<Boolean> cancleRequest(int reqID, boolean isSendToShop, String reason) {
        return requestRepository.cancleRequest(reqID, isSendToShop, reason);
    }

    public Single<Response<RequestDTO>> updateStatusRequest(int reqId, String status) {
        return requestRepository.updateStatusRequest(reqId, status);
    }

    public Single<Boolean> finishedRequest(int reqId, double price){
        return requestRepository.finishedRequest(reqId, price);
    }

    public Single<ReviewRequestDTO> reviewRequest(int requestId, ReviewRequestDTO reviewRequestDTO){
        return requestRepository.reviewRequest(requestId, reviewRequestDTO);
    }

    public Single<List<RequestImg>> getReqImgByReqId(int reqId){
        return requestRepository.getReqImgByReqId(reqId);
    }

    public Single<Boolean> rejectRequest(int reqId, String reason){
        return requestRepository.rejectRequest(reqId, reason);
    }
}
