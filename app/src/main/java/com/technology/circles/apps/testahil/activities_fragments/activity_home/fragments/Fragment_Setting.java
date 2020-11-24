package com.technology.circles.apps.testahil.activities_fragments.activity_home.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.FragmentSettingBinding;
import com.technology.circles.apps.testahil.activities_fragments.activity_contact_us.ContactUsActivity;
import com.technology.circles.apps.testahil.activities_fragments.activity_home.HomeActivity;
import com.technology.circles.apps.testahil.activities_fragments.activity_language.LanguageActivity;
import com.technology.circles.apps.testahil.activities_fragments.activity_make_offer.MakeOfferActivity;
import com.technology.circles.apps.testahil.activities_fragments.activity_setting.SettingsActivity;
import com.technology.circles.apps.testahil.activities_fragments.activity_terms.TermsActivity;
import com.technology.circles.apps.testahil.interfaces.Listeners;
import com.technology.circles.apps.testahil.models.UserModel;
import com.technology.circles.apps.testahil.preferences.Preferences;
import com.technology.circles.apps.testahil.share.Common;

import io.paperdb.Paper;

public class Fragment_Setting extends Fragment implements Listeners.SettingActions {
    private FragmentSettingBinding binding;
    private HomeActivity activity;
    private Preferences preferences;
    private String lang;
    private UserModel userModel;



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
        userModel=preferences.getUserData(activity);

        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.setActions(this);
        binding.setLang(lang);
        //binding.tvVersion.setText(String.format("%s %s","Version : ", BuildConfig.VERSION_NAME));


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
    public void onEditProfile() {

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
    public void notifications() {

    }

    @Override
    public void onLanguageSetting() {

        Intent intent = new Intent(activity, LanguageActivity.class);
        intent.putExtra("type",1);
        startActivity(intent);
    }

    @Override
    public void makeOffer() {
        Intent intent = new Intent(activity, MakeOfferActivity.class);
        startActivity(intent);
    }

    @Override
    public void logout() {
        if (userModel != null) {
            activity.logout();
        } else {
        //   Common.CreateDialogAlert(activity, getString(R.string.please_sign_in_or_sign_up));
        }

    }

    @Override
    public void settings() {
        Intent intent = new Intent(activity, SettingsActivity.class);
        startActivity(intent);
    }


}
