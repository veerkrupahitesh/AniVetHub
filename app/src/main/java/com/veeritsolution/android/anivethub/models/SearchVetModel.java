package com.veeritsolution.android.anivethub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 12/21/2016.
 */

public class SearchVetModel implements Serializable, Comparable<SearchVetModel> {

    @SerializedName("VetId")
    @Expose
    private int vetId = 0;
    @SerializedName("VetName")
    @Expose
    private String vetName = "";
    @SerializedName("Address1")
    @Expose
    private String address1 = "";
    @SerializedName("Address2")
    @Expose
    private String address2 = "";
    @SerializedName("CityId")
    @Expose
    private int cityId = 0;
    @SerializedName("POBox")
    @Expose
    private String pOBox = "";
    @SerializedName("StateId")
    @Expose
    private int stateId = 0;
    @SerializedName("CountryId")
    @Expose
    private int countryId = 0;
    @SerializedName("PhoneNo")
    @Expose
    private String phoneNo = "";
    @SerializedName("EmailId")
    @Expose
    private String emailId = "";
    @SerializedName("Biography")
    @Expose
    private String biography = "";
    @SerializedName("SessionRate")
    @Expose
    private int sessionRate = 0;
    @SerializedName("UserName")
    @Expose
    private String userName = "";
    @SerializedName("Password")
    @Expose
    private String password = "";
    @SerializedName("ProfilePic")
    @Expose
    private String profilePic = "";
    @SerializedName("BannerPic")
    @Expose
    private String bannerPic = "";
    @SerializedName("AcTokenId")
    @Expose
    private String acTokenId = "";
    @SerializedName("IsActive")
    @Expose
    private int isActive = 0;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn = "";
    @SerializedName("EndDate")
    @Expose
    private String endDate = "";
    @SerializedName("IsVerified")
    @Expose
    private int isVerified = 0;
    @SerializedName("DataId")
    @Expose
    private int dataId = 0;
    @SerializedName("RegisteredBy")
    @Expose
    private String registeredBy = "";
    @SerializedName("PaypalAccountId")
    @Expose
    private String paypalAccountId = "";
    @SerializedName("SessionCredit")
    @Expose
    private int sessionCredit = 0;
    @SerializedName("TotalExp")
    @Expose
    private float totalExp = 0;
    @SerializedName("OnlineStatus")
    @Expose
    private int onlineStatus = 0;
    @SerializedName("VetExpertise")
    @Expose
    private String vetExpertise = "";
    @SerializedName("VetEducation")
    @Expose
    private String vetEducation = "";
    @SerializedName("Rating")
    @Expose
    private int rating = 0;
    @SerializedName("MinSessionRate")
    @Expose
    private int minSessionRate = 0;
    @SerializedName("MaxSessionRate")
    @Expose
    private int maxSessionRate = 0;
    @SerializedName("Latitude")
    @Expose
    private float latitude = 0;
    @SerializedName("Longitude")
    @Expose
    private float longitude = 0;
    @SerializedName("Altitude")
    @Expose
    private float altitude = 0;
    @SerializedName("SortBy")
    @Expose
    private int sortBy = 0;
    @SerializedName("SortType")
    @Expose
    private int sortType = 0;
    @SerializedName("RowNo")
    @Expose
    private int rowNo = 0;
    @SerializedName("TotalRowNo")
    @Expose
    private int totalRowNo = 0;
    @SerializedName("TotalPage")
    @Expose
    private int totalPage = 0;
    @SerializedName("City")
    @Expose
    private String city = "";
    @SerializedName("State")
    @Expose
    private String state = "";
    @SerializedName("Country")
    @Expose
    private String country = "";
    @SerializedName("MinDistanceVet")
    @Expose
    private double minDistance = 0;
    @SerializedName("MaxDistanceVet")
    @Expose
    private double maxDistance = 0;
    @SerializedName("Distance")
    @Expose
    private double distance = 0;
    @SerializedName("IsVet")
    @Expose
    private int isVet = 0;
    @SerializedName("IsVetPractiseUser")
    @Expose
    private int isVetPractise = 0;

    private int isOnline = 1;
    private int isOffline = 1;
    private int isBusy = 1;
    private float minRate = 0;
    private float maxRate = 0;

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

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPOBox() {
        return pOBox;
    }

    public void setPOBox(String pOBox) {
        this.pOBox = pOBox;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public int getSessionRate() {
        return sessionRate;
    }

    public void setSessionRate(int sessionRate) {
        this.sessionRate = sessionRate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getBannerPic() {
        return bannerPic;
    }

    public void setBannerPic(String bannerPic) {
        this.bannerPic = bannerPic;
    }

    public String getAcTokenId() {
        return acTokenId;
    }

    public void setAcTokenId(String acTokenId) {
        this.acTokenId = acTokenId;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
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

    public int getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(int isVerified) {
        this.isVerified = isVerified;
    }

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
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

    public int getSessionCredit() {
        return sessionCredit;
    }

    public void setSessionCredit(int sessionCredit) {
        this.sessionCredit = sessionCredit;
    }

    public float getTotalExp() {
        return totalExp;
    }

    public void setTotalExp(float totalExp) {
        this.totalExp = totalExp;
    }

    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getVetExpertise() {
        return vetExpertise;
    }

    public void setVetExpertise(String vetExpertise) {
        this.vetExpertise = vetExpertise;
    }

    public String getVetEducation() {
        return vetEducation;
    }

    public void setVetEducation(String vetEducation) {
        this.vetEducation = vetEducation;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getMinSessionRate() {
        return minSessionRate;
    }

    public void setMinSessionRate(int minSessionRate) {
        this.minSessionRate = minSessionRate;
    }

    public int getMaxSessionRate() {
        return maxSessionRate;
    }

    public void setMaxSessionRate(int maxSessionRate) {
        this.maxSessionRate = maxSessionRate;
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

    public int getSortBy() {
        return sortBy;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public int getIsOffline() {
        return isOffline;
    }

    public void setIsOffline(int isOffline) {
        this.isOffline = isOffline;
    }

    public int getIsBusy() {
        return isBusy;
    }

    public void setIsBusy(int isBusy) {
        this.isBusy = isBusy;
    }

    public float getMinRate() {
        return minRate;
    }

    public void setMinRate(float minRate) {
        this.minRate = minRate;
    }

    public float getMaxRate() {
        return maxRate;
    }

    public void setMaxRate(float maxRate) {
        this.maxRate = maxRate;
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

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public int getIsVet() {
        return isVet;
    }

    public void setIsVet(int isVet) {
        this.isVet = isVet;
    }

    public int getIsVetPractise() {
        return isVetPractise;
    }

    public void setIsVetPractise(int isVetPractise) {
        this.isVetPractise = isVetPractise;
    }

    @Override
    public int compareTo(SearchVetModel o) {

        if (distance > o.distance) {
            return 1;
        } else if (distance < o.distance) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(this.distance);
    }
}
