package com.todo.behtarinhotel.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.todo.behtarinhotel.MainActivity;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.adapters.ChildrenListAdapter;
import com.todo.behtarinhotel.simpleobjects.RoomQueryGuestSO;
import com.todo.behtarinhotel.supportclasses.AppState;

import java.util.ArrayList;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomBuilderFragment extends Fragment {

    ImageButton btnMinus, btnPlus;
    Button btnAddChild;
    TextView tvAdults;
    ListView listView;
    LayoutInflater inflater;
    ArrayList<RoomQueryGuestSO> childrenSOArrayList;
    TextView tvTitleAdults;
    TextView tvTitleChilds;
    int position = 9;
    Button btn;
    int adultCount = 1;


    public RoomBuilderFragment() {
        childrenSOArrayList = new ArrayList<>();
    }

    public RoomBuilderFragment(ArrayList<RoomQueryGuestSO> childrenSOArrayList, int position) {
        this.childrenSOArrayList = new ArrayList<>();
        for (RoomQueryGuestSO item : childrenSOArrayList) {
            if (item.isChild()) {
                this.childrenSOArrayList.add(item);
            } else {
                adultCount = item.getAge();
            }
        }
        this.position = position;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_room_builder, container, false);

        btn = new Button(getActivity().getApplicationContext());
        btn.setBackgroundColor(0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1);
        params.rightMargin = AppState.convertToDp(16);
        btn.setLayoutParams(params);
        btn.setText("Done");
        btn.setTextColor(getResources().getColor(R.color.base_gold));
        btn.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        btn.setId(1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });


        ((MaterialNavigationDrawer) getActivity()).getToolbar().addView(btn);

        btnMinus = (ImageButton) rootView.findViewById(R.id.btn_minus_fragment_room_builder);
        btnPlus = (ImageButton) rootView.findViewById(R.id.btn_plus_fragment_room_builder);
        btnAddChild = (Button) rootView.findViewById(R.id.btn_add_child_fragment_room_builder);


        listView = (ListView) rootView.findViewById(R.id.lv_children_fragment_room_builder);

        final ChildrenListAdapter adapter = new ChildrenListAdapter(getActivity().getApplicationContext(),
                childrenSOArrayList
                , this, position);
        listView.setAdapter(adapter);


        tvAdults = (TextView) rootView.findViewById(R.id.tv_adults_fragment_room_builder);
        tvAdults.setText(adultCount + "");
        tvTitleAdults = (TextView) rootView.findViewById(R.id.tv_title_adults_fragment_room_builder);
        tvTitleAdults.setText("x " + adultCount);
        tvTitleChilds = (TextView) rootView.findViewById(R.id.tv_title_children_fragment_room_builder);

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minusAdultsCount();
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plusAdultsCount();
            }
        });

        btnAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.valueOf(tvTitleChilds.getText().toString().replaceAll("[^0-9]", "")) < 6) {
                    childrenSOArrayList.add(new RoomQueryGuestSO(true, 0));
                    plusChildCount();
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity().getApplicationContext()
                            , "Can`t be more than 6 children in one room", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }


    public void minusChildCount() {
        tvTitleChilds.setText("x " + (Integer.valueOf(tvTitleChilds.getText().toString().replaceAll("[^0-9]", "")) - 1));
    }

    public void plusChildCount() {
        tvTitleChilds.setText("x " + (Integer.valueOf(tvTitleChilds.getText().toString().replaceAll("[^0-9]", "")) + 1));
    }

    public void minusAdultsCount() {
        if (Integer.valueOf(tvAdults.getText().toString().replaceAll("[^0-9]", "")) > 1) {
            tvAdults.setText((Integer.valueOf(tvAdults.getText().toString().replaceAll("[^0-9]", "")) - 1) + "");
            tvTitleAdults.setText("x " + (Integer.valueOf(tvTitleAdults.getText().toString().replaceAll("[^0-9]", "")) - 1));

        } else {
            Toast.makeText(getActivity().getApplicationContext()
                    , "Must be at least 1 adult in the room", Toast.LENGTH_SHORT).show();
        }
    }

    public void plusAdultsCount() {
        if (Integer.valueOf(tvAdults.getText().toString().replaceAll("[^0-9]", "")) < 6) {
            tvAdults.setText((Integer.valueOf(tvAdults.getText().toString().replaceAll("[^0-9]", "")) + 1) + "");
            tvTitleAdults.setText("x " + (Integer.valueOf(tvTitleAdults.getText().toString().replaceAll("[^0-9]", "")) + 1));

        } else {
            Toast.makeText(getActivity().getApplicationContext()
                    , "Can`t be more than 6 adults in one room", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void sendData() {
        boolean isAgeSetted = true;
        for (RoomQueryGuestSO item : childrenSOArrayList) {
            if (item.getAge() == 0) {
                isAgeSetted = false;
            }
        }
        if (isAgeSetted) {
            getActivity().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
            getActivity().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
            RoomQueryGuestSO guest = new RoomQueryGuestSO(false, Integer.valueOf(tvAdults.getText().toString().replaceAll("[^0-9]", "")));
            childrenSOArrayList.add(0, guest);
            ((MainActivity) getActivity()).updateArrayListInFragment(childrenSOArrayList, position);
        } else {
            Toast.makeText(getActivity().getApplicationContext()
                    , "Make sure you have entered children`s ages", Toast.LENGTH_SHORT).show();
        }
    }



}
