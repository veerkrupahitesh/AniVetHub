package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ${hitesh} on 3/3/2017.
 */

public class PetWeightModel implements Serializable {

    @SerializedName("ClientPetWeightHistoryId")
    @Expose
    private int clientPetWeightHistoryId;
    @SerializedName("ClientPetId")
    @Expose
    private int clientPetId;
    @SerializedName("Weight")
    @Expose
    private float weight;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;
    @SerializedName("CreatedOn_Date")
    @Expose
    private String CreatedOnDate;

    public int getClientPetWeightHistoryId() {
        return clientPetWeightHistoryId;
    }

    public void setClientPetWeightHistoryId(int clientPetWeightHistoryId) {
        this.clientPetWeightHistoryId = clientPetWeightHistoryId;
    }

    public int getClientPetId() {
        return clientPetId;
    }

    public void setClientPetId(int clientPetId) {
        this.clientPetId = clientPetId;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedOnDate() {
        return CreatedOnDate;
    }

    public void setCreatedOnDate(String createdOnDate) {
        CreatedOnDate = createdOnDate;
    }
}



