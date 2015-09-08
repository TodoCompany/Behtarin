package com.todo.behtarinhotel.supportclasses;


import java.util.regex.Pattern;

public enum CardTypeEnum {

    UNKNOWN,
    VISA("^4[0-9]{12}(?:[0-9]{3})?$"),
    MASTERCARD("^5[1-5][0-9]{14}$"),
    AMERICAN_EXPRESS("^3[47][0-9]{13}$"),
    DINERS_CLUB("^3(?:0[0-5]|[68][0-9])[0-9]{11}$"),
    DISCOVER("^6(?:011|5[0-9]{2})[0-9]{12}$"),
    JCB("^(?:2131|1800|35\\d{3})\\d{11}$");

    private Pattern pattern;

    CardTypeEnum() {
        this.pattern = null;
    }

    CardTypeEnum(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    public static CardTypeEnum detect(String cardNumber) {

        for (CardTypeEnum cardType : CardTypeEnum.values()) {
            if (null == cardType.pattern) continue;
            if (cardType.pattern.matcher(cardNumber).matches()) return cardType;
        }

        return UNKNOWN;
    }

}
