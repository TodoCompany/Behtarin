package com.todo.behtarinhotel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gc.materialdesign.views.ButtonFlat;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.fragments.BookedRoomFragment;
import com.todo.behtarinhotel.simpleobjects.BookedRoomSO;

import java.util.ArrayList;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * Created by Andriy on 13.08.2015.
 */
public class BookedRoomsAdapter extends BaseAdapter {

    ArrayList<BookedRoomSO> bookedRooms;

    MaterialNavigationDrawer activity;
    LayoutInflater inflater;

    public BookedRoomsAdapter(MaterialNavigationDrawer activity, ArrayList<BookedRoomSO> bookedRooms){
        this.bookedRooms = bookedRooms;
        this.activity = activity;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return bookedRooms.size();
    }

    @Override
    public Object getItem(int position) {
        return bookedRooms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null){
            view = convertView;
        }else{
            view = inflater.inflate(R.layout.booked_room_item, null, false);
        }
        final BookedRoomSO bookedRoom = bookedRooms.get(position);
        TextView tvHotelName = (TextView) view.findViewById(R.id.tvHotelName);
        TextView tvHotelAddress = (TextView) view.findViewById(R.id.tvHotelAddress);
        TextView tvRoomPrice = (TextView) view.findViewById(R.id.tvRoomPrice);
        TextView tvArrivalDate = (TextView) view.findViewById(R.id.tvArrivalDate);
        TextView tvDepartureDate = (TextView) view.findViewById(R.id.tvDepartureDate);
        ButtonFlat btnReadMore = (ButtonFlat) view.findViewById(R.id.btnReadMore);
        ImageView bookedRoomImage = (ImageView) view.findViewById(R.id.bookedRoomImage);

        tvHotelName.setText(bookedRoom.getHotelName());
        tvHotelAddress.setText(bookedRoom.getHotelAddress());
        tvRoomPrice.setText(bookedRoom.getRoomPrice() + " $");
        tvArrivalDate.setText(bookedRoom.getArrivalDate());
        tvDepartureDate.setText(bookedRoom.getDepartureDate());

        Glide.with(activity)
                .load(bookedRoom.getPhotoUrl())
                .into(bookedRoomImage);

        btnReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookedRoomFragment bookedRoomFragment = new BookedRoomFragment();
                bookedRoomFragment.initFragment(bookedRoom);
                activity.setFragmentChild(bookedRoomFragment, "Booked room");
            }
        });


        return view;
    }
}
