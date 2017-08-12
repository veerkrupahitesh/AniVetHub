package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 3/22/2017.
 */

public class ClientCallFeedbackModel implements Serializable {
    @SerializedName("DataId")
    @Expose
    private int dataId;
    @SerializedName("FeedbackTypeId")
    @Expose
    private int feedbackTypeId;
    @SerializedName("FeedbackTypeName")
    @Expose
    private String feedbackTypeName;

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getFeedbackTypeId() {
        return feedbackTypeId;
    }

    public void setFeedbackTypeId(int feedbackTypeId) {
        this.feedbackTypeId = feedbackTypeId;
    }

    public String getFeedbackTypeName() {
        return feedbackTypeName;
    }

    public void setFeedbackTypeName(String feedbackTypeName) {
        this.feedbackTypeName = feedbackTypeName;
    }
}
