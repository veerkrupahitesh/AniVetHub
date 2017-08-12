package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Jaymin on 12/28/2016.
 */

public class VetSpecialization implements Serializable {

    @SerializedName("DataId")
    @Expose
    private int dataId;
    @SerializedName("VetExpertiseId")
    @Expose
    private int vetExpertiseId;
    @SerializedName("VetId")
    @Expose
    private int vetId;
    @SerializedName("PetTypeGroupId")
    @Expose
    private int petTypeGroupId;
    @SerializedName("PetTypeGroupName")
    @Expose
    private String petTypeGroupName;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;
    @SerializedName("EndDate")
    @Expose
    private String endDate;

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getVetExpertiseId() {
        return vetExpertiseId;
    }

    public void setVetExpertiseId(int vetExpertiseId) {
        this.vetExpertiseId = vetExpertiseId;
    }

    public int getVetId() {
        return vetId;
    }

    public void setVetId(int vetId) {
        this.vetId = vetId;
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
