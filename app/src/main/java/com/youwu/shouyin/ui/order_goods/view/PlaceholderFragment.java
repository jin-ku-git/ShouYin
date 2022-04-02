package com.youwu.shouyin.ui.order_goods.view;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.youwu.shouyin.R;


/**
 * fragment
 */
public  class PlaceholderFragment extends Fragment {
    private static final String ARG_SECTION = "section";

    public PlaceholderFragment() {
    }

    public static PlaceholderFragment newInstance(String section) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION, section);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getArguments().getString(ARG_SECTION));
        return rootView;
    }
}


