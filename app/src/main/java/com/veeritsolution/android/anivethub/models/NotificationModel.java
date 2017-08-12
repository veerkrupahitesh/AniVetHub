package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by veerk on 3/22/2017.
 */

public class NotificationModel implements Serializable {

    @SerializedName("DataId")
    @Expose
    private int dataId;
    @SerializedName("NotificationId")
    @Expose
    private int notificationId;
    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("AppHeading")
    @Expose
    private String appHeading;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Remarks")
    @Expose
    private String remarks;
    @SerializedName("AppIconPath")
    @Expose
    private String appIconPath;
    @SerializedName("ImagePath")
    @Expose
    private String imagePath;
    @SerializedName("NotificationType")
    @Expose
    private int notificationType;
    @SerializedName("IsSent")
    @Expose
    private int isSent;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;
    @SerializedName("Flag")
    @Expose
    private int flag;
    @SerializedName("RowNo")
    @Expose
    private int rowNo;
    @SerializedName("TotalRowNo")
    @Expose
    private int totalRowNo;
    @SerializedName("TotalPage")
    @Expose
    private int totalPage;
    @SerializedName("VetAppointmentId")
    @Expose
    private int vetAppointmentId;

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppHeading() {
        return appHeading;
    }

    public void setAppHeading(String appHeading) {
        this.appHeading = appHeading;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAppIconPath() {
        return appIconPath;
    }

    public void setAppIconPath(String appIconPath) {
        this.appIconPath = appIconPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    public int getIsSent() {
        return isSent;
    }

    public void setIsSent(int isSent) {
        this.isSent = isSent;
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

    public int getVetAppointmentId() {
        return vetAppointmentId;
    }

    public void setVetAppointmentId(int vetAppointmentId) {
        this.vetAppointmentId = vetAppointmentId;
    }
}


