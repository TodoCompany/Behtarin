package com.todo.behtarinhotel.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.supportclasses.AppState;

import java.util.ArrayList;

import it.sephiroth.android.library.widget.AbsHListView;

/**
 * Created by Andriy on 05.08.2015.
 */
public class PhotoListAdapter extends BaseAdapter {

    ArrayList<String> imageUrls;
    int checkedItem = 0;

    Context context;
    RequestListener listener;

    public PhotoListAdapter(final Context context, ArrayList<String> urls){
        imageUrls = urls;
        this.context = context;
        listener = new RequestListener() {
            @Override
            public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                String str = model.toString();
                Glide.with(context)
                        .load(str.substring(0, str.length() - 5) + "b.jpg")
                        .error(R.drawable.empty)
                        .into(target);
                return true;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        };

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

        Glide.with(context)
                .load(imageUrls.get(position))
                .error(R.drawable.empty)
                .listener(listener)
                .into(imageView);


        return imageView;
    }


    public void setCheckedItem(int checkedItem) {
        this.checkedItem = checkedItem;
        notifyDataSetChanged();
    }
}
