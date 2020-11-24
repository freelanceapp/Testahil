package com.technology.circles.apps.testahil.activities_fragments.activity_sign_in;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.FragmentSignInBinding;
import com.google.gson.Gson;
import com.technology.circles.apps.testahil.activities_fragments.activity_code_verification.CodeVerificationActivity;
import com.technology.circles.apps.testahil.activities_fragments.activity_home.HomeActivity;
import com.technology.circles.apps.testahil.activities_fragments.activity_sign_up.SignUpActivity;
import com.technology.circles.apps.testahil.interfaces.Listeners;
import com.technology.circles.apps.testahil.models.LoginModel;
import com.technology.circles.apps.testahil.models.UserModel;
import com.technology.circles.apps.testahil.preferences.Preferences;
import com.technology.circles.apps.testahil.remote.Api;
import com.technology.circles.apps.testahil.share.Common;
import com.technology.circles.apps.testahil.tags.Tags;

import java.io.IOException;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Sign_In extends Fragment implements Listeners.LoginListener, Listeners.SkipListener{
    private FragmentSignInBinding binding;
    private LoginActivity activity;
    private String lang;
    private LoginModel loginModel;
    private Preferences preferences;

    public static Fragment_Sign_In newInstance() {
        return new Fragment_Sign_In();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        preferences = Preferences.newInstance();
        loginModel = new LoginModel();
        activity = (LoginActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        binding.setLoginModel(loginModel);
        binding.setLoginListener(this);
        binding.setSkipListener(this);
        binding.setLoginListener(this);
        binding.tvNewAccount.setOnClickListener(view -> {
            Intent intent = new Intent(activity, SignUpActivity.class);
            startActivity(intent);
            activity.finish();
        });

    }


    @Override
    public void checkDataLogin() {

        if (loginModel.isDataValid(activity)) {
            Common.CloseKeyBoard(activity, binding.edtEmail);
            login(loginModel);
        }
    }

    private void login(LoginModel loginModel)
    {

        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .login(loginModel.getEmail(),loginModel.getPassword())
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                preferences.create_update_userData(activity, response.body());
                                preferences.createSession(activity, Tags.session_login);


                                Log.e("mmmmmmmmm",response.body().getId()+"");
                                Intent intent = new Intent(activity, HomeActivity.class);
                                startActivity(intent);
                                activity.finish();

                            } else {


                                if (response.code() == 409) {

                                    try {
                                        UserModel userModel = new Gson().fromJson(response.errorBody().string(),UserModel.class);

                                        Intent intent = new Intent(activity, CodeVerificationActivity.class);
                                        intent.putExtra("data",userModel);
                                        startActivity(intent);
                                        activity.finish();


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                } else if (response.code() == 404) {
                                    Toast.makeText(activity, R.string.inv_email_pass, Toast.LENGTH_SHORT).show();


                                }else if (response.code() == 406) {
                                    Toast.makeText(activity, R.string.user_blocked, Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
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
                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
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




    @Override
    public void skip() {
       /* Intent intent = new Intent(activity, MainAppCategoryActivity.class);
        startActivity(intent);
        activity.finish();*/
    }
}
