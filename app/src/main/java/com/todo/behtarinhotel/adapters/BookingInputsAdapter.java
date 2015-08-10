package com.todo.behtarinhotel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.gc.materialdesign.views.ButtonFlat;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.simpleobjects.AvailableRoomsSO;

import java.util.ArrayList;

/**
 * Created by Andriy on 07.08.2015.
 */
public class BookingInputsAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater inflater;

    EditText etWizardEmail, etWizardFirstName, etWizardLastName, etWizardPhone,
    etWizardCreditCardNumber, etWizardCreditCardIdentifier, etWizardCreditCardExpirationMonth, etWizardCreditCardExpirationYear,
    etWizardAddress, etWizardCity, etWizardCountryCode, etWizardPostalCode;
    ButtonFlat btnPersonalInfo, btnCreditCardInfo, btnLocationInfo;

    private ArrayList<AvailableRoomsSO> rooms;
    View.OnClickListener headerButtonListener;


    public BookingInputsAdapter(Context context, ArrayList<AvailableRoomsSO> rooms){
        ctx = context;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.rooms = rooms;

        headerButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnPersonalInfo.setBackgroundColor(ctx.getResources().getColor(R.color.base_hint));
                btnCreditCardInfo.setBackgroundColor(ctx.getResources().getColor(R.color.base_hint));
                btnLocationInfo.setBackgroundColor(ctx.getResources().getColor(R.color.base_hint));

                v.setBackgroundColor(ctx.getResources().getColor(R.color.base_yellow));
            }
        };
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        switch (position){
            case 0:
                view = inflater.inflate(R.layout.wizard_page_1, null, false);
                btnPersonalInfo = (ButtonFlat) view.findViewById(R.id.wizardExpandButton);
                btnPersonalInfo.setOnClickListener(headerButtonListener);
                etWizardEmail = (EditText) view.findViewById(R.id.etWizardEmail);
                etWizardFirstName = (EditText) view.findViewById(R.id.etWizardFirstName);
                etWizardLastName = (EditText) view.findViewById(R.id.etWizardLastName);
                etWizardPhone = (EditText) view.findViewById(R.id.etWizardPhone);


                break;
            case 1:
                view = inflater.inflate(R.layout.wizard_page_2, null, false);
                btnCreditCardInfo= (ButtonFlat) view.findViewById(R.id.wizardExpandButton);
                btnCreditCardInfo.setOnClickListener(headerButtonListener);
                etWizardCreditCardNumber = (EditText) view.findViewById(R.id.etWizardCreditCardNumber);
                etWizardCreditCardIdentifier = (EditText) view.findViewById(R.id.etWizardCreditCardIdentifier);
                etWizardCreditCardExpirationMonth = (EditText) view.findViewById(R.id.etWizardCreditCardExpirationMonth);
                etWizardCreditCardExpirationYear = (EditText) view.findViewById(R.id.etWizardCreditCardExpirationYear);
                break;
            case 2:
                view = inflater.inflate(R.layout.wizard_page_3, null, false);
                btnLocationInfo = (ButtonFlat) view.findViewById(R.id.wizardExpandButton);
                btnLocationInfo.setOnClickListener(headerButtonListener);
                etWizardAddress = (EditText) view.findViewById(R.id.etWizardAddress);
                etWizardCity = (EditText) view.findViewById(R.id.etWizardCity);
                etWizardCountryCode = (EditText) view.findViewById(R.id.etWizardCountryCode);
                etWizardPostalCode = (EditText) view.findViewById(R.id.etWizardPostalCode);
                break;
            default:
                view = inflater.inflate(R.layout.wizard_room_page, null, false);



        }
        return view;
    }





}
