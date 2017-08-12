package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ${hitesh} on 2/11/2017.
 */

public class ReviewRatingModel implements Serializable {

    @SerializedName("RowNo")
    @Expose
    private int rowNo;
    @SerializedName("VetId")
    @Expose
    private int vetId;
    @SerializedName("ClientId")
    @Expose
    private int clientId;
    @SerializedName("ClientName")
    @Expose
    private String clientName;
    @SerializedName("AvgRating")
    @Expose
    private float avgRating;
    @SerializedName("Review")
    @Expose
    private String review;
    @SerializedName("Client_ProfilePic")
    @Expose
    private String clientProfilePic;
    @SerializedName("Client_BannerPic")
    @Expose
    private String clientBannerPic;
    @SerializedName("TotalRowNo")
    @Expose
    private int totalRowNo;
    @SerializedName("TotalPage")
    @Expose
    private int totalPage;

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public int getVetId() {
        return vetId;
    }

    public void setVetId(int vetId) {
        this.vetId = vetId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getClientProfilePic() {
        return clientProfilePic;
    }

    public void setClientProfilePic(String clientProfilePic) {
        this.clientProfilePic = clientProfilePic;
    }

    public String getClientBannerPic() {
        return clientBannerPic;
    }

    public void setClientBannerPic(String clientBannerPic) {
        this.clientBannerPic = clientBannerPic;
    }

    public int getTotalRowNo() {
        return totalRowNo;
    }

    public void setTotalRowNo(int totalRowNo) {
        this.totalRowNo = totalRowNo;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}

