package com.todo.behtarinhotel.simpleobjects;

/**
 * Created by Andriy on 13.07.2015.
 */
public class UserSO {
    private String firstName;
    private String lastName;
    private int userID;
    private String email;
    private String password;
    private String username;
    private String key;

    public UserSO() {
    }

    public UserSO(String firstName, String lastName, int userID, String email, String password, String username, String key) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.username = username;
        this.key = key;

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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}



