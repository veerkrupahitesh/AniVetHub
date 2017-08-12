package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Jaymin on 12/28/2016.
 */

public class VetEducation implements Serializable {

    @SerializedName("VetEducationId")
    @Expose
    private long vetEducationId;
    @SerializedName("VetId")
    @Expose
    private long vetId;
    @SerializedName("DegreeId")
    @Expose
    private long degreeId;
    @SerializedName("DegreeName")
    @Expose
    private String degreeName;
    @SerializedName("UniversityId")
    @Expose
    private long universityId;
    @SerializedName("UniversityName")
    @Expose
    private String universityName;
    @SerializedName("PassingYear")
    @Expose
    private String passingYear;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;
    @SerializedName("EndDate")
    @Expose
    private String endDate;

    public long getVetEducationId() {
        return vetEducationId;
    }

    public void setVetEducationId(long vetEducationId) {
        this.vetEducationId = vetEducationId;
    }

    public long getVetId() {
        return vetId;
    }

    public void setVetId(long vetId) {
        this.vetId = vetId;
    }

    public long getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(long degreeId) {
        this.degreeId = degreeId;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
    }

    public long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(long universityId) {
        this.universityId = universityId;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getPassingYear() {
        return passingYear;
    }

    public void setPassingYear(String passingYear) {
        this.passingYear = passingYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
