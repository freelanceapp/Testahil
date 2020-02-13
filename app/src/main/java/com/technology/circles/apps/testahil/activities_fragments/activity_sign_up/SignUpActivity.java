package com.technology.circles.apps.testahil.activities_fragments.activity_sign_up;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.ActivitySignUpBinding;
import com.technology.circles.apps.testahil.activities_fragments.activity_code_verification.CodeVerificationActivity;
import com.technology.circles.apps.testahil.activities_fragments.activity_sign_in.LoginActivity;
import com.technology.circles.apps.testahil.interfaces.Listeners;
import com.technology.circles.apps.testahil.language.LanguageHelper;
import com.technology.circles.apps.testahil.models.SignUpModel;
import com.technology.circles.apps.testahil.models.UserModel;
import com.technology.circles.apps.testahil.remote.Api;
import com.technology.circles.apps.testahil.share.Common;
import com.technology.circles.apps.testahil.tags.Tags;

import java.io.IOException;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            signUp(signUpModel);
        }
    }

    private void signUp(SignUpModel signUpModel) {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .signUp(signUpModel.getName(),signUpModel.getEmail(),signUpModel.getPhone_code()+signUpModel.getPhone(),signUpModel.getPassword(),"0")
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                navigateToActivityCodeVerification(response.body());

                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else if (response.code() == 404) {
                                    Toast.makeText(SignUpActivity.this, R.string.inv_email_pass, Toast.LENGTH_SHORT).show();


                                }else {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(SignUpActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();

        }

    }

    private void navigateToActivityCodeVerification(UserModel userModel) {
        Intent intent = new Intent(this, CodeVerificationActivity.class);
        intent.putExtra("data",userModel);
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
