package com.todo.behtarinhotel.simpleobjects;

/**
 * Created by Andriy on 13.07.2015.
 */
public class UserSO {
    private String fullName;
    private String email;
    private String password;

    public UserSO() {

    }

    public UserSO(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
