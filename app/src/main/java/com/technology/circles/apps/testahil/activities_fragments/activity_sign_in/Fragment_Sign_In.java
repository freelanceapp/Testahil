package com.technology.circles.apps.testahil.activities_fragments.activity_sign_in;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.FragmentSignInBinding;
import com.technology.circles.apps.testahil.activities_fragments.activity_sign_up.SignUpActivity;
import com.technology.circles.apps.testahil.interfaces.Listeners;
import com.technology.circles.apps.testahil.models.LoginModel;
import com.technology.circles.apps.testahil.preferences.Preferences;
import com.technology.circles.apps.testahil.share.Common;

import io.paperdb.Paper;

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

    private void login(LoginModel loginModel) {

//        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
//        dialog.setCancelable(false);
//        dialog.show();
//        try {
//
//            Api.getService(Tags.base_url)
//                    .login(lang, loginModel.getPhone_code(),loginModel.getPhone(), loginModel.getPassword())
//                    .enqueue(new Callback<UserModel>() {
//                        @Override
//                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                            dialog.dismiss();
//                            if (response.isSuccessful() && response.body() != null) {
//                                preferences.create_update_userData(activity, response.body());
//                                preferences.createSession(activity, Tags.session_login);
//
//                                Intent intent = new Intent(activity, MainAppCategoryActivity.class);
//                                startActivity(intent);
//                                activity.finish();
//
//                            } else {
//
//                                try {
//
//                                    Log.e("error", response.code() + "_" + response.errorBody().string());
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//
//                                if (response.code() == 500) {
//                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
//
//
//                                } else if (response.code() == 404) {
//                                    Toast.makeText(activity, R.string.user_not_found, Toast.LENGTH_SHORT).show();
//
//
//                                }else if (response.code() == 406) {
//                                    Toast.makeText(activity, R.string.user_bloked, Toast.LENGTH_SHORT).show();
//
//
//                                } else {
//                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//
//
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<UserModel> call, Throwable t) {
//                            try {
//                                dialog.dismiss();
//                                if (t.getMessage() != null) {
//                                    Log.e("error", t.getMessage());
//                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
//                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
//                            } catch (Exception e) {
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//            dialog.dismiss();
//
//        }
    }




    @Override
    public void skip() {
       /* Intent intent = new Intent(activity, MainAppCategoryActivity.class);
        startActivity(intent);
        activity.finish();*/
    }
}
