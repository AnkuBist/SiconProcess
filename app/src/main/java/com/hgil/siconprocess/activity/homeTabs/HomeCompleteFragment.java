package com.hgil.siconprocess.activity.homeTabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hgil.siconprocess.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeCompleteFragment extends Fragment {


    public HomeCompleteFragment() {
        // Required empty public constructor
    }

    public static HomeCompleteFragment newInstance() {
        HomeCompleteFragment fragment = new HomeCompleteFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_complete, container, false);
    }

}
