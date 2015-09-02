package com.todo.behtarinhotel.simpleobjects;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Andriy on 12.08.2015.
 */
public class BookedRoomSO {

    public static final int BOOKED = 1, ACTIVE = 2, OUT_OF_DATE = 3, CANCELLED = 4;

    private final String PHOTO_URL_END = "z.jpg";

    String arrivalDate;
    String departureDate;
    String hotelAddress;
    String hotelName;
    String roomDescription;
    String cancellationPolicy;
    String photoUrl;
    String roomPrice;
    int itineraryId;
    ArrayList<String> valueAdds;
    boolean isCancellable;
    int orderState = BOOKED;
    int[] confirmationNumber;
    String email;
    float SumPrice;
    String currency;
    int nights;
    int userID;
    int hotelID;
    String firstName;
    String lastName;
    String smokingPreference;
    int BedType;
    int adult;
    int[] children;

    float latitude;
    float longitude;

    JSONObject prices;

    public JSONObject getPrices() {
        return prices;
    }

    public void setPrices(JSONObject prices) {
        this.prices = prices;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getHotelID() {
        return hotelID;
    }

    public void setHotelID(int hotelID) {
        this.hotelID = hotelID;
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

    public int getBedType() {
        return BedType;
    }

    public void setBedType(int bedType) {
        BedType = bedType;
    }

    public int getAdult() {
        return adult;
    }

    public void setAdult(int adult) {
        this.adult = adult;
    }

    public int[] getChildren() {
        return children;
    }

    public void setChildren(int[] children) {
        this.children = children;
    }



    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public float getSumPrice() {
        return SumPrice;
    }

    public void setSumPrice(float sumPrice) {
        SumPrice = sumPrice;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int[] getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(int[] confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public String getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(String cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }

    public ArrayList<String> getValueAdds() {
        return valueAdds;
    }

    public void setValueAdds(ArrayList<String> valueAdds) {
        this.valueAdds = valueAdds;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        if(photoUrl!=null){
            String temp = photoUrl
                    .substring(0, photoUrl.length() - 5);
            this.photoUrl = temp + PHOTO_URL_END;
        }
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public boolean isCancellable() {
        return isCancellable;
    }

    public void setIsCancellable(boolean isCancellable) {
        this.isCancellable = isCancellable;
    }

    public int getItineraryId() {
        return itineraryId;
    }
    public void setItineraryId(int itineraryId) {
        this.itineraryId = itineraryId;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}



