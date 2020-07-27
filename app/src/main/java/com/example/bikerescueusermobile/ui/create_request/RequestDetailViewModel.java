package com.example.bikerescueusermobile.ui.create_request;

import androidx.lifecycle.ViewModel;

import com.example.bikerescueusermobile.data.model.request.Request;
import com.example.bikerescueusermobile.data.model.request.RequestDTO;
import com.example.bikerescueusermobile.data.model.request.RequestRepository;
import com.example.bikerescueusermobile.data.model.request.Response;

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

    public Single<Boolean> cancleRequest(int reqID) {
        return requestRepository.cancleRequest(reqID);
    }

    public Single<Response<RequestDTO>> updateStatusRequest(int reqId, boolean isAccept) {
        return requestRepository.updateStatusRequest(reqId, isAccept);
    }

    public Single<Boolean> finishedRequest(int reqId){
        return requestRepository.finishedRequest(reqId);
    }
}