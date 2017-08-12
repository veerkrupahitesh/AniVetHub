package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 12/31/2016.
 */

public class PetSymptomsModel implements Serializable {

    @SerializedName("SymptomsId")
    @Expose
    private long symptomsId;
    @SerializedName("SymptomsName")
    @Expose
    private String symptomsName;

    public long getSymptomsId() {
        return symptomsId;
    }

    public void setSymptomsId(long symptomsId) {
        this.symptomsId = symptomsId;
    }

    public String getSymptomsName() {
        return symptomsName;
    }

    public void setSymptomsName(String symptomsName) {
        this.symptomsName = symptomsName;
    }
}
