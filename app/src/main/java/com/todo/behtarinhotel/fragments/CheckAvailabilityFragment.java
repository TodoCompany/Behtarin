package com.todo.behtarinhotel.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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


    public CheckAvailabilityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_check_availability, container, false);
        roomsListView = (ListView) rootView.findViewById(R.id.rooms_list_view);
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        getData();


        return rootView;
    }

    private void getData() {
        final String url = AppState.generateUrlForHotelAvailability(324424, "09/03/2015", "09/04/2015");


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("API", "Request was: " + url + "; Response: " + response.toString());
                try {
                    response = response.getJSONObject("HotelRoomAvailabilityResponse");
                    availableRoomsSO = gson.fromJson(response.toString(), AvailableRoomsSO.class);
                    AvailableRoomsAdapter adapter = new AvailableRoomsAdapter(getActivity(), availableRoomsSO);
                    roomsListView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("API", "parsing done");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", "Request was: " + url + "; Error response: " + error.toString());
            }
        });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
    }


}
