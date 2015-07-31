package com.todo.behtarinhotel.simpleobjects;

import java.util.ArrayList;

/**
 * Created by dmytro on 7/29/15.
 */
public class SearchRoomSO {

    ArrayList<RoomQueryGuestSO> guests;

    public SearchRoomSO(ArrayList<RoomQueryGuestSO> guests) {
        this.guests = guests;
    }

    public ArrayList<RoomQueryGuestSO> getGuests() {
        return guests;
    }

    public void setGuests(ArrayList<RoomQueryGuestSO> guests) {
        this.guests = guests;
    }
}
