package com.todo.behtarinhotel.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
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
import com.todo.behtarinhotel.fragments.RoomBuilderFragment;
import com.todo.behtarinhotel.fragments.SearchFragment;
import com.todo.behtarinhotel.simpleobjects.SearchRoomSO;

import java.util.ArrayList;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

public class RoomListAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<SearchRoomSO> roomGuests;
    LinearLayout linearLayout;
    TextView tvRoom;
    Resources res;
    ImageButton ibEdit, ibDelete;
    SearchFragment parentFragment;
    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();

    public RoomListAdapter(Context ctx, ArrayList<SearchRoomSO> roomGuests, SearchFragment fragment) {
        this.ctx = ctx;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.roomGuests = (roomGuests);
        res = ctx.getResources();
        this.parentFragment = fragment;
    }

    @Override
    public int getCount() {
        return roomGuests.size();
    }

    @Override
    public Object getItem(int i) {
        return roomGuests.get(i);
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
                ((MaterialNavigationDrawer) parentFragment.getActivity()).setFragmentChild(new RoomBuilderFragment(roomGuests.get(i).getGuests(), i), "");
            }
        });

        ibDelete = (ImageButton) view.findViewById(R.id.ib_delete_fragment_search_room_list);
        ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (roomGuests.size() != 1) {
                    if (doubleBackToExitPressedOnce) {
                        roomGuests.remove(i);
                        notifyDataSetChanged();
                        return;
                    }

                    doubleBackToExitPressedOnce = true;
                    Toast.makeText(ctx, "Please click again to delete", Toast.LENGTH_SHORT).show();
                    mHandler.postDelayed(mRunnable, 2000);
                } else {
                    Toast.makeText(ctx, "Must be at least one room", Toast.LENGTH_SHORT).show();
                }
            }

        });


        tvRoom = (TextView) view.findViewById(R.id.tv_room_fragment_search_room_list);
        tvRoom.setText("Room " + (i + 1));

        linearLayout = (LinearLayout) view.findViewById(R.id.lv_guests_fragment_search_list);


        for (int j = 0; j < roomGuests.get(i).getGuests().size(); j++) {
            LinearLayout ll = (LinearLayout) lInflater.inflate(R.layout.fragment_search_room_list_item_child, null);

            TextView tvCount = (TextView) ll.findViewById(R.id.tv_counter_search_fragment_item_child);


            TextView tvType = (TextView) ll.findViewById(R.id.tv_type_search_fragment_item_child);
            ImageView ivType = (ImageView) ll.findViewById(R.id.iv_type_search_fragment_item_child);
            if (roomGuests.get(i).getGuests().get(j).isChild()) {
                tvType.setText("Child");
                if (roomGuests.get(i).getGuests().get(j).getAge() == 1) {
                    tvCount.setText(roomGuests.get(i).getGuests().get(j).getAge() + " year");
                } else {
                    tvCount.setText(roomGuests.get(i).getGuests().get(j).getAge() + " years");
                }
                ivType.setImageDrawable(res.getDrawable(R.drawable.child));
            } else {
                tvType.setText("Adult");
                tvCount.setText("x " + roomGuests.get(i).getGuests().get(j).getAge());
            }
            linearLayout.addView(ll);
        }

        return view;
    }

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    public void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

}
