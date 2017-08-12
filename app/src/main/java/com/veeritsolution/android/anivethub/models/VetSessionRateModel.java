package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 3/16/2017.
 */

public class VetSessionRateModel implements Serializable {
    @SerializedName("DataId")
    @Expose
    private int dataId;
    @SerializedName("VetRateId")
    @Expose
    private int vetRateId;
    @SerializedName("VetId")
    @Expose
    private int vetId;
    @SerializedName("DayNo")
    @Expose
    private int dayNo;
    @SerializedName("Normal_FromTime")
    @Expose
    private String normalFromTime;
    @SerializedName("Normal_ToTime")
    @Expose
    private String normalToTime;
    @SerializedName("Normal_SessionRate")
    @Expose
    private int normalSessionRate;
    @SerializedName("Special_FromTime")
    @Expose
    private String specialFromTime;
    @SerializedName("Special_ToTime")
    @Expose
    private String specialToTime;
    @SerializedName("Special_SessionRate")
    @Expose
    private int specialSessionRate;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;
    @SerializedName("EndDate")
    @Expose
    private String endDate;

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getVetRateId() {
        return vetRateId;
    }

    public void setVetRateId(int vetRateId) {
        this.vetRateId = vetRateId;
    }

    public int getVetId() {
        return vetId;
    }

    public void setVetId(int vetId) {
        this.vetId = vetId;
    }

    public int getDayNo() {
        return dayNo;
    }

    public void setDayNo(int dayNo) {
        this.dayNo = dayNo;
    }

    public String getNormalFromTime() {
        return normalFromTime;
    }

    public void setNormalFromTime(String normalFromTime) {
        this.normalFromTime = normalFromTime;
    }

    public String getNormalToTime() {
        return normalToTime;
    }

    public void setNormalToTime(String normalToTime) {
        this.normalToTime = normalToTime;
    }

    public int getNormalSessionRate() {
        return normalSessionRate;
    }

    public void setNormalSessionRate(int normalSessionRate) {
        this.normalSessionRate = normalSessionRate;
    }

    public String getSpecialFromTime() {
        return specialFromTime;
    }

    public void setSpecialFromTime(String specialFromTime) {
        this.specialFromTime = specialFromTime;
    }

    public String getSpecialToTime() {
        return specialToTime;
    }

    public void setSpecialToTime(String specialToTime) {
        this.specialToTime = specialToTime;
    }

    public int getSpecialSessionRate() {
        return specialSessionRate;
    }

    public void setSpecialSessionRate(int specialSessionRate) {
        this.specialSessionRate = specialSessionRate;
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
