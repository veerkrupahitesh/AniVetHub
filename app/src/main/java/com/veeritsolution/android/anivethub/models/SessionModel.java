package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Jaymin on 1/31/2017.
 */

public class SessionModel implements Serializable {

    @SerializedName("DataId")
    @Expose
    private Integer dataId;
    @SerializedName("VetSessionId")
    @Expose
    private Integer vetSessionId;
    @SerializedName("VetSessionNo")
    @Expose
    private String vetSessionNo;
    @SerializedName("VetId")
    @Expose
    private Integer vetId;
    @SerializedName("VetName")
    @Expose
    private String vetName;
    @SerializedName("VetPhoneNo")
    @Expose
    private String vetPhoneNo;
    @SerializedName("VetEmailId")
    @Expose
    private String vetEmailId;
    @SerializedName("Vet_ProfilePic")
    @Expose
    private String vetProfilePic;
    @SerializedName("Vet_BannerPic")
    @Expose
    private String vetBannerPic;
    @SerializedName("VetPractiseId")
    @Expose
    private Integer vetPractiseId;
    @SerializedName("VetPractiseName")
    @Expose
    private String vetPractiseName;
    @SerializedName("VetPractisePhoneNo")
    @Expose
    private String vetPractisePhoneNo;
    @SerializedName("VetPractiseEmailId")
    @Expose
    private String vetPractiseEmailId;
    @SerializedName("VetPractiseProfilePic")
    @Expose
    private String vetPractiseProfilePic;
    @SerializedName("VetPractiseBannerPic")
    @Expose
    private String vetPractiseBannerPic;
    @SerializedName("ClientId")
    @Expose
    private Integer clientId;
    @SerializedName("ClientName")
    @Expose
    private String clientName;
    @SerializedName("ClientPhoneNo")
    @Expose
    private String clientPhoneNo;
    @SerializedName("ClientEmailId")
    @Expose
    private String clientEmailId;
    @SerializedName("Client_ProfilePic")
    @Expose
    private String clientProfilePic;
    @SerializedName("Client_BannerPic")
    @Expose
    private String clientBannerPic;
    @SerializedName("ClientPetId")
    @Expose
    private Integer clientPetId;
    @SerializedName("PetName")
    @Expose
    private String petName;
    @SerializedName("PetTypeName")
    @Expose
    private String petTypeName;
    @SerializedName("PetBreedName")
    @Expose
    private String petBreedName;
    @SerializedName("SymptomsDescription")
    @Expose
    private String symptomsDescription;
    @SerializedName("SessionStartOn")
    @Expose
    private String sessionStartOn;
    @SerializedName("SessionEndOn")
    @Expose
    private String sessionEndOn;
    @SerializedName("PaymentAmount")
    @Expose
    private Double paymentAmount;
    @SerializedName("AvgRating")
    @Expose
    private Double avgRating;
    @SerializedName("Review")
    @Expose
    private String review;
    @SerializedName("VideoConsultSummary")
    @Expose
    private String videoConsultSummary;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;
    @SerializedName("EndDate")
    @Expose
    private String endDate;
    @SerializedName("VetAppointmentId")
    @Expose
    private Integer vetAppointmentId;
    @SerializedName("AppointmentNo")
    @Expose
    private String appointmentNo;
    @SerializedName("PaymentTime")
    @Expose
    private String paymentTime;
    @SerializedName("PaymentId")
    @Expose
    private String paymentId;
    @SerializedName("PaymentStatus")
    @Expose
    private String paymentStatus;
    @SerializedName("PaymentResponse")
    @Expose
    private String paymentResponse;
    @SerializedName("TicketStatus")
    @Expose
    private Integer ticketStatus;
    @SerializedName("RowNo")
    @Expose
    private Integer rowNo;
    @SerializedName("TotalRowNo")
    @Expose
    private Integer totalRowNo;
    @SerializedName("TotalPage")
    @Expose
    private Integer totalPage;

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public Integer getVetSessionId() {
        return vetSessionId;
    }

    public void setVetSessionId(Integer vetSessionId) {
        this.vetSessionId = vetSessionId;
    }

    public String getVetSessionNo() {
        return vetSessionNo;
    }

    public void setVetSessionNo(String vetSessionNo) {
        this.vetSessionNo = vetSessionNo;
    }

    public Integer getVetId() {
        return vetId;
    }

    public void setVetId(Integer vetId) {
        this.vetId = vetId;
    }

    public String getVetName() {
        return vetName;
    }

    public void setVetName(String vetName) {
        this.vetName = vetName;
    }

    public String getVetPhoneNo() {
        return vetPhoneNo;
    }

    public void setVetPhoneNo(String vetPhoneNo) {
        this.vetPhoneNo = vetPhoneNo;
    }

    public String getVetEmailId() {
        return vetEmailId;
    }

    public void setVetEmailId(String vetEmailId) {
        this.vetEmailId = vetEmailId;
    }

    public String getVetProfilePic() {
        return vetProfilePic;
    }

    public void setVetProfilePic(String vetProfilePic) {
        this.vetProfilePic = vetProfilePic;
    }

    public String getVetBannerPic() {
        return vetBannerPic;
    }

    public void setVetBannerPic(String vetBannerPic) {
        this.vetBannerPic = vetBannerPic;
    }

    public Integer getVetPractiseId() {
        return vetPractiseId;
    }

    public void setVetPractiseId(Integer vetPractiseId) {
        this.vetPractiseId = vetPractiseId;
    }

    public String getVetPractiseName() {
        return vetPractiseName;
    }

    public void setVetPractiseName(String vetPractiseName) {
        this.vetPractiseName = vetPractiseName;
    }

    public String getVetPractisePhoneNo() {
        return vetPractisePhoneNo;
    }

    public void setVetPractisePhoneNo(String vetPractisePhoneNo) {
        this.vetPractisePhoneNo = vetPractisePhoneNo;
    }

    public String getVetPractiseEmailId() {
        return vetPractiseEmailId;
    }

    public void setVetPractiseEmailId(String vetPractiseEmailId) {
        this.vetPractiseEmailId = vetPractiseEmailId;
    }

    public String getVetPractiseProfilePic() {
        return vetPractiseProfilePic;
    }

    public void setVetPractiseProfilePic(String vetPractiseProfilePic) {
        this.vetPractiseProfilePic = vetPractiseProfilePic;
    }

    public String getVetPractiseBannerPic() {
        return vetPractiseBannerPic;
    }

    public void setVetPractiseBannerPic(String vetPractiseBannerPic) {
        this.vetPractiseBannerPic = vetPractiseBannerPic;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhoneNo() {
        return clientPhoneNo;
    }

    public void setClientPhoneNo(String clientPhoneNo) {
        this.clientPhoneNo = clientPhoneNo;
    }

    public String getClientEmailId() {
        return clientEmailId;
    }

    public void setClientEmailId(String clientEmailId) {
        this.clientEmailId = clientEmailId;
    }

    public String getClientProfilePic() {
        return clientProfilePic;
    }

    public void setClientProfilePic(String clientProfilePic) {
        this.clientProfilePic = clientProfilePic;
    }

    public String getClientBannerPic() {
        return clientBannerPic;
    }

    public void setClientBannerPic(String clientBannerPic) {
        this.clientBannerPic = clientBannerPic;
    }

    public Integer getClientPetId() {
        return clientPetId;
    }

    public void setClientPetId(Integer clientPetId) {
        this.clientPetId = clientPetId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetTypeName() {
        return petTypeName;
    }

    public void setPetTypeName(String petTypeName) {
        this.petTypeName = petTypeName;
    }

    public String getPetBreedName() {
        return petBreedName;
    }

    public void setPetBreedName(String petBreedName) {
        this.petBreedName = petBreedName;
    }

    public String getSymptomsDescription() {
        return symptomsDescription;
    }

    public void setSymptomsDescription(String symptomsDescription) {
        this.symptomsDescription = symptomsDescription;
    }

    public String getSessionStartOn() {
        return sessionStartOn;
    }

    public void setSessionStartOn(String sessionStartOn) {
        this.sessionStartOn = sessionStartOn;
    }

    public String getSessionEndOn() {
        return sessionEndOn;
    }

    public void setSessionEndOn(String sessionEndOn) {
        this.sessionEndOn = sessionEndOn;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getVideoConsultSummary() {
        return videoConsultSummary;
    }

    public void setVideoConsultSummary(String videoConsultSummary) {
        this.videoConsultSummary = videoConsultSummary;
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

    public Integer getVetAppointmentId() {
        return vetAppointmentId;
    }

    public void setVetAppointmentId(Integer vetAppointmentId) {
        this.vetAppointmentId = vetAppointmentId;
    }

    public String getAppointmentNo() {
        return appointmentNo;
    }

    public void setAppointmentNo(String appointmentNo) {
        this.appointmentNo = appointmentNo;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentResponse() {
        return paymentResponse;
    }

    public void setPaymentResponse(String paymentResponse) {
        this.paymentResponse = paymentResponse;
    }

    public Integer getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(Integer ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public Integer getRowNo() {
        return rowNo;
    }

    public void setRowNo(Integer rowNo) {
        this.rowNo = rowNo;
    }

    public Integer getTotalRowNo() {
        return totalRowNo;
    }

    public void setTotalRowNo(Integer totalRowNo) {
        this.totalRowNo = totalRowNo;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

}


