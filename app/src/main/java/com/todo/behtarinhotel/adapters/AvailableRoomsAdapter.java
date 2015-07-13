package com.todo.behtarinhotel.adapters;

import android.content.Context;
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
    TextView tvName, tvRoomDescription;

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

        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvRoomDescription = (TextView) view.findViewById(R.id.tv_room_description);

        tvName.setText(availableRooms.getRoomSO().get(position).getDescription());
        tvRoomDescription.setText("Price: " + availableRooms.getRoomSO().get(position).getRateInfo().getChargeableRateInfo().getAverageRate());
        roomImageView.setImageUrl(availableRooms.getRoomSO().get(position).getRoomImages().getRoomImage().getUrl(), imageLoader);
        roomImageView.setDefaultImageResId(R.color.background_material_light);
        roomImageView.setErrorImageResId(R.mipmap.ic_launcher);

        return view;
    }
}
