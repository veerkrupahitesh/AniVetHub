package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ${hitesh} on 2/2/2017.
 */

public class PackageSetting implements Serializable {

    @SerializedName("PackageSettingId")
    @Expose
    private int packageSettingId;
    @SerializedName("PackageName")
    @Expose
    private String packageName;
    @SerializedName("SessionCount")
    @Expose
    private int sessionCount;
    @SerializedName("PackageAmount")
    @Expose
    private int packageAmount;
    @SerializedName("ColorCode")
    @Expose
    private String colorCode;


    public int getPackageSettingId() {
        return packageSettingId;
    }

    public void setPackageSettingId(int packageSettingId) {
        this.packageSettingId = packageSettingId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(int sessionCount) {
        this.sessionCount = sessionCount;
    }

    public int getPackageAmount() {
        return packageAmount;
    }

    public void setPackageAmount(int packageAmount) {
        this.packageAmount = packageAmount;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}
