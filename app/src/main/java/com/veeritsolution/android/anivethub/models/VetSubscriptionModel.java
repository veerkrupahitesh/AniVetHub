package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ${hitesh} on 2/1/2017.
 */

public class VetSubscriptionModel implements Serializable {

    @SerializedName("VetSubscriptionId")
    @Expose
    private int vetSubscriptionId;
    @SerializedName("VetId")
    @Expose
    private int vetId;
    @SerializedName("SessionBuy")
    @Expose
    private int sessionBuy;
    @SerializedName("PaymentAmount")
    @Expose
    private int paymentAmount;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;
    @SerializedName("EndDate")
    @Expose
    private String endDate;

    public int getVetSubscriptionId() {
        return vetSubscriptionId;
    }

    public void setVetSubscriptionId(int vetSubscriptionId) {
        this.vetSubscriptionId = vetSubscriptionId;
    }

    public int getVetId() {
        return vetId;
    }

    public void setVetId(int vetId) {
        this.vetId = vetId;
    }

    public int getSessionBuy() {
        return sessionBuy;
    }

    public void setSessionBuy(int sessionBuy) {
        this.sessionBuy = sessionBuy;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
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

