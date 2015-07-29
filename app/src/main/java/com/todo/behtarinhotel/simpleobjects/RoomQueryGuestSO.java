package com.todo.behtarinhotel.simpleobjects;

/**
 * Created by Andriy on 23.07.2015.
 */
public class RoomQueryGuestSO {

    private boolean isChild;
    private int age;

    public RoomQueryGuestSO() {
    }

    public RoomQueryGuestSO(boolean isChild, int age) {
        this.isChild = isChild;
        this.age = age;
    }

    public boolean isChild() {
        return isChild;
    }

    public int getAge() {
        return age;
    }

}
