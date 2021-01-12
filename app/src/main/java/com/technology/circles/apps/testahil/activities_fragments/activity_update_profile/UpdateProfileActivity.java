package com.technology.circles.apps.testahil.activities_fragments.activity_update_profile;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;


import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.ActivityUpdateProfileBinding;
import com.squareup.picasso.Picasso;
import com.technology.circles.apps.testahil.interfaces.Listeners;
import com.technology.circles.apps.testahil.language.LanguageHelper;
import com.technology.circles.apps.testahil.models.UpdateProfileModel;
import com.technology.circles.apps.testahil.models.UserModel;
import com.technology.circles.apps.testahil.preferences.Preferences;
import com.technology.circles.apps.testahil.remote.Api;
import com.technology.circles.apps.testahil.share.Common;
import com.technology.circles.apps.testahil.tags.Tags;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityUpdateProfileBinding binding;
    private UpdateProfileModel model;
    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_profile);
        initView();

    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        binding.setLang(lang);
        model = new UpdateProfileModel();
        binding.setModel(model);

        if (userModel != null) {
            model.setName(userModel.getName());
            model.setPhone(userModel.getPhone());
            binding.setModel(model);

        }


        binding.btnUpdate.setOnClickListener(v -> checkDataValid());
    }




    public void checkDataValid() {

        if (model.isDataValid(this)) {
            Common.CloseKeyBoard(this, binding.edtName);
                updateProfile();


        }

    }


    private void updateProfile() {


        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Log.e("cccccc","Bearer " + userModel.getToken());
        Log.e("cccccc","________" + userModel.getName());
        Log.e("cccccc","________" + userModel.getEmail());

        Api.getService(Tags.base_url)
                .updateProfile("Bearer " + userModel.getToken(), model.getName(), userModel.getEmail(), model.getPhone())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {

                            preferences.create_update_userData(UpdateProfileActivity.this, response.body());
                            Toast.makeText(UpdateProfileActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();


                        } else {
                            if (response.code() == 500) {
                                Toast.makeText(UpdateProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(UpdateProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }

                            try {
                                Log.e("error", response.errorBody().string());
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
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(UpdateProfileActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(UpdateProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }


    @Override
    public void back() {
        finish();
    }
}
