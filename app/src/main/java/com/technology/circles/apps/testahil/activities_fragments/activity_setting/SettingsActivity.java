package com.technology.circles.apps.testahil.activities_fragments.activity_setting;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.ActivitySettingsBinding;
import com.technology.circles.apps.testahil.activities_fragments.activity_language.LanguageActivity;
import com.technology.circles.apps.testahil.activities_fragments.activity_sign_up.SignUpActivity;
import com.technology.circles.apps.testahil.interfaces.Listeners;
import com.technology.circles.apps.testahil.language.LanguageHelper;
import com.technology.circles.apps.testahil.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.BuildConfig;
import io.paperdb.Paper;

public class SettingsActivity extends AppCompatActivity implements Listeners.SettingActions {
    private ActivitySettingsBinding binding;
    private String lang;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        super.attachBaseContext(LanguageHelper.updateResources(base, Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        initView();
    }

    private void initView() {
        preferences = Preferences.newInstance();

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setActions(this);
        binding.close.setOnClickListener(v -> finish());


    }


    @Override
    public void onEditProfile() {
       /* Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("data",preferences.getUserData(this));
        startActivityForResult(intent,2);*/
    }

    @Override
    public void terms() {

    }

    @Override
    public void aboutApp() {

    }

    @Override
    public void share() {

    }

    @Override
    public void notifications() {

    }

    @Override
    public void onLanguageSetting() {
        Intent intent = new Intent(this, LanguageActivity.class);
        intent.putExtra("type",1);
        startActivity(intent);
    }

    @Override
    public void makeOffer() {

    }

    @Override
    public void logout() {

    }


    @Override
    public void settings() {

    }

    @Override
    public void contactUs() {

    }



}