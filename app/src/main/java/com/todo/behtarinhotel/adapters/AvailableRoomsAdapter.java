package com.todo.behtarinhotel.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.simpleobjects.AvailableRoomsSO;
import com.todo.behtarinhotel.supportclasses.VolleySingleton;

/**
 * Created by Andriy on 13.07.2015.
 */
public class AvailableRoomsAdapter extends BaseAdapter {

    NetworkImageView roomImageView;
    ImageLoader imageLoader;
    TextView tvCurrentPrice, tvOldPrice, tvRoomDescription, tvRoomBeds;

    Context ctx;
    LayoutInflater lInflater;
    AvailableRoomsSO availableRooms;


    public AvailableRoomsAdapter(Context ctx, AvailableRoomsSO availableRooms) {
        this.ctx = ctx;
        this.availableRooms = availableRooms;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = VolleySingleton.getInstance(ctx).getImageLoader();

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

        roomImageView = (NetworkImageView) view.findViewById(R.id.room_image_view);

        tvCurrentPrice = (TextView) view.findViewById(R.id.tv_room_current_price);
        tvOldPrice = (TextView) view.findViewById(R.id.tv_room_old_price);
        tvRoomDescription = (TextView) view.findViewById(R.id.tv_room_description);
        tvRoomBeds = (TextView) view.findViewById(R.id.tv_room_beds);

        tvRoomDescription.setText(availableRooms.getRoomSO().get(position).getDescription());
        tvCurrentPrice.setText("Price: " + availableRooms.getRoomSO().get(position).getAverageRate());
        tvOldPrice.setText("" + availableRooms.getRoomSO().get(position).getOldPrice());
        tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvRoomBeds.setText(availableRooms.getRoomSO().get(position).getBedDescription());
        String url = availableRooms.getRoomSO().get(position).getRoomImage();
        if (url != null && !url.equals("")) {
            roomImageView.setImageUrl(availableRooms.getRoomSO().get(position).getRoomImage(), imageLoader);
            roomImageView.setDefaultImageResId(R.color.background_material_light);
            roomImageView.setErrorImageResId(R.mipmap.ic_launcher);
        }

        if(availableRooms.getRoomSO().get(position).getAverageRate() == availableRooms.getRoomSO().get(position).getOldPrice()){
            tvOldPrice.setVisibility(View.INVISIBLE);
        }else{
            tvOldPrice.setVisibility(View.VISIBLE);
        }

        return view;
    }
}
