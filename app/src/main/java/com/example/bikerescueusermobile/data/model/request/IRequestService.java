package com.example.bikerescueusermobile.data.model.request;

import com.example.bikerescueusermobile.data.model.user.CurrentUser;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IRequestService {

    @GET("/shop/getRequestByShopOwnerIdFromTo/{shopOwnerId}")
    Single<List<Request>> getRequestByShopId(@Header("Authorization") String token, @Path("shopOwnerId") int shopId, @Query("from") String from, @Query("to") String to);

    @Multipart
    @POST("/biker/create-request")
    Single<Response<RequestDTO>> createRequest(@Header("Authorization") String token, @Part("requestDTO") RequestDTO requestDTO, @Part List<MultipartBody.Part> listImg);

    @GET("/biker/request/{id}")
    Single<Request> getRequestById(@Path("id") int reqId,@Header("Authorization") String token);

    @GET("/biker/cancleRequest")
    Single<Boolean> cancleRequest(@Query("id") int reqID,
                                  @Query("isSendToShop") boolean isSendToShop,
                                  @Query("reason") String reason,
                                  @Header("Authorization") String token);

    @GET("/shop/updateStatusRequest")
    Single<Response<RequestDTO>> updateStatusRequest(@Query("idRequest") int reqId,
                                                     @Query("status") String status,
                                                     @Header("Authorization") String token);

    @GET("/shop/finishedRequest")
    Single<Boolean> finishedRequest(@Query("id") int reqID, @Query("price") double price, @Header("Authorization") String token);

    @GET("/biker/getRequestByBikerIdFromTo/{bikerId}")
    Single<List<Request>> getRequestByBikerId(@Path("bikerId") int bikerId, @Query("from") String fromDate,
                                              @Query("to") String toDate, @Header("Authorization") String token);

    @PUT("/biker/reviewRequest")
    Single<ReviewRequestDTO> reviewRequest(@Query("id") int requestId,
                                           @Body ReviewRequestDTO reviewRequestDTO,
                                           @Header("Authorization") String token);

    @GET("/shop/getReqImgByReqId/{reqId}")
    Single<List<RequestImg>> getReqImgByReqId(@Path("reqId") int reqId, @Header("Authorization") String token);

    @GET("/shop/rejectRequest/{reqId}")
    Single<Boolean> rejectRequest(@Path("reqId") int reqId, @Query("reason") String reason, @Header("Authorization") String token);
}
