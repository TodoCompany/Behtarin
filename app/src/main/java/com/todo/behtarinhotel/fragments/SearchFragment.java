package com.todo.behtarinhotel.fragments;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.edmodo.rangebar.RangeBar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.todo.behtarinhotel.MainActivity;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.simpleobjects.SearchResultSO;
import com.todo.behtarinhotel.supportclasses.AppState;
import com.todo.behtarinhotel.supportclasses.DatePickerFragment;
import com.todo.behtarinhotel.supportclasses.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private static final String API_KEY = "7tuermyqnaf66ujk2dk3rkfk";
    private static final String CID = "55505";
    TextView tvCheckIn;
    TextView tvCheckOut;
    TextView tv;
    EditText etLocation;
    EditText etRoom;
    EditText etAdult;
    EditText etChildren;
    Button btnSearchForHotels;
    RangeBar rbStars;
    String url;
    String apiKey = "&apiKey=";
    String cid = "&cid=";
    String locale = "&locale=enUS";
    String customerSessionID = "&customerSessionID=1";
    String customerIpAddress = "&customerIpAddress=193.93.219.63";
    String arrivalDate = "&arrivalDate=";
    String currencyCode = "&currencyCode=USD";
    String departureDate = "&departureDate=";
    String city = "&destinationString=";
    String sig = "&sig=" + AppState.getMD5EncryptedString(apiKey + "RyqEsq69" + System.currentTimeMillis() / 1000L);
    String customerUserAgent = "&customerUserAgent=TravelWizard/1.0(iOS 10_10_3)MOBILE_APP";
    String minorRev = "&minorRev=30";
    String room = "&room1=";

    GsonBuilder gsonBuilder;
    Gson gson;

    boolean isCheckInSelected;

    ArrayList<SearchResultSO> searchResultSOArrayList;
    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            int realMonth = monthOfYear + 1;
            String month = "" + realMonth;
            String day = "" + dayOfMonth;
            if (realMonth < 10) {
                month = "0" + realMonth;
            }
            if (dayOfMonth < 10) {
                day = "0" + day;
            }
            if (isCheckInSelected) {
                tvCheckIn.setText(month + "/" + day + "/" + year);
            } else {
                tvCheckOut.setText(month + "/" + day + "/" + year);
            }
        }
    };


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        initViewsById(rootView);

        return rootView ;


    }

    private void initViewsById(View view) {

        tvCheckIn = (TextView) view.findViewById(R.id.tv_check_in_search_activity);
        tvCheckOut = (TextView) view.findViewById(R.id.tv_check_out_search_activity);

        View.OnClickListener oclTextViews = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.tv_check_in_search_activity){
                    isCheckInSelected = true;
                }else {
                    isCheckInSelected = false;
                }
                showDatePicker();
            }
        };
        tvCheckOut.setOnClickListener(oclTextViews);
        tvCheckIn.setOnClickListener(oclTextViews);

        etLocation = (EditText) view.findViewById(R.id.et_location_search_activity);
        etRoom = (EditText) view.findViewById(R.id.et_room_search_activity);
        etAdult = (EditText) view.findViewById(R.id.et_adult_search_activity);
        etChildren = (EditText) view.findViewById(R.id.et_children_search_activity);

        rbStars = (RangeBar) view.findViewById(R.id.rb_stars);
        rbStars.setTickCount(5);

        tv = (TextView) view.findViewById(R.id.textView);

        btnSearchForHotels = (Button) view.findViewById(R.id.btn_search_for_hotels_search_activity);
        btnSearchForHotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = "http://api.ean.com/ean-services/rs/hotel/v3/list?"+
                        apiKey+ API_KEY +
                        cid + CID +
                        sig +
                        customerIpAddress +
                        //customerUserAgent +
                        currencyCode +
                        customerSessionID +
                        minorRev +
                        locale +
                        city + etLocation.getText() +
                        arrivalDate + tvCheckIn.getText() +
                        departureDate + tvCheckOut.getText() +
                        room + etRoom.getText();

                gsonBuilder = new GsonBuilder();
                gson = gsonBuilder.create();

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                        url,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                JSONArray arr = null;
                                try {
                                    arr = response.getJSONObject("HotelListResponse").getJSONObject("HotelList").getJSONArray("HotelSummary");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Type listOfTestObject = new TypeToken<ArrayList<SearchResultSO>>(){}.getType();

                                if (arr != null) {
                                    searchResultSOArrayList = gson.fromJson(arr.toString(), listOfTestObject);
                                }

                                tv.setText(response.toString());
                                Log.d("MainActivity", url);
                                Log.d("MainActivity", searchResultSOArrayList.size() + "");
                                Log.d("MainActivity", response.toString());

                                //todo change fragment to mainfragment instead of activity

                                MainActivity parentActivity = (MainActivity) getActivity();
                                MainFragment mainFragment = new MainFragment();
                                parentActivity.setFragment(mainFragment, parentActivity.getString(R.string.fragment_availablehotels));
                                mainFragment.initMailList(searchResultSOArrayList, tvCheckIn.getText().toString(), tvCheckOut.getText().toString());
                                parentActivity.setMainSearchFragment(mainFragment);


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

        });
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }


}
