package com.todo.behtarinhotel.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.simpleobjects.SearchRoomSO;

import java.util.ArrayList;

/**
 * Created by Andriy on 11.08.2015.
 */
public class ConfirmRoomsInfoAdapter extends BaseAdapter {

    ArrayList<SearchRoomSO> rooms;
    SearchRoomSO defaultRoomData;
    LayoutInflater lInflater;

    public ConfirmRoomsInfoAdapter(Context context, ArrayList<SearchRoomSO> rooms, SearchRoomSO defaultRoomData) {

        this.defaultRoomData = defaultRoomData;
        this.rooms = rooms;
        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return rooms.size();
    }

    @Override
    public Object getItem(int position) {
        return rooms.get(position);
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
            view = lInflater.inflate(R.layout.confirm_room_item, null, false);
        }
        TextView tvRoomInfoFirstName = (TextView) view.findViewById(R.id.tvRoomInfoFirstName);
        TextView tvRoomInfoLastName = (TextView) view.findViewById(R.id.tvRoomInfoLastName);
        TextView tvAdultCount = (TextView) view.findViewById(R.id.tvWizardAdultCount);
        TextView tvChildCount = (TextView) view.findViewById(R.id.tvWizardChildCount);
        RadioButton rbSmokingState = (RadioButton) view.findViewById(R.id.rbSmokingState);
        RadioButton rbBedType = (RadioButton) view.findViewById(R.id.rbBedType);

        if (rooms.get(position).getFirstName().equals("") & !rooms.get(position).getFirstName().equals(defaultRoomData.getFirstName())) rooms.get(position).setFirstName(defaultRoomData.getFirstName());
        if (rooms.get(position).getLastName().equals("") & !rooms.get(position).getLastName().equals(defaultRoomData.getLastName())) rooms.get(position).setLastName(defaultRoomData.getLastName());
        tvRoomInfoFirstName.setText(Html.fromHtml("First name: " + "<b>" + rooms.get(position).getFirstName() + "</b>"));
        tvRoomInfoLastName.setText(Html.fromHtml("Last name: "+ "<b>" + rooms.get(position).getLastName() + "</b>"));
        tvAdultCount.setText("x" + rooms.get(position).getGuests().get(0).getAge());
        tvChildCount.setText("x" + (rooms.get(position).getGuests().size() - 1));
        rbBedType.setText(rooms.get(position).getBedType());

        switch (rooms.get(position).getSmokingPreference()){
            case SearchRoomSO.SMOKING:
                rbSmokingState.setText("Smoking");
                break;
            case SearchRoomSO.NOT_SMOKING:
                rbSmokingState.setText("Not smoking");
                break;
            case SearchRoomSO.EITHER:
                rbSmokingState.setText("Either");
                break;
        }


        return view;
    }
}
