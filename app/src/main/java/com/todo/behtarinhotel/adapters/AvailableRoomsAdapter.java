package com.todo.behtarinhotel.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gc.materialdesign.views.ButtonRectangle;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.fragments.BookFragment;
import com.todo.behtarinhotel.simpleobjects.AvailableRoomsSO;
import com.todo.behtarinhotel.simpleobjects.SearchRoomSO;

import java.util.ArrayList;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * Created by Andriy on 13.07.2015.
 */
public class AvailableRoomsAdapter extends BaseAdapter {

    ImageView roomImage;
    TextView tvRoomDescription, tvLocation, tvOldPrice, tvNewPrice, tvMaxGuests, tvBedsQuantity, tvBedsTypes;
    ButtonRectangle btnBook;
    AvailableRoomsSO.RoomSO room;

    MaterialNavigationDrawer activity;
    LayoutInflater lInflater;
    AvailableRoomsSO availableRooms;
    ArrayList<SearchRoomSO> searchRoomSO;

    BookFragment bookFragment;


    public AvailableRoomsAdapter(MaterialNavigationDrawer activity, AvailableRoomsSO availableRooms, ArrayList<SearchRoomSO> searchRoomSO) {
        this.activity = activity;
        this.availableRooms = availableRooms;
        this.searchRoomSO = searchRoomSO;
        lInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        bookFragment = new BookFragment();


    }

    @Override
    public int getCount() {
        return availableRooms.getRoomSO().size();
    }

    @Override
    public Object getItem(int position) {
        return availableRooms.getRoomSO().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.available_room_item, null);
        }

        initViewsById(view);

        room = availableRooms.getRoomSO().get(position);
        tvRoomDescription.setText(room.getDescription());
        tvLocation.setText(availableRooms.getHotelAddress());
        tvOldPrice.setText("$" + room.getOldPrice());
        tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvNewPrice.setText("$" + room.getAverageRate());
        tvBedsTypes.setText(room.getBedDescription());
        tvMaxGuests.setText("x" + room.getMaxGuests());
        tvBedsQuantity.setText("x" + room.getBedsQuantity());
        String temp = "";
        if (room.getRoomImage() != null && !room.getRoomImage().equals("")) {
            temp = room.getRoomImage()
                    .substring(0, room.getRoomImage().length() - 5);
        }
        Glide.with(activity)
                .load(temp + "b.jpg")
                .placeholder(R.color.base_hint)
                .error(R.drawable.empty)
                .into(roomImage);

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookFragment.setRooms(position,availableRooms, searchRoomSO);
                activity.setFragmentChild(bookFragment, "Book");

            }
        });


        if(availableRooms.getRoomSO().get(position).getAverageRate() == availableRooms.getRoomSO().get(position).getOldPrice()){
            tvOldPrice.setVisibility(View.INVISIBLE);
        }else{
            tvOldPrice.setVisibility(View.VISIBLE);
        }

        return view;
    }



    private void initViewsById(View rootView) {
        roomImage = (ImageView) rootView.findViewById(R.id.room_image);
        tvRoomDescription = (TextView) rootView.findViewById(R.id.tvRoomDescription);
        tvLocation = (TextView) rootView.findViewById(R.id.tvLocation);
        tvOldPrice = (TextView) rootView.findViewById(R.id.tvOldPrice);
        tvNewPrice = (TextView) rootView.findViewById(R.id.tvNewPrice);
        tvMaxGuests = (TextView) rootView.findViewById(R.id.tvMaxGuests);
        tvBedsQuantity = (TextView) rootView.findViewById(R.id.tvBedsQuantity);
        tvBedsTypes = (TextView) rootView.findViewById(R.id.tvBedsTypes);
        btnBook = (ButtonRectangle) rootView.findViewById(R.id.btnBook);
    }
}
