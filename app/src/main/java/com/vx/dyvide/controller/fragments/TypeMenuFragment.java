package com.vx.dyvide.controller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.vx.dyvide.R;
import com.vx.dyvide.model.DB.DB;

public class TypeMenuFragment extends Fragment {

    public TypeMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DB.setLanguage(getActivity());
        return inflater.inflate(R.layout.menu_fragment, container, false);


    }
}