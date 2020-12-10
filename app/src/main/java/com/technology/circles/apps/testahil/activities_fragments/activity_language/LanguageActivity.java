package com.technology.circles.apps.testahil.activities_fragments.activity_language;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.ActivityLanguageBinding;
import com.technology.circles.apps.testahil.activities_fragments.activity_home.HomeActivity;

import com.technology.circles.apps.testahil.language.LanguageHelper;

import io.paperdb.Paper;

public class LanguageActivity extends AppCompatActivity {

    private ActivityLanguageBinding binding;
    private String lang;
    private boolean canSelect = false;
    private String selectedLang = "";

    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        super.attachBaseContext(LanguageHelper.updateResources(base, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_language);
        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        selectedLang = lang;

        binding.setLang(lang);
        binding.close.setOnClickListener(v -> finish());
        if (lang.equals("ar")) {
            binding.tvAr.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            binding.imageAr.setVisibility(View.VISIBLE);
            binding.tvEn.setTextColor(ContextCompat.getColor(this, R.color.gray6));
            binding.imageEn.setVisibility(View.GONE);

        } else {
            binding.tvAr.setTextColor(ContextCompat.getColor(this, R.color.gray6));
            binding.imageAr.setVisibility(View.GONE);
            binding.tvEn.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            binding.imageEn.setVisibility(View.VISIBLE);
        }

        binding.consAr.setOnClickListener(v -> {

            binding.tvAr.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            binding.imageAr.setVisibility(View.VISIBLE);
            binding.tvEn.setTextColor(ContextCompat.getColor(this, R.color.gray6));
            binding.imageEn.setVisibility(View.GONE);

            if (lang.equals("ar")) {
                selectedLang = lang;
                canSelect = false;

            } else {

                canSelect = true;
                selectedLang = "ar";


            }

            if (canSelect) {
//                Paper.book().write("lang",selectedLang);
//                Language_Helper.updateResources(this,selectedLang);
//                setResult(RESULT_OK);
//                finish();
                RefreshActivity(selectedLang);
            }
        });


        binding.consEn.setOnClickListener(v -> {
            binding.tvAr.setTextColor(ContextCompat.getColor(this, R.color.gray6));
            binding.imageAr.setVisibility(View.GONE);
            binding.tvEn.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            binding.imageEn.setVisibility(View.VISIBLE);

            if (lang.equals("ar")) {

                canSelect = true;
                selectedLang = "en";

            } else {
                selectedLang = lang;
                canSelect = false;
            }

            if (canSelect) {
//                Paper.book().write("lang",selectedLang);
//                Language_Helper.updateResources(this,selectedLang);
//                setResult(RESULT_OK);
//                finish();
                RefreshActivity(selectedLang);
            }
        });


        binding.btnConfirm.setOnClickListener(v -> {

        });
    }

    public void RefreshActivity(String lang) {
        Paper.book().write("lang", lang);
        LanguageHelper.setNewLocale(this, lang);
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(LanguageActivity.this, HomeActivity.class);
                        finishAffinity();
                        startActivity(intent);
                    }
                }, 1050);


    }

    private void updateBtnUi() {
        if (canSelect) {
            binding.btnConfirm.setTextColor(ContextCompat.getColor(this, R.color.white));
            binding.btnConfirm.setBackgroundResource(R.drawable.small_rounded_primary);
        } else {
            binding.btnConfirm.setTextColor(ContextCompat.getColor(this, R.color.gray9));
            binding.btnConfirm.setBackgroundResource(R.drawable.small_rounded_gray);
        }
    }
}