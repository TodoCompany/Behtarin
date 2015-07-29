package com.todo.behtarinhotel.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.fragments.SearchFragment;
import com.todo.behtarinhotel.simpleobjects.SearchRoomSO;
import com.todo.behtarinhotel.supportclasses.MyListView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by dmytro on 7/28/15.
 */
public class RoomListAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<SearchRoomSO> roomGuests;

    MyListView listView;

    LinearLayout ll;
    LinearLayout linearLayout;
    TextView tvType;
    TextView tvCount;
    TextView tvRoom;
    ImageView ivType;
    Resources res;
    ImageButton ibEdit;
    SearchFragment fragment;
    RoomListItemAdapter adapter;


    public RoomListAdapter(Context ctx, ArrayList<SearchRoomSO> roomGuests) {
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
        return roomGuests.get(roomGuests.size() - i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {


        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.fragment_search_room_list_item, null);
        } else {
            linearLayout = (LinearLayout) view.findViewById(R.id.lv_guests_fragment_search_list);
            linearLayout.removeAllViews();
        }

        ibEdit = (ImageButton) view.findViewById(R.id.ib_edit_fragment_search_room_list);
        ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ctx, roomGuests.get(i).getGuests()[0].getAge() + "", Toast.LENGTH_SHORT).show();
            }
        });


        tvRoom = (TextView) view.findViewById(R.id.tv_room_fragment_search_room_list);
        tvRoom.setText("Room " + (roomGuests.size() - i));

        linearLayout = (LinearLayout) view.findViewById(R.id.lv_guests_fragment_search_list);
//        adapter = new RoomListItemAdapter(ctx,roomGuests.get(i));
//        listView.setAdapter(adapter);

        for (int j = 0; j < roomGuests.get(i).getGuests().length; j++) {
            LinearLayout ll = (LinearLayout) lInflater.inflate(R.layout.fragment_search_room_list_item_child, null);

            TextView tvCount = (TextView) ll.findViewById(R.id.tv_counter_search_fragment_item_child);


            TextView tvType = (TextView) ll.findViewById(R.id.tv_type_search_fragment_item_child);
            if (roomGuests.get(i).getGuests()[j].isChild()) {
                tvType.setText("Child");
                tvCount.setText(roomGuests.get(i).getGuests()[j].getAge() + " years");
            } else {
                tvType.setText("Adult");
                tvCount.setText("x " + roomGuests.get(i).getGuests()[j].getAge());
            }
            linearLayout.addView(ll);
        }


        return view;
    }

}
