package com.todo.behtarinhotel.simpleobjects;

/**
 * Created by dmytro on 7/28/15.
 */
public class SearchRoomSO {

    int adultCount;
    int[] childAges;

    public SearchRoomSO(int adultCount, int[] childAges) {
        this.adultCount = adultCount;
        this.childAges = childAges;
    }

    public int getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(int adultCount) {
        this.adultCount = adultCount;
    }

    public int[] getChildAges() {
        return childAges;
    }

    public void setChildAges(int[] childAges) {
        this.childAges = childAges;
    }
}
