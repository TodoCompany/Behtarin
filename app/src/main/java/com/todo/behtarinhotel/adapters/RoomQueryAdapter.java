package com.todo.behtarinhotel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.simpleobjects.RoomQueryGuestSO;

import java.util.ArrayList;

/**
 * Created by Andriy on 23.07.2015.
 */
public class RoomQueryAdapter extends BaseAdapter {

    ArrayList<RoomQueryGuestSO> roomGuests;
    Context context;
    LayoutInflater inflater;

    public RoomQueryAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        roomGuests = new ArrayList<>();
        addAdult();
    }

    public ArrayList<RoomQueryGuestSO> getRoomGuests() {
        return roomGuests;
    }

    public void addChild(int age) {
        int childsInRoom = 0;
        for (RoomQueryGuestSO guest : roomGuests) {
            if (guest.isChild()) {
                childsInRoom++;
            }
        }
        if (childsInRoom < 5) {
            roomGuests.add(new RoomQueryGuestSO(true,age));
            notifyDataSetChanged();
        } else {
            Toast.makeText(context, "There can't be more than 6 childs in room", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean removeChild(int age) {
        for (int i = 0; i < roomGuests.size(); i++) {
            if (roomGuests.get(i).getAge() == age) {
                roomGuests.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean removeAdult() {
        for (int i = 0; i < roomGuests.size(); i++) {
            if (!roomGuests.get(i).isChild()) {
                roomGuests.remove(i);
                return true;
            }
        }
        return false;
    }

    public void remove(int position) {
        roomGuests.remove(position);
        notifyDataSetChanged();
    }

    public void addAdult() {
        roomGuests.add(new RoomQueryGuestSO());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return roomGuests.size();
    }

    @Override
    public Object getItem(int position) {
        return roomGuests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item;
        if (convertView == null) {
            item = inflater.inflate(R.layout.room_query_child_item, null, false);
        } else {
            item = convertView;
        }

        ImageView touristImageView = (ImageView) item.findViewById(R.id.touristImageView);
        TextView tvChildYears = (TextView) item.findViewById(R.id.tvChildYears);
        if (roomGuests.get(position).isChild()) {
            touristImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_launcher));
            tvChildYears.setText(roomGuests.get(position).getAge() + "years");
        } else {
            touristImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_profile));
            tvChildYears.setText("");
        }

        return item;
    }
}
