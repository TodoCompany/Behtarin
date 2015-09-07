package com.todo.behtarinhotel.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gc.materialdesign.views.ButtonRectangle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.adapters.BookingInputsAdapter;
import com.todo.behtarinhotel.adapters.ConfirmRoomsInfoAdapter;
import com.todo.behtarinhotel.simpleobjects.AvailableRoomsSO;
import com.todo.behtarinhotel.simpleobjects.BookedRoomSO;
import com.todo.behtarinhotel.simpleobjects.SearchRoomSO;
import com.todo.behtarinhotel.supportclasses.AppState;
import com.todo.behtarinhotel.supportclasses.CardTypeEnum;
import com.todo.behtarinhotel.supportclasses.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.card.payment.CardIOActivity;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookFragment extends Fragment {

    ArrayList<SearchRoomSO> rooms;
    ButtonRectangle btnToConfirmPage, btnCancelPay, btnPayForRoom;
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
    TextView tvCheckInInstructions, tvCancellationPolicy, tvTotalCost;
    ImageView ivCardType;
    ScrollView scroll;
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
    private ProgressDialog progressDialog;


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

        bookingInputsAdapter = new BookingInputsAdapter(getActivity(), rooms, availableRooms.getRoomSO().get(position));
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
        btnToConfirmPage = (ButtonRectangle) rootView.findViewById(R.id.btnPay);
        btnCancelPay = (ButtonRectangle) rootView.findViewById(R.id.btnCancelPay);
        btnPayForRoom = (ButtonRectangle) rootView.findViewById(R.id.btnConfirmPay);
        btnCardIo = (Button) rootView.findViewById(R.id.btnCardIo);

        etWizardEmail = (EditText) rootView.findViewById(R.id.etWizardEmail);
        etWizardFirstName = (EditText) rootView.findViewById(R.id.etWizardFirstName);
        etWizardLastName = (EditText) rootView.findViewById(R.id.etWizardLastName);
        etWizardHomePhone = (EditText) rootView.findViewById(R.id.etWizardHomePhone);
        etWizardWorkPhone = (EditText) rootView.findViewById(R.id.etWizardWorkPhone);

        etWizardCreditCardNumber = (EditText) rootView.findViewById(R.id.etWizardCreditCardNumber);
        etWizardCreditCardNumber.addTextChangedListener(new TextWatcher() {
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

        tvCheckInInstructions = (TextView) rootView.findViewById(R.id.tvCheckInInstructions);
        tvCancellationPolicy = (TextView) rootView.findViewById(R.id.tvCancellationPolicy);
        tvTotalCost = (TextView) rootView.findViewById(R.id.tvTotalCost);

        wizardRoomsList = (ListView) rootView.findViewById(R.id.wizardRoomsList);
        confirmRoomInfoList = (ListView) rootView.findViewById(R.id.confirmRoomInfoList);

        payParametersScreen = (LinearLayout) rootView.findViewById(R.id.payParametersScreen);
        confirmPayScreen = (LinearLayout) rootView.findViewById(R.id.confirmPayScreen);

        scroll = (ScrollView) rootView.findViewById(R.id.bookingScroll);

        ivCardType = (ImageView) rootView.findViewById(R.id.iv_card_type);

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

        view = listAdapter.getView(0, view, listView);
        view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
        totalHeight = view.getMeasuredHeight() * listAdapter.getCount();

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void switchToConfirmPayPage(boolean isConfirmPay) {
        if (isConfirmPay) {
            initInfoOnConfirmPage();
            scroll.smoothScrollTo(0, 0);
            confirmPayScreen.setVisibility(View.VISIBLE);
            payParametersScreen.setVisibility(View.GONE);
        } else {
            confirmPayScreen.setVisibility(View.GONE);
            payParametersScreen.setVisibility(View.VISIBLE);
        }
    }

    private void initInfoOnConfirmPage() {
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

        tvCheckInInstructions.setText(Html.fromHtml(availableRooms.getCheckInInstruction()));
        if(tvCheckInInstructions.getText().length()==0){
            rootView.findViewById(R.id.container_check_in_instructions).setVisibility(View.GONE);
        }
        tvCancellationPolicy.setText(availableRooms.getRoomSO().get(position).getCancellationPolicy());
        tvTotalCost.setText(Html.fromHtml("Total nightly rate : "
                + "<b>" + "$" + availableRooms.getRoomSO().get(position).getNightlyRateTotal() + "</b>" + "<br>" + "<br>"
                + "Total surcharges : "
                + "<b>" + "$" + availableRooms.getRoomSO().get(position).getSurchargeTotal() + "</b>" + "<br>" + "<br>"
                + "Total : "
                + "<b>" + "$" + availableRooms.getRoomSO().get(position).getTotal() + "</b>"));

        addRoomsInfoToConfirmPage();

    }

    private void setOnClickListeners() {
        btnToConfirmPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllRequiredInputsFilled()) {
                    rooms = bookingInputsAdapter.getRooms();
                    switchToConfirmPayPage(true);
                } else {
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
                for (SearchRoomSO room : rooms) {
                    room.setFirstName("");
                    room.setLastName("");
                }
            }
        });
        btnPayForRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeBookingRequest();

            }
        });


    }

    private void addRoomsInfoToConfirmPage() {
        SearchRoomSO defaultRoomData = new SearchRoomSO();
        defaultRoomData.setFirstName(etWizardFirstName.getText().toString());
        defaultRoomData.setLastName(etWizardLastName.getText().toString());
        defaultRoomData.setBedType(availableRooms.getRoomSO().get(position).getBeds().get(0).getBedDescript());
        defaultRoomData.setBedTypeId(availableRooms.getRoomSO().get(position).getBeds().get(0).getId());
        defaultRoomData.setNightRates(availableRooms.getRoomSO().get(position).getNightlyRates());


        ConfirmRoomsInfoAdapter confirmRoomsInfoAdapter = new ConfirmRoomsInfoAdapter(getActivity(), rooms, defaultRoomData);
        confirmRoomInfoList.setAdapter(confirmRoomsInfoAdapter);
        setListViewHeightBasedOnChildren(confirmRoomInfoList);
    }

    private void makeBookingRequest() {
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
                "&chargeableRate=" + availableRooms.getRoomSO().get(position).getTotal() +
                makeRoomString() +
                "&email=" + etWizardEmail.getText().toString() +
                "&firstName=" + etWizardFirstName.getText().toString() +
                "&lastName=" + etWizardLastName.getText().toString() +
                "&homePhone=" + etWizardHomePhone.getText().toString() +
                "&workPhone=" + etWizardWorkPhone.getText().toString() +
                "&creditCardType=CA" +
                "&creditCardNumber=" + etWizardCreditCardNumber.getText().toString() +
                "&creditCardIdentifier=" + etWizardCreditCardIdentifier.getText().toString() +
                "&creditCardExpirationMonth=" + etWizardCreditCardExMonth.getText().toString() +
                "&creditCardExpirationYear=20" + etWizardCreditCardExYear.getText().toString() +
                "&address1=" + etWizardAddress.getText().toString() +
                "&city=" + etWizardCity.getText().toString() +
                //"&stateProvinceCode=" + etStateProvinceCode.getText().toString() +
                "&countryCode=" + etWizardCountryCode.getText().toString() +
                "&postalCode=" + etWizardPostalCode.getText().toString();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url,

                new Response.Listener<org.json.JSONObject>() {
                    @Override
                    public void onResponse(org.json.JSONObject response) {

                        // response :"status":200,"success":"Yep"
                        progressDialog.dismiss();
                        Log.i("Response :", response.toString());
                        try {
                            if (getActivity() != null) {
                                BookedRoomSO bookedRoomSO = null;
                                ArrayList<BookedRoomSO> bookedRooms = parseResponseIntoSO(response.getJSONObject("HotelRoomReservationResponse"));
                                sendDataToAPI(bookedRooms);
                                AppState.saveBookedRoom(bookedRooms);
                                getActivity().onBackPressed();
                                getActivity().onBackPressed();
                                getActivity().onBackPressed();
                                WishListFragment wishListFragment = new WishListFragment();
                                wishListFragment.switchTab();
                                ((MaterialNavigationDrawer) getActivity()).setFragment(wishListFragment, "Room management");
                                ((MaterialNavigationDrawer) getActivity()).setSection(((MaterialNavigationDrawer) getActivity()).newSection("Room Management", new WishListFragment()));
                            }
                        } catch (JSONException e) {
                            try {
                                Toast.makeText(getActivity(),
                                        response.getJSONObject("HotelRoomReservationResponse")
                                                .getJSONObject("EanWsError")
                                                .getString("presentationMessage"),
                                        Toast.LENGTH_SHORT).show();
                            } catch (JSONException e1) {
                                Toast.makeText(getActivity(), "Some error occurred, check all and try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                progressDialog.dismiss();
            }
        });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(req);
    }

    private ArrayList<BookedRoomSO> parseResponseIntoSO(JSONObject response) throws JSONException {

        ArrayList<BookedRoomSO> bookedRooms = new ArrayList<>();


        for (int i = 0; i < rooms.size(); i++) {
            BookedRoomSO bookedRoomSO = new BookedRoomSO();
            bookedRoomSO.setUserID(AppState.getLoggedUser().getUserID());
            bookedRoomSO.setHotelID(availableRooms.getHotelId());
            bookedRoomSO.setFirstName(rooms.get(i).getFirstName());
            bookedRoomSO.setLastName(rooms.get(i).getFirstName());
            bookedRoomSO.setSmokingPreference(rooms.get(i).getSmokingPreferenceApiCode());
            bookedRoomSO.setSumPrice((float) response.getJSONObject("RateInfos").getJSONObject("RateInfo").getJSONObject("ChargeableRateInfo").getDouble("@total"));
            bookedRoomSO.setPhotoUrl(availableRooms.getRoomSO().get(position).getRoomImage());
            bookedRoomSO.setCurrency("USD");
            bookedRoomSO.setNights(response.getJSONObject("RateInfos")
                    .getJSONObject("RateInfo")
                    .getJSONObject("CancelPolicyInfoList")
                    .getJSONArray("CancelPolicyInfo")
                    .getJSONObject(0)
                    .getInt("nightCount"));
            JSONObject obj = response.getJSONObject("RateInfos").getJSONObject("RateInfo").getJSONObject("ChargeableRateInfo");
            bookedRoomSO.setPrices(obj);
            bookedRoomSO.setArrivalDate(response.getString("arrivalDate"));
            bookedRoomSO.setDepartureDate(response.getString("departureDate"));
            bookedRoomSO.setHotelAddress(response.getString("hotelAddress"));
            bookedRoomSO.setHotelName(response.getString("hotelName"));
            bookedRoomSO.setRoomDescription(response.getString("roomDescription"));
            bookedRoomSO.setItineraryId(response.getInt("itineraryId"));
            bookedRoomSO.setCancellationPolicy(availableRooms.getRoomSO().get(position).getCancellationPolicy());
            bookedRoomSO.setRoomPrice("" + availableRooms.getRoomSO().get(position).getAverageRate());
            if (rooms.size() > 1) {
                JSONArray arr = response.getJSONArray("confirmationNumbers");
                bookedRoomSO.setConfirmationNumber(arr.getInt(i));
            } else {
                bookedRoomSO.setConfirmationNumber(response.getInt("confirmationNumbers"));
            }
            bookedRooms.add(bookedRoomSO);
        }


        return bookedRooms;
    }

    private String makeRoomString() {
        String roomsString = "";
        for (int i = 0; i < rooms.size(); i++) {
            roomsString = roomsString + "&room" + (i + 1) + "=";
            for (int j = 0; j < rooms.get(i).getGuests().size(); j++) {
                roomsString = roomsString + rooms.get(i).getGuests().get(j).getAge() + ",";
            }
            roomsString = roomsString + "&room" + (i + 1) + "FirstName=" + rooms.get(i).getFirstName();
            roomsString = roomsString + "&room" + (i + 1) + "LastName=" + rooms.get(i).getLastName();
            roomsString = roomsString + "&room" + (i + 1) + "BedTypeId=" + rooms.get(i).getBedTypeId();
            roomsString = roomsString + "&room" + (i + 1) + "SmokingPreference=" + rooms.get(i).getSmokingPreferenceApiCode();
        }
        return roomsString;
    }

    private boolean isAllRequiredInputsFilled() {
        for (EditText editText : requiredEditTexts) {
            if (editText.getText().toString().length() == 0) {
                return false;
            }
        }
        return true;
    }

    private void sendDataToAPI(ArrayList<BookedRoomSO> bookedRooms) {

        HashMap<String, Object> booking = new HashMap<>();
        ArrayList<HashMap> orderedRooms = new ArrayList<>();
        for (int i = 0; i < rooms.size(); i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("ItineraryID", bookedRooms.get(i).getItineraryId());
            map.put("FirstName", rooms.get(i).getFirstName());
            map.put("LastName", rooms.get(i).getLastName());
            map.put("BedType", rooms.get(i).getBedTypeId());
            map.put("SmokingPreference", rooms.get(i).getSmokingPreferenceApiCode());
            map.put("Photo", bookedRooms.get(i).getPhotoUrl());
            map.put("adult", rooms.get(i).getGuests().get(0).getAge());
            String children = "";
            if (rooms.get(i).getGuests().size() > 1) {
                for (int a = 1; a < rooms.get(i).getGuests().size(); a++) {
                    children = children + rooms.get(i).getGuests().get(a).getAge() + ",";
                }
            }
            map.put("children", children);
            map.put("PricesArray", bookedRooms.get(i).getPrices());
            map.put("ConfirmationNumber", bookedRooms.get(i).getConfirmationNumber());
            map.put("cancellationNumber", "");
            map.put("cancellationDate", 0);
            map.put("u_id", 1);

            orderedRooms.add(map);
        }
        HashMap<String, Object> expedia = new HashMap<>();

        expedia.put("ItineraryID", bookedRooms.get(0).getItineraryId());
        expedia.put("RoomName", bookedRooms.get(0).getRoomDescription());
        expedia.put("SumPrice", bookedRooms.get(0).getSumPrice());
        expedia.put("Currency", bookedRooms.get(0).getCurrency());
        expedia.put("StartDate", bookedRooms.get(0).getArrivalDate());
        expedia.put("EndDate", bookedRooms.get(0).getDepartureDate());
        expedia.put("Nights", bookedRooms.get(0).getNights());
        expedia.put("UserID", bookedRooms.get(0).getUserID());
        expedia.put("HotelID", bookedRooms.get(0).getHotelID());

        //booking.put("expedia_order", expedia);
        booking.put("ordered_room", orderedRooms);

        JSONObject obj = new JSONObject(booking);
        String str = obj.toString();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, "http://dev.behtarinhotel.com/api/user/booking/",
                new JSONObject(booking),
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
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);
        VolleySingleton.getInstance(AppState.getMyContext()).addToRequestQueue(req);
    }


}
