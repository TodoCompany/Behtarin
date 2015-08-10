package com.todo.behtarinhotel.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.simpleobjects.SearchRoomSO;

import java.util.ArrayList;

/**
 * Created by Andriy on 07.08.2015.
 */
public class BookingInputsAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater inflater;


    private ArrayList<SearchRoomSO> rooms;



    public BookingInputsAdapter(Context context, ArrayList<SearchRoomSO> rooms){
        ctx = context;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.rooms = rooms;


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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null){
            view = convertView;
        }else{
            view = inflater.inflate(R.layout.wizard_room_page, null, false);
        }

        final EditText etWizardRoomFirstName = (EditText) view.findViewById(R.id.etWizardRoomFirstName);
        final EditText etWizardRoomLastName = (EditText) view.findViewById(R.id.etWizardRoomLastName);
        etWizardRoomFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                rooms.get(position).setFirstName(etWizardRoomFirstName.getText().toString());
            }
        });
        etWizardRoomLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                rooms.get(position).setLastName(etWizardRoomLastName.getText().toString());
            }
        });


        RadioGroup radioGroupSmoking = (RadioGroup) view.findViewById(R.id.radioGroupSmoking);
        radioGroupSmoking.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbSmoking:
                        //TODO change to code from api
                        rooms.get(position).setSmokingPreference("NS");
                        break;
                    case R.id.rbNotSmoking:
                        //TODO change to code from api
                        rooms.get(position).setSmokingPreference("NS");
                        break;
                    case R.id.rbEither:
                        //TODO change to code from api
                        rooms.get(position).setSmokingPreference("NS");
                        break;
                }
            }
        });




        return view;
    }


    public ArrayList<SearchRoomSO> getRooms() {
        return rooms;
    }
}
