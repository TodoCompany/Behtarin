package com.todo.behtarinhotel.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.adapters.PhotoListAdapter;
import com.todo.behtarinhotel.adapters.PhotoPagerAdapter;

import java.util.ArrayList;

import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.HListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotelPhotosFragment extends Fragment {


    ArrayList<String> imagesUrls;
    int positionToShow;

    PhotoPagerAdapter photoPagerAdapter;

    ViewPager pager;
    HListView horizontalPhotosList;

    public HotelPhotosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hotel_photos, container, false);
        if (imagesUrls != null) {
            pager = (ViewPager) rootView.findViewById(R.id.photoPager);
            horizontalPhotosList = new HListView(getActivity());
            final PhotoPagerAdapter photoPagerAdapter = new PhotoPagerAdapter(getActivity(), imagesUrls);
            final PhotoListAdapter photoListAdapter = new PhotoListAdapter(getActivity(), imagesUrls);
            horizontalPhotosList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            pager.setAdapter(photoPagerAdapter);
            horizontalPhotosList.setAdapter(photoListAdapter);
            ((FrameLayout) rootView.findViewById(R.id.horizontalListContainer)).addView(horizontalPhotosList);
            if (positionToShow != -1) {
                pager.setCurrentItem(positionToShow);
                photoListAdapter.setCheckedItem(positionToShow);
            }else{
                pager.setCurrentItem(0);
                photoListAdapter.setCheckedItem(0);
            }

            horizontalPhotosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    view.setBackgroundResource(R.color.base_yellow);
                    photoListAdapter.setCheckedItem(i);
                    pager.setCurrentItem(i);
                }
            });
            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    horizontalPhotosList.smoothScrollToPosition(position);
                    photoListAdapter.setCheckedItem(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
        return rootView;
    }

    public void setHotelId(ArrayList<String> imagesUrls, int positionToShow){
        this.imagesUrls = imagesUrls;
        this.positionToShow = positionToShow;
    }


}
