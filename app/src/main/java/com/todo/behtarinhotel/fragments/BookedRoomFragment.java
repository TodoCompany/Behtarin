package com.todo.behtarinhotel.fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gc.materialdesign.views.ButtonRectangle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.simpleobjects.BookedRoomSO;
import com.todo.behtarinhotel.supportclasses.AppState;
import com.todo.behtarinhotel.supportclasses.VolleySingleton;
import com.todo.behtarinhotel.views.MyMapView;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookedRoomFragment extends Fragment {

    private static final String API_KEY = "7tuermyqnaf66ujk2dk3rkfk";
    private static final String CID = "55505";

    String apiKey = "&apiKey=";
    String cid = "&cid=";
    String locale = "&locale=enUS";
    String customerSessionID = "&customerSessionID=1";
    String customerIpAddress = "&customerIpAddress=193.93.219.63";
    String sig = "&sig=" + AppState.getMD5EncryptedString(apiKey + "RyqEsq69" + System.currentTimeMillis() / 1000L);
    String minorRev = "&minorRev=30";
    String currencyCode = "&currencyCode=USD";
    String itineraryId = "&itineraryId=";
    String confirmationNumber = "&confirmationNumber=";
    String email = "&email=";

    ImageView roomImage;
    TextView tvHotelName, tvRoomDescription, tvHotelLocation, tvArrival, tvDeparture, tvRoomPrice;
    ButtonRectangle btnCancelBooking;
    View rootView;
    MyMapView mapView;
    GoogleMap googleMap;

    BookedRoomSO bookedRoomSO;
    RequestListener listener;

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
      // TODO coordinates must be not 0;  setUpMapIfNeeded(savedInstanceState);


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
        tvHotelLocation.setText(bookedRoomSO.getHotelAddress());
        tvArrival.setText(bookedRoomSO.getArrivalDate());
        tvDeparture.setText(bookedRoomSO.getDepartureDate());
        tvRoomPrice.setText("$" + bookedRoomSO.getRoomPrice());
    }

    private void setOnClickListeners() {
        btnCancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvMessage = new TextView(getActivity());
                tvMessage.setText(bookedRoomSO.getCancellationPolicy());
                tvMessage.setTextColor(getResources().getColor(R.color.base_white));
                tvMessage.setBackgroundColor(getResources().getColor(R.color.base_text));
                tvMessage.setPadding(16, 16, 16, 16);
                tvMessage.setTextSize(12);
                new AlertDialog.Builder(getActivity())
                        .setView(tvMessage)
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
                                //TODO cancel booking
                                final String url = "http://api.ean.com/ean-services/rs/hotel/v3/cancel?" +
                                        apiKey + API_KEY +
                                        cid + CID +
                                        sig +
                                        customerIpAddress +
                                        currencyCode +
                                        customerSessionID +
                                        minorRev +
                                        locale +
                                        itineraryId + bookedRoomSO.getItineraryId() +
                                        confirmationNumber + bookedRoomSO.getConfirmationNumber() +
                                        email + "someNiceEmail@gmail.com";
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                                        url,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Log.d("ExpediaRequest", "First hotels loading from: " + url);
                                                try {
                                                    response.getJSONObject("HotelRoomCancellationResponse");
                                                    AppState.removeRoomFromBooking(bookedRoomSO);
                                                    getActivity().onBackPressed();
                                                    Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();
                                                } catch (Exception e) {
                                                    Toast.makeText(getActivity(), "Bad", Toast.LENGTH_SHORT).show();
                                                    getActivity().onBackPressed();
                                                    dialog.dismiss();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        VolleyLog.e("Error: ", error.getMessage());

                                    }
                                }

                                );
                                VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);

                            }
                        })
                        .show();
            }
        });
    }

    public void initFragment(BookedRoomSO bookedRoomSO) {
        this.bookedRoomSO = bookedRoomSO;
    }


    private void setUpMapIfNeeded(Bundle savedInstanceState) {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mapView == null) {
            mapView = (MyMapView) rootView.findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);
            googleMap = mapView.getMap();
            if (googleMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(bookedRoomSO.getLatitude(), bookedRoomSO.getLongitude()))
                .title(bookedRoomSO.getHotelName())
                .snippet(bookedRoomSO.getHotelAddress()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(bookedRoomSO.getLatitude(), bookedRoomSO.getLongitude()), 12));

    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();

    }
}
