package com.todo.behtarinhotel.fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gc.materialdesign.views.ButtonRectangle;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.simpleobjects.BookedRoomSO;
import com.todo.behtarinhotel.supportclasses.AppState;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookedRoomFragment extends Fragment {

    ImageView roomImage;
    TextView tvHotelName, tvRoomDescription, tvHotelLocation, tvArrival, tvDeparture, tvRoomPrice;
    ButtonRectangle btnCancelBooking;
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
        initViewsWithData();
        setOnClickListeners();


        return rootView;
    }

    private void initViews() {
        roomImage = (ImageView) rootView.findViewById(R.id.roomImage);
        tvHotelName = (TextView) rootView.findViewById(R.id.tvHotelName);
        tvRoomDescription = (TextView) rootView.findViewById(R.id.tvRoomDescription);
        tvHotelLocation = (TextView) rootView.findViewById(R.id.tvHotelLocation);
        tvArrival = (TextView) rootView.findViewById(R.id.tvArrival);
        tvDeparture = (TextView) rootView.findViewById(R.id.tvDeparture);
        tvRoomPrice = (TextView) rootView.findViewById(R.id.tvRoomPrice);
        btnCancelBooking = (ButtonRectangle) rootView.findViewById(R.id.btnCancelBooking);
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
        tvRoomPrice.setText(bookedRoomSO.getRoomPrice() + " $");

    }

    private void setOnClickListeners() {
        btnCancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvMessage = new TextView(getActivity());
                tvMessage.setText(bookedRoomSO.getCancellationPolicy());
                tvMessage.setTextColor(getResources().getColor(R.color.base_white));
                tvMessage.setBackgroundColor(getResources().getColor(R.color.base_text));
                tvMessage.setPadding(16, 16, 16, 16);
                tvMessage.setTextSize(12);
                new AlertDialog.Builder(getActivity())
                        .setView(tvMessage)
                        .setTitle("Cancel booking?")
                        .setNegativeButton("No, don't cancel booking", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Yes, cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO cancel booking
                                AppState.removeRoomFromBooking(bookedRoomSO);
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    public void initFragment(BookedRoomSO bookedRoomSO) {
        this.bookedRoomSO = bookedRoomSO;
    }
}
