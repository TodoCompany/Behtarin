package com.todo.behtarinhotel.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.adapters.ChildrenListAdapter;
import com.todo.behtarinhotel.simpleobjects.RoomQueryGuestSO;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomBuilderFragment extends Fragment {

    Button btnMinus, btnPlus;
    Button btnAddChild;
    TextView tvAdults;
    ListView listView;
    LayoutInflater inflater;
    ArrayList<RoomQueryGuestSO> childrenSOArrayList;



    public RoomBuilderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_room_builder, container, false);

        btnMinus = (Button) rootView.findViewById(R.id.btn_minus_fragment_room_builder);
        btnPlus = (Button) rootView.findViewById(R.id.btn_plus_fragment_room_builder);
        btnAddChild = (Button) rootView.findViewById(R.id.btn_add_child_fragment_room_builder);

        listView = (ListView) rootView.findViewById(R.id.lv_children_fragment_room_builder);
        childrenSOArrayList = new ArrayList<>();
        final ChildrenListAdapter adapter = new ChildrenListAdapter(getActivity().getApplicationContext(),childrenSOArrayList);
        listView.setAdapter(adapter);


        tvAdults = (TextView) rootView.findViewById(R.id.tv_adults_fragment_room_builder);

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.valueOf(tvAdults.getText().toString()) > 1) {
                    tvAdults.setText((Integer.valueOf(tvAdults.getText().toString()) - 1) + "");
                }
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.valueOf(tvAdults.getText().toString()) < 8) {
                    tvAdults.setText((Integer.valueOf(tvAdults.getText().toString()) + 1) + "");
                }
            }
        });

        btnAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childrenSOArrayList.add(new RoomQueryGuestSO(true,0));
                adapter.notifyDataSetChanged();
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }


}
