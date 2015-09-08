package com.todo.behtarinhotel.simpleobjects;

import com.todo.behtarinhotel.supportclasses.CardTypeEnum;

/**
 * Created by Andriy on 9/3/2015.
 */
public class PaymentCardSO {
    public static final int ANOTHER = 0, VISA = 1, MASTER_CARD = 2;

    private String creditCardNumber;
    private String month, year;
    private String cvvCode;

    public PaymentCardSO(String creditCardNumber, String month, String year, String cvvCode) {
        this.creditCardNumber = creditCardNumber;
        this.month = month;
        this.year = year;
        this.cvvCode = cvvCode;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getHiddenCardNumber(){
        StringBuilder stringBuilder = new StringBuilder(creditCardNumber);
        stringBuilder.replace(2, 12, "** **** **** ");
        return stringBuilder.toString();
    }


    public int getType() {
        switch (CardTypeEnum.detect(creditCardNumber)) {
            case VISA:
                return VISA;
            case MASTERCARD:
                return MASTER_CARD;
            default:
               return ANOTHER;
        }
    }
    public String getCvvCode() {
        return cvvCode;
    }

    public void setCvvCode(String cvvCode) {
        this.cvvCode = cvvCode;
    }
}
