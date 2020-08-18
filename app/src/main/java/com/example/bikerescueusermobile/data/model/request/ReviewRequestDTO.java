package com.example.bikerescueusermobile.data.model.request;

import com.example.bikerescueusermobile.data.model.user.User;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

@Data
public class ReviewRequestDTO {

    @SerializedName("reviewComment")
    private String reviewComment;

    @SerializedName("reviewRating")
    private Double reviewRating;

    @SerializedName("reviewUpdateDate")
    private Timestamp reviewUpdateDate;

    @SerializedName("listReqShopService")
    List<RequestShopService> listReqShopService;

    @SerializedName("created")
    private User created;

    public ReviewRequestDTO(String reviewComment, double reviewRating) {
        this.reviewComment = reviewComment;
        this.reviewRating = reviewRating;
    }

    public ReviewRequestDTO() {
    }
}
