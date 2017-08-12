package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ${hitesh} on 1/31/2017.
 */
public class VetAccountInfoModel implements Serializable {

    @SerializedName("VetAccountId")
    @Expose
    private int vetAccountId;
    @SerializedName("VetId")
    @Expose
    private int vetId;
    @SerializedName("PaypalAccountId")
    @Expose
    private String paypalAccountId;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;
    @SerializedName("EndDate")
    @Expose
    private String endDate;


    public int getVetAccountId() {
        return vetAccountId;
    }

    public void setVetAccountId(int vetAccountId) {
        this.vetAccountId = vetAccountId;
    }

    public int getVetId() {
        return vetId;
    }

    public void setVetId(int vetId) {
        this.vetId = vetId;
    }

    public String getPaypalAccountId() {
        return paypalAccountId;
    }

    public void setPaypalAccountId(String paypalAccountId) {
        this.paypalAccountId = paypalAccountId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}
