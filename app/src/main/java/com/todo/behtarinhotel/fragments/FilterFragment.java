package com.todo.behtarinhotel.fragments;


import android.app.Dialog;
import android.app.Fragment;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.simpleobjects.FilterSO;

import java.lang.reflect.Field;

public class FilterFragment extends Fragment {

    TextView tvFrom;
    TextView tvTo;

    CheckBox chbStar1, chbStar2, chbStar3, chbStar4, chbStar5;
    CheckBox chbTrip1, chbTrip2, chbTrip3, chbTrip4, chbTrip5;
    CheckBox[] arrStar;
    CheckBox[] arrTrip;

    FilterSO filterParams;
    MainFragment parentFragment;
    Button btnApply;


    public FilterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_filter, container, false);

        initViewsById(v);

        return v;
    }

    private void initViewsById(View v) {
        tvFrom = (TextView) v.findViewById(R.id.tv_from_filter_fragment);
        tvTo = (TextView) v.findViewById(R.id.tv_to_filter_fragment);
        View.OnClickListener oclNumPicker = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show(view);
            }
        };
        tvFrom.setOnClickListener(oclNumPicker);
        tvTo.setOnClickListener(oclNumPicker);

        chbStar1 = (CheckBox) v.findViewById(R.id.chb_star_rate_1_filter_fragment);
        chbStar2 = (CheckBox) v.findViewById(R.id.chb_star_rate_2_filter_fragment);
        chbStar3 = (CheckBox) v.findViewById(R.id.chb_star_rate_3_filter_fragment);
        chbStar4 = (CheckBox) v.findViewById(R.id.chb_star_rate_4_filter_fragment);
        chbStar5 = (CheckBox) v.findViewById(R.id.chb_star_rate_5_filter_fragment);

        chbTrip1 = (CheckBox) v.findViewById(R.id.chb_trip_rate_1_filter_fragment);
        chbTrip2 = (CheckBox) v.findViewById(R.id.chb_trip_rate_2_filter_fragment);
        chbTrip3 = (CheckBox) v.findViewById(R.id.chb_trip_rate_3_filter_fragment);
        chbTrip4 = (CheckBox) v.findViewById(R.id.chb_trip_rate_4_filter_fragment);
        chbTrip5 = (CheckBox) v.findViewById(R.id.chb_trip_rate_5_filter_fragment);

        arrStar = new CheckBox[]{chbStar1, chbStar2, chbStar3, chbStar4, chbStar5};
        arrTrip = new CheckBox[]{chbTrip1, chbTrip2, chbTrip3, chbTrip4, chbTrip5};

        View.OnClickListener oclStars = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectData();
                setArr();
            }
        };
        for (int i = 0; i < 5; i++) {
            arrStar[i].setOnClickListener(oclStars);
            arrTrip[i].setOnClickListener(oclStars);
        }

        btnApply = (Button) v.findViewById(R.id.btn_apply_filter_fragment);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectData();
                getActivity().onBackPressed();
                parentFragment.setFilteredResults(filterParams);

            }


        });
        if (filterParams != null) {
            tvFrom.setText(filterParams.getMinPrice() + "");
            tvTo.setText(filterParams.getMaxPrice() + "");
            setArr();
        }
    }


    public void show(final View view) {

        final Dialog d = new Dialog(getActivity());
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.date_picker_dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final EditText et = (EditText) d.findViewById(R.id.editText);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(50); // max value 100
        np.setMinValue(0);   // min value 0
        if (view.getId() == R.id.tv_from_filter_fragment) {
            et.setText(tvFrom.getText().toString());
        } else {
            et.setText(tvTo.getText().toString());
        }

        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                int temp = value * 100;
                return "" + temp;
            }
        };
        np.setFormatter(formatter);
        np.setWrapSelectorWheel(false);
        setNumberPickerTextColor(np, getResources().getColor(R.color.base_black));
        setDividerColor(np, getResources().getColor(R.color.base_black));
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                et.setText("" + i1 * 100);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set the value to textview
                ((TextView) view).setText(et.getText().toString());
                if (v.getId() == R.id.tv_from_filter_fragment) {
                    filterParams.setMinPrice(Integer.valueOf(et.getText().toString()));
                } else {
                    filterParams.setMaxPrice(Integer.valueOf(et.getText().toString()));
                }
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
                    child.setFocusable(false);
                    child.setEnabled(false);

                    return true;
                } catch (Exception e) {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


    public void setFilterParams(FilterSO filterParams, MainFragment fragment) {
        this.filterParams = filterParams;
        parentFragment = fragment;
    }

    private void collectData() {
        filterParams.setMinPrice(Integer.valueOf(tvFrom.getText().toString()));
        filterParams.setMaxPrice(Integer.valueOf(tvTo.getText().toString()));
        boolean isMinStarSet = false;
        boolean isMinTripSet = false;
        for (int i = 0; i < 5; i++) {
            if (!isMinStarSet) {
                if (arrStar[i].isChecked()) {
                    filterParams.setMinStarRate(i);
                    filterParams.setMaxStarRate(i);
                    isMinStarSet = true;
                }
            } else {
                if (arrStar[i].isChecked()) {
                    filterParams.setMaxStarRate(i);
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            if (!isMinTripSet) {
                if (arrTrip[i].isChecked()) {
                    filterParams.setMinTripRate(i);
                    filterParams.setMaxTripRate(i);
                    isMinTripSet = true;
                }
            } else {
                if (arrTrip[i].isChecked()) {
                    filterParams.setMaxTripRate(i);
                }
            }
        }
    }

    private void setArr() {
        for (int i = 0; i < 5; i++) {
            if (i >= filterParams.getMinStarRate() && i <= filterParams.getMaxStarRate()) {
                arrStar[i].setChecked(true);
            }
        }
        for (int i = 0; i < 5; i++) {
            if (i >= filterParams.getMinTripRate() && i <= filterParams.getMaxTripRate()) {
                arrTrip[i].setChecked(true);
            }
        }
    }

}
