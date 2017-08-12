package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ${hitesh} on 2/14/2017.
 */

public class VetTimeSlotModel implements Serializable {

    @SerializedName("VetTimeSlotId")
    @Expose
    private int vetTimeSlotId;
    @SerializedName("VetId")
    @Expose
    private int vetId;
    @SerializedName("TimeSlotId")
    @Expose
    private int timeSlotId;
    @SerializedName("TimeSlotName")
    @Expose
    private String timeSlotName;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;
    @SerializedName("EndDate")
    @Expose
    private String endDate;

    public int getVetTimeSlotId() {
        return vetTimeSlotId;
    }

    public void setVetTimeSlotId(int vetTimeSlotId) {
        this.vetTimeSlotId = vetTimeSlotId;
    }

    public int getVetId() {
        return vetId;
    }

    public void setVetId(int vetId) {
        this.vetId = vetId;
    }

    public int getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(int timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public String getTimeSlotName() {
        return timeSlotName;
    }

    public void setTimeSlotName(String timeSlotName) {
        this.timeSlotName = timeSlotName;
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



