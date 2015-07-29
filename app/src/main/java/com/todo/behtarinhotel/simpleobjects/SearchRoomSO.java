package com.todo.behtarinhotel.simpleobjects;

/**
 * Created by dmytro on 7/29/15.
 */
public class SearchRoomSO {

    RoomQueryGuestSO[] guests;

    public SearchRoomSO(RoomQueryGuestSO[] guests) {
        this.guests = guests;
    }

    public RoomQueryGuestSO[] getGuests() {
        return guests;
    }

    public void setGuests(RoomQueryGuestSO[] guests) {
        this.guests = guests;
    }
}
