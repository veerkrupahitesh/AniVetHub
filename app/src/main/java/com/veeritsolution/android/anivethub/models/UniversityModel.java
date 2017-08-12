package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ${Hitesh} on 4/1/2017.
 */

public class UniversityModel implements Serializable {

    @SerializedName("DataId")
    @Expose
    private int dataId;
    @SerializedName("UniversityId")
    @Expose
    private int universityId;
    @SerializedName("UniversityName")
    @Expose
    private String universityName;


    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getUniversityId() {
        return universityId;
    }

    public void setUniversityId(int universityId) {
        this.universityId = universityId;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }
}
