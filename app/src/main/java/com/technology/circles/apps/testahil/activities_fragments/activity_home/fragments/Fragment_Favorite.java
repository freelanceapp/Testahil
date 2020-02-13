package com.technology.circles.apps.testahil.activities_fragments.activity_home.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.FragmentFavoriteBinding;
import com.technology.circles.apps.testahil.activities_fragments.activity_home.HomeActivity;
import com.technology.circles.apps.testahil.activities_fragments.activity_product_details.ProductDetailsActivity;
import com.technology.circles.apps.testahil.adapter.ProductAdapter;
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

public class Fragment_Favorite extends Fragment {
    private FragmentFavoriteBinding binding;
    private HomeActivity activity;
    private Preferences preferences;
    private String lang;
    private UserModel userModel;
    private List<ProductModel> productModelList;
    private ProductAdapter productAdapter;
    private int selectedPos = -1;



    public static Fragment_Favorite newInstance() {
        return new Fragment_Favorite();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        productModelList = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        productAdapter = new ProductAdapter(productModelList,activity,this);
        binding.recView.setAdapter(productAdapter);

        getProduct();
    }


    public void getProduct()
    {
        try {

            Api.getService(Tags.base_url).getMyFavoriteProduct("Bearer "+userModel.getToken(),lang,"love")
            .enqueue(new Callback<ProductDataModel>() {
                @Override
                public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                    binding.progBar.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null&&response.body().getData()!=null) {

                        productModelList.clear();

                        if (response.body().getData().size()>0)
                        {
                            productModelList.addAll(response.body().getData());

                            binding.tvNoData.setVisibility(View.GONE);
                            productAdapter.notifyDataSetChanged();



                        }else
                        {
                            binding.tvNoData.setVisibility(View.VISIBLE);
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
                public void onFailure(Call<ProductDataModel> call, Throwable t) {
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

    public void setItemProduct(ProductModel model, int adapterPosition) {
        this.selectedPos = adapterPosition;
        Intent intent = new Intent(activity, ProductDetailsActivity.class);
        intent.putExtra("data",model);
        startActivityForResult(intent,100);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode== Activity.RESULT_OK)
        {
            if (productModelList.size()>0&&selectedPos!=-1)
            {
                productModelList.remove(selectedPos);
                productAdapter.notifyItemRemoved(selectedPos);

                if (productModelList.size()>0)
                {
                    binding.tvNoData.setVisibility(View.GONE);
                }else
                    {
                        binding.tvNoData.setVisibility(View.VISIBLE);

                    }
            }
        }
    }
}
