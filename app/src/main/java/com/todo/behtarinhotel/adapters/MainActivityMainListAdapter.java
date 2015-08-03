package com.todo.behtarinhotel.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.gc.materialdesign.views.ButtonFlat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.fragments.CheckAvailabilityFragment;
import com.todo.behtarinhotel.fragments.ReadMoreFragment;
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
import roboguice.inject.ContentView;

/**
 * Created by dmytro on 7/8/15.
 */
@ContentView(R.layout.main_activity_main_list_item)
public class MainActivityMainListAdapter extends BaseAdapter {

    public static final String PHOTO_URL_START = "http://images.travelnow.com";
    public static final String PHOTO_URL_END = "b.jpg";
    ImageLoader imageLoader;
    ImageView ivPhoto;
    ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;
    ImageView ivTripAdvisorRate;
    ButtonFlat btnReadMore, btnCheckAvailability;
    TextView tvHotelName;
    //  TextView tvCity;
    TextView tvAddress;
    TextView tvPrice;
    TextView tvLocationDescription;
    ArrayList<ImageView> imageViews;
    float rate;
    Resources res;
    TextView tvLikeCounter;
    //    @InjectView(R.id.btn_read_more_main_activity_main_list)
//    Button btnReadMore;
//    @InjectView(R.id.btn_check_availability_main_activity_main_list)
//    Button btnCheckAvailability;
    Activity activity;
    LayoutInflater lInflater;
    ArrayList<SearchResultSO> searchResultSOArrayList;
    String tripAdvisorWebURL;
    String tripAdvisorApiURL;
    String arrivalDate, departureDate;
    ArrayList<SearchRoomSO> rooms;
    String cacheKey;
    String cacheLocation;

    GsonBuilder gsonBuilder;
    Gson gson;

    String url;
    private int posForLoading = 19;


    public MainActivityMainListAdapter(Activity activity, ArrayList<SearchResultSO> searchResultSOArrayList, String arrivalDate, String departureDate, ArrayList<SearchRoomSO> rooms, String cacheKey, String cacheLocation, String url) {
        this.activity = activity;
        this.searchResultSOArrayList = searchResultSOArrayList;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.rooms = rooms;
        lInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = VolleySingleton.getInstance(activity).getImageLoader();
        res = activity.getResources();
        this.cacheKey = "&cacheKey=" + cacheKey;
        this.cacheLocation = "&cacheLocation=" + cacheLocation;
        this.url = url;
    }

    @Override
    public int getCount() {
        return searchResultSOArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return searchResultSOArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.hotel_item, null);
        }


        btnReadMore = (ButtonFlat) view.findViewById(R.id.btn_read_more);
        btnReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadMoreFragment readMoreFragment = new ReadMoreFragment();
                ((MaterialNavigationDrawer) activity).setFragmentChild(readMoreFragment, "About Hotel");
                readMoreFragment.setHotelData(searchResultSOArrayList.get(position), arrivalDate, departureDate, rooms);
            }
        });
        btnCheckAvailability = (ButtonFlat) view.findViewById(R.id.btn_check_availability);
        btnCheckAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchResultSO searchResultSO = (SearchResultSO) getItem(position);
                CheckAvailabilityFragment checkAvailabilityFragment = new CheckAvailabilityFragment();
                ((MaterialNavigationDrawer) activity).setFragmentChild(checkAvailabilityFragment, activity.getString(R.string.fragment_checkavailablerooms));
                checkAvailabilityFragment.getData(searchResultSO.getHotelId(), arrivalDate, departureDate, rooms);
            }
        });

        ivPhoto = (ImageView) view.findViewById(R.id.iv_photo_main_activity_main_list);

        ivStar1 = (ImageView) view.findViewById(R.id.iv_star1_main_activity_main_list);
        ivStar2 = (ImageView) view.findViewById(R.id.iv_star2_main_activity_main_list);
        ivStar3 = (ImageView) view.findViewById(R.id.iv_star3_main_activity_main_list);
        ivStar4 = (ImageView) view.findViewById(R.id.iv_star4_main_activity_main_list);
        ivStar5 = (ImageView) view.findViewById(R.id.iv_star5_main_activity_main_list);


        imageViews = new ArrayList<>();
        imageViews.add(ivStar1);
        imageViews.add(ivStar2);
        imageViews.add(ivStar3);
        imageViews.add(ivStar4);
        imageViews.add(ivStar5);


        ivTripAdvisorRate = (ImageView) view.findViewById(R.id.iv_tripadvisor_rate_main_activity_main_list);

        tvHotelName = (TextView) view.findViewById(R.id.tv_hotel_name_main_activity_main_list);
//        tvCity = (TextView) view.findViewById(R.id.tv_city_name_main_activity_main_list);
        tvAddress = (TextView) view.findViewById(R.id.tv_address_main_activity_main_list);
        tvPrice = (TextView) view.findViewById(R.id.tv_price_main_activity_main_list);
        tvLocationDescription = (TextView) view.findViewById(R.id.tv_location_description_main_activity_main_list);


        SearchResultSO searchResultSO = searchResultSOArrayList.get(position);
        tvHotelName.setText(searchResultSO.getHotelName());
//        tvCity.setText(searchResultSOArrayList.get(position).getCity());
        tvAddress.setText(searchResultSO.getAddress());
        tvLocationDescription.setText(Html.fromHtml(Html.fromHtml(searchResultSO.getLocationDescription()).toString()));
        tvPrice.setText("$ " + searchResultSO.getMinPrice());

        String temp;
        if (searchResultSO.getPhotoURL() != null && !searchResultSO.getPhotoURL().equals("")) {
            temp = searchResultSO.getPhotoURL()
                    .substring(0, searchResultSO.getPhotoURL().length() - 5);
        }
        temp = searchResultSO.getPhotoURL()
                .substring(0, searchResultSO.getPhotoURL().length() - 5);
        Glide.with(activity)
                .load(PHOTO_URL_START + temp + PHOTO_URL_END)
                .fitCenter()
                .placeholder(R.color.base_grey)
                .error(R.drawable.empty)
                .into(ivPhoto);

        Glide.with(activity)
                .load(searchResultSO.getTripAdvisorRatingURL())
                .fitCenter()
                .error(R.mipmap.ic_launcher)
                .into(ivTripAdvisorRate);


        rate = searchResultSO.getStars();
        for (int i = 0; i < 5; i++) {
            if (rate >= 1) {
                rate--;
                imageViews.get(i).setImageDrawable(res.getDrawable(R.drawable.star_selected));
            } else if (rate == 0.5) {
                imageViews.get(i).setImageDrawable(res.getDrawable(R.drawable.star_half_selected));
                rate = 0;
            } else if (rate == 0) {
                imageViews.get(i).setImageDrawable(null);
            }
        }

        tvLikeCounter = (TextView) view.findViewById(R.id.tv_like_counter_activity_main_main_list);
        tvLikeCounter.setText("" + searchResultSO.getLikeCounter());


        ivTripAdvisorRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tripAdvisorWebURL != null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tripAdvisorWebURL));
                    activity.startActivity(browserIntent);
                    Log.d("MainListAdapter", "Open URL");
                }
            }
        });

        tripAdvisorApiURL = "http://api.tripadvisor.com/api/partner/2.0/map/" + searchResultSO.getLatitude() +
                "," + searchResultSO.getLongitude() + "/hotels?key=cc1fb67fbf9c4c4592a1b7071a926087";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                tripAdvisorApiURL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            JSONObject obj = data.getJSONObject(0);
                            tripAdvisorWebURL = obj.getString("web_url");
                            Log.d("MainListAdapter", tripAdvisorWebURL);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());

            }
        }
        );
        VolleySingleton.getInstance(activity).addToRequestQueue(jsonObjectRequest);

        if (position == posForLoading) {
            loadNextHotels();
            posForLoading += 20;
        }


        return view;
    }

    private void loadNextHotels() {

        String tempUrl = url +
                cacheKey +
                cacheLocation;

        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
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


                        if (arr != null) {

                            ArrayList<SearchResultSO> temp = new ArrayList<>();
                            temp = gson.fromJson(arr.toString(), listOfTestObject);

                            searchResultSOArrayList.addAll(temp);
                            notifyDataSetChanged();

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());

            }
        }

        );
        VolleySingleton.getInstance(AppState.getMyContext()).addToRequestQueue(jsonObjectRequest);
    }


}