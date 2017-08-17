package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.helper.PrefHelper;

import java.io.Serializable;

/**
 * Created by veerk on 3/27/2017.
 */

public class PractiseLoginModel implements Serializable {

    @SerializedName("IsDataExists")
    @Expose
    private int isDataExists = 0;
    @SerializedName("VetId")
    @Expose
    private int vetId;
    @SerializedName("VetName")
    @Expose
    private String vetName;
    @SerializedName("Address1")
    @Expose
    private String address1;
    @SerializedName("Address2")
    @Expose
    private String address2;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("POBox")
    @Expose
    private String pOBox;
    @SerializedName("State")
    @Expose
    private String state;
    @SerializedName("Country")
    @Expose
    private String country;
    @SerializedName("PhoneNo")
    @Expose
    private String phoneNo;
    @SerializedName("EmailId")
    @Expose
    private String emailId;
    @SerializedName("SessionRate")
    @Expose
    private int sessionRate;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("ProfilePic")
    @Expose
    private String profilePic;
    @SerializedName("BannerPic")
    @Expose
    private String bannerPic;
    @SerializedName("AcTokenId")
    @Expose
    private String acTokenId;
    @SerializedName("IsActive")
    @Expose
    private int isActive;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;
    @SerializedName("EndDate")
    @Expose
    private String endDate;
    @SerializedName("IsVerified")
    @Expose
    private int isVerified;
    @SerializedName("DataId")
    @Expose
    private int dataId;
    @SerializedName("OnlineStatus")
    @Expose
    private int onlineStatus;
    @SerializedName("VetExperience")
    @Expose
    private String vetExpertise;
    @SerializedName("VetEducation")
    @Expose
    private String vetEducation;
    @SerializedName("Rating")
    @Expose
    private int rating;
    @SerializedName("RegisteredBy")
    @Expose
    private String registeredBy;
    @SerializedName("paypalAccountId")
    @Expose
    private String paypalAccountId;
    @SerializedName("sessionCredit")
    @Expose
    private String sessionCredit;
    @SerializedName("totalExp")
    @Expose
    private float totalExp;
    @SerializedName("Biography")
    @Expose
    private String biography;
    @SerializedName("CityId")
    @Expose
    private int cityId;
    @SerializedName("StateId")
    @Expose
    private int stateId;
    @SerializedName("CountryId")
    @Expose
    private int countryId;

    @SerializedName("IsVetPractiseUser")
    @Expose
    private int isVetPractiseUser;
    @SerializedName("IsVetProfile")
    @Expose
    private int isVetProfile;
    @SerializedName("IsVetAccount")
    @Expose
    private int isVetAccount;

    public static void savePractiseCredentials(String loginResponse) {

        PrefHelper.getInstance().setString(PrefHelper.CLINIC_LOGIN_CREDENTIALS, loginResponse);
    }

    public static PractiseLoginModel getPractiseCredentials() {

        return RestClient.getGsonInstance().fromJson(PrefHelper.getInstance().getString(PrefHelper.CLINIC_LOGIN_CREDENTIALS, ""), PractiseLoginModel.class);
    }

    public String getRegisterBy() {
        return registeredBy;
    }

    public void setRegisterBy(String RegisteredBy) {
        this.registeredBy = RegisteredBy;
    }

    public int getIsDataExists() {
        return isDataExists;
    }

    public void setIsDataExists(int isDataExists) {
        this.isDataExists = isDataExists;
    }

    /**
     * @return The vetId
     */
    public int getVetId() {
        return vetId;
    }

    /**
     * @param vetId The VetId
     */
    public void setVetId(int vetId) {
        this.vetId = vetId;
    }

    /**
     * @return The vetName
     */
    public String getVetName() {
        return vetName;
    }

    /**
     * @param vetName The VetName
     */
    public void setVetName(String vetName) {
        this.vetName = vetName;
    }

    /**
     * @return The address1
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * @param address1 The Address1
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * @return The address2
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * @param address2 The Address2
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * @return The city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city The City
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return The pOBox
     */
    public String getPOBox() {
        return pOBox;
    }

    /**
     * @param pOBox The POBox
     */
    public void setPOBox(String pOBox) {
        this.pOBox = pOBox;
    }

    /**
     * @return The state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state The State
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return The country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country The Country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return The phoneNo
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     * @param phoneNo The PhoneNo
     */
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    /**
     * @return The emailId
     */
    public String getEmailId() {
        return emailId;
    }

    /**
     * @param emailId The EmailId
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    /**
     * @return The sessionRate
     */
    public int getSessionRate() {
        return sessionRate;
    }

    /**
     * @param sessionRate The SessionRate
     */
    public void setSessionRate(int sessionRate) {
        this.sessionRate = sessionRate;
    }

    /**
     * @return The userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName The UserName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password The Password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return The profilePic
     */
    public String getProfilePic() {
        return profilePic;
    }

    /**
     * @param profilePic The ProfilePic
     */
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    /**
     * @return The bannerPic
     */
    public String getBannerPic() {
        return bannerPic;
    }

    /**
     * @param bannerPic The BannerPic
     */
    public void setBannerPic(String bannerPic) {
        this.bannerPic = bannerPic;
    }

    /**
     * @return The acTokenId
     */
    public String getAcTokenId() {
        return acTokenId;
    }

    /**
     * @param acTokenId The AcTokenId
     */
    public void setAcTokenId(String acTokenId) {
        this.acTokenId = acTokenId;
    }

    /**
     * @return The isActive
     */
    public int getIsActive() {
        return isActive;
    }

    /**
     * @param isActive The IsActive
     */
    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    /**
     * @return The createdOn
     */
    public String getCreatedOn() {
        return createdOn;
    }

    /**
     * @param createdOn The CreatedOn
     */
    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * @return The endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate The EndDate
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * @return The isVerified
     */
    public int getIsVerified() {
        return isVerified;
    }

    /**
     * @param isVerified The IsVerified
     */
    public void setIsVerified(int isVerified) {
        this.isVerified = isVerified;
    }

    /**
     * @return The dataId
     */
    public int getDataId() {
        return dataId;
    }

    /**
     * @param dataId The DataId
     */
    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    /**
     * @return The onlineStatus
     */
    public int getOnlineStatus() {
        return onlineStatus;
    }

    /**
     * @param onlineStatus The OnlineStatus
     */
    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    /**
     * @return The vetExpertise
     */
    public String getVetExpertise() {
        return vetExpertise;
    }

    /**
     * @param vetExpertise The VetExperience
     */
    public void setVetExpertise(String vetExpertise) {
        this.vetExpertise = vetExpertise;
    }

    /**
     * @return The vetEducation
     */
    public String getVetEducation() {
        return vetEducation;
    }

    /**
     * @param vetEducation The VetEducation
     */
    public void setVetEducation(String vetEducation) {
        this.vetEducation = vetEducation;
    }

    /**
     * @return The rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * @param rating The Rating
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getpOBox() {
        return pOBox;
    }

    public void setpOBox(String pOBox) {
        this.pOBox = pOBox;
    }

    public String getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(String registeredBy) {
        this.registeredBy = registeredBy;
    }

    public String getPaypalAccountId() {
        return paypalAccountId;
    }

    public void setPaypalAccountId(String paypalAccountId) {
        this.paypalAccountId = paypalAccountId;
    }

    public String getSessionCredit() {
        return sessionCredit;
    }

    public void setSessionCredit(String sessionCredit) {
        this.sessionCredit = sessionCredit;
    }

    public float getTotalExp() {
        return totalExp;
    }

    public void setTotalExp(float totalExp) {
        this.totalExp = totalExp;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getIsVetPractiseUser() {
        return isVetPractiseUser;
    }

    public void setIsVetPractiseUser(int isVetPractiseUser) {
        this.isVetPractiseUser = isVetPractiseUser;
    }

    public int getIsVetProfile() {
        return isVetProfile;
    }

    public void setIsVetProfile(int isVetProfile) {
        this.isVetProfile = isVetProfile;
    }

    public int getIsVetAccount() {
        return isVetAccount;
    }

    public void setIsVetAccount(int isVetAccount) {
        this.isVetAccount = isVetAccount;
    }
}

