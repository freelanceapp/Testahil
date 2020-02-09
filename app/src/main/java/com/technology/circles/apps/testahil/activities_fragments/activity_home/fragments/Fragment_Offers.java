package com.technology.circles.apps.testahil.activities_fragments.activity_home.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.FragmentOffersBinding;
import com.technology.circles.apps.testahil.activities_fragments.activity_home.HomeActivity;
import com.technology.circles.apps.testahil.adapter.CategoryAdapter;
import com.technology.circles.apps.testahil.adapter.ProductAdapter;
import com.technology.circles.apps.testahil.models.CategoryDataModel;
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

public class Fragment_Offers extends Fragment {
    private FragmentOffersBinding binding;
    private HomeActivity activity;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private CategoryAdapter categoryAdapter;
    private List<CategoryDataModel.CategoryModel> categoryModelList;
    private List<ProductModel> productModelList;
    private ProductAdapter productAdapter;
    private String category_id = "";
    private Call<ProductDataModel> callProduct, callLoadMore;
    private boolean isCategoryLoading = false;
    private int category_current_page = 0;

    private boolean isProductLoading = false;
    private int product_current_page = 0;


    public static Fragment_Offers newInstance() {
        return new Fragment_Offers();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offers, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        productModelList = new ArrayList<>();
        categoryModelList = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);

        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBar2.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));

        categoryAdapter = new CategoryAdapter(categoryModelList, activity, this);
        binding.recViewCategory.setAdapter(categoryAdapter);

        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        productAdapter = new ProductAdapter(productModelList, activity, this);
        binding.recView.setAdapter(productAdapter);

        binding.recViewCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalItems = recyclerView.getAdapter().getItemCount();
                if (lastItem == (totalItems - 2) && !isCategoryLoading) {
                    isCategoryLoading = true;
                    int page = category_current_page + 1;
                    categoryModelList.add(null);
                    categoryAdapter.notifyItemInserted(categoryModelList.size() - 1);
                    loadMoreCategory(page);
                }
            }
        });

        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalItems = recyclerView.getAdapter().getItemCount();
                if (dy > 0 && lastItem == (totalItems - 2) && !isProductLoading) {
                    isProductLoading = true;
                    int page = product_current_page + 1;
                    productModelList.add(null);
                    productAdapter.notifyItemInserted(categoryModelList.size() - 1);
                    loadMoreProduct(page);
                }
            }
        });

        getCategories();
    }

    private void getCategories() {
        try {

            Api.getService(Tags.base_url)
                    .getCategories("Bearer " + userModel.getToken(), lang, 1)
                    .enqueue(new Callback<CategoryDataModel>() {
                        @Override
                        public void onResponse(Call<CategoryDataModel> call, Response<CategoryDataModel> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                categoryModelList.clear();

                                if (response.body().getData().size() > 0) {
                                    categoryModelList.add(null);
                                    categoryModelList.addAll(response.body().getData());

                                    binding.tvNoData.setVisibility(View.GONE);
                                    categoryAdapter.notifyDataSetChanged();

                                    getProduct("All");
                                } else {
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

    private void loadMoreCategory(int page) {
        try {

            Api.getService(Tags.base_url)
                    .getCategories("Bearer " + userModel.getToken(), lang, page)
                    .enqueue(new Callback<CategoryDataModel>() {
                        @Override
                        public void onResponse(Call<CategoryDataModel> call, Response<CategoryDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            isCategoryLoading = false;

                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                categoryModelList.remove(categoryModelList.size() - 1);
                                categoryAdapter.notifyItemRemoved(categoryModelList.size() - 1);
                                int oldPos = categoryModelList.size() - 1;

                                if (response.body().getData().size() > 0) {
                                    categoryModelList.addAll(response.body().getData());
                                    categoryAdapter.notifyItemRangeInserted(oldPos, categoryModelList.size());
                                    category_current_page = response.body().getCurrent_page();

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

                                isCategoryLoading = false;
                                if (categoryModelList.get(categoryModelList.size() - 1) == null) {
                                    categoryModelList.remove(categoryModelList.size() - 1);
                                    categoryAdapter.notifyItemRemoved(categoryModelList.size() - 1);

                                }
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

    private void getProduct(String category_id) {
        this.category_id = category_id;
        binding.tvNoData2.setVisibility(View.GONE);
        binding.progBar2.setVisibility(View.VISIBLE);
        productModelList.clear();
        productAdapter.notifyDataSetChanged();
        try {

            if (callProduct != null && callProduct.isExecuted()) {
                callProduct.cancel();
            }
            callProduct = Api.getService(Tags.base_url).getProductByCategory("Bearer " + userModel.getToken(), lang, category_id, 1);
            callProduct.enqueue(new Callback<ProductDataModel>() {
                @Override
                public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                    binding.progBar2.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                        productModelList.clear();

                        if (response.body().getData().size() > 0) {
                            productModelList.addAll(response.body().getData());

                            binding.tvNoData2.setVisibility(View.GONE);
                            productAdapter.notifyDataSetChanged();
                        } else {
                            binding.tvNoData2.setVisibility(View.VISIBLE);
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
                        binding.progBar2.setVisibility(View.GONE);
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


    private void loadMoreProduct(int page) {
        try {

            callLoadMore = Api.getService(Tags.base_url).getProductByCategory("Bearer " + userModel.getToken(), lang, category_id, page);

            callLoadMore.enqueue(new Callback<ProductDataModel>() {
                @Override
                public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                    isProductLoading = false;

                    if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                        productModelList.remove(productModelList.size() - 1);
                        productAdapter.notifyItemRemoved(productModelList.size() - 1);
                        int oldPos = productModelList.size() - 1;

                        if (response.body().getData().size() > 0) {
                            productModelList.addAll(response.body().getData());
                            productAdapter.notifyItemRangeInserted(oldPos, productModelList.size());
                            product_current_page = response.body().getCurrent_page();

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

                        isProductLoading = false;
                        if (productModelList.get(productModelList.size() - 1) == null) {
                            productModelList.remove(productModelList.size() - 1);
                            productAdapter.notifyItemRemoved(productModelList.size() - 1);

                        }
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


    public void setItemCategorySelected(String category_id) {

        if (!this.category_id.equals(category_id)) {
            if (callLoadMore !=null&& callLoadMore.isExecuted())
            {
                callLoadMore.cancel();
            }
            getProduct(category_id);
        }
    }
}
