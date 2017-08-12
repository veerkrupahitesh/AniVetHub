package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ${Hitesh} on 4/10/2017.
 */

public class PetPicsModel implements Serializable {

    @SerializedName("DataId")
    @Expose
    private long dataId;
    @SerializedName("ClientPetPicsId")
    @Expose
    private long clientPetPicsId;
    @SerializedName("ClientPetId")
    @Expose
    private long clientPetId;
    @SerializedName("PicTitle")
    @Expose
    private String picTitle;
    @SerializedName("PicPath")
    @Expose
    private String picPath;
    @SerializedName("CreatedOn")
    @Expose
    private Object createdOn;
    @SerializedName("EndDate")
    @Expose
    private String endDate;


    public long getDataId() {
        return dataId;
    }

    public void setDataId(long dataId) {
        this.dataId = dataId;
    }

    public long getClientPetPicsId() {
        return clientPetPicsId;
    }

    public void setClientPetPicsId(long clientPetPicsId) {
        this.clientPetPicsId = clientPetPicsId;
    }

    public long getClientPetId() {
        return clientPetId;
    }

    public void setClientPetId(long clientPetId) {
        this.clientPetId = clientPetId;
    }

    public String getPicTitle() {
        return picTitle;
    }

    public void setPicTitle(String picTitle) {
        this.picTitle = picTitle;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public Object getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Object createdOn) {
        this.createdOn = createdOn;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}


