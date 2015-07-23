package com.todo.behtarinhotel.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.todo.behtarinhotel.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends Fragment {


    public TestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test, null, false);
        return rootView;
    }


}
