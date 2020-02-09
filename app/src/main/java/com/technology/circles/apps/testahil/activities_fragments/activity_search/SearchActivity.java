package com.technology.circles.apps.testahil.activities_fragments.activity_search;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.ActivitySearchBinding;
import com.technology.circles.apps.testahil.adapter.ProductAdapter;
import com.technology.circles.apps.testahil.interfaces.Listeners;
import com.technology.circles.apps.testahil.language.LanguageHelper;
import com.technology.circles.apps.testahil.models.ProductDataModel;
import com.technology.circles.apps.testahil.models.ProductModel;
import com.technology.circles.apps.testahil.models.UserModel;
import com.technology.circles.apps.testahil.preferences.Preferences;
import com.technology.circles.apps.testahil.remote.Api;
import com.technology.circles.apps.testahil.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements Listeners.BackListener{
    private ActivitySearchBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private Animation animation;
    private LinearLayoutManager manager;
    private List<ProductModel> productModelList;
    private ProductAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        initView();

    }


    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        productModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        binding.setBackListener(this);
        animation = AnimationUtils.loadAnimation(this,R.anim.search_anim);
        binding.ll.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.ll.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);
        adapter = new ProductAdapter(productModelList,this,null);
        binding.recView.setAdapter(adapter);

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String query = editable.toString().trim();
                if (!query.isEmpty())
                {
                    search(query);
                }else
                    {
                        productModelList.clear();
                        adapter.notifyDataSetChanged();
                        binding.tvNoSearchResult.setVisibility(View.VISIBLE);
                    }
            }
        });

    }



    private void search(String query)
    {
        binding.tvNoSearchResult.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.VISIBLE);
        productModelList.clear();
        adapter.notifyDataSetChanged();
        try {

           Api.getService(Tags.base_url).searchByName(lang,"Bearer "+userModel.getToken(),query)
                   .enqueue(new Callback<ProductDataModel>() {
                @Override
                public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                    binding.progBar.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null&&response.body().getData()!=null) {

                        productModelList.clear();

                        if (response.body().getData().size()>0)
                        {
                            productModelList.addAll(response.body().getData());

                            binding.tvNoSearchResult.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        }else
                        {
                            binding.tvNoSearchResult.setVisibility(View.VISIBLE);
                        }



                    } else {

                        try {

                            Log.e("error", response.code() + "_" + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (response.code() == 500) {
                            Toast.makeText(SearchActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(SearchActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                        }
                    }
                }

                @Override
                public void onFailure(Call<ProductDataModel> call, Throwable t) {
                    try {
                        binding.progBar.setVisibility(View.GONE);
                        if (t.getMessage() != null) {
                            Log.e("error", t.getMessage());
                            if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                Toast.makeText(SearchActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SearchActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {

        }
    }


    @Override
    public void back() {
        finish();
    }



}
