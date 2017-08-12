package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.helper.PrefHelper;

import java.io.Serializable;

/**
 * Created by ${hitesh} on 12/7/2016.
 */

public class ClientLoginModel implements Serializable {

    @SerializedName("IsDataExists")
    @Expose
    private int isDataExists = 0;
    @SerializedName("ClientId")
    @Expose
    private int clientId;
    @SerializedName("ClientName")
    @Expose
    private String clientName;
    @SerializedName("Address1")
    @Expose
    private String address1;
    @SerializedName("Address2")
    @Expose
    private String address2;
    @SerializedName("CityId")
    @Expose
    private int cityId;
    @SerializedName("POBox")
    @Expose
    private String pOBox;
    @SerializedName("StateId")
    @Expose
    private int stateId;
    @SerializedName("CountryId")
    @Expose
    private int countryId;
    @SerializedName("PhoneNo")
    @Expose
    private String phoneNo;
    @SerializedName("EmailId")
    @Expose
    private String emailId;
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
    @SerializedName("VetName")
    @Expose
    private String vetName;
    @SerializedName("VetAddress")
    @Expose
    private String vetAddress;
    @SerializedName("VetContactNo")
    @Expose
    private String vetContactNo;
    @SerializedName("VetEmailId")
    @Expose
    private String vetEmailId;
    @SerializedName("IsActive")
    @Expose
    private int isActive;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;
    @SerializedName("EndDate")
    @Expose
    private String endDate;
    @SerializedName("DataId")
    @Expose
    private int dataId;
    @SerializedName("RegisteredBy")
    @Expose
    private String registeredBy;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("State")
    @Expose
    private String state;
    @SerializedName("Country")
    @Expose
    private String country;
    @SerializedName("Latitude")
    @Expose
    private float latitude;
    @SerializedName("Longitude")
    @Expose
    private float longitude;
    @SerializedName("Altitude")
    @Expose
    private float altitude;
    @SerializedName("IsClientProfile")
    @Expose
    private float isClientProfile;
    @SerializedName("IsClientPetProfile")
    @Expose
    private float isClientPetProfile;

    public static void saveClientCredentials(String loginResponse) {

        PrefHelper.getInstance().setString(PrefHelper.CLIENT_LOGIN_CREDENTIALS, loginResponse);

    }

    public static ClientLoginModel getClientCredentials() {

        return RestClient.getGsonInstance().fromJson(PrefHelper.getInstance().getString(PrefHelper.CLIENT_LOGIN_CREDENTIALS, ""),
                ClientLoginModel.class);
    }

    public int getIsDataExists() {
        return isDataExists;
    }

    public void setIsDataExists(int isDataExists) {
        this.isDataExists = isDataExists;
    }

    /**
     * @return The clientId
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * @param clientId The ClientId
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * @return The clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @param clientName The ClientName
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
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

    public String getVetName() {
        return vetName;
    }

    public void setVetName(String vetName) {
        this.vetName = vetName;
    }

    public String getVetAddress() {
        return vetAddress;
    }

    public void setVetAddress(String vetAddress) {
        this.vetAddress = vetAddress;
    }

    public String getVetContactNo() {
        return vetContactNo;
    }

    public void setVetContactNo(String vetContactNo) {
        this.vetContactNo = vetContactNo;
    }

    public String getVetEmailId() {
        return vetEmailId;
    }

    public void setVetEmailId(String vetEmailId) {
        this.vetEmailId = vetEmailId;
    }

    public String getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(String registeredBy) {
        this.registeredBy = registeredBy;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getAltitude() {
        return altitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    public float getIsClientProfile() {
        return isClientProfile;
    }

    public void setIsClientProfile(float isClientProfile) {
        this.isClientProfile = isClientProfile;
    }

    public float getIsClientPetProfile() {
        return isClientPetProfile;
    }

    public void setIsClientPetProfile(float isClientPetProfile) {
        this.isClientPetProfile = isClientPetProfile;
    }
}
