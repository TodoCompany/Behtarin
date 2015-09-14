package com.todo.behtarinhotel.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.todo.behtarinhotel.R;

import java.util.ArrayList;

public class PhotoPagerAdapter extends PagerAdapter {

    ArrayList<String> imageUrls;
    Context context;

    RequestListener listener;

    public PhotoPagerAdapter(final Context context, ArrayList<String> urls) {
        imageUrls = urls;
        this.context = context;

        listener = new RequestListener() {
            @Override
            public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                String str = model.toString();
                Glide.with(context)
                        .load(str.substring(0, str.length() - 5) + "b.jpg")
                        .error(R.drawable.empty)
                        .listener(listener)
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
    public Object instantiateItem(View collection, int position) {

        ImageView imageView = new ImageView(context);
        imageView.setBackgroundColor(context.getResources().getColor(R.color.base_dark));
        String temp = imageUrls.get(position)
                .substring(0, imageUrls.get(position).length() - 5);
        Glide.with(context)
                .load(temp + "z.jpg")
                .error(R.drawable.empty)
                .listener(listener)
                .into(imageView);
        ((ViewPager) collection).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((View) view);
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

}
