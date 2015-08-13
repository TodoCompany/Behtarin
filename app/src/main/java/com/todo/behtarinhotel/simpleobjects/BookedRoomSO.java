package com.todo.behtarinhotel.simpleobjects;

import java.util.ArrayList;

/**
 * Created by Andriy on 12.08.2015.
 */
public class BookedRoomSO {

    private final String PHOTO_URL_END = "b.jpg";

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
        String temp = photoUrl
                .substring(0, photoUrl.length() - 5);
        this.photoUrl = temp + PHOTO_URL_END;

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
}
