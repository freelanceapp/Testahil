package com.technology.circles.apps.testahil.activities_fragments.activity_sign_up;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.ActivitySignUpBinding;
import com.technology.circles.apps.testahil.activities_fragments.activity_code_verification.CodeVerificationActivity;
import com.technology.circles.apps.testahil.activities_fragments.activity_sign_in.LoginActivity;
import com.technology.circles.apps.testahil.interfaces.Listeners;
import com.technology.circles.apps.testahil.language.LanguageHelper;
import com.technology.circles.apps.testahil.models.SignUpModel;

import io.paperdb.Paper;

public class SignUpActivity extends AppCompatActivity implements Listeners.BackListener,Listeners.SignUpListener{
    private ActivitySignUpBinding binding;
    private SignUpModel signUpModel;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        initView();
    }

    private void initView() {
        signUpModel = new SignUpModel();
        signUpModel.setPhone_code("00966");
        binding.setSignUpModel(signUpModel);
        binding.setBackListener(this);
        binding.setSignupListener(this);
    }

    @Override
    public void checkDataSignUp(SignUpModel signUpModel) {

        if (signUpModel.isDataValid(this))
        {
            signUp();
        }
    }

    private void signUp() {

        navigateToActivityCodeVerification();
    }

    private void navigateToActivityCodeVerification() {
        Intent intent = new Intent(this, CodeVerificationActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void back() {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }
}
