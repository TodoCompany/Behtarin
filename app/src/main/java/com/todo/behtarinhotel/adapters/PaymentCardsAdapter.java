package com.todo.behtarinhotel.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.simpleobjects.PaymentCardSO;
import com.todo.behtarinhotel.supportclasses.AppState;

import java.security.GeneralSecurityException;
import java.util.ArrayList;

/**
 * Created by Andriy on 9/3/2015.
 */
public class PaymentCardsAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;

    TextView tvCreditCardNumber;
    ImageView btnDeleteCreditCard;
    ImageView cardTypeImage;

    private ArrayList<PaymentCardSO> paymentCards = new ArrayList<>();

    public PaymentCardsAdapter(Context context){
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            paymentCards = AppState.getCreditCards();
        } catch (GeneralSecurityException e) {
            paymentCards = new ArrayList<>();
            //Key is invalid
        }


    }

    @Override
    public int getCount() {
        return paymentCards.size();
    }

    @Override
    public Object getItem(int i) {
        return paymentCards.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View rootView;
        if (view == null) {
            rootView = inflater.inflate(R.layout.payment_card_item, null, false);
        }else{
            rootView = view;
        }

        tvCreditCardNumber = (TextView) rootView.findViewById(R.id.tvCreditCardNumber);
        btnDeleteCreditCard = (ImageView) rootView.findViewById(R.id.btnDeleteCreditCard);
        cardTypeImage = (ImageView) rootView.findViewById(R.id.cardTypeImage);
        tvCreditCardNumber.setText(paymentCards.get(i).getHiddenCardNumber());
        switch (paymentCards.get(i).getType()){

            case PaymentCardSO.VISA:
                cardTypeImage.setImageDrawable(context.getResources().getDrawable(R.drawable.visa));
                break;
            case PaymentCardSO.MASTER_CARD:
                cardTypeImage.setImageDrawable(context.getResources().getDrawable(R.drawable.mastercard_logo));
                break;
            case PaymentCardSO.ANOTHER:
                cardTypeImage.setImageDrawable(context.getResources().getDrawable(R.drawable.creditcard));
                break;
        }
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/credit_card_font.ttf");
        tvCreditCardNumber.setTypeface(customFont);
        btnDeleteCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AppState.removePaymentCard(paymentCards.get(i));
                } catch (GeneralSecurityException e) {
                    //Key is invalid
                    paymentCards = new ArrayList<PaymentCardSO>();
                }
                notifyDataSetChanged();
            }
        });

        return rootView;
    }

    @Override
    public void notifyDataSetChanged() {
        try {
            paymentCards = AppState.getCreditCards();
        } catch (GeneralSecurityException e) {
            paymentCards = new ArrayList<>();
            //Key is invalid
        }
        super.notifyDataSetChanged();

    }
}
