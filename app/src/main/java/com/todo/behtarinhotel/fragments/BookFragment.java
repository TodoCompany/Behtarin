package com.todo.behtarinhotel.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gc.materialdesign.views.ButtonRectangle;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.adapters.BookingInputsAdapter;
import com.todo.behtarinhotel.adapters.ConfirmRoomsInfoAdapter;
import com.todo.behtarinhotel.simpleobjects.AvailableRoomsSO;
import com.todo.behtarinhotel.simpleobjects.SearchRoomSO;
import com.todo.behtarinhotel.supportclasses.AppState;
import com.todo.behtarinhotel.supportclasses.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

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


    EditText etWizardEmail, etWizardFirstName, etWizardLastName, etWizardHomePhone, etWizardWorkPhone;
    EditText etWizardCreditCardNumber, etWizardCreditCardIdentifier, etWizardCreditCardExMonth, etWizardCreditCardExYear;
    EditText etWizardCity, etWizardAddress, etWizardCountryCode, etWizardPostalCode, etStateProvinceCode;
    ArrayList<EditText> requiredEditTexts = new ArrayList<>();
    TextView tvWizardEmail, tvWizardFirstName, tvWizardLastName, tvWizardPhone;
    TextView tvWizardCreditCardNumber, tvWizardCreditCardIdentifier, tvWizardCreditCardExpiration;
    TextView tvWizardCity, tvWizardAddress, tvWizardCountryCode, tvWizardPostalCode;
    AvailableRoomsSO availableRooms;
    int position;

    String apiKey = "&apiKey=RyqEsq69";
    String sig = "&sig=" + AppState.getMD5EncryptedString(apiKey + System.currentTimeMillis() / 1000L);
    String url;


    ListView wizardRoomsList;
    ListView confirmRoomInfoList;
    BookingInputsAdapter bookingInputsAdapter;
    private String arrivalDate;
    private String departureDate;


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



        bookingInputsAdapter  = new BookingInputsAdapter(getActivity(), rooms, availableRooms.getRoomSO().get(position));
        wizardRoomsList.setAdapter(bookingInputsAdapter);
        setListViewHeightBasedOnChildren(wizardRoomsList);


        return rootView;
    }

    private void setRequiredEditTexts() {
        requiredEditTexts.add(etWizardEmail);
        requiredEditTexts.add(etWizardFirstName);
        requiredEditTexts.add(etWizardLastName);
        requiredEditTexts.add(etWizardHomePhone);

        requiredEditTexts.add(etWizardCreditCardNumber);
        requiredEditTexts.add(etWizardCreditCardIdentifier);
        requiredEditTexts.add(etWizardCreditCardExMonth);
        requiredEditTexts.add(etWizardCreditCardExYear);

        requiredEditTexts.add(etWizardCity);
        requiredEditTexts.add(etWizardAddress);
        requiredEditTexts.add(etWizardPostalCode);
        requiredEditTexts.add(etStateProvinceCode);
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
        etWizardHomePhone = (EditText) rootView.findViewById(R.id.etWizardHomePhone);
        etWizardWorkPhone = (EditText) rootView.findViewById(R.id.etWizardWorkPhone);

        etWizardCreditCardNumber = (EditText) rootView.findViewById(R.id.etWizardCreditCardNumber);
        etWizardCreditCardIdentifier = (EditText) rootView.findViewById(R.id.etWizardCreditCardIdentifier);
        etWizardCreditCardExMonth = (EditText) rootView.findViewById(R.id.etWizardCreditCardExpirationMonth);
        etWizardCreditCardExYear = (EditText) rootView.findViewById(R.id.etWizardCreditCardExpirationYear);

        etWizardCity = (EditText) rootView.findViewById(R.id.etWizardCity);
        etWizardAddress = (EditText) rootView.findViewById(R.id.etWizardAddress);
        etWizardCountryCode = (EditText) rootView.findViewById(R.id.etWizardCountryCode);
        etWizardPostalCode = (EditText) rootView.findViewById(R.id.etWizardPostalCode);
        etStateProvinceCode = (EditText) rootView.findViewById(R.id.etStateProvinceCode);

        tvWizardEmail = (TextView) rootView.findViewById(R.id.tvWizardEmail);
        tvWizardFirstName = (TextView) rootView.findViewById(R.id.tvWizardFirstName);
        tvWizardLastName = (TextView) rootView.findViewById(R.id.tvWizardLastName);
        tvWizardPhone = (TextView) rootView.findViewById(R.id.tvWizardPhone);

        tvWizardCreditCardNumber = (TextView) rootView.findViewById(R.id.tvWizardCreditCardNumber);
        tvWizardCreditCardIdentifier = (TextView) rootView.findViewById(R.id.tvWizardCreditCardIdentifier);
        tvWizardCreditCardExpiration = (TextView) rootView.findViewById(R.id.tvWizardCrediCardExpiration);

        tvWizardCity = (TextView) rootView.findViewById(R.id.tvWizardCity);
        tvWizardAddress = (TextView) rootView.findViewById(R.id.tvWizardAddress);
        tvWizardCountryCode = (TextView) rootView.findViewById(R.id.tvWizardCountryCode);
        tvWizardPostalCode = (TextView) rootView.findViewById(R.id.tvWizardPostalCode);

        wizardRoomsList = (ListView) rootView.findViewById(R.id.wizardRoomsList);
        confirmRoomInfoList = (ListView) rootView.findViewById(R.id.confirmRoomInfoList);

        payParametersScreen = (LinearLayout) rootView.findViewById(R.id.payParametersScreen);
        confirmPayScreen = (LinearLayout) rootView.findViewById(R.id.confirmPayScreen);


    }

    public void setRooms(int position, AvailableRoomsSO availableRooms, ArrayList<SearchRoomSO> rooms, String arrivalDate, String departureDate) {
        this.rooms = rooms;
        this.position = position;
        this.availableRooms = availableRooms;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
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
        tvWizardEmail.setText(Html.fromHtml("Email: " + "<b>" + etWizardEmail.getText().toString() + "</b>"));
        tvWizardFirstName.setText(Html.fromHtml("First name: " + "<b>" + etWizardFirstName.getText().toString() + "</b>"));
        tvWizardLastName.setText(Html.fromHtml("Last name: " + "<b>" + etWizardLastName.getText().toString() + "</b>"));
        tvWizardPhone.setText(Html.fromHtml("Phone: " + "<b>" + etWizardHomePhone.getText().toString() + "</b>"));

        tvWizardCreditCardNumber.setText(Html.fromHtml("Credit card number: " + "<b>" + etWizardCreditCardNumber.getText().toString() + "</b>"));
        tvWizardCreditCardIdentifier.setText(Html.fromHtml("Credit card identifier: " + "<b>" + etWizardCreditCardIdentifier.getText().toString() + "</b>"));
        tvWizardCreditCardExpiration.setText(Html.fromHtml("Credit card expiration: " + "<b>" + etWizardCreditCardExMonth.getText().toString() + "/" + etWizardCreditCardExYear.getText().toString() + "</b>"));

        tvWizardCity.setText(Html.fromHtml("City: " + "<b>" + etWizardCity.getText().toString() + "</b>"));
        tvWizardAddress.setText(Html.fromHtml("Address: " + "<b>" + etWizardAddress.getText().toString() + "</b>"));
        tvWizardCountryCode.setText(Html.fromHtml("Country code: " + "<b>" + etWizardCountryCode.getText().toString() + "</b>"));
        tvWizardPostalCode.setText(Html.fromHtml("Postal code: " + "<b>" + etWizardPostalCode.getText().toString() + "</b>"));

        addRoomsInfoToConfirmPage();

    }

    private void setOnClickListeners() {
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllRequiredInputsFilled()) {
                    rooms = bookingInputsAdapter.getRooms();
                    switchToConfirmPayPage(true);
                }else{
                    Toast.makeText(getActivity(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
                }
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
    
    private void addRoomsInfoToConfirmPage(){
        SearchRoomSO defaultRoomData = new SearchRoomSO();
        defaultRoomData.setFirstName(etWizardFirstName.getText().toString());
        defaultRoomData.setLastName(etWizardLastName.getText().toString());
        defaultRoomData.setBedType(availableRooms.getRoomSO().get(0).getBeds().get(0).getBedDescript());
        defaultRoomData.setBedTypeId(availableRooms.getRoomSO().get(0).getBeds().get(0).getId());

        ConfirmRoomsInfoAdapter confirmRoomsInfoAdapter = new ConfirmRoomsInfoAdapter(getActivity(), rooms, defaultRoomData);
        confirmRoomInfoList.setAdapter(confirmRoomsInfoAdapter);
        setListViewHeightBasedOnChildren(wizardRoomsList);
    }  

    private void makeBookingRequest(){
        url = "https://book.api.ean.com/ean-services/rs/hotel/v3/res?" +
                "&cid=55505" +
                sig +
                apiKey +
                "&customerIpAddress=192.168.0.1" +
                "&customerSessionId=0" +
                "&locale=en_US" +
                "&minorRev=30" +
                "&currencyCode=USD" +
                "&hotelId=" + availableRooms.getHotelId() +
                "&arrivalDate=" + arrivalDate +
                "&departureDate=" + departureDate +
                "&supplierType=E" +
                "&rateKey=" + availableRooms.getRoomSO().get(position).getRateKey() +
                "&roomTypeCode=" + availableRooms.getRoomSO().get(position).getRoomTypeCode() +
                "&rateCode=" + availableRooms.getRoomSO().get(position).getRateCode() +
                "&chargeableRate=" + availableRooms.getRoomSO().get(position).getAverageRate() +
                makeRoomstring() +
                "&email=" + etWizardEmail.getText().toString() +
                "&firstName=" + etWizardFirstName.getText().toString() +
                "&lastName=" + etWizardFirstName.getText().toString() +
                "&homePhone=" + etWizardHomePhone.getText().toString() +
                "&workPhone=" + etWizardWorkPhone.getText().toString() +
                "&creditCardType=CA" +
                "&creditCardNumber=" + etWizardCreditCardNumber.getText().toString() +
                "&creditCardIdentifier=" + etWizardCreditCardIdentifier.getText().toString() +
                "&creditCardExpirationMonth=" + etWizardCreditCardExMonth.getText().toString() +
                "&creditCardExpirationYear=" + etWizardCreditCardExYear.getText().toString() +
                "&address1=" + etWizardAddress.getText().toString() +
                "&city=" + etWizardCity.getText().toString() +
                //"&stateProvinceCode=" + etStateProvinceCode.getText().toString() +
                "&countryCode=" + etWizardCountryCode.getText().toString() +
                "&postalCode=" + etWizardPostalCode.getText().toString();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // response :"status":200,"success":"Yep"

                            Log.i("Response :", response.toString());

                            if (response.getInt("status") == 200) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(req);
    }

    private String makeRoomstring() {
        String roomsString = "";
        for (int i = 0; i < rooms.size(); i++) {
            roomsString = "&room" + (i + 1) + "=";
            for (int j = 0; j < rooms.get(i).getGuests().size(); j++) {
                roomsString = roomsString + rooms.get(i).getGuests().get(j).getAge() + ",";
            }
            roomsString = roomsString + "&room" + (i + 1) + "FirstName=" + rooms.get(i).getFirstName();
            roomsString = roomsString + "&room" + (i + 1) + "LastName=" + rooms.get(i).getLastName();
            roomsString = roomsString + "&room" + (i + 1) + "BedTypeId=";
            roomsString = roomsString + "&room" + (i + 1) + "SmokingPreference=" + rooms.get(i).getSmokingPreference();
        }
        return roomsString;
    }

    private boolean isAllRequiredInputsFilled(){
        for (EditText editText : requiredEditTexts){
            if (editText.getText().toString().length() == 0){
                return false;
            }
        }
        return true;
    }


}
