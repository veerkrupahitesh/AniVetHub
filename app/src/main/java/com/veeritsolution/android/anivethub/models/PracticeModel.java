package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by hitesh on 8/22/2017.
 */

public class PracticeModel implements Serializable {


    private int position = 0;

    @SerializedName("DataId")
    @Expose
    private int dataId = 0;
    @SerializedName("VetPractiseId")
    @Expose
    private int vetPractiseId = 0;
    @SerializedName("VetId")
    @Expose
    private int vetId = 0;
    @SerializedName("VetPractiseName")
    @Expose
    private String vetPractiseName = "";
    @SerializedName("VetName")
    @Expose
    private String vetName = "";
    @SerializedName("Flag")
    @Expose
    private int flag = 0;
    @SerializedName("FlagDisp")
    @Expose
    private String flagDisp = "";

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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getFlagDisp() {
        return flagDisp;
    }

    public void setFlagDisp(String flagDisp) {
        this.flagDisp = flagDisp;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
