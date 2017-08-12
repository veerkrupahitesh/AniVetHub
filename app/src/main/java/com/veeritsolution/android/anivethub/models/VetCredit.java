package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ${hitesh} on 2/2/2017.
 */

public class VetCredit implements Serializable {

    @SerializedName("VetId")
    @Expose
    private int vetId;
    @SerializedName("SessionCredit")
    @Expose
    private int sessionCredit;

    public int getVetId() {
        return vetId;
    }

    public void setVetId(int vetId) {
        this.vetId = vetId;
    }

    public int getSessionCredit() {
        return sessionCredit;
    }

    public void setSessionCredit(int sessionCredit) {
        this.sessionCredit = sessionCredit;
    }
}
