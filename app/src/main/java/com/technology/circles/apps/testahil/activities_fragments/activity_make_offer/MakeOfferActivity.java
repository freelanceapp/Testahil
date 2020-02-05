package com.technology.circles.apps.testahil.activities_fragments.activity_make_offer;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.ActivityMakeOfferBinding;
import com.technology.circles.apps.testahil.interfaces.Listeners;
import com.technology.circles.apps.testahil.language.LanguageHelper;
import com.technology.circles.apps.testahil.models.MakeOfferModel;

import java.util.Locale;

import io.paperdb.Paper;

public class MakeOfferActivity extends AppCompatActivity implements Listeners.BackListener,Listeners.MakeOfferListener{
    private ActivityMakeOfferBinding binding;
    private String lang;
    private MakeOfferModel makeOfferModel;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_make_offer);
        initView();
    }



    private void initView()
    {
        makeOfferModel = new MakeOfferModel();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setModel(makeOfferModel);
        binding.setBackListener(this);
        binding.setLang(lang);
        binding.setMakeOfferListener(this);




    }




    @Override
    public void back() {
        finish();
    }

    @Override
    public void sendOffer(MakeOfferModel makeOfferModel) {

        if (makeOfferModel.isDataValid(this))
        {
            makeOffer(makeOfferModel);
        }
    }

    private void makeOffer(MakeOfferModel makeOfferModel) {

    }
}
