package com.todo.behtarinhotel.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gc.materialdesign.views.ButtonRectangle;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.adapters.BookingInputsAdapter;
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
    ButtonRectangle btnPay;
    Button btnCardIo;
    View rootView;
    EditText etWizardEmail, etWizardFirstName, etWizardLastName, etWizardPhone;
    EditText etWizardCreditCardNumber, etWizardCreditCardIdentifier, etWizardCreditCardExMonth, etWizardCreditCardExYear;
    EditText etWizardCity, etWizardAddress, etWizardCountryCode, etWizardPostalCode;
    ArrayList<EditText> requiredEditTexts = new ArrayList<>();
    TextView tvWizardEmail, tvWizardFirstName, tvWizardLastName, tvWizardPhone;
    TextView tvWizardCreditCardNumber, tvWizardCreditCardIdentifier, tvWizardCrediCardExpiration;
    TextView tvWizardCity, tvWizardAddress, tvWizardCountryCode, tvWizardPostalCode;
    AvailableRoomsSO availableRooms;
    int position;

    String apiKey = "&apiKey=RyqEsq69";
    String sig = "&sig=" + AppState.getMD5EncryptedString(apiKey + System.currentTimeMillis() / 1000L);
    String url;


    ListView wizardRoomsList;
    BookingInputsAdapter bookingInputsAdapter;


    public BookFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_book, container, false);

        initViews();
        setRequiredEditTexts();

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
    }

    public void setRooms(int position,AvailableRoomsSO availableRooms, ArrayList<SearchRoomSO> rooms){
        this.rooms = rooms;
        this.position = position;
        this.availableRooms = availableRooms;
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
                "&hotelId=" + availableRooms.getHotelId()+
                "&arrivalDate=09/05/2015" +
                "&departureDate=09/07/2015" +
                "&supplierType=E" +
                "&rateKey=af00b688-acf4-409e-8bdc-fcfc3d1cb80c" +
                "&roomTypeCode=198058" +
                "&rateCode=484072" +
                "&chargeableRate=257.20" +
                "&room1=2,5,7" +
                "&room1FirstName=test" +
                "&room1LastName=testers" +
                "&room1BedTypeId=23" +
                "&room1SmokingPreference=NS" +
                "&email=test@yourSite.com" +
                "&firstName=tester" +
                "&lastName=testing" +
                "&homePhone=123123" +
                "&workPhone=123123" +
                "&creditCardType=CA" +
                "&creditCardNumber=5401999999999999" +
                "&creditCardIdentifier=123" +
                "&creditCardExpirationMonth=11" +
                "&creditCardExpirationYear=2017" +
                "&address1=travelnow" +
                "&city=Seattle" +
                "&stateProvinceCode=WA" +
                "&countryCode=US" +
                "&postalCode=98004";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,"https://book.api.ean.com/ean-services/rs/hotel/v3/res?cid=55505&apiKey=7tuermyqnaf66ujk2dk3rkfk&minorRev=26&customerSessionId=7916870766fac2d71c1d740919589836&customerIpAddress=193.93.218.85&customerUserAgent=Mozilla%2F5.0+%28Windows+NT+6.3%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Chrome%2F44.0.2403.130+Safari%2F537.36¤cyCode=USD&hotelId=463538&arrivalDate=08%2F13%2F2015&departureDate=08%2F14%2F2015&supplierType=E&rateKey=0ce2e36d-4514-4656-b890-945520ce1b49&roomTypeCode=200719009&rateCode=203567761&chargeableRate=65.38&email=kryvun.roman%40gmail.com&firstName=Roman&lastName=Kryvun&homePhone=1234234&creditCardType=CA&creditCardNumber=5401999999999999&creditCardIdentifier=123&creditCardExpirationMonth=11&creditCardExpirationYear=2015&address1=Zelena+53&city=Lviv&stateProvinceCode=123&countryCode=UA&postalCode=123&affiliateConfirmationId=d0e779996556b3b62e17d2361a9a01d6&specialInformation=&room1=2&room1FirstName=zxvc&room1LastName=Last&room1BedTypeId=43&room1SmokingPreference=NS",

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // response :"status":200,"success":"Yep"

                            Log.i("Response :", response.toString());

                            if(response.getInt("status") == 200){

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


}
