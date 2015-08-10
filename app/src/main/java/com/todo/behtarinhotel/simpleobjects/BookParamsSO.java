package com.todo.behtarinhotel.simpleobjects;

/**
 * Created by Andriy on 06.08.2015.
 */
public class BookParamsSO {

    private String email;
    private String firstName, lastName;
    private String homePhone;

    private String creditCardNumber;
    private int creditCardIdentifier;
    private int creditCardExpirationMonth;
    private int creditCardExpirationYear;

    private String adress;
    private String city;
    private String countryCode;
    private int postalCode;

    public BookParamsSO(String email, String firstName, String lastName, String homePhone, String creditCardNumber, int creditCardIdentifier, int creditCardExpirationMonth, int creditCardExpirationYear, String adress, String city, String countryCode, int postalCode) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.homePhone = homePhone;
        this.creditCardNumber = creditCardNumber;
        this.creditCardIdentifier = creditCardIdentifier;
        this.creditCardExpirationMonth = creditCardExpirationMonth;
        this.creditCardExpirationYear = creditCardExpirationYear;
        this.adress = adress;
        this.city = city;
        this.countryCode = countryCode;
        this.postalCode = postalCode;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public int getCreditCardIdentifier() {
        return creditCardIdentifier;
    }

    public int getCreditCardExpirationMonth() {
        return creditCardExpirationMonth;
    }

    public int getCreditCardExpirationYear() {
        return creditCardExpirationYear;
    }

    public String getAdress() {
        return adress;
    }

    public String getCity() {
        return city;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public int getPostalCode() {
        return postalCode;
    }
}
