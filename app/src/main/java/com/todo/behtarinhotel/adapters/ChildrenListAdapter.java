package com.todo.behtarinhotel.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.fragments.RoomBuilderFragment;
import com.todo.behtarinhotel.simpleobjects.RoomQueryGuestSO;

import java.util.ArrayList;

/**
 * Created by dmytro on 7/29/15.
 */
public class ChildrenListAdapter extends BaseAdapter {

    static Dialog d ;
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<RoomQueryGuestSO> childrenSOArrayList;
    TextView childCounter;
    RoomBuilderFragment fragment;

    public ChildrenListAdapter(Context ctx, ArrayList<RoomQueryGuestSO> childrenSOArrayList,RoomBuilderFragment fragment) {
        this.ctx = ctx;
        this.childrenSOArrayList = childrenSOArrayList;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragment = fragment;
    }



    @Override
    public int getCount() {
        return childrenSOArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return childrenSOArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if(view == null){
            view = lInflater.inflate(R.layout.fragment_room_builder_list_item, null);
        }
            childCounter = (TextView) view.findViewById(R.id.tv_counter_room_builder_fragment_item_child);
        if(childrenSOArrayList.get(i).getAge() == 0){
            childCounter.setText("Age");
        }else{
            childCounter.setText(childrenSOArrayList.get(i).getAge() + "");
        }

        childCounter.setTag(i);


        childCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show(view,i);
            }
        });


        return view;
    }

    public void show(final View view, final int i)
    {

        final Dialog d = new Dialog(fragment.getActivity());
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(18); // max value 100
        np.setMinValue(1);   // min value 0
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ( (TextView) view).setText(String.valueOf(np.getValue())); //set the value to textview
                childrenSOArrayList.get(i).setAge(np.getValue());
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();
    }


}
