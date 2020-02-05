package com.technology.circles.apps.testahil.activities_fragments.activity_contact_us;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.ActivityContactUsBinding;
import com.technology.circles.apps.testahil.interfaces.Listeners;
import com.technology.circles.apps.testahil.language.LanguageHelper;
import com.technology.circles.apps.testahil.models.ContactUsModel;

import io.paperdb.Paper;

public class ContactUsActivity extends AppCompatActivity implements Listeners.BackListener , Listeners.ContactListener {
    private ActivityContactUsBinding binding;
    private String lang;
    private ContactUsModel contactUsModel;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        initView();

    }


    private void initView() {
        contactUsModel = new ContactUsModel();
        binding.setContactUs(contactUsModel);
        binding.setContactListener(this);
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        binding.setContactListener(this);
        binding.setBackListener(this);


    }


    @Override
    public void back() {
        finish();
    }


    @Override
    public void sendContact(ContactUsModel contactUsModel) {
        if (contactUsModel.isDataValid(this))
        {

        }
    }
}
