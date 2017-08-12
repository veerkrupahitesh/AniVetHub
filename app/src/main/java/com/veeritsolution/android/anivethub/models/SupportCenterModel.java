package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by veerk on 3/15/2017.
 */

public class SupportCenterModel implements Serializable {

    @SerializedName("DataId")
    @Expose
    private int dataId;
    @SerializedName("VetSession_TicketId")
    @Expose
    private int vetSessionTicketId;
    @SerializedName("VetSessionId")
    @Expose
    private int vetSessionId;
    @SerializedName("VetSessionNo")
    @Expose
    private String vetSessionNo;
    @SerializedName("IsFreeCall")
    @Expose
    private int isFreeCall;
    @SerializedName("IsRefund")
    @Expose
    private int isRefund;
    @SerializedName("ClientId")
    @Expose
    private int clientId;
    @SerializedName("ClientName")
    @Expose
    private String clientName;
    @SerializedName("VetId")
    @Expose
    private int vetId;
    @SerializedName("VetName")
    @Expose
    private String vetName;
    @SerializedName("LoginId")
    @Expose
    private int loginId;
    @SerializedName("LoginName")
    @Expose
    private String loginName;
    @SerializedName("Remarks")
    @Expose
    private String remarks;
    @SerializedName("ShowToVet")
    @Expose
    private int showToVet;
    @SerializedName("ShowToClient")
    @Expose
    private int showToClient;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;
    @SerializedName("Flag")
    @Expose
    private int flag;


    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getVetSessionTicketId() {
        return vetSessionTicketId;
    }

    public void setVetSessionTicketId(int vetSessionTicketId) {
        this.vetSessionTicketId = vetSessionTicketId;
    }

    public int getVetSessionId() {
        return vetSessionId;
    }

    public void setVetSessionId(int vetSessionId) {
        this.vetSessionId = vetSessionId;
    }

    public String getVetSessionNo() {
        return vetSessionNo;
    }

    public void setVetSessionNo(String vetSessionNo) {
        this.vetSessionNo = vetSessionNo;
    }

    public int getIsFreeCall() {
        return isFreeCall;
    }

    public void setIsFreeCall(int isFreeCall) {
        this.isFreeCall = isFreeCall;
    }

    public int getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(int isRefund) {
        this.isRefund = isRefund;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getVetId() {
        return vetId;
    }

    public void setVetId(int vetId) {
        this.vetId = vetId;
    }

    public String getVetName() {
        return vetName;
    }

    public void setVetName(String vetName) {
        this.vetName = vetName;
    }

    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getShowToVet() {
        return showToVet;
    }

    public void setShowToVet(int showToVet) {
        this.showToVet = showToVet;
    }

    public int getShowToClient() {
        return showToClient;
    }

    public void setShowToClient(int showToClient) {
        this.showToClient = showToClient;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}


