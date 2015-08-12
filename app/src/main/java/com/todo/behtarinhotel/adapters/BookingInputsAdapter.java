package com.todo.behtarinhotel.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.simpleobjects.AvailableRoomsSO;
import com.todo.behtarinhotel.simpleobjects.SearchRoomSO;

import java.util.ArrayList;

/**
 * Created by Andriy on 07.08.2015.
 */
public class BookingInputsAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater inflater;


    private ArrayList<SearchRoomSO> rooms;
    AvailableRoomsSO.RoomSO roomInfo;


    public BookingInputsAdapter(Context context, ArrayList<SearchRoomSO> rooms, AvailableRoomsSO.RoomSO roomInfo) {
        ctx = context;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.rooms = rooms;
        this.roomInfo = roomInfo;
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
        final View view;
        if (convertView != null) {
            view = convertView;
        } else {
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
                switch (checkedId) {
                    case R.id.rbSmoking:
                        rooms.get(position).setSmokingPreference(SearchRoomSO.SMOKING);
                        break;
                    case R.id.rbNotSmoking:
                        rooms.get(position).setSmokingPreference(SearchRoomSO.NOT_SMOKING);
                        break;
                    case R.id.rbEither:
                        rooms.get(position).setSmokingPreference(SearchRoomSO.EITHER);
                        break;
                }
            }
        });

        RadioGroup radioGroupBeds = (RadioGroup) view.findViewById(R.id.radioGroupBeds);

        for(AvailableRoomsSO.Bed bed : roomInfo.getBeds()){
            RadioButton rb = new RadioButton(ctx);
            rb.setText(bed.getBedDescript());
            rb.setTextColor(ctx.getResources().getColor(R.color.base_text));
            rb.setId(bed.getId());
            radioGroupBeds.addView(rb);
        }
        radioGroupBeds.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) view.findViewById(checkedId);
                rooms.get(position).setBedTypeId(checkedId);
                rooms.get(position).setBedType(checkedRadioButton.getText().toString());
            }
        });


        return view;
    }


    public ArrayList<SearchRoomSO> getRooms() {

        return rooms;
    }
}
