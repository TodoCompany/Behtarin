package com.todo.behtarinhotel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.todo.behtarinhotel.R;

import java.util.ArrayList;

/**
 * Created by Andriy on 03.08.2015.
 */
public class HotelImagesAdapter extends BaseAdapter {

    public static final int PHOTOS_GRID_SIZE = 6;

    Context ctx;
    ArrayList<String> urls;
    LayoutInflater layoutInflater;
    RequestListener listener;

     public HotelImagesAdapter(final Context ctx, ArrayList<String> urls) {
         this.ctx = ctx;
         this.urls = urls;
         layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         listener = new RequestListener() {
             @Override
             public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                 String str = model.toString();
                 Glide.with(ctx)
                         .load(str.substring(0, str.length() - 5) + "b.jpg")
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
        return urls.size();
    }

    @Override
    public Object getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (position == PHOTOS_GRID_SIZE - 1 & getCount() > PHOTOS_GRID_SIZE){
            view = layoutInflater.inflate(R.layout.more_photos_item, null, false);
            Button btnLoadMorePhotos = (Button) view.findViewById(R.id.btnLoadMorePhotos);
            btnLoadMorePhotos.setText("+" + (getCount() - PHOTOS_GRID_SIZE));
            ImageView imageView = (ImageView) view.findViewById(R.id.imageLoadMore);
            imageView.setImageDrawable(ctx.getResources().getDrawable(R.drawable.empty));

            Glide.with(ctx)
                    .load(urls.get(position))
                    .listener(listener)
                    .into(imageView);
        }else{
            ImageView imageView = new ImageView(ctx);
            imageView.setImageDrawable(ctx.getResources().getDrawable(R.drawable.empty));
            Glide.with(ctx)
                    .load(urls.get(position))
                    .listener(listener)
                    .into(imageView);
            view = imageView;
        }


        return view;
    }
}
