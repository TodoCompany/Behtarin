package com.todo.behtarinhotel.simpleobjects;

/**
 * Created by Andriy on 23.07.2015.
 */
public class RoomQueryGuestSO {

    private boolean isChild = false;
    private int age = 0;

    public RoomQueryGuestSO(int age) {
        this.age = age;
        isChild = true;
    }

    public RoomQueryGuestSO() {

    }

    public boolean isChild() {
        return isChild;
    }

    public int getAge() {
        return age;
    }

}
