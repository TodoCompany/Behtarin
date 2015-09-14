package com.todo.behtarinhotel.fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gc.materialdesign.views.ButtonRectangle;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.simpleobjects.BookedRoomSO;
import com.todo.behtarinhotel.supportclasses.AppState;
import com.todo.behtarinhotel.supportclasses.DataLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class BookedRoomFragment extends Fragment {

    ImageView roomImage;
    TextView tvHotelName, tvRoomDescription, tvHotelLocation, tvArrival, tvDeparture, tvRoomPrice, tvName, tvSmokingPreferences;
    ButtonRectangle btnCancelBooking;
    View rootView;

    BookedRoomSO bookedRoomSO;
    RequestListener listener;
    AlertDialog dialog;
    EditText et;

    public BookedRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_booked_room, container, false);

        initViews();
        initViewsWithData();
        setOnClickListeners();

        return rootView;
    }

    private void initViews() {
        roomImage = (ImageView) rootView.findViewById(R.id.roomImage);
        tvHotelName = (TextView) rootView.findViewById(R.id.tvHotelName);
        tvRoomDescription = (TextView) rootView.findViewById(R.id.tvRoomDescription);
        tvHotelLocation = (TextView) rootView.findViewById(R.id.tvHotelLocation);
        tvArrival = (TextView) rootView.findViewById(R.id.tvArrival);
        tvDeparture = (TextView) rootView.findViewById(R.id.tvDeparture);
        tvRoomPrice = (TextView) rootView.findViewById(R.id.tvRoomPrice);
        tvName = (TextView) rootView.findViewById(R.id.tv_name);
        tvSmokingPreferences = (TextView) rootView.findViewById(R.id.tv_smoking_preferences);
        btnCancelBooking = (ButtonRectangle) rootView.findViewById(R.id.btnCancelBooking);
    }

    private void initViewsWithData() {
        listener = new RequestListener() {
            @Override
            public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                String str = model.toString();
                Glide.with(getActivity().getApplicationContext())
                        .load(str.substring(0, str.length() - 5) + "b.jpg")
                        .into(target);
                return true;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        };
        if (bookedRoomSO.getPhotoUrl() != null && !bookedRoomSO.getPhotoUrl().equals("")) {
            String str = bookedRoomSO.getPhotoUrl().substring(0, bookedRoomSO.getPhotoUrl().length() - 5);
            Glide.with(getActivity())
                    .load(str + "z.jpg")
                    .listener(listener)
                    .into(roomImage);
        }
        tvHotelName.setText(bookedRoomSO.getHotelName());
        tvRoomDescription.setText(bookedRoomSO.getRoomDescription());
        tvHotelLocation.setText(Html.fromHtml(bookedRoomSO.getHotelAddress()));
        tvArrival.setText(bookedRoomSO.getArrivalDate());
        tvDeparture.setText(bookedRoomSO.getDepartureDate());
        tvRoomPrice.setText("$" + bookedRoomSO.getRoomPrice());
        tvName.setText(bookedRoomSO.getFirstName() + " " + bookedRoomSO.getLastName());

        switch (bookedRoomSO.getSmokingPreference()) {
            case "NS":
                tvSmokingPreferences.setText("Non smoking");
                break;
            case "S":
                tvSmokingPreferences.setText("Smoking");
                break;
            case "E":
                tvSmokingPreferences.setText("Either");
                break;
        }
    }

    private void setOnClickListeners() {
        btnCancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ll = new LinearLayout(getActivity());
                ll.setOrientation(LinearLayout.VERTICAL);
                TextView tvMessage = new TextView(getActivity());
                tvMessage.setText(bookedRoomSO.getCancellationPolicy());
                tvMessage.setTextColor(getResources().getColor(R.color.base_white));
                tvMessage.setBackgroundColor(getResources().getColor(R.color.base_text));
                tvMessage.setPadding(16, 16, 16, 16);
                tvMessage.setTextSize(12);
                et = new EditText(getActivity());
                et.setText(AppState.getLoggedUser().getEmail());
                ll.addView(tvMessage);
                ll.addView(et);
                dialog = new AlertDialog.Builder(getActivity())
                        .setView(ll)
                        .setTitle("Cancel booking?")
                        .setNegativeButton("No, don't cancel booking", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Yes, cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                makeCancellationRequest();
                            }
                        })
                        .show();
            }
        });
    }

    public void initFragment(BookedRoomSO bookedRoomSO) {
        this.bookedRoomSO = bookedRoomSO;
    }

    private void sendDataToApi(JSONObject obj, int cancellationNumber) {
        HashMap<String, Object> cancellation = new HashMap<>();
        HashMap<String, Object> data = new HashMap<>();
        data.put("cancellationNumber", cancellationNumber);
        data.put("confirmationNumber", bookedRoomSO.getConfirmationNumber());
        data.put("itineraryID", bookedRoomSO.getItineraryId());
        data.put("cancellationDate", System.currentTimeMillis() / 1000);
        data.put("chargeableRateInfo", obj);
        cancellation.put("ordered_cancellation", data);

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        };

        DataLoader.makeRequest(true, DataLoader.apiUrl, new JSONObject(cancellation), listener);
    }

    private void makeItineraryRequest(final int cancellationNumber) {

        final String itineraryUrl = "http://api.ean.com/ean-services/rs/hotel/v3/cancel?" +
                DataLoader.apiKey + DataLoader.API_KEY +
                DataLoader.cid + DataLoader.CID +
                DataLoader.sig +
                DataLoader.customerIpAddress +
                DataLoader.currencyCode +
                DataLoader.customerSessionID +
                DataLoader.minorRev +
                DataLoader.itineraryId + bookedRoomSO.getItineraryId() +
                DataLoader.email + et.getText().toString();

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ExpediaRequest", "First hotels loading from: ");
                try {
                    JSONObject obj = response.getJSONObject("Itinerary").getJSONObject("HotelConfirmation").getJSONObject("RateInfos").getJSONObject("RateInfo").getJSONObject("ChargeableRateInfo");
                    sendDataToApi(obj, cancellationNumber);
                } catch (Exception e) {
                    try {
                        Toast.makeText(AppState.getMyContext(), response.getJSONObject("HotelRoomCancellationResponse").getJSONObject("EanWsError").getString("presentationMessage"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    dialog.dismiss();
                }
            }
        };
        DataLoader.makeRequest(itineraryUrl, listener);
    }

    private void makeCancellationRequest() {

        final String url = "http://api.ean.com/ean-services/rs/hotel/v3/cancel?" +
                DataLoader.apiKey + DataLoader.API_KEY +
                DataLoader.cid + DataLoader.CID +
                DataLoader.sig +
                DataLoader.customerIpAddress +
                DataLoader.currencyCode +
                DataLoader.customerSessionID +
                DataLoader.minorRev +
                DataLoader.locale +
                DataLoader.itineraryId + bookedRoomSO.getItineraryId() +
                DataLoader.confirmationNumber + bookedRoomSO.getConfirmationNumber() +
                DataLoader.email + et.getText().toString();

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int cancellationNumber = response.getJSONObject("HotelRoomCancellationResponse").getInt("cancellationNumber");
                    AppState.removeRoomFromBooking(bookedRoomSO);
                    getActivity().onBackPressed();
                    Toast.makeText(getActivity(), "Successfully canceled", Toast.LENGTH_SHORT).show();
                    makeItineraryRequest(cancellationNumber);
                } catch (Exception e) {
                    try {
                        Toast.makeText(AppState.getMyContext(), response.getJSONObject("HotelRoomCancellationResponse").getJSONObject("EanWsError").getString("presentationMessage"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    dialog.dismiss();
                }
            }
        };
        DataLoader.makeRequest(url, listener);
    }
}
