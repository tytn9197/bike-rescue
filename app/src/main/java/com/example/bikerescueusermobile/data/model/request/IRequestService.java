package com.example.bikerescueusermobile.data.model.request;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IRequestService {

    @GET("/shop/getRequestByShopId/{id}")
    Single<List<Request>> getRequestByShopId(@Header("Authorization") String token, @Path("id") int shopId);

    @POST("/biker/create-request")
    Single<Response<RequestDTO>> createRequest(@Header("Authorization") String token, @Body RequestDTO request);

    @GET("/biker/request/{id}")
    Single<Request> getRequestById(@Path("id") int reqId,@Header("Authorization") String token);

    @GET("/biker/cancleRequest")
    Single<Boolean> cancleRequest(@Query("id") int reqID, @Header("Authorization") String token);

    @GET("/shop/updateStatusRequest")
    Single<Response<RequestDTO>> updateStatusRequest(@Query("idRequest") int reqId,
                                                     @Query("accepted") boolean isAccept,
                                                     @Header("Authorization") String token);

    @GET("/shop/finishedRequest")
    Single<Boolean> finishedRequest(@Query("id") int reqID, @Header("Authorization") String token);

    @GET("/biker/getRequestByBikerId/{bikerId}")
    Single<List<Request>> getRequestByBikerId(@Path("bikerId") int bikerId,@Header("Authorization") String token );
}
