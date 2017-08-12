package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ${hitesh} on 2/10/2017.
 */

public class AppointmentModel implements Serializable {

    @SerializedName("DataId")
    @Expose
    private int dataId;
    @SerializedName("VetAppointmentId")
    @Expose
    private int vetAppointmentId;
    @SerializedName("AppointmentNo")
    @Expose
    private String appointmentNo;
    @SerializedName("VetId")
    @Expose
    private int vetId;
    @SerializedName("VetName")
    @Expose
    private String vetName;
    @SerializedName("VetUserName")
    @Expose
    private String vetUserName;
    @SerializedName("VetPhoneNo")
    @Expose
    private String vetPhoneNo;
    @SerializedName("VetEmailId")
    @Expose
    private String vetEmailId;
    @SerializedName("VetProfilePic")
    @Expose
    private String vetProfilePic;
    @SerializedName("VetBannerPic")
    @Expose
    private String vetBannerPic;
    @SerializedName("VetPractiseId")
    @Expose
    private int vetPractiseId;
    @SerializedName("VetPractiseName")
    @Expose
    private String vetPractiseName;
    @SerializedName("VetPractiseUserName")
    @Expose
    private String vetPractiseUserName;
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
    private int clientId;
    @SerializedName("ClientName")
    @Expose
    private String clientName;
    @SerializedName("ClientUserName")
    @Expose
    private String clientUserName;
    @SerializedName("ClientPhoneNo")
    @Expose
    private String clientPhoneNo;
    @SerializedName("ClientEmailId")
    @Expose
    private String clientEmailId;
    @SerializedName("ClientProfilePic")
    @Expose
    private String clientProfilePic;
    @SerializedName("ClientBannerPic")
    @Expose
    private String clientBannerPic;
    @SerializedName("ClientPetId")
    @Expose
    private int clientPetId;
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
    @SerializedName("VetTimeSubSlotId")
    @Expose
    private int vetTimeSubSlotId;
    @SerializedName("VetTimeSlotId")
    @Expose
    private int vetTimeSlotId;
    @SerializedName("TimeSlotId")
    @Expose
    private int timeSlotId;
    @SerializedName("TimeSlotName")
    @Expose
    private String timeSlotName;
    @SerializedName("AppointmentDate")
    @Expose
    private String appointmentDate;
    @SerializedName("AppointmentDateDisp")
    @Expose
    private String appointmentDateDisp;
    @SerializedName("AppointmentStatus")
    @Expose
    private int appointmentStatus;
    @SerializedName("AppointmentStatusDisp")
    @Expose
    private String appointmentStatusDisp;
    @SerializedName("RejectReasonId")
    @Expose
    private int rejectReasonId;
    @SerializedName("RejectReasonName")
    @Expose
    private String rejectReasonName;
    @SerializedName("RejectReasonRemarks")
    @Expose
    private String rejectReasonRemarks;
    @SerializedName("FromTime")
    @Expose
    private String fromTime;
    @SerializedName("ToTime")
    @Expose
    private String toTime;
    @SerializedName("IsClientConfirm")
    @Expose
    private int isClientConfirm;
    @SerializedName("ClientConfirmDisp")
    @Expose
    private String clientConfirmDisp;
    @SerializedName("IsVetConfirm")
    @Expose
    private int isVetConfirm;
    @SerializedName("VetConfirmDisp")
    @Expose
    private String vetConfirmDisp;
    @SerializedName("ClientCancelReasonId")
    @Expose
    private int clientCancelReasonId;
    @SerializedName("ClientCancelReasonName")
    @Expose
    private String clientCancelReasonName;
    @SerializedName("ClientCancelReasonRemarks")
    @Expose
    private String clientCancelReasonRemarks;
    @SerializedName("VetCancelReasonId")
    @Expose
    private int vetCancelReasonId;
    @SerializedName("VetCancelReasonName")
    @Expose
    private String vetCancelReasonName;
    @SerializedName("VetCancelReasonRemarks")
    @Expose
    private String vetCancelReasonRemarks;
    @SerializedName("SessionStartOn")
    @Expose
    private String sessionStartOn;
    @SerializedName("SessionEndOn")
    @Expose
    private String sessionEndOn;
    @SerializedName("PaymentAmount")
    @Expose
    private int paymentAmount;
    @SerializedName("AvgRating")
    @Expose
    private float avgRating;
    @SerializedName("Review")
    @Expose
    private String review;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;
    @SerializedName("EndDate")
    @Expose
    private String endDate;
    @SerializedName("RowNo")
    @Expose
    private int rowNo;
    @SerializedName("TotalRowNo")
    @Expose
    private int totalRowNo;
    @SerializedName("TotalPage")
    @Expose
    private int totalPage;


    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getVetAppointmentId() {
        return vetAppointmentId;
    }

    public void setVetAppointmentId(int vetAppointmentId) {
        this.vetAppointmentId = vetAppointmentId;
    }

    public String getAppointmentNo() {
        return appointmentNo;
    }

    public void setAppointmentNo(String appointmentNo) {
        this.appointmentNo = appointmentNo;
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

    public String getVetUserName() {
        return vetUserName;
    }

    public void setVetUserName(String vetUserName) {
        this.vetUserName = vetUserName;
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

    public int getVetPractiseId() {
        return vetPractiseId;
    }

    public void setVetPractiseId(int vetPractiseId) {
        this.vetPractiseId = vetPractiseId;
    }

    public String getVetPractiseName() {
        return vetPractiseName;
    }

    public void setVetPractiseName(String vetPractiseName) {
        this.vetPractiseName = vetPractiseName;
    }

    public String getVetPractiseUserName() {
        return vetPractiseUserName;
    }

    public void setVetPractiseUserName(String vetPractiseUserName) {
        this.vetPractiseUserName = vetPractiseUserName;
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

    public String getClientUserName() {
        return clientUserName;
    }

    public void setClientUserName(String clientUserName) {
        this.clientUserName = clientUserName;
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

    public int getClientPetId() {
        return clientPetId;
    }

    public void setClientPetId(int clientPetId) {
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

    public int getVetTimeSubSlotId() {
        return vetTimeSubSlotId;
    }

    public void setVetTimeSubSlotId(int vetTimeSubSlotId) {
        this.vetTimeSubSlotId = vetTimeSubSlotId;
    }

    public int getVetTimeSlotId() {
        return vetTimeSlotId;
    }

    public void setVetTimeSlotId(int vetTimeSlotId) {
        this.vetTimeSlotId = vetTimeSlotId;
    }

    public int getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(int timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public String getTimeSlotName() {
        return timeSlotName;
    }

    public void setTimeSlotName(String timeSlotName) {
        this.timeSlotName = timeSlotName;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentDateDisp() {
        return appointmentDateDisp;
    }

    public void setAppointmentDateDisp(String appointmentDateDisp) {
        this.appointmentDateDisp = appointmentDateDisp;
    }

    public int getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(int appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getAppointmentStatusDisp() {
        return appointmentStatusDisp;
    }

    public void setAppointmentStatusDisp(String appointmentStatusDisp) {
        this.appointmentStatusDisp = appointmentStatusDisp;
    }

    public int getRejectReasonId() {
        return rejectReasonId;
    }

    public void setRejectReasonId(int rejectReasonId) {
        this.rejectReasonId = rejectReasonId;
    }

    public String getRejectReasonName() {
        return rejectReasonName;
    }

    public void setRejectReasonName(String rejectReasonName) {
        this.rejectReasonName = rejectReasonName;
    }

    public String getRejectReasonRemarks() {
        return rejectReasonRemarks;
    }

    public void setRejectReasonRemarks(String rejectReasonRemarks) {
        this.rejectReasonRemarks = rejectReasonRemarks;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public int getIsClientConfirm() {
        return isClientConfirm;
    }

    public void setIsClientConfirm(int isClientConfirm) {
        this.isClientConfirm = isClientConfirm;
    }

    public String getClientConfirmDisp() {
        return clientConfirmDisp;
    }

    public void setClientConfirmDisp(String clientConfirmDisp) {
        this.clientConfirmDisp = clientConfirmDisp;
    }

    public int getIsVetConfirm() {
        return isVetConfirm;
    }

    public void setIsVetConfirm(int isVetConfirm) {
        this.isVetConfirm = isVetConfirm;
    }

    public String getVetConfirmDisp() {
        return vetConfirmDisp;
    }

    public void setVetConfirmDisp(String vetConfirmDisp) {
        this.vetConfirmDisp = vetConfirmDisp;
    }

    public int getClientCancelReasonId() {
        return clientCancelReasonId;
    }

    public void setClientCancelReasonId(int clientCancelReasonId) {
        this.clientCancelReasonId = clientCancelReasonId;
    }

    public String getClientCancelReasonName() {
        return clientCancelReasonName;
    }

    public void setClientCancelReasonName(String clientCancelReasonName) {
        this.clientCancelReasonName = clientCancelReasonName;
    }

    public String getClientCancelReasonRemarks() {
        return clientCancelReasonRemarks;
    }

    public void setClientCancelReasonRemarks(String clientCancelReasonRemarks) {
        this.clientCancelReasonRemarks = clientCancelReasonRemarks;
    }

    public int getVetCancelReasonId() {
        return vetCancelReasonId;
    }

    public void setVetCancelReasonId(int vetCancelReasonId) {
        this.vetCancelReasonId = vetCancelReasonId;
    }

    public String getVetCancelReasonName() {
        return vetCancelReasonName;
    }

    public void setVetCancelReasonName(String vetCancelReasonName) {
        this.vetCancelReasonName = vetCancelReasonName;
    }

    public String getVetCancelReasonRemarks() {
        return vetCancelReasonRemarks;
    }

    public void setVetCancelReasonRemarks(String vetCancelReasonRemarks) {
        this.vetCancelReasonRemarks = vetCancelReasonRemarks;
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

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
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

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public int getTotalRowNo() {
        return totalRowNo;
    }

    public void setTotalRowNo(int totalRowNo) {
        this.totalRowNo = totalRowNo;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}

