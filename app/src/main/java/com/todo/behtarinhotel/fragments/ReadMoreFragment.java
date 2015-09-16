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

import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonFloat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.simpleobjects.SearchResultSO;
import com.todo.behtarinhotel.simpleobjects.SearchRoomSO;
import com.todo.behtarinhotel.supportclasses.AppState;
import com.todo.behtarinhotel.supportclasses.DataLoader;
import com.todo.behtarinhotel.views.MyMapView;

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
    public static final String PHOTO_URL_END = "z.jpg";

    TextView tvHotelName, tvHotelAddress, tvHotelDescription, tvHotelPrice, tvHotelLikes;
    ImageView hotelImage, imageTripAdvisor, hotelStar1, hotelStar2, hotelStar3, hotelStar4, hotelStar5;
    ButtonFloat btnFloat;
    ButtonFlat btnCheckAvailability;
    View rootView;
    LayoutInflater inflater;
    private MyMapView mapView;
    private GoogleMap googleMap;

    SearchResultSO searchResultSO;
    float rate;
    String arrival;
    String departure;

    ArrayList<SearchRoomSO> rooms;
    ArrayList<ImageView> imageViews;
    ArrayList<ImageView> hotelImages;
    ArrayList<String> hotelImagesUrls;

    RequestListener listener;


    public ReadMoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_read_more, container, false);
        hotelImages = new ArrayList<>();
        this.inflater = inflater;

        mapView = (MyMapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        initViewsById();
        fillWithData();
        loadImagesUrls();
        setUpMapIfNeeded(savedInstanceState);

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
        btnFloat = (ButtonFloat) rootView.findViewById(R.id.btn_add_wish_read_more);
        btnCheckAvailability = (ButtonFlat) rootView.findViewById(R.id.btn_check_availability);

        listener = new RequestListener() {
            @Override
            public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                String str = model.toString();
                Glide.with(getActivity().getApplicationContext())
                        .load(str.substring(0, str.length() - 5) + "b.jpg")
                        .error(R.drawable.empty)
                        .into(target);
                return true;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        };
    }

    public void setHotelData(SearchResultSO searchResultSO, String arrival, String departure, ArrayList<SearchRoomSO> rooms) {
        this.searchResultSO = searchResultSO;
        this.arrival = arrival;
        this.departure = departure;
        this.rooms = rooms;

    }

    public void setHotelData(SearchResultSO searchResultSO) {
        this.searchResultSO = searchResultSO;
    }

    private void fillWithData() {
        imageViews = new ArrayList<>();
        imageViews.add(hotelStar1);
        imageViews.add(hotelStar2);
        imageViews.add(hotelStar3);
        imageViews.add(hotelStar4);
        imageViews.add(hotelStar5);

        tvHotelName.setText(Html.fromHtml(searchResultSO.getHotelName()));
        tvHotelAddress.setText(Html.fromHtml(searchResultSO.getAddress()));
        tvHotelDescription.setText(Html.fromHtml(Html.fromHtml(searchResultSO.getLocationDescription()).toString()));
        tvHotelPrice.setText(searchResultSO.getMinPrice() + " $");
        tvHotelLikes.setText("" + searchResultSO.getLikeCounter());


        btnCheckAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rooms != null) {
                    CheckAvailabilityFragment checkAvailabilityFragment = new CheckAvailabilityFragment();
                    ((MaterialNavigationDrawer) getActivity()).setFragmentChild(checkAvailabilityFragment, getActivity().getString(R.string.fragment_checkavailablerooms));
                    checkAvailabilityFragment.getData(searchResultSO.getHotelId(), arrival, departure, rooms);
                } else {
                    SearchFragment searchFragment = new SearchFragment();
                    searchFragment.setParameters(searchResultSO.getHotelId(), searchResultSO.getHotelName());
                    ((MaterialNavigationDrawer) getActivity()).setFragmentChild(searchFragment, "Search");
                }
            }
        });

        if (!AppState.isInWishList(searchResultSO.getHotelId())) {
            btnFloat.setBackgroundColor(getResources().getColor(R.color.base_white));
            btnFloat.setDrawableIcon(getResources().getDrawable(R.drawable.star_selected));
        } else {
            btnFloat.setBackgroundColor(getResources().getColor(R.color.base_yellow));
            btnFloat.setDrawableIcon(getResources().getDrawable(R.drawable.abc_btn_rating_star_on_mtrl_alpha));
        }
        btnFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!AppState.isInWishList(searchResultSO.getHotelId())) {
                    AppState.addToWishList(searchResultSO.getHotelId());
                    btnFloat.setBackgroundColor(getResources().getColor(R.color.base_yellow));
                    btnFloat.setDrawableIcon(getResources().getDrawable(R.drawable.abc_btn_rating_star_on_mtrl_alpha));
                } else {
                    AppState.removeFromWishList(searchResultSO.getHotelId());
                    btnFloat.setBackgroundColor(getResources().getColor(R.color.base_white));
                    btnFloat.setDrawableIcon(getResources().getDrawable(R.drawable.star_selected));
                }
            }
        });


        String temp = searchResultSO.getPhotoURL()
                .substring(0, searchResultSO.getPhotoURL().length() - 5);

        Glide.with(getActivity())
                .load(PHOTO_URL_START + temp + PHOTO_URL_END)
                .placeholder(R.mipmap.ic_hotel_placeholder)
                .error(R.drawable.empty)
                .listener(listener)
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

    private void loadImagesUrls() {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hotelImagesUrls = new ArrayList<>();

                try {
                    JSONArray imagesArray = response
                            .getJSONObject("HotelInformationResponse")
                            .getJSONObject("HotelImages")
                            .getJSONArray("HotelImage");
                    for (int i = 0; i < imagesArray.length(); i++) {
                        hotelImagesUrls.add(imagesArray.getJSONObject(i).getString("url"));
                    }
                    fillHotelImages();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e){
                    // we getting nullpointer when response comes after we stopped this fragment
                }
            }
        };

        DataLoader.makeRequest(DataLoader.getHotelImagesUrl(searchResultSO.getHotelId()),listener);
    }

    private void fillHotelImages() {
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
        if (hotelImagesUrls.size() <= 6) {
            for (int i = 0; i < hotelImagesUrls.size(); i++) {
                ImageView imageView = new ImageView(getActivity());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setId(i);
                imageView.setLayoutParams(params);
                Glide.with(getActivity())
                        .load(hotelImagesUrls.get(i))
                        .listener(listener)
                        .into(imageView);
                photos.add(imageView);
            }
        } else {
            for (int i = 0; i < 5; i++) {
                ImageView imageView = new ImageView(getActivity());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setLayoutParams(params);
                imageView.setId(i);
                Glide.with(getActivity())
                        .load(hotelImagesUrls.get(i))
                        .listener(listener)
                        .into(imageView);
                photos.add(imageView);
            }
            View view = inflater.inflate(R.layout.more_photos_item, null, false);
            view.setLayoutParams(params);
            //noinspection ResourceType
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
                    .listener(listener)
                    .into(imageLoadMore);
            photos.add(view);
        }

        firstRow.addView(photos.get(0));
        firstRow.addView(photos.get(1));
        firstRow.addView(photos.get(2));
        secondRow.addView(photos.get(3));
        secondRow.addView(photos.get(4));
        secondRow.addView(photos.get(5));

        for (View view : photos) {

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

    private void setUpMapIfNeeded(Bundle savedInstanceState) {
        mapView = (MyMapView) rootView.findViewById(R.id.mapView);
        googleMap = mapView.getMap();
        setUpMap();
    }

    private void setUpMap() {
        if (googleMap != null) {
            MapsInitializer.initialize(getActivity());
            googleMap.addMarker(new MarkerOptions().position(new LatLng(searchResultSO.getLatitude(), searchResultSO.getLongitude())).title(searchResultSO.getHotelName()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(searchResultSO.getLatitude(), searchResultSO.getLongitude()), 12));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
