package com.veeritsolution.android.anivethub.models;

import java.io.Serializable;

/**
 * Created by admin on 12/21/2016.
 */

public class PetDetailsModel implements Serializable {

    private int ClientPetId;
    private String ClientId;
    private String PetName;
    private String PetTypeId;
    private String PetTypeName;
    private String PetBreedId;
    private String PetBreedName;
    private int Gender;
    private String Status;
    private String BirthDate;
    private double Weight;
    private String Description;
    private String CreatedOn;
    private String EndDate;
    private String ClientPetPicPath;
    private String ClientPetPicPath1;
    private String ClientPetPicPath2;
    private String ClientPetPicPath3;

    public int getClientPetId() {
        return ClientPetId;
    }

    public void setClientPetId(int clientPetId) {

        ClientPetId = clientPetId;
    }

    public String getClientId() {
        return ClientId;
    }

    public void setClientId(String clientId) {
        ClientId = clientId;
    }

    public String getPetTypeId() {
        return PetTypeId;
    }

    public void setPetTypeId(String petTypeId) {
        PetTypeId = petTypeId;
    }

    public String getPetName() {
        return PetName;

    }

    public void setPetName(String petName) {
        PetName = petName;
    }

    public String getPetTypeName() {
        return PetTypeName;
    }

    public void setPetTypeName(String petTypeName) {
        PetTypeName = petTypeName;
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int gender) {
        Gender = gender;
    }

    public String getPetBreedId() {
        return PetBreedId;
    }

    public void setPetBreedId(String petBreedId) {
        PetBreedId = petBreedId;
    }

    public String getPetBreedName() {
        return PetBreedName;
    }

    public void setPetBreedName(String petBreedName) {
        PetBreedName = petBreedName;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }

    public String getDescription() {

        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getStatus() {
        return Status;

    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getClientPetPicPath() {
        return ClientPetPicPath;
    }

    public void setClientPetPicPath(String clientPetPicPath) {
        ClientPetPicPath = clientPetPicPath;
    }

    public String getClientPetPicPath1() {
        return ClientPetPicPath1;
    }

    public void setClientPetPicPath1(String clientPetPicPath1) {
        ClientPetPicPath1 = clientPetPicPath1;
    }

    public String getClientPetPicPath2() {
        return ClientPetPicPath2;
    }

    public void setClientPetPicPath2(String clientPetPicPath2) {
        ClientPetPicPath2 = clientPetPicPath2;
    }

    public String getClientPetPicPath3() {
        return ClientPetPicPath3;
    }

    public void setClientPetPicPath3(String clientPetPicPath3) {
        ClientPetPicPath3 = clientPetPicPath3;
    }
}
