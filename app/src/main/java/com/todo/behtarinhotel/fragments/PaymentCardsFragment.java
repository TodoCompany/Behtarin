package com.todo.behtarinhotel.fragments;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.adapters.PaymentCardsAdapter;
import com.todo.behtarinhotel.simpleobjects.PaymentCardSO;
import com.todo.behtarinhotel.supportclasses.AppState;
import com.todo.behtarinhotel.supportclasses.CardTypeEnum;

import java.security.GeneralSecurityException;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentCardsFragment extends Fragment {


    private View rootView;
    private PaymentCardsAdapter paymentCardsAdapter;
    private ListView paymentCardsList;
    private ButtonRectangle btnAddNewCard;
    private View addItemView;
    private EditText etCreditCardNumber, etCreditCardIdentifier, etCreditCardExMonth, etCreditCardExYear;
    private ImageView ivCardType;
    boolean startAddCardDialog = false;

    public PaymentCardsFragment() {
        // Required empty public constructor
    }

    //This is valid coz we can use it only when we need it, not when android recreates fragment
    @SuppressLint("ValidFragment")
    public PaymentCardsFragment(boolean startAddCardDialog) {
        this.startAddCardDialog = startAddCardDialog;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_payment_cards, container, false);
        paymentCardsList = (ListView) rootView.findViewById(R.id.paymentCardsList);
        btnAddNewCard = (ButtonRectangle) rootView.findViewById(R.id.btnAddNewCard);

        paymentCardsAdapter = new PaymentCardsAdapter(getActivity());
        paymentCardsList.setAdapter(paymentCardsAdapter);

        btnAddNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initAddItemView(inflater);
                showAddCardDialog();
            }
        });

        return rootView;
    }

    private void initAddItemView(LayoutInflater inflater){
        addItemView = inflater.inflate(R.layout.payment_card_additem, null, false);
        etCreditCardNumber = (EditText) addItemView.findViewById(R.id.etCreditCardNumber);
        etCreditCardExMonth = (EditText) addItemView.findViewById(R.id.etCreditCardExpirationMonth);
        etCreditCardExYear = (EditText) addItemView.findViewById(R.id.etCreditCardExpirationYear);
        etCreditCardIdentifier = (EditText) addItemView.findViewById(R.id.etCreditCardIdentifier);
        ivCardType = (ImageView) addItemView.findViewById(R.id.iv_card_type);

        etCreditCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (CardTypeEnum.detect(s.toString())) {
                    case VISA:
                        ivCardType.setImageDrawable(getResources().getDrawable(R.drawable.visa5));
                        break;
                    case MASTERCARD:
                        ivCardType.setImageDrawable(getResources().getDrawable(R.drawable.mastercard5));
                        break;
                    default:
                        ivCardType.setImageDrawable(getResources().getDrawable(R.drawable.cards4));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showAddCardDialog(){
        new AlertDialog.Builder(getActivity())
                .setView(addItemView)
                .setTitle("Add new card")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Add card", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        if (isRequiredFieldsFilled()) {
                            try {
                                AppState.addPaymentCard(new PaymentCardSO(etCreditCardNumber.getText().toString(), etCreditCardExMonth.getText().toString(), etCreditCardExYear.getText().toString(), etCreditCardIdentifier.getText().toString()));
                            } catch (GeneralSecurityException e) {
                                dialog.dismiss();
                                //Key is invalid
                            }
                            paymentCardsAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "Wrong card number", Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .show();
    }


    public boolean isRequiredFieldsFilled() {
        if (etCreditCardNumber.getText().length() == 16 &
                etCreditCardExYear.getText().length() == 2 &
                etCreditCardExMonth.getText().length() == 2 &
                etCreditCardIdentifier.getText().length() == 3){
            return true;
        }
        return false;
    }
}
