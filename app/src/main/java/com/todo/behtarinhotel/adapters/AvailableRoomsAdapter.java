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
import com.todo.behtarinhotel.simpleobjects.AvailableRoomsSO;

/**
 * Created by Andriy on 13.07.2015.
 */
public class AvailableRoomsAdapter extends BaseAdapter {

    ImageView roomImage;
    TextView tvRoomDescription, tvLocation, tvOldPrice, tvNewPrice, tvMaxGuests, tvBedsQuantity, tvBedsTypes;
    ButtonRectangle btnBook;
    AvailableRoomsSO.RoomSO room;

    Context ctx;
    LayoutInflater lInflater;
    AvailableRoomsSO availableRooms;


    public AvailableRoomsAdapter(Context ctx, AvailableRoomsSO availableRooms) {
        this.ctx = ctx;
        this.availableRooms = availableRooms;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


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
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.available_room_item, null);
        }

        initViewsById(view);

//        ImageView roomImage;
//        TextView tvRoomDescription, tvLocation, tvOldPrice, tvNewPrice, tvMaxGuests, tvBedsQuantity, tvBedsTypes;
//        Button btnBook;

        room = availableRooms.getRoomSO().get(position);
        tvRoomDescription.setText(room.getDescription());
        tvLocation.setText(availableRooms.getHotelAddress());
        tvOldPrice.setText("$" + room.getOldPrice());
        tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvNewPrice.setText("$" + room.getAverageRate());
        tvBedsTypes.setText(room.getBedDescription());
        tvMaxGuests.setText("x" + room.getMaxGuests());
        tvBedsQuantity.setText("x" + room.getBedsQuantity());
        String temp = room.getRoomImage()
                .substring(0, room.getRoomImage().length() - 5);
        Glide.with(ctx)
                .load(temp + "b.jpg")
                .placeholder(R.mipmap.icon_profile)
                .error(R.mipmap.ic_launcher)
                .into(roomImage);



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
