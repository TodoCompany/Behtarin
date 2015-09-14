package com.todo.behtarinhotel.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.fragments.RoomBuilderFragment;
import com.todo.behtarinhotel.simpleobjects.RoomQueryGuestSO;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ChildrenListAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<RoomQueryGuestSO> childrenSOArrayList;
    TextView childCounter;
    RoomBuilderFragment fragment;
    ImageButton btnRemoveChild;
    int position;

    public ChildrenListAdapter(Context ctx, ArrayList<RoomQueryGuestSO> childrenSOArrayList, RoomBuilderFragment fragment, int position) {
        this.ctx = ctx;
        this.childrenSOArrayList = childrenSOArrayList;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragment = fragment;
        this.position = position;
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
        if (view == null) {
            view = lInflater.inflate(R.layout.fragment_room_builder_list_item, null);
        }
        childCounter = (TextView) view.findViewById(R.id.tv_counter_room_builder_fragment_item_child);
        if (childrenSOArrayList.get(i).getAge() == 0) {
            childCounter.setText("Age");
        } else if (childrenSOArrayList.get(i).getAge() == 1) {
            childCounter.setText(childrenSOArrayList.get(i).getAge() + " year");
        } else {
            childCounter.setText(childrenSOArrayList.get(i).getAge() + " years");
        }

        childCounter.setTag(i);
        childCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show(view, i);
            }
        });

        btnRemoveChild = (ImageButton) view.findViewById(R.id.btn_remove_child_room_builder_fragment_item);
        btnRemoveChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childrenSOArrayList.remove(i);
                notifyDataSetChanged();
                fragment.minusChildCount();
            }
        });

        return view;
    }

    public void show(final View view, final int i) {

        final Dialog d = new Dialog(fragment.getActivity());
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.date_picker_dialog);
        MaterialEditText editText = (MaterialEditText) d.findViewById(R.id.editText);
        editText.setVisibility(View.GONE);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(18); // max value 100
        np.setMinValue(1);   // min value 0
        np.setWrapSelectorWheel(false);
        setNumberPickerTextColor(np, ctx.getResources().getColor(R.color.base_black));
        setDividerColor(np, ctx.getResources().getColor(R.color.base_black));
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (np.getValue() == 1) {
                    ((TextView) view).setText(String.valueOf(np.getValue()) + " year");
                } else {
                    ((TextView) view).setText(String.valueOf(np.getValue()) + " years");
                }
                //set the value to textview
                childrenSOArrayList.get(i).setAge(np.getValue());
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the date_picker_dialog
            }
        });
        d.show();
    }

    private boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText) child).setTextColor(color);
                    ((EditText) child).setFocusable(false);
                    ((EditText) child).setEnabled(false);

                    return true;
                } catch (NoSuchFieldException e) {
                    Log.w("setNumberPickerTextColo", e);
                } catch (IllegalAccessException e) {
                    Log.w("setNumberPickerTextColo", e);
                } catch (IllegalArgumentException e) {
                    Log.w("setNumberPickerTextColo", e);
                }
            }
        }
        return false;
    }

    private void setDividerColor(NumberPicker picker, int color) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

}
