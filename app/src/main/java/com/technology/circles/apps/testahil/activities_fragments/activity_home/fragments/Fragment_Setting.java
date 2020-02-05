package com.technology.circles.apps.testahil.activities_fragments.activity_home.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.testahil.BuildConfig;
import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.FragmentSettingBinding;
import com.technology.circles.apps.testahil.activities_fragments.activity_contact_us.ContactUsActivity;
import com.technology.circles.apps.testahil.activities_fragments.activity_home.HomeActivity;
import com.technology.circles.apps.testahil.activities_fragments.activity_terms.TermsActivity;
import com.technology.circles.apps.testahil.interfaces.Listeners;
import com.technology.circles.apps.testahil.preferences.Preferences;

import io.paperdb.Paper;

public class Fragment_Setting extends Fragment implements Listeners.MoreActions {
    private FragmentSettingBinding binding;
    private HomeActivity activity;
    private Preferences preferences;
    private String lang;



    public static Fragment_Setting newInstance() {
        return new Fragment_Setting();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.setAction(this);
        binding.tvVersion.setText(String.format("%s %s","Version : ", BuildConfig.VERSION_NAME));


    }


    @Override
    public void aboutApp() {
        Intent intent = new Intent(activity, TermsActivity.class);
        intent.putExtra("type",2);
        startActivity(intent);
    }

    @Override
    public void contactUs() {
        Intent intent = new Intent(activity, ContactUsActivity.class);
        startActivity(intent);
    }

    @Override
    public void rateApp() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + activity.getPackageName())));

        }
    }

    @Override
    public void terms() {
        Intent intent = new Intent(activity, TermsActivity.class);
        intent.putExtra("type",1);
        startActivity(intent);
    }

    @Override
    public void share() {
        String app_url = "https://play.google.com/store/apps/details?id=" + activity.getPackageName();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "Home Care App ");
        intent.putExtra(Intent.EXTRA_TEXT, app_url);
        startActivity(intent);
    }

    @Override
    public void editProfile() {

    }
}
