package com.todo.behtarinhotel.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.supportclasses.AppState;

import java.util.ArrayList;

import it.sephiroth.android.library.widget.AbsHListView;

/**
 * Created by Andriy on 05.08.2015.
 */
public class PhotoListAdapter extends BaseAdapter {

    public static final String PHOTO_URL_START = "http://images.travelnow.com";
    public static final String PHOTO_URL_END = "b.jpg";

    ArrayList<String> imageUrls;
    int checkedItem = 0;

    Context context;

    public PhotoListAdapter(Context context, ArrayList<String> urls){
        imageUrls = urls;
        this.context = context;
    }


    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
        }else{
            imageView = (ImageView) convertView;
        }

        AbsHListView.LayoutParams params = new AbsHListView.LayoutParams(AppState.convertToDp(100), ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setPadding(3, 3, 3, 3);
        if (checkedItem == position){
            Log.d("Adapter", "Setting yellow " + position + " item");
            imageView.setBackgroundColor(context.getResources().getColor(R.color.base_yellow));
        }else{
            Log.d("Adapter", "Setting inactive " + position + " item");
            imageView.setBackgroundColor(context.getResources().getColor(R.color.base_dark));
        }

        String temp = imageUrls.get(position)
                .substring(0, imageUrls.get(position).length() - 5);

        Glide.with(context)
                .load(PHOTO_URL_START + temp + PHOTO_URL_END)
                .error(R.drawable.empty)
                .into(imageView);

        Glide.with(context)
                .load(imageUrls.get(position))
                .error(R.drawable.empty)
                .into(imageView);


        return imageView;
    }


    public void setCheckedItem(int checkedItem) {
        this.checkedItem = checkedItem;
        notifyDataSetChanged();
    }
}
