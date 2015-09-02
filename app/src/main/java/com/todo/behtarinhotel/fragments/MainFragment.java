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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gc.materialdesign.views.ButtonFlat;
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
import java.util.Collections;
import java.util.Comparator;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private static final String API_KEY = "7tuermyqnaf66ujk2dk3rkfk";
    private static final String CID = "55505";

    private static final int NO_SORTING = 0;
    private static final int SORT_BY_HIGHEST_PRICE = 1;
    private static final int SORT_BY_LOWEST_PRICE = 2;
    private static final int SORT_BY_NAME = 3;
    private int sortingType = NO_SORTING;

    private static final int NO_INTERNET = 1, NO_HOTELS = 2, HOTEL_NOT_AVAILABLE = 3;

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
    String hotelIdList = "&hotelIdList=";
    String minStar = "&minStarRating=";
    String limit = "&numberOfResults=";
    String cacheKey, cacheLocation;
    int hotelId;

    GsonBuilder gsonBuilder;
    Gson gson;

    View rootView;
    TextView tvError;
    ImageView imageSort;
    PopupMenu popupMenu;
    ImageView btnFilter;
    LinearLayout errorLayout;

    SearchParamsSO searchParams;
    ArrayList<SearchResultSO> searchResultSOArrayList = new ArrayList<>();
    ListView listView;
    SlideExpandableListAdapter slideExpandableListAdapter;
    MainActivityMainListAdapter adapter;
    JsonObjectRequest jsonObjectRequest;

    private SwipeRefreshLayout swipeContainer;
    private ProgressBarCircularIndeterminate progressBar;
    FilterSO filterParams;
    NetworkStateReceiver networkStateReceiver = new NetworkStateReceiver();
    ButtonFlat buttonFlat;

    JsonObjectRequest nextPageRequest;
    View ll;

    // Used to count "null" responses from expedia and start new requests
    int expediaBadResponseCounter = 0;

    boolean isFiltersChanged = false;
    boolean isErorShowing = false;
    private boolean isWishListSearch;


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
        tvError.setGravity(Gravity.CENTER);
        errorLayout = (LinearLayout) rootView.findViewById(R.id.errorLayout);
        errorLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        errorLayout.setGravity(Gravity.CENTER);
        buttonFlat = (ButtonFlat) rootView.findViewById(R.id.btnCheckWifi);
        buttonFlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                startActivity(intent);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.GONE);
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

        getActivity().registerReceiver(
                networkStateReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));


        return rootView;
    }

    public void setSearchParams(SearchParamsSO searchParams, boolean isWishListSearch, int hotelId) {
        this.searchParams = searchParams;
        filterParams = new FilterSO(0, 5000, 0, 4, 0, 4);
        filterParams.setMinStarRate(searchParams.getMinStar() - 1);
        this.isWishListSearch = isWishListSearch;
        this.hotelId = hotelId;
    }


    public void loadDataFromExpedia() {
        if (searchParams != null) {
            if (isWishListSearch) {
                url = "http://api.ean.com/ean-services/rs/hotel/v3/list?" +
                        apiKey + API_KEY +
                        cid + CID +
                        sig +
                        customerIpAddress +
                        currencyCode +
                        customerSessionID +
                        minorRev +
                        locale +
                        hotelIdList + hotelId +
                        arrivalDate + searchParams.getArrivalDate() +
                        departureDate + searchParams.getDepartureDate() +
                        makeRoomString(searchParams.getRooms())
                ;

            } else {
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
                        limit + 200 +
                        makeFilterString()
                ;
            }
            gsonBuilder = new GsonBuilder();
            gson = gsonBuilder.create();

            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("ExpediaRequest", "First hotels loading from: " + url);
                            expediaBadResponseCounter = 0;
                            try {
                                cacheLocation = "&cacheLocation=" + response.getJSONObject("HotelListResponse").getString("cacheLocation");
                                cacheKey = "&cacheKey=" + response.getJSONObject("HotelListResponse").getString("cacheKey");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            JSONArray arr = null;
                            JSONObject obj = null;
                            try {
                                arr = response.getJSONObject("HotelListResponse").getJSONObject("HotelList").getJSONArray("HotelSummary");
                            } catch (JSONException e) {
                                try {
                                    obj = response.getJSONObject("HotelListResponse").getJSONObject("HotelList").getJSONObject("HotelSummary");
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                                e.printStackTrace();
                            }
                            Type listOfTestObject = new TypeToken<ArrayList<SearchResultSO>>() {
                            }.getType();

                            if (isWishListSearch) {
                                try {
                                    SearchResultSO so = gson.fromJson(obj.toString(), SearchResultSO.class);
                                    searchResultSOArrayList = new ArrayList<>();
                                    searchResultSOArrayList.add(so);
                                    if (getActivity() != null) {
                                        adapter = new MainActivityMainListAdapter(getActivity(), searchResultSOArrayList, searchParams.getArrivalDate(), searchParams.getDepartureDate(), searchParams.getRooms(), cacheKey, cacheLocation, url);
                                        slideExpandableListAdapter = new SlideExpandableListAdapter(adapter, R.id.hotel_layout, R.id.expandableLayout);
                                        listView.setAdapter(slideExpandableListAdapter);
                                        clearLoadingScreen();
                                    }
                                }catch (NullPointerException e){
                                    // Hotel have no free roms for that date
                                    showError(HOTEL_NOT_AVAILABLE);
                                }
                            } else {
                                if (arr == null || arr.length() == 0) {
                                    showError(NO_HOTELS);
                                    return;
                                }

                                if (getActivity() != null) {
                                    searchResultSOArrayList = gson.fromJson(arr.toString(), listOfTestObject);
                                    Log.d("ExpediaRequest", "There was " + searchResultSOArrayList.size() + " hotels");
                                    loadNextHotels();

                                }
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    expediaBadResponseCounter++;
                    if (expediaBadResponseCounter > 3){
                        showError(NO_INTERNET);
                    }else{
                        VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
                    }
                }
            }

            );
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
        }

    }

    private void showError(int errorCode) {
        if(getActivity() != null) {
            listView.setAdapter(new MainActivityMainListAdapter(getActivity(), new ArrayList<SearchResultSO>(), "", "", new ArrayList<SearchRoomSO>(), "", "", ""));
            isErorShowing = true;
            progressBar.setVisibility(View.GONE);
            swipeContainer.setRefreshing(false);

            switch (errorCode) {
                case NO_INTERNET:
                    tvError.setText("No internet connection");
                    buttonFlat.setVisibility(View.VISIBLE);
                    break;
                case NO_HOTELS:
                    tvError.setText("No hotels for this query.");
                    buttonFlat.setVisibility(View.GONE);;
                    break;
                case HOTEL_NOT_AVAILABLE:
                    tvError.setText("There are no free rooms for that days.");
                    buttonFlat.setVisibility(View.GONE);
                    break;
            }

            errorLayout.setVisibility(View.VISIBLE);
        }
    }



    private void clearLoadingScreen() {
        isErorShowing = false;
        errorLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        tvError.setText("No hotels found");
        listView.setEmptyView(errorLayout);
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
        isFiltersChanged = true;
    }

    private String makeFilterString() {
        String filter = "";
        if (filterParams == null) {
            return filter;
        }
        filter = filter + "&minRate=" + filterParams.getMinPrice() + "&maxRate=" + filterParams.getMaxPrice() +
                "&minStarRating=" + (filterParams.getMinStarRate() + 1) + "&maxStarRating=" + (filterParams.getMaxStarRate() + 1) +
                "&minTripAdvisorRating=" + (filterParams.getMinTripRate() + 1) + "&maxTripAdvisorRating=" + (filterParams.getMaxTripRate() + 1);
        return filter;
    }

    private void loadNextHotels() {

        String tempUrl = url +
                cacheKey +
                cacheLocation;
        nextPageRequest = new JsonObjectRequest(Request.Method.GET,
                tempUrl,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            cacheLocation = "&cacheLocation=" + response.getJSONObject("HotelListResponse").getString("cacheLocation");
                            cacheKey = "&cacheKey=" + response.getJSONObject("HotelListResponse").getString("cacheKey");
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

                        if (arr != null && arr.length() > 0) {
                            ArrayList<SearchResultSO> temp;
                            temp = gson.fromJson(arr.toString(), listOfTestObject);
                            searchResultSOArrayList.addAll(temp);
                            loadNextHotels();
                            Log.d("ExpediaRequest", "All hotels: " + searchResultSOArrayList.size() + ", loading more");

                        } else {
                            if (getActivity() != null) {
                                adapter = new MainActivityMainListAdapter(getActivity(), searchResultSOArrayList, searchParams.getArrivalDate(), searchParams.getDepartureDate(), searchParams.getRooms(), cacheKey, cacheLocation, url);
                                slideExpandableListAdapter = new SlideExpandableListAdapter(adapter, R.id.hotel_layout, R.id.expandableLayout);
                                listView.setAdapter(slideExpandableListAdapter);
                                clearLoadingScreen();
                            }
                            sortData();
                            Log.d("ExpediaRequest", "All hotels: " + searchResultSOArrayList.size() + ", no more hotels");
                            isFiltersChanged = false;
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        }

        );
        VolleySingleton.getInstance(AppState.getMyContext()).addToRequestQueue(nextPageRequest);
    }


    private void showPopupMenu(View v) {
        popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.inflate(R.menu.filter_menu);
        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (adapter != null) {
                            switch (item.getItemId()) {

                                case R.id.sort_by_lowest_price:
                                    sortingType = SORT_BY_LOWEST_PRICE;
                                    sortData();
                                    adapter.setVisibleElementsToDefault();
                                    adapter.notifyDataSetChanged();
                                    listView.setSelection(0);
                                    return true;
                                case R.id.sort_by_highest_price:
                                    sortingType = SORT_BY_HIGHEST_PRICE;
                                    sortData();
                                    adapter.setVisibleElementsToDefault();
                                    adapter.notifyDataSetChanged();
                                    listView.setSelection(0);
                                    return true;
                                case R.id.sort_by_name:
                                    sortingType = SORT_BY_NAME;
                                    sortData();
                                    adapter.setVisibleElementsToDefault();
                                    adapter.notifyDataSetChanged();
                                    listView.setSelection(0);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                        return false;
                    }
                });


        popupMenu.show();
    }

    public void sortData() {
        switch (sortingType) {
            case SORT_BY_LOWEST_PRICE:
                Collections.sort(searchResultSOArrayList, new Comparator<SearchResultSO>() {
                    @Override
                    public int compare(SearchResultSO lhs, SearchResultSO rhs) {
                        if (lhs.getMinPrice() > rhs.getMinPrice()) return 1;
                        if (lhs.getMinPrice() < rhs.getMinPrice()) return -1;
                        return 0;
                    }
                });
                break;
            case SORT_BY_HIGHEST_PRICE:
                Collections.sort(searchResultSOArrayList, new Comparator<SearchResultSO>() {
                    @Override
                    public int compare(SearchResultSO lhs, SearchResultSO rhs) {
                        if (lhs.getMinPrice() < rhs.getMinPrice()) return 1;
                        if (lhs.getMinPrice() > rhs.getMinPrice()) return -1;
                        return 0;
                    }
                });
                break;
            case SORT_BY_NAME:
                Collections.sort(searchResultSOArrayList, new Comparator<SearchResultSO>() {
                    public int compare(SearchResultSO v1, SearchResultSO v2) {
                        return v1.getHotelName().compareTo(v2.getHotelName());
                    }
                });
                break;
        }

    }

    public class NetworkStateReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            Log.d("app", "Network connectivity change");
            if(intent.getExtras()!=null) {
                NetworkInfo ni=(NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
                if(ni!=null && ni.getState()==NetworkInfo.State.CONNECTED) {
                    Log.i("app","Network "+ni.getTypeName()+" connected");
                    if(isErorShowing){
                        progressBar.setVisibility(View.VISIBLE);
                        loadDataFromExpedia();
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


    @Override
    public void onPause() {
        super.onPause();
        ((MaterialNavigationDrawer) getActivity()).getToolbar().removeView(ll);
    }

    @Override
    public void onResume() {
        super.onResume();
        ll = ((LayoutInflater) (getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))).inflate(R.layout.toolbar_buttons, null, false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1);
        ll.setLayoutParams(params);
        imageSort = (ImageView) ll.findViewById(R.id.iv_sort_toolbar);
        btnFilter = (ImageView) ll.findViewById(R.id.iv_filter_toolbar);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter != null && adapter.getCount() != 0) {
                    FilterFragment filterFragment = new FilterFragment();
                    if (filterParams == null) {
                        filterFragment.setFilterParams(new FilterSO(0, 1000, 1, 5, 1, 5), MainFragment.this);
                    } else {
                        filterFragment.setFilterParams(filterParams, MainFragment.this);
                    }
                    ((MaterialNavigationDrawer) getActivity()).setFragmentChild(filterFragment, "Filter");
                }
            }
        });
        imageSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });


        ((MaterialNavigationDrawer) getActivity()).getToolbar().addView(ll);

        if (isFiltersChanged) {
            progressBar.setVisibility(View.VISIBLE);
            loadDataFromExpedia();
        }
    }
}