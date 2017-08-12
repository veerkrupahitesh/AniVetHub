package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 3/30/2017.
 */

public class VetPracticeUserModel implements Serializable{
    @SerializedName("DataId")
    @Expose
    private int dataId;
    @SerializedName("VetPractiseId")
    @Expose
    private int vetPractiseId;
    @SerializedName("VetId")
    @Expose
    private int vetId;
    @SerializedName("VetPractiseName")
    @Expose
    private String vetPractiseName;
    @SerializedName("VetName")
    @Expose
    private String vetName;

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getVetPractiseId() {
        return vetPractiseId;
    }

    public void setVetPractiseId(int vetPractiseId) {
        this.vetPractiseId = vetPractiseId;
    }

    public int getVetId() {
        return vetId;
    }

    public void setVetId(int vetId) {
        this.vetId = vetId;
    }

    public String getVetPractiseName() {
        return vetPractiseName;
    }

    public void setVetPractiseName(String vetPractiseName) {
        this.vetPractiseName = vetPractiseName;
    }

    public String getVetName() {
        return vetName;
    }

    public void setVetName(String vetName) {
        this.vetName = vetName;
    }
}
