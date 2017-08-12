package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 3/1/2017.
 */

public class TreatmentModel implements Serializable {

    @SerializedName("ClientPetTreatmentId")
    @Expose
    private int clientPetTreatmentId;
    @SerializedName("ClientPetId")
    @Expose
    private int clientPetId;
    @SerializedName("SymptomsId")
    @Expose
    private int symptomsId;
    @SerializedName("SymptomsName")
    @Expose
    private String symptomsName;
    @SerializedName("Treatment")
    @Expose
    private String treatment;
    @SerializedName("FromDate")
    @Expose
    private String fromDate;
    @SerializedName("ToDate")
    @Expose
    private String toDate;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;

    public int getClientPetTreatmentId() {
        return clientPetTreatmentId;
    }

    public void setClientPetTreatmentId(int clientPetTreatmentId) {
        this.clientPetTreatmentId = clientPetTreatmentId;
    }

    public int getClientPetId() {
        return clientPetId;
    }

    public void setClientPetId(int clientPetId) {
        this.clientPetId = clientPetId;
    }

    public int getSymptomsId() {
        return symptomsId;
    }

    public void setSymptomsId(int symptomsId) {
        this.symptomsId = symptomsId;
    }

    public String getSymptomsName() {
        return symptomsName;
    }

    public void setSymptomsName(String symptomsName) {
        this.symptomsName = symptomsName;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
}
