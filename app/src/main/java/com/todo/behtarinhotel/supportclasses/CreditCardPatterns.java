package com.todo.behtarinhotel.supportclasses;

import android.content.Context;

import com.alihafizji.library.CreditCardEditText;
import com.todo.behtarinhotel.R;

import java.util.ArrayList;
import java.util.List;

public class CreditCardPatterns implements CreditCardEditText.CreditCartEditTextInterface {

    private Context mContext;

    public CreditCardPatterns(Context context) {
        mContext = context;
    }

    @Override
    public List<CreditCardEditText.CreditCard> mapOfRegexStringAndImageResourceForCreditCardEditText(CreditCardEditText creditCardEditText) {
        List<CreditCardEditText.CreditCard> listOfPatterns = new ArrayList<CreditCardEditText.CreditCard>();

        CreditCardEditText.CreditCard newCard = new CreditCardEditText.CreditCard("^4[0-9]{12}(?:[0-9]{3})?$", mContext.getResources().getDrawable(R.drawable.visa5), "visa");
        CreditCardEditText.CreditCard masterCard = new CreditCardEditText.CreditCard("^5[1-5][0-9]{14}$", mContext.getResources().getDrawable(R.drawable.mastercard5), "masterCard");


        listOfPatterns.add(newCard);
        listOfPatterns.add(masterCard);
        return listOfPatterns;
    }
}