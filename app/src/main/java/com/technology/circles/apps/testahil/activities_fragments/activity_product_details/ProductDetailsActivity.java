package com.technology.circles.apps.testahil.activities_fragments.activity_product_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.ActivityProductDetailsBinding;
import com.technology.circles.apps.testahil.activities_fragments.activity_contact_us.ContactUsActivity;
import com.technology.circles.apps.testahil.activities_fragments.activity_product_details.fragments.Fragment_Details_Merchant;
import com.technology.circles.apps.testahil.activities_fragments.activity_product_details.fragments.Fragment_Details_Merchant_Location;
import com.technology.circles.apps.testahil.activities_fragments.activity_product_details.fragments.Fragment_Details_Offers;
import com.technology.circles.apps.testahil.activities_fragments.activity_product_details.fragments.Fragment_Details_Used_Way;
import com.technology.circles.apps.testahil.adapter.ViewPagerAdapter;
import com.technology.circles.apps.testahil.interfaces.Listeners;
import com.technology.circles.apps.testahil.language.LanguageHelper;
import com.technology.circles.apps.testahil.models.ProductModel;
import com.technology.circles.apps.testahil.models.UserModel;
import com.technology.circles.apps.testahil.preferences.Preferences;
import com.technology.circles.apps.testahil.remote.Api;
import com.technology.circles.apps.testahil.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityProductDetailsBinding binding;
    private ProductModel productModel;
    private List<Fragment>  fragmentList;
    private List<String> titles;
    private ViewPagerAdapter adapter;
    private String lang;
    private Preferences preferences;
    private UserModel  userModel;
    private boolean isFavoriteChange = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_product_details);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("data"))
        {
            productModel = (ProductModel) intent.getSerializableExtra("data");
        }
    }

    private void initView()
    {
        this.setFinishOnTouchOutside(false);
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        fragmentList = new ArrayList<>();
        titles = new ArrayList<>();
        binding.setModel(productModel);
        binding.setBackListener(this);

        titles.add(getString(R.string.offer));
        titles.add(getString(R.string.how_to_use));
        titles.add(getString(R.string.merchant_info));
        titles.add(getString(R.string.location));

        fragmentList.add(Fragment_Details_Offers.newInstance(productModel));
        fragmentList.add(Fragment_Details_Used_Way.newInstance(productModel));
        fragmentList.add(Fragment_Details_Merchant.newInstance(productModel));
        fragmentList.add(Fragment_Details_Merchant_Location.newInstance(productModel));

        binding.tab.setupWithViewPager(binding.pager);


        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddTitle(titles);
        adapter.AddFragments(fragmentList);
        binding.pager.setAdapter(adapter);

        if (productModel.getCompany().getCompany_type().equals("local")){
            binding.tvLocal.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
            binding.icon1.setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary));
            binding.tvOnline.setTextColor(ContextCompat.getColor(this,R.color.gray6));
            binding.icon2.setColorFilter(ContextCompat.getColor(this,R.color.gray6));

        }else {
            binding.tvLocal.setTextColor(ContextCompat.getColor(this,R.color.gray6));
            binding.icon1.setColorFilter(ContextCompat.getColor(this,R.color.gray6));
            binding.tvOnline.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
            binding.icon2.setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary));

        }

        binding.flContact.setOnClickListener(view -> {

            Intent intent  = new Intent(this, ContactUsActivity.class);
            startActivity(intent);

        });
        binding.checkbox.setOnClickListener(view -> {
            like_dislike();
        });
        getProductDetails();

    }

    private void getProductDetails()
    {

        try {

            Api.getService(Tags.base_url).singleProduct(lang,"Bearer "+userModel.getToken(),productModel.getId())
                    .enqueue(new Callback<ProductModel>() {
                        @Override
                        public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                binding.setModel(response.body());

                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(ProductDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(ProductDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ProductModel> call, Throwable t) {
                            try {
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(ProductDetailsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ProductDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    private void like_dislike()
    {

        try {

            Api.getService(Tags.base_url).like_dislike(lang,"Bearer "+userModel.getToken(),productModel.getId())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful() && response.body() != null) {

                                isFavoriteChange = true;
                                if (productModel.getIs_favourite()==0)
                                {
                                    productModel.setIs_favourite(1);
                                    binding.setModel(productModel);
                                }else
                                    {
                                        productModel.setIs_favourite(0);
                                        binding.setModel(productModel);
                                    }


                                Log.e("fav",productModel.getIs_favourite()+"_");
                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(ProductDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(ProductDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(ProductDetailsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ProductDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

        if (isFavoriteChange)
        {
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
