package com.technology.circles.apps.testahil.activities_fragments.activity_home.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.FragmentOffersBinding;
import com.technology.circles.apps.testahil.activities_fragments.activity_home.HomeActivity;
import com.technology.circles.apps.testahil.adapter.CategoryAdapter;
import com.technology.circles.apps.testahil.models.CategoryDataModel;
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

public class Fragment_Offers extends Fragment {
    private FragmentOffersBinding binding;
    private HomeActivity activity;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private CategoryAdapter categoryAdapter;
    private List<CategoryDataModel.CategoryModel> categoryModelList;




    public static Fragment_Offers newInstance() {
        return new Fragment_Offers();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offers,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();

        categoryModelList = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);

        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBar2.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));

        categoryAdapter = new CategoryAdapter(categoryModelList,activity,this);
        binding.recViewCategory.setAdapter(categoryAdapter);

        getCategories();
    }

    private void getCategories()
    {
        try {

            Api.getService(Tags.base_url)
                    .getCategories("Bearer "+userModel.getToken(),lang)
                    .enqueue(new Callback<CategoryDataModel>() {
                        @Override
                        public void onResponse(Call<CategoryDataModel> call, Response<CategoryDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null&&response.body().getData()!=null) {

                                categoryModelList.clear();

                                if (response.body().getData().size()>0)
                                {
                                    categoryModelList.add(null);
                                    categoryModelList.addAll(response.body().getData());

                                    binding.tvNoData.setVisibility(View.GONE);
                                    categoryAdapter.notifyDataSetChanged();
                                }else
                                    {
                                        binding.tvNoData.setVisibility(View.VISIBLE);
                                        binding.tvNoData2.setVisibility(View.VISIBLE);
                                        binding.progBar2.setVisibility(View.GONE);
                                    }



                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CategoryDataModel> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);
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

        }
    }


}
