package com.example.bikerescueusermobile.ui.history;

import androidx.lifecycle.ViewModel;

import com.example.bikerescueusermobile.data.model.request.Request;
import com.example.bikerescueusermobile.data.model.request.RequestRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class HistoryViewModel extends ViewModel {

    private final RequestRepository requestRepository;

    @Inject
    public HistoryViewModel(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public Single<List<Request>> getRequestByBikerId(int bikerId) {
        return requestRepository.getRequestByBikerId(bikerId);
    }

}
