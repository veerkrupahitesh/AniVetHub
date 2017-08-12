package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 3/14/2017.
 */

public class PetTypeGroupModel implements Serializable {

    @SerializedName("DataId")
    @Expose
    private int dataId;
    @SerializedName("PetTypeGroupId")
    @Expose
    private int petTypeGroupId;
    @SerializedName("PetTypeGroupName")
    @Expose
    private String petTypeGroupName;

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getPetTypeGroupId() {
        return petTypeGroupId;
    }

    public void setPetTypeGroupId(int petTypeGroupId) {
        this.petTypeGroupId = petTypeGroupId;
    }

    public String getPetTypeGroupName() {
        return petTypeGroupName;
    }

    public void setPetTypeGroupName(String petTypeGroupName) {
        this.petTypeGroupName = petTypeGroupName;
    }

}
