package com.todo.behtarinhotel.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.adapters.MainActivityMainListAdapter;
import com.todo.behtarinhotel.simpleobjects.SearchResultSO;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    ArrayList<SearchResultSO> searchResultSOArrayList;
    ListView listView;




    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        listView = (ListView) rootView.findViewById(R.id.lv_main_list_main_activity);

        if (searchResultSOArrayList != null){
            listView.setAdapter(new MainActivityMainListAdapter(getActivity(), searchResultSOArrayList));
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    public void initMailList(ArrayList<SearchResultSO> soArrayList){
         searchResultSOArrayList = soArrayList;


    }



}