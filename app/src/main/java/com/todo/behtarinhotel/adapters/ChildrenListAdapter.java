package com.todo.behtarinhotel.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.simpleobjects.RoomQueryGuestSO;

import java.util.ArrayList;

/**
 * Created by dmytro on 7/29/15.
 */
public class ChildrenListAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<RoomQueryGuestSO> childrenSOArrayList;
    MaterialEditText metChildrenCounter;

    public ChildrenListAdapter(Context ctx, ArrayList<RoomQueryGuestSO> childrenSOArrayList) {
        this.ctx = ctx;
        this.childrenSOArrayList = childrenSOArrayList;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.fragment_room_builder_list_item, null);
        }

        return view;
    }

}
