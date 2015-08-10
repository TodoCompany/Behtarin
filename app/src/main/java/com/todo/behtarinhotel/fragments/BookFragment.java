package com.todo.behtarinhotel.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

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
    ButtonRectangle btnPay;
    Button btnCardIo;
    View rootView;
    EditText etWizardEmail, etWizardFirstName, etWizardLastName, etWizardPhone;
    EditText etWizardCreditCardNumber, etWizardCreditCardIdentifier, etWizardCreditCardExMonth, etWizardCreditCardExYear;
    EditText etWizardCity, etWizardAddress, etWizardCountryCode, etWizardPostalCode;
    ArrayList<EditText> requiredEditTexts = new ArrayList<>();
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

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rooms = bookingInputsAdapter.getRooms();
            }
        });
        btnCardIo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payWithCardIo();
            }
        });

        bookingInputsAdapter  = new BookingInputsAdapter(getActivity(), rooms);
        wizardRoomsList.setAdapter(bookingInputsAdapter);
        setListViewHeightBasedOnChildren(wizardRoomsList);



        return rootView;
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

        wizardRoomsList = (ListView) rootView.findViewById(R.id.wizardRoomsList);
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




}
