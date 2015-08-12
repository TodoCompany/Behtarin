package com.todo.behtarinhotel.simpleobjects;

import java.util.ArrayList;

/**
 * Created by dmytro on 7/29/15.
 */
public class SearchRoomSO {

    public static final int SMOKING = 1, NOT_SMOKING = 2, EITHER = 3;

    ArrayList<RoomQueryGuestSO> guests;
    private String firstName = "", lastName = "";
    private int smokingPreference = NOT_SMOKING;
    private String bedType;
    private int bedTypeId;

    public SearchRoomSO(ArrayList<RoomQueryGuestSO> guests) {
        this.guests = guests;
    }
    public SearchRoomSO() {
    }

    public ArrayList<RoomQueryGuestSO> getGuests() {
        return guests;
    }

    public void setGuests(ArrayList<RoomQueryGuestSO> guests) {
        this.guests = guests;
    }

    public int getSmokingPreference() {
        return smokingPreference;
    }

    public void setSmokingPreference(int smokingPreference) {
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

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public int getBedTypeId() {
        return bedTypeId;
    }

    public void setBedTypeId(int bedTypeId) {
        this.bedTypeId = bedTypeId;
    }
}
