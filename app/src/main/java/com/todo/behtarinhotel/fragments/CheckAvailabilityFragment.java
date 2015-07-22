package com.todo.behtarinhotel.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.adapters.AvailableRoomsAdapter;
import com.todo.behtarinhotel.simpleobjects.AvailableRoomsSO;
import com.todo.behtarinhotel.supportclasses.AppState;
import com.todo.behtarinhotel.supportclasses.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckAvailabilityFragment extends Fragment {

    GsonBuilder gsonBuilder;
    Gson gson;
    AvailableRoomsSO availableRoomsSO;
    ListView roomsListView;
    ViewGroup rootView;


    public CheckAvailabilityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_check_availability, container, false);
        roomsListView = (ListView) rootView.findViewById(R.id.rooms_list_view);
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();


        return rootView;
    }

    public void getData(int hotelId, String dateArrival, String dateDeparture) {
        final String url = AppState.generateUrlForHotelAvailability(hotelId, dateArrival, dateDeparture);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("API", "Request was: " + url + "; Response: " + response.toString());
                try {
                    if (getActivity() != null) {
                        response = response.getJSONObject("HotelRoomAvailabilityResponse");
                        availableRoomsSO = gson.fromJson(response.toString(), AvailableRoomsSO.class);
                        AvailableRoomsAdapter adapter = new AvailableRoomsAdapter(getActivity(), availableRoomsSO);
                        roomsListView.setAdapter(adapter);
                        if (availableRoomsSO.getRoomSO().size() == 0){
                            setError("There are no free rooms in this hotel for that days");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("API", "parsing done");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", "Request was: " + url + "; Error response: " + error.toString());
                setError("Something went wrong =( cant get hotel rooms. Check your internet connection");
            }
        });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
    }

    private void setError(String errorText){
        if (getActivity() != null) {
            rootView.removeAllViews();
            TextView tvError = new TextView(getActivity());
            tvError.setText(errorText);
            rootView.addView(tvError);
        }
    }

}
