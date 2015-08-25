package com.todo.behtarinhotel.simpleobjects;

import com.google.gson.annotations.SerializedName;

public class OrderedRoomSO {

    @SerializedName("ItineraryID")
    private int itineraryID;
    @SerializedName("FirstName")
    private String firstName;
    @SerializedName("LastName")
    private String lastName;
    @SerializedName("SmokingPreference")
    private String smokingPreference;
    @SerializedName("BedType")
    private int Bedtype;
    @SerializedName("Photo")
    private String photo;
    @SerializedName("adult")
    private int adult;
    @SerializedName("children")
    private String children;
    @SerializedName("PricesArray")
    private String PricesArray;
    @SerializedName("ConfirmationNumber")
    private int confirmationNumber;

    public OrderedRoomSO() {
    }

    public int getItineraryID() {
        return itineraryID;
    }

    public void setItineraryID(int itineraryID) {
        this.itineraryID = itineraryID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSmokingPreference() {
        return smokingPreference;
    }

    public void setSmokingPreference(String smokingPreference) {
        this.smokingPreference = smokingPreference;
    }

    public int getBedtype() {
        return Bedtype;
    }

    public void setBedtype(int bedtype) {
        Bedtype = bedtype;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getAdult() {
        return adult;
    }

    public void setAdult(int adult) {
        this.adult = adult;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getPricesArray() {
        return PricesArray;
    }

    public void setPricesArray(String pricesArray) {
        PricesArray = pricesArray;
    }

    public int getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(int confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }
}
