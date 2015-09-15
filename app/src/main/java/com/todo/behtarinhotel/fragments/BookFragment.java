package com.todo.behtarinhotel.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gc.materialdesign.views.ButtonRectangle;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.adapters.BookingInputsAdapter;
import com.todo.behtarinhotel.adapters.ConfirmRoomsInfoAdapter;
import com.todo.behtarinhotel.simpleobjects.AvailableRoomsSO;
import com.todo.behtarinhotel.simpleobjects.BookedRoomSO;
import com.todo.behtarinhotel.simpleobjects.PaymentCardSO;
import com.todo.behtarinhotel.simpleobjects.SearchRoomSO;
import com.todo.behtarinhotel.supportclasses.AppState;
import com.todo.behtarinhotel.supportclasses.CardTypeEnum;
import com.todo.behtarinhotel.supportclasses.DataLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;


public class BookFragment extends Fragment {

    private ArrayList<SearchRoomSO> rooms;
    private ButtonRectangle btnToConfirmPage, btnCancelPay, btnPayForRoom, btnAddNewCard;
    private View rootView;
    private LinearLayout payParametersScreen, confirmPayScreen;
    private PaymentCardSO currentPaymentCard;
    private ArrayList<PaymentCardSO> paymentCards;

    private EditText etWizardEmail, etWizardFirstName, etWizardLastName, etWizardHomePhone, etWizardWorkPhone;
    private EditText etWizardCity, etWizardAddress, etWizardCountryCode, etWizardPostalCode, etStateProvinceCode;
    private EditText etWizardCreditCardNumber, etWizardCreditCardIdentifier, etWizardCreditCardExMonth, etWizardCreditCardExYear;
    private ArrayList<EditText> requiredEditTexts = new ArrayList<>();
    private TextView tvWizardEmail, tvWizardFirstName, tvWizardLastName, tvWizardPhone;
    private TextView tvWizardCreditCardNumber, tvWizardCreditCardIdentifier, tvWizardCreditCardExpiration;
    private TextView tvWizardCity, tvWizardAddress, tvWizardCountryCode, tvWizardPostalCode;
    private TextView tvCheckInInstructions, tvCancellationPolicy, tvTotalCost;
    private ImageView ivCardType;
    private ScrollView scroll;
    private AvailableRoomsSO availableRooms;
    private RadioGroup rGroupCreditCard;
    private int position;

    private String apiKey = "&apiKey=RyqEsq69";
    private String sig = "&sig=" + DataLoader.getMD5EncryptedString(apiKey + System.currentTimeMillis() / 1000L);


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
        setUiListeners();
        setRequiredEditTexts();
        switchToConfirmPayPage(false);

        bookingInputsAdapter = new BookingInputsAdapter(getActivity(), rooms, availableRooms.getRoomSO().get(position));
        wizardRoomsList.setAdapter(bookingInputsAdapter);
        setListViewHeightBasedOnChildren(wizardRoomsList);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initCreditCardsRadioGroup();
    }

    private void setRequiredEditTexts() {
        requiredEditTexts.add(etWizardEmail);
        requiredEditTexts.add(etWizardFirstName);
        requiredEditTexts.add(etWizardLastName);
        requiredEditTexts.add(etWizardHomePhone);
        requiredEditTexts.add(etWizardCity);
        requiredEditTexts.add(etWizardAddress);
        requiredEditTexts.add(etWizardPostalCode);
        requiredEditTexts.add(etStateProvinceCode);
        requiredEditTexts.add(etWizardCountryCode);
    }

    private void initViews() {
        btnToConfirmPage = (ButtonRectangle) rootView.findViewById(R.id.btnPay);
        btnCancelPay = (ButtonRectangle) rootView.findViewById(R.id.btnCancelPay);
        btnPayForRoom = (ButtonRectangle) rootView.findViewById(R.id.btnConfirmPay);
        btnAddNewCard = (ButtonRectangle) rootView.findViewById(R.id.btnAddNewCard);

        etWizardEmail = (EditText) rootView.findViewById(R.id.etWizardEmail);
        etWizardEmail.setText(AppState.getLoggedUser().getEmail());
        etWizardFirstName = (EditText) rootView.findViewById(R.id.etWizardFirstName);
        etWizardFirstName.setText(AppState.getLoggedUser().getFirstName());
        etWizardLastName = (EditText) rootView.findViewById(R.id.etWizardLastName);
        etWizardLastName.setText(AppState.getLoggedUser().getLastName());
        etWizardHomePhone = (EditText) rootView.findViewById(R.id.etWizardHomePhone);
        etWizardWorkPhone = (EditText) rootView.findViewById(R.id.etWizardWorkPhone);

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
        etWizardCreditCardNumber = (EditText) rootView.findViewById(R.id.etWizardCreditCardNumber);
        etWizardCreditCardExMonth = (EditText) rootView.findViewById(R.id.etWizardCreditCardExpirationMonth);
        etWizardCreditCardExYear = (EditText) rootView.findViewById(R.id.etWizardCreditCardExpirationYear);
        etWizardCreditCardIdentifier = (EditText) rootView.findViewById(R.id.etWizardCreditCardIdentifier);
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
        int totalHeight;

        View view = listAdapter.getView(0, null, listView);
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
        if (!isCreditCardInputsFilled()) {
            currentPaymentCard = paymentCards.get(rGroupCreditCard.getCheckedRadioButtonId());
        }

        tvWizardEmail.setText(Html.fromHtml("Email: " + "<b>" + etWizardEmail.getText().toString() + "</b>"));
        tvWizardFirstName.setText(Html.fromHtml("First name: " + "<b>" + etWizardFirstName.getText().toString() + "</b>"));
        tvWizardLastName.setText(Html.fromHtml("Last name: " + "<b>" + etWizardLastName.getText().toString() + "</b>"));
        tvWizardPhone.setText(Html.fromHtml("Phone: " + "<b>" + etWizardHomePhone.getText().toString() + "</b>"));

        tvWizardCreditCardNumber.setText(Html.fromHtml("Credit card number: " + "<b>" + currentPaymentCard.getHiddenCardNumber() + "</b>"));
        tvWizardCreditCardIdentifier.setText(Html.fromHtml("Credit card identifier: " + "<b>" + currentPaymentCard.getCvvCode() + "</b>"));
        tvWizardCreditCardExpiration.setText(Html.fromHtml("Credit card expiration: " + "<b>" + currentPaymentCard.getMonth() + "/" + currentPaymentCard.getYear() + "</b>"));

        tvWizardCity.setText(Html.fromHtml("City: " + "<b>" + etWizardCity.getText().toString() + "</b>"));
        tvWizardAddress.setText(Html.fromHtml("Address: " + "<b>" + etWizardAddress.getText().toString() + "</b>"));
        tvWizardCountryCode.setText(Html.fromHtml("Country code: " + "<b>" + etWizardCountryCode.getText().toString() + "</b>"));
        tvWizardPostalCode.setText(Html.fromHtml("Postal code: " + "<b>" + etWizardPostalCode.getText().toString() + "</b>"));

        tvCheckInInstructions.setText(Html.fromHtml(availableRooms.getCheckInInstruction()));
        if (tvCheckInInstructions.getText().length() == 0) {
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

    private void setUiListeners() {
        btnAddNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MaterialNavigationDrawer) getActivity()).setFragmentChild(new PaymentCardsFragment(true), "Credit cards");
            }
        });
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
        String url = "https://book.api.ean.com/ean-services/rs/hotel/v3/res?" +
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
                "&creditCardNumber=" + currentPaymentCard.getCreditCardNumber() +
                "&creditCardIdentifier=" + currentPaymentCard.getCvvCode() +
                "&creditCardExpirationMonth=" + currentPaymentCard.getMonth() +
                "&creditCardExpirationYear=20" + currentPaymentCard.getYear() +
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
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.i("Response :", response.toString());
                try {
                    if (getActivity() != null) {
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
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        };

        DataLoader.makeRequest(true, url, listener, errorListener);
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
            bookedRoomSO.setRoomPrice(availableRooms.getRoomSO().get(position).getAverageRate());
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
        if (rGroupCreditCard.getCheckedRadioButtonId() == -1 & !isCreditCardInputsFilled()) {
            return false;
        }
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
        booking.put("ordered_room", orderedRooms);

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Response :", response.toString());
            }
        };
        DataLoader.makeRequest(true, DataLoader.apiUrl, new JSONObject(booking), listener);

        booking.clear();
        DateFormat dfm = new SimpleDateFormat("mm/dd/yyyy");
        HashMap<String, Object> expedia = new HashMap<>();

        expedia.put("hotelName",bookedRooms.get(0).getHotelName());
        expedia.put("hotelAddress",bookedRooms.get(0).getHotelAddress());
        expedia.put("ItineraryID", bookedRooms.get(0).getItineraryId());
        expedia.put("RoomName", bookedRooms.get(0).getRoomDescription());
        expedia.put("SumPrice", bookedRooms.get(0).getSumPrice());
        expedia.put("Currency", bookedRooms.get(0).getCurrency());
        try {
            expedia.put("StartDate", dfm.parse(bookedRooms.get(0).getArrivalDate()).getTime() / 1000);
            expedia.put("EndDate", dfm.parse(bookedRooms.get(0).getDepartureDate()).getTime() / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        expedia.put("Nights", bookedRooms.get(0).getNights());
        expedia.put("UserID", bookedRooms.get(0).getUserID());
        expedia.put("HotelID", bookedRooms.get(0).getHotelID());
        expedia.put("mail", etWizardEmail.getText().toString());
        booking.put("expedia_order", expedia);

        DataLoader.makeRequest(true, DataLoader.apiUrl, new JSONObject(booking), listener);
    }


    private void initCreditCardsRadioGroup() {
        try {
            paymentCards = AppState.getCreditCards();
        } catch (GeneralSecurityException e) {
            // Key is not valid
        }

        rGroupCreditCard = (RadioGroup) rootView.findViewById(R.id.rgroupCreditCards);
        rGroupCreditCard.removeAllViews();
        if (paymentCards != null) {
            for (int i = 0; i < paymentCards.size(); i++) {
                RadioButton radioButton = new RadioButton(getActivity());
                radioButton.setId(i);
                radioButton.setText(paymentCards.get(i).getHiddenCardNumber());
                radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        etWizardCreditCardNumber.setText("");
                        etWizardCreditCardExMonth.setText("");
                        etWizardCreditCardExYear.setText("");
                        etWizardCreditCardIdentifier.setText("");
                    }
                });
                rGroupCreditCard.addView(radioButton);
            }
        }
    }

    public boolean isCreditCardInputsFilled() {
        if (etWizardCreditCardNumber.getText().length() == 16 &
                etWizardCreditCardExYear.getText().length() == 2 &
                etWizardCreditCardExMonth.getText().length() == 2 &
                etWizardCreditCardIdentifier.getText().length() == 3) {

            currentPaymentCard = new PaymentCardSO(etWizardCreditCardNumber.getText().toString(), etWizardCreditCardExMonth.getText().toString(), etWizardCreditCardExYear.getText().toString(), etWizardCreditCardIdentifier.getText().toString());

            return true;
        }
        return false;
    }
}
