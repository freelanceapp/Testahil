package com.technology.circles.apps.testahil.activities_fragments.activity_home.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.FragmentFavoriteBinding;
import com.technology.circles.apps.testahil.activities_fragments.activity_home.HomeActivity;
import com.technology.circles.apps.testahil.preferences.Preferences;

import io.paperdb.Paper;

public class Fragment_Favorite extends Fragment {
    private FragmentFavoriteBinding binding;
    private HomeActivity activity;
    private Preferences preferences;
    private String lang;



    public static Fragment_Favorite newInstance() {
        return new Fragment_Favorite();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


    }




}
