package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Jaymin on 2/13/2017.
 */

public class AppointmentRejectModel implements Serializable {

    @SerializedName("RejectReasonId")
    @Expose
    private int rejectReasonId;
    @SerializedName("RejectReasonName")
    @Expose
    private String rejectReasonName;

    private String rejectionRemarks;

    public int getRejectReasonId() {
        return rejectReasonId;
    }

    public void setRejectReasonId(int rejectReasonId) {
        this.rejectReasonId = rejectReasonId;
    }

    public String getRejectReasonName() {
        return rejectReasonName;
    }

    public void setRejectReasonName(String rejectReasonName) {
        this.rejectReasonName = rejectReasonName;
    }

    public String getRejectionRemarks() {
        return rejectionRemarks;
    }

    public void setRejectionRemarks(String rejectionRemarks) {
        this.rejectionRemarks = rejectionRemarks;
    }
}
