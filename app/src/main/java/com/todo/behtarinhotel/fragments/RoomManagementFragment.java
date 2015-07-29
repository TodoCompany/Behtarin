package com.todo.behtarinhotel.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.adapters.RoomQueryAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomManagementFragment extends Fragment {


    LinearLayout roomsContainer;
    Button btnAddRoom, btnAddAdult, btnAddChild;
    EditText etChildAge;
    LayoutInflater inflater;


    ArrayList<View> rooms = new ArrayList<>();
    ArrayList<RoomQueryAdapter> lastRoomsAdapters = new ArrayList<>();
    View.OnClickListener addRoomListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final View roomView = inflater.inflate(R.layout.room_query_item, null, false);
            roomsContainer.addView(roomView);
            rooms.add(roomView);
            GridView roomGuestsList = (GridView) rooms.get(rooms.size() - 1).findViewById(R.id.roomGuestsList);
            TextView tvRoomPosition = (TextView) rooms.get(rooms.size() - 1).findViewById(R.id.tvRoomPosition);
            Button btnDeleteRoom = (Button) rooms.get(rooms.size() - 1).findViewById(R.id.btnDeleteRoom);
            final RoomQueryAdapter roomQueryAdapter = new RoomQueryAdapter(getActivity());
            lastRoomsAdapters.add(roomQueryAdapter);
            tvRoomPosition.setText("Room");
            roomGuestsList.setAdapter(roomQueryAdapter);
            btnDeleteRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rooms.remove(roomView);
                    roomsContainer.removeView(roomView);
                    lastRoomsAdapters.remove(roomQueryAdapter);

                }
            });
            roomGuestsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    roomQueryAdapter.remove(position);
                }
            });

        }
    };
    View.OnClickListener addAdultListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (lastRoomsAdapters.size() == 0) {
                Toast.makeText(getActivity(), "You must add room first", Toast.LENGTH_SHORT).show();
                return;
            }

            lastRoomsAdapters.get(lastRoomsAdapters.size() - 1).addAdult();
        }
    };
    View.OnClickListener addChildListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (lastRoomsAdapters.size() == 0) {
                Toast.makeText(getActivity(), "You must add room first", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                lastRoomsAdapters.get(lastRoomsAdapters.size() - 1).addChild(Integer.parseInt(etChildAge.getText().toString()));
            } catch (NumberFormatException nan) {
                Toast.makeText(getActivity(), "Please fill adult's age", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public RoomManagementFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_room_management, container, false);
        this.inflater = inflater;

        roomsContainer = (LinearLayout) rootView.findViewById(R.id.roomsContainer);
        btnAddRoom = (Button) rootView.findViewById(R.id.btnAddRoom);
        btnAddAdult = (Button) rootView.findViewById(R.id.btnAddAdult);
        btnAddChild = (Button) rootView.findViewById(R.id.btnAddChild);
        etChildAge = (EditText) rootView.findViewById(R.id.etChildAge);
        btnAddRoom.setOnClickListener(addRoomListener);
        btnAddAdult.setOnClickListener(addAdultListener);
        btnAddChild.setOnClickListener(addChildListener);

        addFirstRoom();

        return rootView;
    }

    private void addFirstRoom() {
        final View roomView = inflater.inflate(R.layout.room_query_item, null, false);
        roomsContainer.addView(roomView);
        rooms.add(roomView);
        GridView roomGuestsList = (GridView) rooms.get(rooms.size() - 1).findViewById(R.id.roomGuestsList);
        TextView tvRoomPosition = (TextView) rooms.get(rooms.size() - 1).findViewById(R.id.tvRoomPosition);
        Button btnDeleteRoom = (Button) rooms.get(rooms.size() - 1).findViewById(R.id.btnDeleteRoom);
        final RoomQueryAdapter roomQueryAdapter = new RoomQueryAdapter(getActivity());
        lastRoomsAdapters.add(roomQueryAdapter);
        tvRoomPosition.setText("Room: " + (rooms.size() - 1));
        roomGuestsList.setAdapter(roomQueryAdapter);
        btnDeleteRoom.setVisibility(View.GONE);
        roomGuestsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    roomQueryAdapter.remove(position);
                } else {
                    Toast.makeText(getActivity(), "In each room must be at least 1 child", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
