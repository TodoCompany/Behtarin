package com.todo.behtarinhotel.fragments;


import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.internal.widget.ActivityChooserView;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;
import com.todo.behtarinhotel.MainActivity;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.adapters.MainActivityMainListAdapter;
import com.todo.behtarinhotel.simpleobjects.FilterSO;
import com.todo.behtarinhotel.simpleobjects.SearchResultSO;
import com.todo.behtarinhotel.supportclasses.AppState;
import com.todo.behtarinhotel.supportclasses.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends Fragment {

    TextView tvFrom;
    TextView tvTo;

    CheckBox chbStar1, chbStar2, chbStar3, chbStar4, chbStar5;
    CheckBox chbTrip1, chbTrip2, chbTrip3, chbTrip4, chbTrip5;
    CheckBox[] arrStar;
    CheckBox[] arrTrip;

    GsonBuilder gsonBuilder;
    Gson gson;
    private ArrayList<SearchResultSO> searchResultSOArrayList;
    FilterSO filterParams;
    MainFragment parentFragment;
    Button btnApply;
    String apiKey = "&apiKey=";
    String sig = "&sig=" + AppState.getMD5EncryptedString(apiKey + "RyqEsq69" + System.currentTimeMillis() / 1000L);
    String url;



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
        for(int i = 0; i<5; i++){
            arrStar[i].setOnClickListener(oclStars);
            arrTrip[i].setOnClickListener(oclStars);
        }

        btnApply = (Button) v.findViewById(R.id.btn_apply_filter_fragment);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                collectData();
//                getActivity().onBackPressed();
//                parentFragment.setFilteredResults(filterParams);
                url = "https://book.api.ean.com/ean-services/rs/hotel/v3/res?" +
                        "&cid=55505" +
                        sig +
                        "&apiKey=RyqEsq69" +
                        "&customerIpAddress=192.168.0.1" +
                        "&customerSessionId=0" +
                        "&locale=en_US" +
                        "&minorRev=30" +
                        "&currencyCode=USD" +
                        "&hotelId=106347" +
                        "&arrivalDate=09/05/2015" +
                        "&departureDate=09/07/2015" +
                        "&supplierType=E" +
                        "&rateKey=af00b688-acf4-409e-8bdc-fcfc3d1cb80c" +
                        "&roomTypeCode=198058" +
                        "&rateCode=484072" +
                        "&chargeableRate=257.20" +
                        "&room1=2,5,7" +
                        "&room1FirstName=test" +
                        "&room1LastName=testers" +
                        "&room1BedTypeId=23" +
                        "&room1SmokingPreference=NS" +
                        "&email=test@yourSite.com" +
                        "&firstName=tester" +
                        "&lastName=testing" +
                        "&homePhone=123123" +
                        "&workPhone=123123" +
                        "&creditCardType=CA" +
                        "&creditCardNumber=5401999999999999" +
                        "&creditCardIdentifier=123" +
                        "&creditCardExpirationMonth=11" +
                        "&creditCardExpirationYear=2017" +
                        "&address1=travelnow" +
                        "&city=Seattle" +
                        "&stateProvinceCode=WA" +
                        "&countryCode=US" +
                        "&postalCode=98004";

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,"https://book.api.ean.com/ean-services/rs/hotel/v3/res?cid=55505&apiKey=7tuermyqnaf66ujk2dk3rkfk&minorRev=26&customerSessionId=7916870766fac2d71c1d740919589836&customerIpAddress=193.93.218.85&customerUserAgent=Mozilla%2F5.0+%28Windows+NT+6.3%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Chrome%2F44.0.2403.130+Safari%2F537.36¤cyCode=USD&hotelId=463538&arrivalDate=08%2F13%2F2015&departureDate=08%2F14%2F2015&supplierType=E&rateKey=0ce2e36d-4514-4656-b890-945520ce1b49&roomTypeCode=200719009&rateCode=203567761&chargeableRate=65.38&email=kryvun.roman%40gmail.com&firstName=Roman&lastName=Kryvun&homePhone=1234234&creditCardType=CA&creditCardNumber=5401999999999999&creditCardIdentifier=123&creditCardExpirationMonth=11&creditCardExpirationYear=2015&address1=Zelena+53&city=Lviv&stateProvinceCode=123&countryCode=UA&postalCode=123&affiliateConfirmationId=d0e779996556b3b62e17d2361a9a01d6&specialInformation=&room1=2&room1FirstName=zxvc&room1LastName=Last&room1BedTypeId=43&room1SmokingPreference=NS",

                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    // response :"status":200,"success":"Yep"

                                    Log.i("Response :", response.toString());

                                    if(response.getInt("status") == 200){

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });
                VolleySingleton.getInstance(getActivity()).addToRequestQueue(req);
            }


        });
        if(filterParams!=null){
            tvFrom.setText(filterParams.getMinPrice()+"");
            tvTo.setText(filterParams.getMaxPrice()+"");
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
        if(view.getId() == R.id.tv_from_filter_fragment){
            et.setText(tvFrom.getText().toString());
        }else{
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
                et.setText("" + i1*100);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set the value to textview
                ((TextView) view).setText(et.getText().toString());
                if(v.getId() == R.id.tv_from_filter_fragment){
                    filterParams.setMinPrice(Integer.valueOf(et.getText().toString()));
                }else{
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


    public void setFilterParams(FilterSO filterParams,MainFragment fragment){
        this.filterParams = filterParams;
        parentFragment = fragment;
    }

    private void collectData() {
        filterParams.setMinPrice(Integer.valueOf(tvFrom.getText().toString()));
        filterParams.setMaxPrice(Integer.valueOf(tvTo.getText().toString()));
        boolean isMinStarSet=false;
        boolean isMinTripSet=false;
        for(int i = 0; i<5;i++){
            if(!isMinStarSet){
                if(arrStar[i].isChecked()){
                    filterParams.setMinStarRate(i);
                    filterParams.setMaxStarRate(i);
                    isMinStarSet = true;
                }
            }else{
                if(arrStar[i].isChecked()){
                    filterParams.setMaxStarRate(i);
                }
            }
        }
        for(int i = 0; i<5;i++){
            if(!isMinTripSet){
                if(arrTrip[i].isChecked()){
                    filterParams.setMinTripRate(i);
                    filterParams.setMaxTripRate(i);
                    isMinTripSet = true;
                }
            }else{
                if(arrTrip[i].isChecked()){
                    filterParams.setMaxTripRate(i);
                }
            }
        }
    }

    private void setArr(){
        for(int i = 0; i < 5; i++){
            if(i>=filterParams.getMinStarRate()&&i<=filterParams.getMaxStarRate()){
                    arrStar[i].setChecked(true);
            }
        }
        for(int i = 0; i < 5; i++){
            if(i >= filterParams.getMinTripRate()&& i <= filterParams.getMaxTripRate()){
                arrTrip[i].setChecked(true);
            }
        }
    }

}
