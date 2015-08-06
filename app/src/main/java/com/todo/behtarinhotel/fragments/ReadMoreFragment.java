package com.todo.behtarinhotel.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonFloat;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.simpleobjects.SearchResultSO;
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
public class ReadMoreFragment extends Fragment {
    public static final String PHOTO_URL_START = "http://images.travelnow.com";
    public static final String PHOTO_URL_END = "b.jpg";

    TextView tvHotelName, tvHotelAddress, tvHotelDescription, tvHotelPrice, tvHotelLikes;
    ImageView hotelImage, imageTripAdvisor, hotelStar1, hotelStar2, hotelStar3, hotelStar4, hotelStar5;
    ButtonFloat btnFloat;
    ButtonFlat btnCheckAvailability;
    View rootView;
    LayoutInflater inflater;

    SearchResultSO searchResultSO;
    float rate;
    String arrival;
    String departure;

    ArrayList<SearchRoomSO> rooms;
    ArrayList<ImageView> imageViews;
    ArrayList<ImageView> hotelImages;
    ArrayList<String> hotelImagesUrls;
    int checkedImageNumber;


    public ReadMoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_read_more, container, false);
        hotelImages = new ArrayList<>();
        this.inflater = inflater;

        initViewsById();
        fillWithData();
        loadImagesUrls();

        return rootView;
    }

    private void initViewsById() {
        tvHotelName = (TextView) rootView.findViewById(R.id.tv_hotel_name);
        tvHotelAddress = (TextView) rootView.findViewById(R.id.tv_adress);
        tvHotelDescription = (TextView) rootView.findViewById(R.id.tv_hotel_description);
        tvHotelPrice = (TextView) rootView.findViewById(R.id.tv_hotel_price);
        tvHotelLikes = (TextView) rootView.findViewById(R.id.tv_hotel_likes);
        hotelImage = (ImageView) rootView.findViewById(R.id.hotel_image);
        imageTripAdvisor = (ImageView) rootView.findViewById(R.id.image_trip_advisor);
        hotelStar1 = (ImageView) rootView.findViewById(R.id.hotel_star_1);
        hotelStar2 = (ImageView) rootView.findViewById(R.id.hotel_star_2);
        hotelStar3 = (ImageView) rootView.findViewById(R.id.hotel_star_3);
        hotelStar4 = (ImageView) rootView.findViewById(R.id.hotel_star_4);
        hotelStar5 = (ImageView) rootView.findViewById(R.id.hotel_star_5);
        btnFloat = (ButtonFloat) rootView.findViewById(R.id.btn_float);
        btnCheckAvailability = (ButtonFlat) rootView.findViewById(R.id.btn_check_availability);
    }

    public void setHotelData(SearchResultSO searchResultSO, String arrival, String departure, ArrayList<SearchRoomSO> rooms) {
        this.searchResultSO = searchResultSO;
        this.arrival = arrival;
        this.departure = departure;
        this.rooms = rooms;

    }

    public void setHotelData(SearchResultSO searchResultSO){
        this.searchResultSO = searchResultSO;
    }

    private void fillWithData() {
        imageViews = new ArrayList<>();
        imageViews.add(hotelStar1);
        imageViews.add(hotelStar2);
        imageViews.add(hotelStar3);
        imageViews.add(hotelStar4);
        imageViews.add(hotelStar5);

        tvHotelName.setText(searchResultSO.getHotelName());
//        tvCity.setText(searchResultSOArrayList.get(position).getCity());
        tvHotelAddress.setText(searchResultSO.getAddress());
        tvHotelDescription.setText(Html.fromHtml(Html.fromHtml(searchResultSO.getLocationDescription()).toString()));
        tvHotelPrice.setText(searchResultSO.getMinPrice() + " $");
        tvHotelLikes.setText("" + searchResultSO.getLikeCounter());

        btnCheckAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rooms!=null){
                    CheckAvailabilityFragment checkAvailabilityFragment = new CheckAvailabilityFragment();
                    ((MaterialNavigationDrawer) getActivity()).setFragmentChild(checkAvailabilityFragment, getActivity().getString(R.string.fragment_checkavailablerooms));
                    checkAvailabilityFragment.getData(searchResultSO.getHotelId(), arrival, departure, rooms);
                }else{
                    SearchFragment searchFragment = new SearchFragment();
                    searchFragment.setParameters(searchResultSO.getHotelId(),searchResultSO.getHotelName());
                    ((MaterialNavigationDrawer) getActivity()).setFragmentChild(searchFragment, "Search");
                }
            }
        });


        String temp = searchResultSO.getPhotoURL()
                .substring(0, searchResultSO.getPhotoURL().length() - 5);

        Glide.with(getActivity())
                .load(PHOTO_URL_START + temp + PHOTO_URL_END)
                .placeholder(R.mipmap.ic_hotel_placeholder)
                .error(R.drawable.empty)
                .into(hotelImage);

        Glide.with(getActivity())
                .load(searchResultSO.getTripAdvisorRatingURL())
                .placeholder(R.color.background_material_light)
                .error(R.mipmap.ic_launcher)
                .into(imageTripAdvisor);


        rate = searchResultSO.getStars();
        for (int i = 0; i < 5; i++) {
            if (rate >= 1) {
                rate--;
                imageViews.get(i).setImageDrawable(getResources().getDrawable(R.drawable.star_selected));
            } else if (rate == 0.5) {
                imageViews.get(i).setImageDrawable(getResources().getDrawable(R.drawable.star_half_selected));
                rate = 0;
            } else if (rate == 0) {
            }
        }
    }

    private void loadImagesUrls(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                AppState.getHotelImagesUrl(searchResultSO.getHotelId()),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hotelImagesUrls = new ArrayList<>();

                        try {
                            JSONArray imagesArray = response
                                    .getJSONObject("HotelInformationResponse")
                                    .getJSONObject("HotelImages")
                                    .getJSONArray("HotelImage");
                            for(int i = 0; i < imagesArray.length(); i++){
                                hotelImagesUrls.add(imagesArray.getJSONObject(i).getString("url"));
                            }
                            fillHotelImages();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }

        );
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
    }

    private void fillHotelImages(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        params.bottomMargin = 3;
        params.topMargin = 3;
        params.leftMargin = 3;
        params.rightMargin = 3;
        LinearLayout firstRow = (LinearLayout) rootView.findViewById(R.id.firstRow);
        LinearLayout secondRow = (LinearLayout) rootView.findViewById(R.id.secondRow);
        firstRow.removeAllViews();
        secondRow.removeAllViews();
        ArrayList<View> photos = new ArrayList<>();
        if (hotelImagesUrls.size() <= 6){
            for (int i = 0; i < hotelImagesUrls.size(); i++){
                ImageView imageView = new ImageView(getActivity());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setId(i);
                imageView.setLayoutParams(params);
                Glide.with(getActivity())
                        .load(hotelImagesUrls.get(i))
                        .into(imageView);
                photos.add(imageView);
            }
        }else{
            for (int i = 0; i < 5; i++){
                ImageView imageView = new ImageView(getActivity());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setLayoutParams(params);
                imageView.setId(i);
                Glide.with(getActivity())
                        .load(hotelImagesUrls.get(i))
                        .into(imageView);
                photos.add(imageView);
            }
            View view = inflater.inflate(R.layout.more_photos_item, null, false);
            view.setLayoutParams(params);
            view.setId(6);
            Button btnLoadMorePhotos = (Button) view.findViewById(R.id.btnLoadMorePhotos);
            btnLoadMorePhotos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HotelPhotosFragment hotelPhotosFragment = new HotelPhotosFragment();
                    hotelPhotosFragment.setHotelId(hotelImagesUrls, -1);
                    ((MaterialNavigationDrawer) getActivity()).setFragmentChild(hotelPhotosFragment, "Hotel photos");
                }
            });
            ImageView imageLoadMore = (ImageView) view.findViewById(R.id.imageLoadMore);
            btnLoadMorePhotos.setText("+" + (hotelImagesUrls.size() - 6));
            Glide.with(getActivity())
                    .load(hotelImagesUrls.get(5))
                    .into(imageLoadMore);
            photos.add(view);
        }


        firstRow.addView(photos.get(0));
        firstRow.addView(photos.get(1));
        firstRow.addView(photos.get(2));
        secondRow.addView(photos.get(3));
        secondRow.addView(photos.get(4));
        secondRow.addView(photos.get(5));

        for (View view : photos){

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HotelPhotosFragment hotelPhotosFragment = new HotelPhotosFragment();
                    hotelPhotosFragment.setHotelId(hotelImagesUrls, v.getId());
                    ((MaterialNavigationDrawer) getActivity()).setFragmentChild(hotelPhotosFragment, "Hotel photos");
                }
            });
        }

    }







}
