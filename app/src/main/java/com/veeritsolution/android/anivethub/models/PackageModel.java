package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ${hitesh} on 2/2/2017.
 */

public class PackageModel implements Serializable {

    @SerializedName("VetCredit")
    @Expose
    private List<VetCredit> vetCredit = null;
    @SerializedName("PackageSettings")
    @Expose
    private List<PackageSetting> packageSettings = null;

    public List<VetCredit> getVetCredit() {
        return vetCredit;
    }

    public void setVetCredit(List<VetCredit> vetCredit) {
        this.vetCredit = vetCredit;
    }

    public List<PackageSetting> getPackageSettings() {
        return packageSettings;
    }

    public void setPackageSettings(List<PackageSetting> packageSettings) {
        this.packageSettings = packageSettings;
    }

}