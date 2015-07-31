package com.todo.behtarinhotel.simpleobjects;

import java.util.ArrayList;

/**
 * Created by maxvitruk on 07.07.15.
 */
public class SearchParamsSO {
    String city;
    String arrivalDate = "10/10/2015";
    String departureDate = "10/12/2015";
    ArrayList<SearchRoomSO> rooms;

    public SearchParamsSO(String city, String arrivalDate, String departureDate,ArrayList<SearchRoomSO> rooms) {
        this.city = city;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.rooms = rooms;
    }

    public ArrayList<SearchRoomSO> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<SearchRoomSO> rooms) {
        this.rooms = rooms;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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


}
