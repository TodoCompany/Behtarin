package com.todo.behtarinhotel.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.adapters.AvailableRoomsAdapter;
import com.todo.behtarinhotel.simpleobjects.AvailableRoomsSO;
import com.todo.behtarinhotel.simpleobjects.SearchRoomSO;
import com.todo.behtarinhotel.supportclasses.AppState;
import com.todo.behtarinhotel.supportclasses.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckAvailabilityFragment extends Fragment {

    GsonBuilder gsonBuilder;
    Gson gson;
    AvailableRoomsSO availableRoomsSO;
    ListView roomsListView;
    ViewGroup rootView;
    TextView tvError;

    int hotelId;
    String arrivalDate, departureDate;
    ArrayList<SearchRoomSO> rooms;

    private SwipeRefreshLayout swipeContainer;
    private ProgressBarCircularIndeterminate progressBar;


    public CheckAvailabilityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_check_availability, container, false);
        roomsListView = (ListView) rootView.findViewById(R.id.rooms_list_view);
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        progressBar = (ProgressBarCircularIndeterminate) rootView.findViewById(R.id.pbRoomLoading);
        tvError = (TextView) rootView.findViewById(R.id.tvError);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                getData(hotelId, arrivalDate, departureDate, rooms);
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

    public void getData(int hotelId, String dateArrival, String dateDeparture, final ArrayList<SearchRoomSO> rooms) {
        this.hotelId = hotelId;
        this.arrivalDate = dateArrival;
        this.departureDate = dateDeparture;
        this.rooms = rooms;
        showLoadingScreen();
        final String url = AppState.generateUrlForHotelAvailability(hotelId, dateArrival, dateDeparture, rooms);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("API", "Request was: " + url + "; Response: " + response.toString());
                try {
                    if (getActivity() != null) {
                        response = response.getJSONObject("HotelRoomAvailabilityResponse");
                        availableRoomsSO = gson.fromJson(response.toString(), AvailableRoomsSO.class);
                        JSONArray hotelRoomsResponse = response.getJSONArray("HotelRoomResponse");
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
                            } catch (Exception isNotObject) {
                                JSONArray arr = rateInfos.getJSONObject("RateInfo")
                                        .getJSONObject("RoomGroup").getJSONArray("Room");
                                    String rateKey = arr.getJSONObject(0).getString("rateKey");
                                    availableRoomsSO.getRoomSO().get(i).setRateKey(rateKey);

                            }

                            availableRoomsSO.getRoomSO().get(i).setBedDescription(bedDescription);
                            availableRoomsSO.getRoomSO().get(i).setBed(beds);


                        }


                        AvailableRoomsAdapter adapter = new AvailableRoomsAdapter((MaterialNavigationDrawer) getActivity(), availableRoomsSO, rooms, arrivalDate, departureDate);
                        roomsListView.setAdapter(adapter);
                        if (availableRoomsSO.getRoomSO().size() == 0) {
                            showError("There are no free rooms in this hotel for that days");
                        } else {
                            clearLoadingScreen();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IllegalStateException ie) {

                }
                Log.d("API", "parsing done");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", "Request was: " + url + "; Error response: " + error.toString());
                showError("Something went wrong =( cant get hotel rooms. Check your internet connection");
            }
        });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
    }

    private void showError(String errorMessage) {

        progressBar.setVisibility(View.GONE);
        swipeContainer.setRefreshing(false);
        if (tvError != null) {
            tvError.setText("Error: " + errorMessage);
            tvError.setVisibility(View.VISIBLE);
        }
    }

    private void showLoadingScreen() {
        if (tvError != null) {
            tvError.setVisibility(View.GONE);
        }
    }

    private void clearLoadingScreen() {
        if (tvError != null) {
            tvError.setVisibility(View.GONE);
        }
        progressBar.setVisibility(View.GONE);
        swipeContainer.setRefreshing(false);

    }



}