package com.todo.behtarinhotel.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.adapters.MainActivityMainListAdapter;
import com.todo.behtarinhotel.simpleobjects.FilterSO;
import com.todo.behtarinhotel.simpleobjects.SearchParamsSO;
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
public class MainFragment extends Fragment {

    private static final String API_KEY = "7tuermyqnaf66ujk2dk3rkfk";
    private static final String CID = "55505";

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
    String minorRev = "&minorRev=30";
    String minStar = "&minStarRating=";

    GsonBuilder gsonBuilder;
    Gson gson;

    View rootView;
    TextView tvError;

    SearchParamsSO searchParams;
    ArrayList<SearchResultSO> searchResultSOArrayList = new ArrayList<>();
    ListView listView;
    SlideExpandableListAdapter slideExpandableListAdapter;

    private SwipeRefreshLayout swipeContainer;
    private ProgressBarCircularIndeterminate progressBar;
    FilterSO filterParams;


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        listView = (ListView) rootView.findViewById(R.id.lv_main_list_main_activity);
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        progressBar = (ProgressBarCircularIndeterminate) rootView.findViewById(R.id.pbHotelLoading);
        tvError = (TextView) rootView.findViewById(R.id.tvError);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                loadDataFromExpedia();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.base_yellow);
        if (searchResultSOArrayList.isEmpty()) {
            loadDataFromExpedia();
            progressBar.setVisibility(View.VISIBLE);
        } else {
            listView.setAdapter(slideExpandableListAdapter);
        }
        Button btnFilter = (Button) rootView.findViewById(R.id.btn_filter_main_fragment);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterFragment filterFragment = new FilterFragment();
                if (filterParams == null) {
                    filterFragment.setFilterParams(new FilterSO(0, 1000, 1, 5, 1, 5), MainFragment.this);
                } else {
                    filterFragment.setFilterParams(filterParams, MainFragment.this);
                }
                ((MaterialNavigationDrawer) getActivity()).setFragmentChild(filterFragment, "Filter");
            }
        });


        return rootView;
    }

    public void setSearchParams(SearchParamsSO searchParams) {
        this.searchParams = searchParams;
        filterParams = new FilterSO(0,5000,0,4,0,4);
        filterParams.setMinStarRate(searchParams.getMinStar());
    }

    public void loadDataFromExpedia() {
        if (searchParams != null) {
            showLoadingScreen();
            url = "http://api.ean.com/ean-services/rs/hotel/v3/list?" +
                    apiKey + API_KEY +
                    cid + CID +
                    sig +
                    customerIpAddress +
                    currencyCode +
                    customerSessionID +
                    minorRev +
                    locale +
                    city + searchParams.getCity() +
                    arrivalDate + searchParams.getArrivalDate() +
                    departureDate + searchParams.getDepartureDate() +
                    makeRoomString(searchParams.getRooms()) +
                    makeFilterString()
            ;


            gsonBuilder = new GsonBuilder();
            gson = gsonBuilder.create();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String cacheLocation = "";
                            String cacheKey = "";
                            try {
                                cacheLocation = response.getJSONObject("HotelListResponse").getString("cacheLocation");
                                cacheKey = response.getJSONObject("HotelListResponse").getString("cacheKey");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            JSONArray arr = null;
                            try {
                                arr = response.getJSONObject("HotelListResponse").getJSONObject("HotelList").getJSONArray("HotelSummary");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Type listOfTestObject = new TypeToken<ArrayList<SearchResultSO>>() {
                            }.getType();

                            if (arr == null || arr.length() == 0) {
                                showError("No hotels");
                                return;
                            }

                            if (getActivity() != null) {
                                searchResultSOArrayList = new ArrayList<>();
                                searchResultSOArrayList = gson.fromJson(arr.toString(), listOfTestObject);
                                MainActivityMainListAdapter adapter = new MainActivityMainListAdapter(getActivity(), searchResultSOArrayList, searchParams.getArrivalDate(), searchParams.getDepartureDate(), searchParams.getRooms(), cacheKey, cacheLocation, url);
                                slideExpandableListAdapter = new SlideExpandableListAdapter(adapter, R.id.hotel_layout, R.id.expandableLayout);
                                listView.setAdapter(slideExpandableListAdapter);
                                clearLoadingScreen();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    showError(error.getMessage());
                }
            }

            );
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
        }
    }

    private void showError(String errorMessage) {
        progressBar.setVisibility(View.GONE);
        swipeContainer.setRefreshing(false);
        tvError.setText("Error: " + errorMessage);
        tvError.setVisibility(View.VISIBLE);
    }

    private void showLoadingScreen() {
        tvError.setVisibility(View.GONE);

    }

    private void clearLoadingScreen() {
        tvError.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        TextView tvError = new TextView(getActivity());
        tvError.setText("No hotels found");
        listView.setEmptyView(tvError);
        swipeContainer.setRefreshing(false);
    }

    private String makeRoomString(ArrayList<SearchRoomSO> rooms) {
        String room = "";
        for (int a = 0; a < rooms.size(); a++) {
            room = room + "&room" + (a + 1) + "=";
            for (int b = 0; b < rooms.get(a).getGuests().size(); b++) {
                if (b == rooms.get(a).getGuests().size() - 1) {
                    room = room + rooms.get(a).getGuests().get(b).getAge();
                } else {
                    room = room + rooms.get(a).getGuests().get(b).getAge() + ",";
                }
            }

        }
        return room;
    }

    public void setFilteredResults(FilterSO filter) {
        filterParams = filter;
        loadDataFromExpedia();
    }

    private String makeFilterString(){
        String filter = "";
        if(filterParams==null){
            return filter;
        }
        filter = filter + "&minRate=" + filterParams.getMinPrice() + "&maxRate=" + filterParams.getMaxPrice()+
                "&minStarRating=" + filterParams.getMinStarRate() + "&maxStarRating=" + filterParams.getMaxStarRate()+
                "&minTripAdvisorRating=" + filterParams.getMinTripRate() + "&maxTripAdvisorRating=" + filterParams.getMaxTripRate();
        return filter;
    }

}