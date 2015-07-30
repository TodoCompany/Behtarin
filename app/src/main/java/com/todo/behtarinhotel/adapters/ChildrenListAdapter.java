package com.todo.behtarinhotel.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.todo.behtarinhotel.R;
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

             View view = lInflater.inflate(R.layout.fragment_room_builder_list_item, null);
            childCounter = (TextView) view.findViewById(R.id.tv_counter_room_builder_fragment_item_child);

        childCounter.setTag(i);


        childCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });


        return view;
    }

    private void showPopupMenu(final View v) {
        PopupMenu popupMenu = new PopupMenu(ctx, v);
        popupMenu.inflate(R.menu.popupmenu); // Для Android 4.0
        // для версии Android 3.0 нужно использовать длинный вариант
        // popupMenu.getMenuInflater().inflate(R.menu.popupmenu,
        // popupMenu.getMenu());

        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Toast.makeText(PopupMenuDemoActivity.this,
                        // item.toString(), Toast.LENGTH_LONG).show();
                        // return true;
                        switch (item.getItemId()) {

                            case R.id.menu1:
                                ( (TextView) v).setText("1");
                                childrenSOArrayList.get((int)v.getTag()).setAge(1);
                                return true;
                            case R.id.menu2:
                                ( (TextView) v).setText("1");
                                childrenSOArrayList.get((int)v.getTag()).setAge(1);
                                return true;
                            case R.id.menu3:
                                ( (TextView) v).setText("1");
                                childrenSOArrayList.get((int)v.getTag()).setAge(1);
                                return true;
                            case R.id.menu4:
                                ( (TextView) v).setText("1");
                                childrenSOArrayList.get((int)v.getTag()).setAge(1);
                                return true;
                            case R.id.menu5:
                                ( (TextView) v).setText("1");
                                childrenSOArrayList.get((int)v.getTag()).setAge(1);
                                return true;
                            case R.id.menu6:
                                ( (TextView) v).setText("1");
                                childrenSOArrayList.get((int)v.getTag()).setAge(1);
                                return true;
                            case R.id.menu7:
                                ( (TextView) v).setText("1");
                                childrenSOArrayList.get((int)v.getTag()).setAge(1);
                                return true;
                            case R.id.menu8:
                                ( (TextView) v).setText("1");
                                childrenSOArrayList.get((int)v.getTag()).setAge(1);
                                return true;
                            case R.id.menu9:
                                childCounter.setText("9");
                                return true;
                            case R.id.menu10:
                                childCounter.setText("10");
                                return true;
                            case R.id.menu11:
                                childCounter.setText("11");
                                return true;
                            case R.id.menu12:
                                childCounter.setText("12");
                                return true;
                            case R.id.menu13:
                                childCounter.setText("13");
                                return true;
                            case R.id.menu14:
                                childCounter.setText("14");
                                return true;
                            case R.id.menu15:
                                childCounter.setText("15");
                                return true;
                            case R.id.menu16:
                                childCounter.setText("16");
                                return true;
                            case R.id.menu17:
                                childCounter.setText("17");
                                return true;
                            case R.id.menu18:
                                childCounter.setText("18");
                                return true;

                            default:
                                return false;
                        }
                    }
                });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {

            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(ctx, "onDismiss",
                        Toast.LENGTH_SHORT).show();
            }
        });
        popupMenu.show();
    }

}
