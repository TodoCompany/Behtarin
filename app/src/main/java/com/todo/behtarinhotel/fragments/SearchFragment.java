package com.todo.behtarinhotel.fragments;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.edmodo.rangebar.RangeBar;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.todo.behtarinhotel.MainActivity;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.adapters.RoomListAdapter;
import com.todo.behtarinhotel.simpleobjects.SearchParamsSO;
import com.todo.behtarinhotel.simpleobjects.SearchRoomSO;
import com.todo.behtarinhotel.supportclasses.DatePickerFragment;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    ListView listView;
    RoomListAdapter listAdapter;
    ArrayList<SearchRoomSO> soArrayList;

    MaterialEditText etCheckIn;
    MaterialEditText etCheckOut;

    ImageButton ibStar1,ibStar2,ibStar3,ibStar4,ibStar5;
    ImageButton[] ibArray;

    EditText etLocation;
    EditText etRoom;
    EditText etAdult;
    EditText etChildren;
    Button btnSearchForHotels;
    RangeBar rbStars;

    LayoutInflater inflater;

    boolean isCheckInSelected;

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            int realMonth = monthOfYear + 1;
            String month = "" + realMonth;
            String day = "" + dayOfMonth;
            if (realMonth < 10) {
                month = "0" + realMonth;
            }
            if (dayOfMonth < 10) {
                day = "0" + day;
            }
            if (isCheckInSelected) {
                etCheckIn.setText(month + "/" + day + "/" + year);
            } else {
                etCheckOut.setText(month + "/" + day + "/" + year);
            }
        }
    };


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_search, null, false);

        initViewsById(rootView);

        listView = (ListView) rootView.findViewById(R.id.lv_room_fragment_search);
        soArrayList = new ArrayList<>();
        soArrayList.add(new SearchRoomSO(2,new int[]{10,20,15}));
        soArrayList.add(new SearchRoomSO(1,new int[]{10}));
        listAdapter = new RoomListAdapter(getActivity().getApplicationContext(),soArrayList);
        listView.setAdapter(listAdapter);
        //listView.addFooterView(rootView.findViewById(R.id.right_labels));

        return rootView ;


    }

    private void initViewsById(View view) {

        etCheckIn = (MaterialEditText) view.findViewById(R.id.et_check_in_search_fragment);
        etCheckOut = (MaterialEditText) view.findViewById(R.id.et_check_out_search_fragment);

        View.OnClickListener oclDatePicker = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.et_check_in_search_fragment) {
                    isCheckInSelected = true;
                }else {
                    isCheckInSelected = false;
                }
                showDatePicker();
            }
        };
        etCheckOut.setOnClickListener(oclDatePicker);
        etCheckIn.setOnClickListener(oclDatePicker);

        ibStar1 = (ImageButton)view.findViewById(R.id.ib_star_1_search_fragment);
        ibStar2 = (ImageButton)view.findViewById(R.id.ib_star_2_search_fragment);
        ibStar3 = (ImageButton)view.findViewById(R.id.ib_star_3_search_fragment);
        ibStar4 = (ImageButton)view.findViewById(R.id.ib_star_4_search_fragment);
        ibStar5 = (ImageButton)view.findViewById(R.id.ib_star_5_search_fragment);
        ibArray = new ImageButton[]{ibStar1,ibStar2,ibStar3,ibStar4,ibStar5};

        View.OnClickListener oclStars = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i<5;i++){
                    ibArray[i].setBackground(getResources().getDrawable(R.drawable.star_unselected));
                }
                switch (view.getId()){
                    case R.id.ib_star_1_search_fragment:{
                        for(int i = 0; i < 1;i++){
                            ibArray[i].setBackground(getResources().getDrawable(R.drawable.star_selected));
                        }
                        break;
                    }
                    case R.id.ib_star_2_search_fragment:{
                        for(int i = 0; i < 2;i++){
                            ibArray[i].setBackground(getResources().getDrawable(R.drawable.star_selected));
                        }
                        break;
                    }
                    case R.id.ib_star_3_search_fragment:{
                        for(int i = 0; i < 3;i++){
                            ibArray[i].setBackground(getResources().getDrawable(R.drawable.star_selected));
                        }
                        break;
                    }
                    case R.id.ib_star_4_search_fragment:{
                        for(int i = 0; i < 4;i++){
                            ibArray[i].setBackground(getResources().getDrawable(R.drawable.star_selected));
                        }
                        break;
                    }
                    case R.id.ib_star_5_search_fragment:{
                        for(int i = 0; i < 5;i++){
                            ibArray[i].setBackground(getResources().getDrawable(R.drawable.star_selected));
                        }
                        break;
                    }
                }
            }
        };
        for (int i = 0; i< 5 ; i++){
            ibArray[i].setOnClickListener(oclStars);
        }

//
//        etLocation = (EditText) view.findViewById(R.id.et_location_search_fragment);
//        etRoom = (EditText) view.findViewById(R.id.et_room_search_activity);
//        etAdult = (EditText) view.findViewById(R.id.et_adult_search_activity);
//        etChildren = (EditText) view.findViewById(R.id.et_children_search_activity);
//
//        rbStars = (RangeBar) view.findViewById(R.id.rb_stars);
//        rbStars.setTickCount(5);
//
//
        btnSearchForHotels = (Button) view.findViewById(R.id.btn_search_for_hotels_search_activity);
        btnSearchForHotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity parentActivity = (MainActivity) getActivity();
                MainFragment mainFragment = new MainFragment();
                parentActivity.setFragmentChild(mainFragment, parentActivity.getString(R.string.fragment_availablehotels));
                SearchParamsSO searchParamsSO = new SearchParamsSO("London", "10/10/2015", "10/12/2015");
                mainFragment.setSearchParams(searchParamsSO);

            }


        });
    }



    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */


        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

}
