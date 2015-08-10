package com.todo.behtarinhotel.simpleobjects;

import java.util.ArrayList;

/**
 * Created by dmytro on 7/29/15.
 */
public class SearchRoomSO {

    ArrayList<RoomQueryGuestSO> guests;
    private String firstName, lastName, smokingPreference;

    public SearchRoomSO(ArrayList<RoomQueryGuestSO> guests) {
        this.guests = guests;
    }

    public ArrayList<RoomQueryGuestSO> getGuests() {
        return guests;
    }

    public void setGuests(ArrayList<RoomQueryGuestSO> guests) {
        this.guests = guests;
    }

    public String getSmokingPreference() {
        return smokingPreference;
    }

    public void setSmokingPreference(String smokingPreference) {
        this.smokingPreference = smokingPreference;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
