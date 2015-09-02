package com.todo.behtarinhotel.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.edmodo.rangebar.RangeBar;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.todo.behtarinhotel.MainActivity;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.adapters.RoomListAdapter;
import com.todo.behtarinhotel.adapters.SearchCityAdapter;
import com.todo.behtarinhotel.simpleobjects.RoomQueryGuestSO;
import com.todo.behtarinhotel.simpleobjects.SearchParamsSO;
import com.todo.behtarinhotel.simpleobjects.SearchResultSO;
import com.todo.behtarinhotel.simpleobjects.SearchRoomSO;
import com.todo.behtarinhotel.supportclasses.AppState;
import com.todo.behtarinhotel.supportclasses.DatePickerFragment;
import com.todo.behtarinhotel.supportclasses.DelayAutoCompleteTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private boolean isSearchWithHotelId = false;
    private int hotelID;
    ListView listView;
    public RoomListAdapter listAdapter;
    public ArrayList<SearchRoomSO> soArrayList;

    FloatingActionButton fabSearch, fabAddRoom;
    FloatingActionsMenu famMenu;

    MaterialEditText etCheckIn;
    MaterialEditText etCheckOut;

    ImageButton ibStar1, ibStar2, ibStar3, ibStar4, ibStar5;
    ImageButton[] ibArray;

    public RoomBuilderFragment builderFragment;

    DelayAutoCompleteTextView etLocation;
    LayoutInflater inflater;
    MainFragment mainFragment;
    private String hotelName;


    boolean isCheckInSelected;
    int starCount;

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
        if (soArrayList == null) {
            soArrayList = new ArrayList<SearchRoomSO>();
            RoomQueryGuestSO guest = new RoomQueryGuestSO(false, 2);
            ArrayList<RoomQueryGuestSO> guestSOs = new ArrayList<>();
            guestSOs.add(guest);
            soArrayList.add(new SearchRoomSO(guestSOs));
        }


        if (((MaterialNavigationDrawer) getActivity()).getToolbar().findViewById(1) != null) {
            ((MaterialNavigationDrawer) getActivity()).getToolbar().removeView(((MaterialNavigationDrawer) getActivity()).getToolbar().findViewById(1));
        }

        listAdapter = new RoomListAdapter(getActivity().getApplicationContext(), soArrayList, this);
        listView.setAdapter(listAdapter);


        return rootView;
    }

    @Override
    public void onDestroy() {
        listAdapter.onDestroy();
        super.onDestroy();
    }

    private void initViewsById(View view) {

        etLocation = (DelayAutoCompleteTextView) view.findViewById(R.id.et_location_search_fragment);
        etLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        etLocation.setThreshold(2);
        etLocation.setAdapter(new SearchCityAdapter(getActivity()));
        ProgressBar pb = (ProgressBar) view.findViewById(R.id.progress_bar);
        pb.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.base_gold), PorterDuff.Mode.SRC_IN);
        etLocation.setLoadingIndicator(pb);

        fabSearch = (FloatingActionButton) view.findViewById(R.id.fab_search_fragment_search);
        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("MMddyyyy");
                int currentDate = Integer.valueOf(format.format(calendar.getTime()));
                if (etLocation.getText().toString().equals("") ||
                        etCheckIn.getText().toString().equals("") ||
                        etCheckOut.getText().toString().equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (Integer.valueOf(etCheckIn.getText().toString().replaceAll("[^0-9]", "")) >=
                            Integer.valueOf(etCheckOut.getText().toString().replaceAll("[^0-9]", "")) ||
                            Integer.valueOf(etCheckIn.getText().toString().replaceAll("[^0-9]", ""))<
                                    currentDate ) {
                        Toast.makeText(getActivity().getApplicationContext(), "Wrong dates", Toast.LENGTH_SHORT).show();
                    } else {
                        MainActivity parentActivity = (MainActivity) getActivity();
                        mainFragment = new MainFragment();
                        parentActivity.setFragmentChild(mainFragment, parentActivity.getString(R.string.fragment_availablehotels));
                        SearchParamsSO searchParamsSO = new SearchParamsSO(etLocation.getText().toString(),
                                etCheckIn.getText().toString(), etCheckOut.getText().toString(), soArrayList, starCount);

                        mainFragment.setSearchParams(searchParamsSO, isSearchWithHotelId, hotelID);
                        famMenu.collapse();
                    }
                }
            }
        });


        fabAddRoom = (FloatingActionButton) view.findViewById(R.id.fab_add_room_fragment_search);
        fabAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (soArrayList.size() < 8) {
                    builderFragment = new RoomBuilderFragment();
                    ((MaterialNavigationDrawer) getActivity()).setFragmentChild(builderFragment, "Complete");
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Can`t be more than 8 rooms", Toast.LENGTH_SHORT).show();
                }


                famMenu.collapse();

            }
        });
        famMenu = (FloatingActionsMenu) view.findViewById(R.id.right_labels);


        etCheckIn = (MaterialEditText) view.findViewById(R.id.et_check_in_search_fragment);
        etCheckOut = (MaterialEditText) view.findViewById(R.id.et_check_out_search_fragment);


        View.OnClickListener oclDatePicker = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.et_check_in_search_fragment) {
                    isCheckInSelected = true;
                } else {
                    isCheckInSelected = false;
                }
                showDatePicker();
            }
        };
        etCheckOut.setOnClickListener(oclDatePicker);
        etCheckIn.setOnClickListener(oclDatePicker);

        ibStar1 = (ImageButton) view.findViewById(R.id.ib_star_1_search_fragment);
        ibStar2 = (ImageButton) view.findViewById(R.id.ib_star_2_search_fragment);
        ibStar3 = (ImageButton) view.findViewById(R.id.ib_star_3_search_fragment);
        ibStar4 = (ImageButton) view.findViewById(R.id.ib_star_4_search_fragment);
        ibStar5 = (ImageButton) view.findViewById(R.id.ib_star_5_search_fragment);
        ibArray = new ImageButton[]{ibStar1, ibStar2, ibStar3, ibStar4, ibStar5};

        View.OnClickListener oclStars = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 5; i++) {
                    ibArray[i].setBackground(getResources().getDrawable(R.drawable.star_unselected));
                }
                switch (view.getId()) {
                    case R.id.ib_star_1_search_fragment: {
                        for (int i = 0; i < 1; i++) {
                            ibArray[i].setBackground(getResources().getDrawable(R.drawable.star_selected));
                            starCount = 1;
                        }
                        break;
                    }
                    case R.id.ib_star_2_search_fragment: {
                        for (int i = 0; i < 2; i++) {
                            ibArray[i].setBackground(getResources().getDrawable(R.drawable.star_selected));
                            starCount = 2;
                        }
                        break;
                    }
                    case R.id.ib_star_3_search_fragment: {
                        for (int i = 0; i < 3; i++) {
                            ibArray[i].setBackground(getResources().getDrawable(R.drawable.star_selected));
                        }
                        starCount = 3;
                        break;
                    }
                    case R.id.ib_star_4_search_fragment: {
                        for (int i = 0; i < 4; i++) {
                            ibArray[i].setBackground(getResources().getDrawable(R.drawable.star_selected));
                        }
                        starCount = 4;
                        break;
                    }
                    case R.id.ib_star_5_search_fragment: {
                        for (int i = 0; i < 5; i++) {
                            ibArray[i].setBackground(getResources().getDrawable(R.drawable.star_selected));
                        }
                        starCount = 5;
                        break;
                    }
                }
            }
        };
        for (int i = 0; i < 5; i++) {
            ibArray[i].setOnClickListener(oclStars);
        }
        if(isSearchWithHotelId){
            changeSearchType();
        }
    }




    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();

        /**
         * Set Up Current Date Into date_picker_dialog
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

    public void addRoom(SearchRoomSO room) {
        soArrayList.add(room);
        listAdapter.notifyDataSetChanged();
    }

    public void addRoom(SearchRoomSO room, int position) {
        soArrayList.get(position).setGuests(room.getGuests());
        listAdapter.notifyDataSetChanged();
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void setParameters(int holelId,String hotelName){
        this.hotelID = holelId;
        isSearchWithHotelId = true;
        this.hotelName = hotelName;
    }

    private void changeSearchType(){
        etLocation.setText(Html.fromHtml(Html.fromHtml(hotelName).toString()).toString());
        etLocation.setEnabled(false);
    }


}
