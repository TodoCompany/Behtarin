package com.todo.behtarinhotel.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.simpleobjects.AvailableRoomsSO;
import com.todo.behtarinhotel.simpleobjects.RoomQueryGuestSO;
import com.todo.behtarinhotel.simpleobjects.SearchRoomSO;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by dmytro on 7/28/15.
 */
public class RoomListAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<SearchRoomSO> roomGuests;

    LinearLayout ll;
    LinearLayout linearLayout;
    TextView tvType;
    TextView tvCount;
    TextView tvRoom;
    ImageView ivType;
    Resources res;


    public RoomListAdapter(Context ctx,ArrayList<SearchRoomSO> roomGuests) {
        this.ctx = ctx;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Collections.reverse(roomGuests);
        this.roomGuests = (roomGuests);
        res = ctx.getResources();
    }

    @Override
    public int getCount() {
        return roomGuests.size();
    }

    @Override
    public Object getItem(int i) {
        return roomGuests.get(roomGuests.size()-i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.fragment_search_room_list_item, null);
        }


        tvRoom = (TextView) view.findViewById(R.id.tv_room_fragment_search_room_list);
        tvRoom.setText("Room " + (roomGuests.size()-i-1));

        ll = (LinearLayout) lInflater.inflate(R.layout.fragment_search_room_list_item_child, null, false);
        linearLayout = (LinearLayout) view.findViewById(R.id.container_guests);
        tvType = (TextView) ll.findViewById(R.id.tv_type_search_fragment_item_child);
        tvType.setText("Adult");
        tvCount = (TextView) ll.findViewById(R.id.tv_counter_search_fragment_item_child);
        tvCount.setText("x" + roomGuests.get(i).getAdultCount());


        linearLayout.addView(ll);
        if(roomGuests.get(i).getChildAges().length != 0){
            for(int j = 0; j<roomGuests.get(i).getChildAges().length;j++){
                ll = (LinearLayout) lInflater.inflate(R.layout.fragment_search_room_list_item_child, null, false);
                tvType = (TextView) ll.findViewById(R.id.tv_type_search_fragment_item_child);
                tvCount = (TextView) ll.findViewById(R.id.tv_counter_search_fragment_item_child);
                tvType.setText("Child");
                tvCount.setText(roomGuests.get(i).getChildAges()[j] + " years");
                linearLayout.addView(ll);
                ivType = (ImageView) ll.findViewById(R.id.iv_type_search_fragment_item_child);
                ivType.setBackground(res.getDrawable(R.drawable.child));
            }
        }

        return view;
    }




}
