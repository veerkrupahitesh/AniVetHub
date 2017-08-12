package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.utility.Utils;

import java.io.Serializable;

/**
 * Created by ${hitesh} on 1/28/2017.
 */

public class GeneralSettingsModel implements Serializable {

    @SerializedName("GeneralSettingId")
    @Expose
    private long generalSettingId;
    @SerializedName("MinutePerSession")
    @Expose
    private int minutePerSession;
    @SerializedName("FreeSession")
    @Expose
    private int freeSession;
    @SerializedName("TotalSession1")
    @Expose
    private int totalSession1;
    @SerializedName("RatePerTotalSession1")
    @Expose
    private int ratePerTotalSession1;
    @SerializedName("TotalSession2")
    @Expose
    private int totalSession2;
    @SerializedName("RatePerTotalSession2")
    @Expose
    private int ratePerTotalSession2;
    @SerializedName("TotalSession3")
    @Expose
    private int totalSession3;
    @SerializedName("RatePerTotalSession3")
    @Expose
    private int ratePerTotalSession3;

    public static void saveGeneralSettings(GeneralSettingsModel response) {
        PrefHelper.getInstance().setString(PrefHelper.GENERAL_SETTINGS, Utils.objectToString(response));
    }

    public static GeneralSettingsModel getGeneralSettings() {
        return (GeneralSettingsModel) Utils.stringToObject(PrefHelper.getInstance().getString(PrefHelper.GENERAL_SETTINGS, ""));
    }

    public long getGeneralSettingId() {
        return generalSettingId;
    }

    public void setGeneralSettingId(long generalSettingId) {
        this.generalSettingId = generalSettingId;
    }

    public int getMinutePerSession() {
        return minutePerSession;
    }

    public void setMinutePerSession(int minutePerSession) {
        this.minutePerSession = minutePerSession;
    }

    public int getFreeSession() {
        return freeSession;
    }

    public void setFreeSession(int freeSession) {
        this.freeSession = freeSession;
    }

    public int getTotalSession1() {
        return totalSession1;
    }

    public void setTotalSession1(int totalSession1) {
        this.totalSession1 = totalSession1;
    }

    public int getRatePerTotalSession1() {
        return ratePerTotalSession1;
    }

    public void setRatePerTotalSession1(int ratePerTotalSession1) {
        this.ratePerTotalSession1 = ratePerTotalSession1;
    }

    public int getTotalSession2() {
        return totalSession2;
    }

    public void setTotalSession2(int totalSession2) {
        this.totalSession2 = totalSession2;
    }

    public int getRatePerTotalSession2() {
        return ratePerTotalSession2;
    }

    public void setRatePerTotalSession2(int ratePerTotalSession2) {
        this.ratePerTotalSession2 = ratePerTotalSession2;
    }

    public int getTotalSession3() {
        return totalSession3;
    }

    public void setTotalSession3(int totalSession3) {
        this.totalSession3 = totalSession3;
    }

    public int getRatePerTotalSession3() {
        return ratePerTotalSession3;
    }

    public void setRatePerTotalSession3(int ratePerTotalSession3) {
        this.ratePerTotalSession3 = ratePerTotalSession3;
    }
}


