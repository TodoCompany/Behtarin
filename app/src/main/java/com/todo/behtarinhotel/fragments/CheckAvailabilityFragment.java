package com.todo.behtarinhotel.fragments;


import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.adapters.AvailableRoomsAdapter;
import com.todo.behtarinhotel.adapters.MainActivityMainListAdapter;
import com.todo.behtarinhotel.simpleobjects.AvailableRoomsSO;
import com.todo.behtarinhotel.simpleobjects.SearchResultSO;
import com.todo.behtarinhotel.simpleobjects.SearchRoomSO;
import com.todo.behtarinhotel.supportclasses.AppState;
import com.todo.behtarinhotel.supportclasses.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckAvailabilityFragment extends Fragment {

    private static final int NO_INTERNET = 1;


    GsonBuilder gsonBuilder;
    Gson gson;
    AvailableRoomsSO availableRoomsSO;
    ListView roomsListView;
    ViewGroup rootView;
    TextView tvError;
    LinearLayout errorLayout;
    ButtonFlat buttonFlat;

    int hotelId;
    String arrivalDate, departureDate;
    ArrayList<SearchRoomSO> rooms;
    boolean isErrorShowing = false;

    private SwipeRefreshLayout swipeContainer;
    private ProgressBarCircularIndeterminate progressBar;
    NetworkStateReceiver networkStateReceiver = new NetworkStateReceiver();
    float longitude, latitude;


    public CheckAvailabilityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_check_availability, container, false);
        roomsListView = (ListView) rootView.findViewById(R.id.rooms_list_view);
        errorLayout = (LinearLayout) rootView.findViewById(R.id.errorLayout);
        tvError = (TextView) rootView.findViewById(R.id.tvError);
        errorLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        errorLayout.setGravity(Gravity.CENTER);
        getActivity().registerReceiver(
                networkStateReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));

        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        progressBar = (ProgressBarCircularIndeterminate) rootView.findViewById(R.id.pbRoomLoading);
        progressBar.setVisibility(View.VISIBLE);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.GONE);
                getData(hotelId, arrivalDate, departureDate, rooms);
            }
        });
        buttonFlat = (ButtonFlat) rootView.findViewById(R.id.btnCheckWifi);
        buttonFlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                startActivity(intent);
            }
        });

        swipeContainer.setColorSchemeResources(R.color.base_yellow);
        progressBar.setVisibility(View.VISIBLE);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(hotelId, arrivalDate, departureDate, rooms);
    }

    public void setCoordinates(float longitude, float latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void getData(int hotelId, String dateArrival, String dateDeparture, final ArrayList<SearchRoomSO> rooms) {
        this.hotelId = hotelId;
        this.arrivalDate = dateArrival;
        this.departureDate = dateDeparture;
        this.rooms = rooms;

        final String url = AppState.generateUrlForHotelAvailability(hotelId, dateArrival, dateDeparture, rooms);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("API", "Request was: " + url + "; Response: " + response.toString());
                try {
                    if (getActivity() != null) {
                        response = response.getJSONObject("HotelRoomAvailabilityResponse");
                        availableRoomsSO = gson.fromJson(response.toString(), AvailableRoomsSO.class);
                        ArrayList<Float> nightRates = new ArrayList<>();
                        try {
                            JSONArray hotelRoomsResponse = response.getJSONArray("HotelRoomResponse");
                            Type roomArray = new TypeToken<ArrayList<AvailableRoomsSO.RoomSO>>() {
                            }.getType();
                            ArrayList<AvailableRoomsSO.RoomSO> rooms = new ArrayList<>();
                            rooms = gson.fromJson(hotelRoomsResponse.toString(), roomArray);
                            availableRoomsSO.setRoomSO(rooms);

                            for (int i = 0; i < hotelRoomsResponse.length(); i++) {
                                JSONObject bedTypesObject = hotelRoomsResponse.getJSONObject(i).getJSONObject("BedTypes");
                                JSONObject rateInfos = hotelRoomsResponse.getJSONObject(i).getJSONObject("RateInfos");

                                String bedDescription = "";
                                ArrayList<AvailableRoomsSO.Bed> beds = new ArrayList<>();
                                try {
                                    JSONArray bedTypesArray = bedTypesObject.getJSONArray("BedType");
                                    availableRoomsSO.getRoomSO().get(i).setBedsQuantity(bedTypesArray.length());
                                    for (int n = 0; n < bedTypesArray.length(); n++) {
                                        JSONObject object = bedTypesArray.getJSONObject(n);
                                        bedDescription = bedDescription + object.getString("description") + "\n";
                                        AvailableRoomsSO.Bed bed = availableRoomsSO.buildBedObject();
                                        bed.setBedDescript(object.getString("description"));
                                        bed.setId(object.getInt("@id"));
                                        beds.add(bed);
                                    }
                                } catch (Exception itIsNotAnArray) {

                                    JSONObject bedType = bedTypesObject.getJSONObject("BedType");
                                    bedDescription = bedDescription + bedType.getString("description") + "\n";
                                    availableRoomsSO.getRoomSO().get(i).setBedsQuantity(1);
                                    AvailableRoomsSO.Bed bed = availableRoomsSO.buildBedObject();
                                    bed.setBedDescript(bedType.getString("description"));
                                    bed.setId(bedType.getInt("@id"));
                                    beds.add(bed);
                                }

                                try {
                                    String rateKey = rateInfos.getJSONObject("RateInfo")
                                            .getJSONObject("RoomGroup").getJSONObject("Room").getString("rateKey");
                                    availableRoomsSO.getRoomSO().get(0).setRateKey(rateKey);
                                    try {
                                        Float nightRate = (float) rateInfos.getJSONObject("RateInfo")
                                                .getJSONObject("RoomGroup").getJSONObject("Room").getJSONObject("ChargeableNightlyRates").getDouble("@rate");
                                        float[] arr = new float[]{nightRate};
                                        nightRates.add(nightRate);
                                        availableRoomsSO.getRoomSO().get(i).setNightlyRates(arr);
                                    } catch (Exception isNotObject) {
                                        JSONArray nightRatesArr = rateInfos.getJSONObject("RateInfo")
                                                .getJSONObject("RoomGroup").getJSONObject("Room").getJSONArray("ChargeableNightlyRates");
                                        float[] arr = new float[nightRatesArr.length()];
                                        for (int z = 0; z < nightRatesArr.length(); z++) {
                                            arr[z] = ((float) nightRatesArr.getJSONObject(z).getDouble("@rate"));
                                        }
                                        availableRoomsSO.getRoomSO().get(i).setNightlyRates(arr);
                                    }
                                } catch (Exception isNotObject) {
                                    JSONArray arr = rateInfos.getJSONObject("RateInfo")
                                            .getJSONObject("RoomGroup").getJSONArray("Room");
                                    String rateKey = arr.getJSONObject(0).getString("rateKey");
                                    availableRoomsSO.getRoomSO().get(i).setRateKey(rateKey);
                                    try {
                                        Float nightRate = (float) rateInfos.getJSONObject("RateInfo")
                                                .getJSONObject("RoomGroup").getJSONArray("Room").getJSONObject(0).getJSONObject("ChargeableNightlyRates").getDouble("@rate");
                                        float[] nightRatesArr = new float[]{nightRate};
                                        nightRates.add(nightRate);
                                        availableRoomsSO.getRoomSO().get(i).setNightlyRates(nightRatesArr);
                                    } catch (Exception NightlyRatesIsNotObject) {
                                        JSONArray nightRatesArr = rateInfos.getJSONObject("RateInfo")
                                                .getJSONObject("RoomGroup").getJSONArray("Room").getJSONObject(0).getJSONArray("ChargeableNightlyRates");
                                        float[] nightRatesArray = new float[nightRatesArr.length()];
                                        for (int z = 0; z < nightRatesArr.length(); z++) {
                                            nightRatesArray[z] = ((float) nightRatesArr.getJSONObject(z).getDouble("@rate"));
                                        }
                                        availableRoomsSO.getRoomSO().get(i).setNightlyRates(nightRatesArray);
                                    }
                                }

                                availableRoomsSO.getRoomSO().get(i).setBedDescription(bedDescription);
                                availableRoomsSO.getRoomSO().get(i).setBed(beds);


                            }
                        } catch (Exception isNotArray) {
                            JSONObject hotelRoomsResponse = response.getJSONObject("HotelRoomResponse");
                            ArrayList<AvailableRoomsSO.RoomSO> rooms = new ArrayList<>();
                            AvailableRoomsSO.RoomSO room = gson.fromJson(hotelRoomsResponse.toString(), AvailableRoomsSO.RoomSO.class);
                            rooms.add(room);
                            availableRoomsSO.setRoomSO(rooms);
                            JSONObject bedTypesObject = hotelRoomsResponse.getJSONObject("BedTypes");
                            JSONObject rateInfos = hotelRoomsResponse.getJSONObject("RateInfos");

                            String bedDescription = "";
                            ArrayList<AvailableRoomsSO.Bed> beds = new ArrayList<>();
                            try {
                                JSONArray bedTypesArray = bedTypesObject.getJSONArray("BedType");
                                availableRoomsSO.getRoomSO().get(0).setBedsQuantity(bedTypesArray.length());
                                for (int n = 0; n < bedTypesArray.length(); n++) {
                                    JSONObject object = bedTypesArray.getJSONObject(n);
                                    bedDescription = bedDescription + object.getString("description") + "\n";
                                    AvailableRoomsSO.Bed bed = availableRoomsSO.buildBedObject();
                                    bed.setBedDescript(object.getString("description"));
                                    bed.setId(object.getInt("@id"));
                                    beds.add(bed);
                                }
                            } catch (Exception itIsNotAnArray) {

                                JSONObject bedType = bedTypesObject.getJSONObject("BedType");
                                bedDescription = bedDescription + bedType.getString("description") + "\n";
                                availableRoomsSO.getRoomSO().get(0).setBedsQuantity(1);
                                AvailableRoomsSO.Bed bed = availableRoomsSO.buildBedObject();
                                bed.setBedDescript(bedType.getString("description"));
                                bed.setId(bedType.getInt("@id"));
                                beds.add(bed);
                            }

                            try {
                                String rateKey = rateInfos.getJSONObject("RateInfo")
                                        .getJSONObject("RoomGroup").getJSONObject("Room").getString("rateKey");
                                availableRoomsSO.getRoomSO().get(0).setRateKey(rateKey);
                                try {
                                    Float nightRate = (float) rateInfos.getJSONObject("RateInfo")
                                            .getJSONObject("RoomGroup").getJSONObject("Room").getJSONObject("ChargeableNightlyRates").getDouble("@rate");
                                    float[] nightRatesArr = new float[]{nightRate};
                                    nightRates.add(nightRate);
                                    availableRoomsSO.getRoomSO().get(0).setNightlyRates(nightRatesArr);
                                } catch (Exception NightlyRatesIsNotObject) {
                                    JSONArray nightRatesArr = rateInfos.getJSONObject("RateInfo")
                                            .getJSONObject("RoomGroup").getJSONObject("Room").getJSONArray("ChargeableNightlyRates");
                                    float[] nightRatesArray = new float[nightRatesArr.length()];
                                    for (int z = 0; z < nightRatesArr.length(); z++) {
                                        nightRatesArray[z] = ((float) nightRatesArr.getJSONObject(z).getDouble("@rate"));
                                    }
                                    availableRoomsSO.getRoomSO().get(0).setNightlyRates(nightRatesArray);
                                }
                            } catch (Exception isNotObject) {
                                JSONArray arr = rateInfos.getJSONObject("RateInfo")
                                        .getJSONObject("RoomGroup").getJSONArray("Room");
                                String rateKey = arr.getJSONObject(0).getString("rateKey");
                                availableRoomsSO.getRoomSO().get(0).setRateKey(rateKey);
                                try {
                                    Float nightRate = (float) rateInfos.getJSONObject("RateInfo")
                                            .getJSONObject("RoomGroup").getJSONArray("Room").getJSONObject(0).getJSONObject("ChargeableNightlyRates").getDouble("@rate");
                                    float[] nightRatesArr = new float[]{nightRate};
                                    nightRates.add(nightRate);
                                    availableRoomsSO.getRoomSO().get(0).setNightlyRates(nightRatesArr);
                                } catch (Exception NightlyRatesIsNotObject) {
                                    JSONArray nightRatesArr = rateInfos.getJSONObject("RateInfo")
                                            .getJSONObject("RoomGroup").getJSONArray("Room").getJSONObject(0).getJSONArray("ChargeableNightlyRates");
                                    float[] nightRatesArray = new float[nightRatesArr.length()];
                                    for (int z = 0; z < nightRatesArr.length(); z++) {
                                        nightRatesArray[z] = ((float) nightRatesArr.getJSONObject(z).getDouble("@rate"));
                                    }
                                    availableRoomsSO.getRoomSO().get(0).setNightlyRates(nightRatesArray);
                                }
                            }

                            availableRoomsSO.getRoomSO().get(0).setBedDescription(bedDescription);
                            availableRoomsSO.getRoomSO().get(0).setBed(beds);
                        }


                        AvailableRoomsAdapter adapter = new AvailableRoomsAdapter((MaterialNavigationDrawer) getActivity(), availableRoomsSO, rooms, arrivalDate, departureDate, longitude, latitude);
                        roomsListView.setAdapter(adapter);
                        if (availableRoomsSO.getRoomSO().size() == 0) {
                            showError(NO_INTERNET);
                        } else {
                            clearLoadingScreen();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IllegalStateException ie) {
                    ie.printStackTrace();
                }
                Log.d("API", "parsing done");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", "Request was: " + url + "; Error response: " + error.toString());
                showError(NO_INTERNET);
            }
        });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
    }

    private void showError(int errorCode) {
        if(getActivity() != null) {
            roomsListView.setAdapter(new MainActivityMainListAdapter(getActivity(), new ArrayList<SearchResultSO>(), "", "", new ArrayList<SearchRoomSO>(), "", "", ""));
            isErrorShowing = true;
            progressBar.setVisibility(View.GONE);
            swipeContainer.setRefreshing(false);

            switch (errorCode) {
                case NO_INTERNET:
                    tvError.setText("Error: No internet");
                    buttonFlat.setVisibility(View.VISIBLE);
                    break;
            }

            errorLayout.setVisibility(View.VISIBLE);
        }
    }



    private void clearLoadingScreen() {
        isErrorShowing = false;
        errorLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        tvError.setText("No hotels found");
        roomsListView.setEmptyView(errorLayout);
        swipeContainer.setRefreshing(false);
    }


    public class NetworkStateReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            Log.d("app", "Network connectivity change");
            if(intent.getExtras()!=null) {
                NetworkInfo ni=(NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
                if(ni!=null && ni.getState()==NetworkInfo.State.CONNECTED) {
                    Log.i("app","Network "+ni.getTypeName()+" connected");
                    if(isErrorShowing){
                        progressBar.setVisibility(View.VISIBLE);
                        getData(hotelId, arrivalDate, departureDate, rooms);
                    }
                }
            }
            if(intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE)) {
                Log.d("app", "There's no network connectivity");
                clearLoadingScreen();
                showError(NO_INTERNET);

            }
        }
    }


}