package com.example.bikerescueusermobile.data.model.request;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ReviewRequestDTO {

    @SerializedName("reviewComment")
    private String reviewComment;

    @SerializedName("reviewRating")
    private double reviewRating;

    @SerializedName("reviewUpdateDate")
    private Timestamp reviewUpdateDate;

    public ReviewRequestDTO(String reviewComment, double reviewRating) {
        this.reviewComment = reviewComment;
        this.reviewRating = reviewRating;
    }

    public ReviewRequestDTO() {
    }
}
