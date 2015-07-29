package com.todo.behtarinhotel.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.simpleobjects.RoomQueryGuestSO;
import com.todo.behtarinhotel.simpleobjects.SearchRoomSO;

/**
 * Created by dmytro on 7/29/15.
 */
public class RoomListItemAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    SearchRoomSO roomGuests;

    TextView tvType;
    TextView tvCount;
    ImageView ivType;
    Resources res;


    public RoomListItemAdapter(Context ctx, SearchRoomSO roomGuests) {
        this.ctx = ctx;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.roomGuests = roomGuests;
        res = ctx.getResources();
    }

    @Override
    public int getCount() {
        return roomGuests.getGuests().length;
    }

    @Override
    public Object getItem(int i) {
        return roomGuests.getGuests()[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.fragment_search_room_list_item_child, null);
        }

        RoomQueryGuestSO guest = roomGuests.getGuests()[i];

        tvType = (TextView) view.findViewById(R.id.tv_type_search_fragment_item_child);
        tvCount = (TextView) view.findViewById(R.id.tv_counter_search_fragment_item_child);

        if(!guest.isChild()){
            tvType.setText("Adult");
            tvCount.setText("x" + guest.getAge());
        }else {
            tvType.setText("Child");
            tvCount.setText(guest.getAge() + " years");
            ivType = (ImageView) view.findViewById(R.id.iv_type_search_fragment_item_child);
            ivType.setBackground(res.getDrawable(R.drawable.child));
        }

        return view;
    }


}



