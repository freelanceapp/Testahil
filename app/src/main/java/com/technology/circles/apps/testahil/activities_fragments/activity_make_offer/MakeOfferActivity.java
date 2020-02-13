package com.technology.circles.apps.testahil.activities_fragments.activity_make_offer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.ActivityMakeOfferBinding;
import com.creative.share.apps.testahil.databinding.DialogAlertBinding;
import com.creative.share.apps.testahil.databinding.DialogCategoryBinding;
import com.technology.circles.apps.testahil.adapter.CategorySpinnerAdapter;
import com.technology.circles.apps.testahil.interfaces.Listeners;
import com.technology.circles.apps.testahil.language.LanguageHelper;
import com.technology.circles.apps.testahil.models.CategoryDataModel;
import com.technology.circles.apps.testahil.models.MakeOfferModel;
import com.technology.circles.apps.testahil.models.UserModel;
import com.technology.circles.apps.testahil.preferences.Preferences;
import com.technology.circles.apps.testahil.remote.Api;
import com.technology.circles.apps.testahil.share.Common;
import com.technology.circles.apps.testahil.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeOfferActivity extends AppCompatActivity implements Listeners.BackListener, Listeners.MakeOfferListener {
    private ActivityMakeOfferBinding binding;
    private String lang;
    private MakeOfferModel makeOfferModel;
    private Preferences preferences;
    private UserModel userModel;
    private boolean isCategoryLoading = false;
    private int category_current_page = 0;
    private List<CategoryDataModel.CategoryModel> categoryModelList;
    private CategorySpinnerAdapter adapter;
    private int category_id = 0;
    private AlertDialog dialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_make_offer);
        initView();
    }


    private void initView() {
        categoryModelList = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        makeOfferModel = new MakeOfferModel();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setModel(makeOfferModel);
        binding.setBackListener(this);
        binding.setLang(lang);
        binding.setMakeOfferListener(this);
        binding.consWorkType.setOnClickListener(view -> {
            createCategoryDialogAlert();
        });
        adapter = new CategorySpinnerAdapter(categoryModelList, this);
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
                                categoryModelList.addAll(response.body().getData());

                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(MakeOfferActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(MakeOfferActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CategoryDataModel> call, Throwable t) {
                            try {
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(MakeOfferActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MakeOfferActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
                            isCategoryLoading = false;
                            categoryModelList.remove(categoryModelList.size() - 1);
                            adapter.notifyItemRemoved(categoryModelList.size() - 1);

                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {


                                int oldPos = categoryModelList.size() - 1;

                                if (response.body().getData().size() > 0) {
                                    categoryModelList.addAll(response.body().getData());
                                    adapter.notifyItemRangeChanged(oldPos, categoryModelList.size());
                                    category_current_page = response.body().getCurrent_page();

                                }


                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(MakeOfferActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(MakeOfferActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CategoryDataModel> call, Throwable t) {
                            try {

                                isCategoryLoading = false;
                                if (categoryModelList.get(categoryModelList.size() - 1) == null) {
                                    categoryModelList.remove(categoryModelList.size() - 1);
                                    adapter.notifyItemRemoved(categoryModelList.size() - 1);

                                }
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(MakeOfferActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MakeOfferActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }


    private void createCategoryDialogAlert() {

        dialog = new AlertDialog.Builder(this)
                .create();

        DialogCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_category, null, false);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);
        binding.progBar.setVisibility(View.GONE);
        if (categoryModelList.size() > 0) {
            binding.tvNoData.setVisibility(View.GONE);
        } else {
            binding.tvNoData.setVisibility(View.VISIBLE);

        }

        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalItems = recyclerView.getAdapter().getItemCount();
                if (lastItem == (totalItems - 2) && !isCategoryLoading) {
                    isCategoryLoading = true;
                    int page = category_current_page + 1;
                    categoryModelList.add(null);
                    adapter.notifyItemInserted(categoryModelList.size() - 1);
                    loadMoreCategory(page);
                }
            }
        });

        binding.btnCancel.setOnClickListener(view -> dialog.dismiss());

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    @Override
    public void back() {
        finish();
    }

    @Override
    public void sendOffer(MakeOfferModel makeOfferModel) {

        if (makeOfferModel.isDataValid(this)) {
            makeOffer(makeOfferModel);
        }
    }

    private void makeOffer(MakeOfferModel makeOfferModel) {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .makeOffer(lang, "Bearer " + userModel.getToken(), makeOfferModel.getStoreName(), makeOfferModel.getWorkId(), makeOfferModel.getResponsibleName(), makeOfferModel.getPhone(), makeOfferModel.getEmail())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                createDialogAlert(getString(R.string.suc));
                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(MakeOfferActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(MakeOfferActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(MakeOfferActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MakeOfferActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void createDialogAlert(String msg) {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .create();

        DialogAlertBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert, null, false);

        binding.tvMsg.setText(msg);
        binding.btnCancel.setOnClickListener(v -> {
                    dialog.dismiss();
                    new Handler().postDelayed(this::finish, 2000);
                }

        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    public void setItemCategory(CategoryDataModel.CategoryModel categoryModel) {
        binding.tvCategory.setText(categoryModel.getTitle());
        this.category_id = categoryModel.getId();
        makeOfferModel.setWorkId(String.valueOf(category_id));
        binding.setModel(makeOfferModel);
        if (dialog != null) {
            dialog.dismiss();
        }

    }
}
