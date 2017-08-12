package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Jaymin on 12/28/2016.
 */

public class VetExpertise implements Serializable {

    @SerializedName("VetExpertiseId")
    @Expose
    private long vetExpertiseId;
    @SerializedName("VetId")
    @Expose
    private long vetId;
    @SerializedName("PetTypeId")
    @Expose
    private long petTypeId;
    @SerializedName("PetTypeName")
    @Expose
    private String petTypeName;
    @SerializedName("PetBreedId")
    @Expose
    private long petBreedId;
    @SerializedName("PetBreedName")
    @Expose
    private String petBreedName;
    @SerializedName("Proficiency")
    @Expose
    private int proficiency;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;
    @SerializedName("EndDate")
    @Expose
    private String endDate;

    public long getVetExpertiseId() {
        return vetExpertiseId;
    }

    public void setVetExpertiseId(long vetExpertiseId) {
        this.vetExpertiseId = vetExpertiseId;
    }

    public long getVetId() {
        return vetId;
    }

    public void setVetId(long vetId) {
        this.vetId = vetId;
    }

    public long getPetTypeId() {
        return petTypeId;
    }

    public void setPetTypeId(long petTypeId) {
        this.petTypeId = petTypeId;
    }

    public String getPetTypeName() {
        return petTypeName;
    }

    public void setPetTypeName(String petTypeName) {
        this.petTypeName = petTypeName;
    }

    public long getPetBreedId() {
        return petBreedId;
    }

    public void setPetBreedId(long petBreedId) {
        this.petBreedId = petBreedId;
    }

    public String getPetBreedName() {
        return petBreedName;
    }

    public void setPetBreedName(String petBreedName) {
        this.petBreedName = petBreedName;
    }

    public int getProficiency() {
        return proficiency;
    }

    public void setProficiency(int proficiency) {
        this.proficiency = proficiency;
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
