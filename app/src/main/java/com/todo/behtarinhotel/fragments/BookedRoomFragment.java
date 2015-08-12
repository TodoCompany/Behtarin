package com.todo.behtarinhotel.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.simpleobjects.BookedRoomSO;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookedRoomFragment extends Fragment {

    ImageView roomImage;
    TextView tvHotelName, tvRoomDescription, tvHotelLocation, tvArrival, tvDeparture;
    View rootView;

    BookedRoomSO bookedRoomSO;

    public BookedRoomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_booked_room, container, false);

        initViews();




        return rootView;
    }

    private void initViews() {
        roomImage = (ImageView) rootView.findViewById(R.id.roomImage);
        tvHotelName = (TextView) rootView.findViewById(R.id.tvHotelName);
        tvRoomDescription = (TextView) rootView.findViewById(R.id.tvRoomDescription);
        tvHotelLocation = (TextView) rootView.findViewById(R.id.tvHotelLocation);
        tvArrival = (TextView) rootView.findViewById(R.id.tvArrival);
        tvDeparture = (TextView) rootView.findViewById(R.id.tvDeparture);
        initViewsWithData();
    }

    private void initViewsWithData() {
        Glide.with(getActivity())
                .load(bookedRoomSO.getPhotoUrl())
                .into(roomImage);
        tvHotelName.setText(bookedRoomSO.getHotelName());
        tvRoomDescription.setText(bookedRoomSO.getRoomDescription());
        tvHotelLocation.setText(bookedRoomSO.getHotelAddress());
        tvArrival.setText(bookedRoomSO.getArrivalDate());
        tvDeparture.setText(bookedRoomSO.getDepartureDate());
    }

    public void initFragment(BookedRoomSO bookedRoomSO) {
        this.bookedRoomSO = bookedRoomSO;
    }
}
