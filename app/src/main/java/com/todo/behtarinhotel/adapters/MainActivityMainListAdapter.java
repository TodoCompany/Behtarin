package com.todo.behtarinhotel.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.simpleobjects.SearchResultSO;
import com.todo.behtarinhotel.supportclasses.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by dmytro on 7/8/15.
 */
@ContentView(R.layout.main_activity_main_list_item)
public class MainActivityMainListAdapter extends BaseAdapter {

    ImageLoader imageLoader;

    NetworkImageView ivPhoto;
    ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;
    NetworkImageView ivTripAdvisorRate;

    TextView tvHotelName;
    TextView tvCity;
    TextView tvAddress;
    TextView tvPrice;
    TextView tvLocationDescription;

    ArrayList<ImageView> imageViews;
    float rate;
    Resources res;

    Button btnLikeCounter;
    @InjectView(R.id.btn_read_more_main_activity_main_list)
    Button btnReadMore;
    @InjectView(R.id.btn_check_availability_main_activity_main_list)
    Button btnCheckAvailability;

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<SearchResultSO> searchResultSOArrayList;

    String tripAdvisorWebURL;
    String tripAdvisorApiURL;
    public static final String PHOTO_URL_START = "http://images.travelnow.com";
    public static final String PHOTO_URL_END = "b.jpg";


    public MainActivityMainListAdapter(Context ctx, ArrayList<SearchResultSO> searchResultSOArrayList) {
        this.ctx = ctx;
        this.searchResultSOArrayList = searchResultSOArrayList;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = VolleySingleton.getInstance(ctx).getImageLoader();
        res = ctx.getResources();
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
            view = lInflater.inflate(R.layout.main_activity_main_list_item, null);
        }

        ivPhoto = (NetworkImageView) view.findViewById(R.id.iv_photo_main_activity_main_list);

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


        ivTripAdvisorRate = (NetworkImageView) view.findViewById(R.id.iv_tripadvisor_rate_main_activity_main_list);

        tvHotelName = (TextView) view.findViewById(R.id.tv_hotel_name_main_activity_main_list);
        tvCity = (TextView) view.findViewById(R.id.tv_city_name_main_activity_main_list);
        tvAddress = (TextView) view.findViewById(R.id.tv_address_main_activity_main_list);
        tvPrice = (TextView) view.findViewById(R.id.tv_price_main_activity_main_list);
        tvLocationDescription = (TextView) view.findViewById(R.id.tv_location_description_main_activity_main_list);

        tvHotelName.setText(searchResultSOArrayList.get(position).getHotelName());
        tvCity.setText(searchResultSOArrayList.get(position).getCity());
        tvAddress.setText(searchResultSOArrayList.get(position).getAddress());
        //todo encode from html, works 50/50
        tvLocationDescription.setText(Html.fromHtml(searchResultSOArrayList.get(position).getLocationDescription()));
        tvPrice.setText("" + searchResultSOArrayList.get(position).getMinPrice());

        String temp = searchResultSOArrayList.get(position).getPhotoURL()
                .substring(0, searchResultSOArrayList.get(position).getPhotoURL().length() - 5);
        ivPhoto.setImageUrl(PHOTO_URL_START + temp + PHOTO_URL_END , imageLoader);
        ivPhoto.setDefaultImageResId(R.color.background_material_light);
        ivPhoto.setErrorImageResId(R.mipmap.ic_launcher);

        ivTripAdvisorRate.setImageUrl(searchResultSOArrayList.get(position).getTripAdvisorRatingURL(), imageLoader);
        ivTripAdvisorRate.setDefaultImageResId(R.color.background_material_light);
        ivTripAdvisorRate.setErrorImageResId(R.mipmap.ic_launcher);

        rate = searchResultSOArrayList.get(position).getStars();
        for (int i = 0; i < 5; i++) {
            if (rate >= 1) {
                rate--;
            } else if (rate == 0.5) {
                imageViews.get(i).setBackground(res.getDrawable(R.drawable.abc_btn_radio_to_on_mtrl_000));
                rate = 0;
            } else if (rate == 0) {
                imageViews.get(i).setBackground(res.getDrawable(R.mipmap.ic_launcher));
            }
        }

        btnLikeCounter = (Button) view.findViewById(R.id.btn_like_counter_activity_main_main_list);
        btnLikeCounter.setText("" + searchResultSOArrayList.get(position).getLikeCounter());

        btnReadMore = (Button) view.findViewById(R.id.btn_read_more_main_activity_main_list);
        btnReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo onclicklistener
            }
        });

        btnCheckAvailability = (Button) view.findViewById(R.id.btn_check_availability_main_activity_main_list);
        btnCheckAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo onclicklistener
            }
        });

        ivTripAdvisorRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tripAdvisorWebURL != null){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tripAdvisorWebURL));
                    ctx.startActivity(browserIntent);
                    Log.d("MainListAdapter","Open URL");
                }
            }
        });

        tripAdvisorApiURL = "http://api.tripadvisor.com/api/partner/2.0/map/" + searchResultSOArrayList.get(position).getLatitude() +
                "," + searchResultSOArrayList.get(position).getLongitude() + "/hotels?key=cc1fb67fbf9c4c4592a1b7071a926087";
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
        VolleySingleton.getInstance(ctx).addToRequestQueue(jsonObjectRequest);


        return view;
    }

}