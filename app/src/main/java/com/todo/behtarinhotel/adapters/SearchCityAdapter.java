package com.todo.behtarinhotel.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.supportclasses.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dmytro on 07.08.2015.
 */
public class SearchCityAdapter extends BaseAdapter implements Filterable {


    private final Context mContext;
    private List<String> mResults;


    public SearchCityAdapter(Context context) {
        mContext = context;
        mResults = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public String getItem(int index) {
        return mResults.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.search_city_list_item, parent, false);
        }
        String city = getItem(position);
        ((TextView) convertView.findViewById(R.id.tv_city_searh_list_item)).setText(city);


        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected Filter.FilterResults performFiltering(CharSequence constraint) {
                Filter.FilterResults filterResults = new Filter.FilterResults();
                if (constraint != null) {
                    List<String> cities = findClients(constraint.toString());
                    // Assign the data to the FilterResults
                    if (cities != null) {
                        filterResults.values = cities;
                        filterResults.count = cities.size();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    mResults = (List<String>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return filter;
    }

    /**
     * Returns a search result for the given book title.
     */
    private List<String> findClients(String city) {
        HashMap<String, String> params = new HashMap<>();
        params.put("getCity", city);
        JSONObject obj = new JSONObject(params);
        String str = obj.toString();
        String url;
        url = "http://dev.behtarinhotel.com/api/user/booking/";
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url,new JSONObject(params), future, future);
        VolleySingleton.getInstance(mContext).addToRequestQueue(request);

        try {
            JSONObject response = future.get(30, TimeUnit.SECONDS); // this will block (forever)

            if (response != null) {
                Log.d("Response", response.toString());
                ArrayList<String> results = new ArrayList<>();
                JSONArray arr = new JSONArray();
                try {
                    arr = response.getJSONArray("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(arr!=null){
                    for(int i = 0; i<arr.length();i++){
                        try {
                            JSONObject tempObj = arr.getJSONObject(i);
                            String tempStr;
                            if(tempObj.getString("StateProvince").length()!=0){
                                tempStr = tempObj.getString("City") + (",") + tempObj.getString("StateProvince") + (",") + tempObj.getString("Country");
                            }else{
                                tempStr = tempObj.getString("City") + (",") + tempObj.getString("Country");
                            }
                            results.add(tempStr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return results;
            }

        } catch (Exception e) {
            e.printStackTrace();
            ArrayList<String> results = new ArrayList<>();
            results.add("123");
            return results;
        }

        return null;
    }

}