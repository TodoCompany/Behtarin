package com.todo.behtarinhotel.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.todo.behtarinhotel.R;

import java.util.ArrayList;

/**
 * Created by Andriy on 04.08.2015.
 */
public class PhotoPagerAdapter extends PagerAdapter{

    ArrayList<String> imageUrls;
    Context context;

    public PhotoPagerAdapter(Context context, ArrayList<String> urls){
        imageUrls = urls;
        this.context = context;
    }

    @Override
    public Object instantiateItem(View collection, int position){

        ImageView imageView = new ImageView(context);
        imageView.setBackgroundColor(context.getResources().getColor(R.color.base_dark));
        Glide.with(context)
                .load(imageUrls.get(position))
                .error(R.drawable.empty)
                .into(imageView);


                ((ViewPager) collection).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(View collection, int position, Object view){
        ((ViewPager) collection).removeView((View) view);
    }

    @Override
    public int getCount(){
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object){
        return view.equals(object);
    }

}
