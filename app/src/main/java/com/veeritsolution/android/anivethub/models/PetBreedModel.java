package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 3/14/2017.
 */

public class PetBreedModel implements Serializable {

    @SerializedName("DataId")
    @Expose
    private int dataId;
    @SerializedName("PetBreedId")
    @Expose
    private int petBreedId;
    @SerializedName("PetBreedName")
    @Expose
    private String petBreedName;

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getPetBreedId() {
        return petBreedId;
    }

    public void setPetBreedId(int petBreedId) {
        this.petBreedId = petBreedId;
    }

    public String getPetBreedName() {
        return petBreedName;
    }

    public void setPetBreedName(String petBreedName) {
        this.petBreedName = petBreedName;
    }
}
