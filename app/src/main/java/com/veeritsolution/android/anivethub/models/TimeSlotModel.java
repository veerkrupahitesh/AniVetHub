package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Jaymin on 2/11/2017.
 */

public class TimeSlotModel implements Serializable {
    @SerializedName("TimeSlotId")
    @Expose
    private int timeSlotId;
    @SerializedName("TimeSlotName")
    @Expose
    private String timeSlotName;

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

}
