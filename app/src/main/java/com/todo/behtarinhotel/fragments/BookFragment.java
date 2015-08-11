package com.todo.behtarinhotel.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.adapters.BookingInputsAdapter;
import com.todo.behtarinhotel.simpleobjects.SearchRoomSO;
import com.todo.behtarinhotel.supportclasses.AppState;

import java.util.ArrayList;

import io.card.payment.CardIOActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookFragment extends Fragment {


    ArrayList<SearchRoomSO> rooms;
    ButtonRectangle btnPay, btnCancelPay, btnConfirmPay;
    Button btnCardIo;
    View rootView;
    LinearLayout payParametersScreen, confirmPayScreen;


    EditText etWizardEmail, etWizardFirstName, etWizardLastName, etWizardPhone;
    EditText etWizardCreditCardNumber, etWizardCreditCardIdentifier, etWizardCreditCardExMonth, etWizardCreditCardExYear;
    EditText etWizardCity, etWizardAddress, etWizardCountryCode, etWizardPostalCode;
    ArrayList<EditText> requiredEditTexts = new ArrayList<>();
    TextView tvWizardEmail, tvWizardFirstName, tvWizardLastName, tvWizardPhone;
    TextView tvWizardCreditCardNumber, tvWizardCreditCardIdentifier, tvWizardCrediCardExpiration;
    TextView tvWizardCity, tvWizardAddress, tvWizardCountryCode, tvWizardPostalCode;
    int roomType;
    int roomNumber;

    ListView wizardRoomsList;
    BookingInputsAdapter bookingInputsAdapter;


    public BookFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_book, container, false);

        initViews();
        setOnClickListeners();
        setRequiredEditTexts();
        switchToConfirmPayPage(false);



        bookingInputsAdapter  = new BookingInputsAdapter(getActivity(), rooms);
        wizardRoomsList.setAdapter(bookingInputsAdapter);
        setListViewHeightBasedOnChildren(wizardRoomsList);



        return rootView;
    }

    private void setRequiredEditTexts() {
        requiredEditTexts.add(etWizardEmail);
        requiredEditTexts.add(etWizardFirstName);
        requiredEditTexts.add(etWizardLastName);
        requiredEditTexts.add(etWizardPhone);

        requiredEditTexts.add(etWizardCreditCardNumber);
        requiredEditTexts.add(etWizardCreditCardIdentifier);
        requiredEditTexts.add(etWizardCreditCardExMonth);
        requiredEditTexts.add(etWizardCreditCardExYear);

        requiredEditTexts.add(etWizardCity);
        requiredEditTexts.add(etWizardAddress);
        requiredEditTexts.add(etWizardPostalCode);
        requiredEditTexts.add(etWizardCountryCode);
    }

    private void payWithCardIo() {
        Intent scanIntent = new Intent(getActivity(), CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, AppState.MY_SCAN_REQUEST_CODE);
    }

    private void initViews() {
        btnPay = (ButtonRectangle) rootView.findViewById(R.id.btnPay);
        btnCancelPay = (ButtonRectangle) rootView.findViewById(R.id.btnCancelPay);
        btnConfirmPay = (ButtonRectangle) rootView.findViewById(R.id.btnConfirmPay);
        btnCardIo = (Button) rootView.findViewById(R.id.btnCardIo);

        etWizardEmail = (EditText) rootView.findViewById(R.id.etWizardEmail);
        etWizardFirstName = (EditText) rootView.findViewById(R.id.etWizardFirstName);
        etWizardLastName = (EditText) rootView.findViewById(R.id.etWizardLastName);
        etWizardPhone = (EditText) rootView.findViewById(R.id.etWizardPhone);

        etWizardCreditCardNumber = (EditText) rootView.findViewById(R.id.etWizardCreditCardNumber);
        etWizardCreditCardIdentifier = (EditText) rootView.findViewById(R.id.etWizardCreditCardIdentifier);
        etWizardCreditCardExMonth = (EditText) rootView.findViewById(R.id.etWizardCreditCardExpirationMonth);
        etWizardCreditCardExYear = (EditText) rootView.findViewById(R.id.etWizardCreditCardExpirationYear);

        etWizardCity = (EditText) rootView.findViewById(R.id.etWizardCity);
        etWizardAddress = (EditText) rootView.findViewById(R.id.etWizardAddress);
        etWizardCountryCode = (EditText) rootView.findViewById(R.id.etWizardCountryCode);
        etWizardPostalCode = (EditText) rootView.findViewById(R.id.etWizardPostalCode);

        tvWizardEmail = (TextView) rootView.findViewById(R.id.tvWizardEmail);
        tvWizardFirstName = (TextView) rootView.findViewById(R.id.tvWizardFirstName);
        tvWizardLastName = (TextView) rootView.findViewById(R.id.tvWizardLastName);
        tvWizardPhone = (TextView) rootView.findViewById(R.id.tvWizardPhone);

        tvWizardCreditCardNumber = (TextView) rootView.findViewById(R.id.tvWizardCreditCardNumber);
        tvWizardCreditCardIdentifier = (TextView) rootView.findViewById(R.id.tvWizardCreditCardIdentifier);
        tvWizardCrediCardExpiration = (TextView) rootView.findViewById(R.id.tvWizardCrediCardExpiration);

        tvWizardCity = (TextView) rootView.findViewById(R.id.tvWizardCity);
        tvWizardAddress = (TextView) rootView.findViewById(R.id.tvWizardAddress);
        tvWizardCountryCode = (TextView) rootView.findViewById(R.id.tvWizardCountryCode);
        tvWizardPostalCode = (TextView) rootView.findViewById(R.id.tvWizardPostalCode);

        wizardRoomsList = (ListView) rootView.findViewById(R.id.wizardRoomsList);

        payParametersScreen = (LinearLayout) rootView.findViewById(R.id.payParametersScreen);
        confirmPayScreen = (LinearLayout) rootView.findViewById(R.id.confirmPayScreen);


    }

    public void setRooms(int roomType, ArrayList<SearchRoomSO> rooms){
        this.rooms = rooms;
        this.roomType = roomType;

    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private void switchToConfirmPayPage(boolean isConfirmPay){
        if (isConfirmPay){
            initInfoOnConfirmPage();
            confirmPayScreen.setVisibility(View.VISIBLE);
            payParametersScreen.setVisibility(View.GONE);
        }else{
            confirmPayScreen.setVisibility(View.GONE);
            payParametersScreen.setVisibility(View.VISIBLE);
        }
    }

    private void initInfoOnConfirmPage(){
        tvWizardEmail.setText("Email: " + etWizardEmail.getText().toString());
        tvWizardFirstName.setText("First name: " + etWizardFirstName.getText().toString());
        tvWizardLastName.setText("Last name: " + etWizardLastName.getText().toString());
        tvWizardPhone.setText("Phone: " + etWizardPhone.getText().toString());

        tvWizardCreditCardNumber.setText("Credit card number: " + etWizardCreditCardNumber.getText().toString());
        tvWizardCreditCardIdentifier.setText("Credit card identifier: " + etWizardCreditCardIdentifier.getText().toString());
        tvWizardCrediCardExpiration.setText("Credit card expiration: " + etWizardCreditCardExMonth.getText().toString() + "/" + etWizardCreditCardExYear.getText().toString());

        tvWizardCity.setText("City: " + etWizardCity.getText().toString());
        tvWizardAddress.setText("Address: " + etWizardAddress.getText().toString());
        tvWizardCountryCode.setText("Country code: " + etWizardCountryCode.getText().toString());
        tvWizardPostalCode.setText("Postal code: " + etWizardPostalCode.getText().toString());



    }

    private void setOnClickListeners(){
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rooms = bookingInputsAdapter.getRooms();
                switchToConfirmPayPage(true);
            }
        });
        btnCardIo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payWithCardIo();
            }
        });
        btnCancelPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToConfirmPayPage(false);
            }
        });

    }




}
