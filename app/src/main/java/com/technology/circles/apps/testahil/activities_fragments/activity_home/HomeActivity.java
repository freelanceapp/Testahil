package com.technology.circles.apps.testahil.activities_fragments.activity_home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.creative.share.apps.testahil.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.technology.circles.apps.testahil.activities_fragments.activity_home.fragments.Fragment_Favorite;
import com.technology.circles.apps.testahil.activities_fragments.activity_home.fragments.Fragment_NearBy;
import com.technology.circles.apps.testahil.activities_fragments.activity_home.fragments.Fragment_Offers;
import com.technology.circles.apps.testahil.activities_fragments.activity_home.fragments.Fragment_Setting;
import com.technology.circles.apps.testahil.activities_fragments.activity_make_offer.MakeOfferActivity;
import com.technology.circles.apps.testahil.activities_fragments.activity_search.SearchActivity;
import com.technology.circles.apps.testahil.activities_fragments.activity_sign_in.LoginActivity;
import com.technology.circles.apps.testahil.adapter.CityAdapter;
import com.technology.circles.apps.testahil.language.LanguageHelper;
import com.technology.circles.apps.testahil.models.CityDataModel;
import com.technology.circles.apps.testahil.models.UserModel;
import com.technology.circles.apps.testahil.preferences.Preferences;
import com.technology.circles.apps.testahil.remote.Api;
import com.technology.circles.apps.testahil.share.Common;
import com.technology.circles.apps.testahil.tags.Tags;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private TabLayout tab;
    private String lang;
    private UserModel userModel;
    private Preferences preferences;
    private AHBottomNavigation ah_bottom_nav;

    private Fragment_Offers fragment_offers;
    private Fragment_Favorite fragment_favorite;
    private Fragment_NearBy fragment_nearBy;
    private Fragment_Setting fragment_setting;

    private LinearLayout llMainContent, llContentSearch, llMakeOffer, llFavorite;
    private CoordinatorLayout llHomeContent;
    private ImageView imageFilter, imageSearch;
    private CardView cardViewCity;
    private RoundedImageView arrow;
    private ExpandableLayout expandLayout;
    private RecyclerView recViewCity;
    private ProgressBar progBarCity;
    private TextView tvNoCityData,tvDisableFilter,tvName;
    private Button btnLogout;
    private RadioButton rbBranch,rbOnline;
    private CityAdapter cityAdapter;
    private List<CityDataModel.CityModel> cityModelList;
    private int isFilter = 0;
    ////////////////////////////////////////////////////
    private int city_id =0;
    private int merchant_type=0;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();

        setContentView(R.layout.activity_home);
        initView();
        if (savedInstanceState == null) {
            DisplayFragmentOffer();
        }

    }

    private void initView() {
        cityModelList = new ArrayList<>();

        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        tab = findViewById(R.id.tab);
        llMainContent = findViewById(R.id.llMainContent);
        llContentSearch = findViewById(R.id.llContentSearch);
        llHomeContent = findViewById(R.id.llHomeContent);
        imageFilter = findViewById(R.id.imageFilter);
        imageSearch = findViewById(R.id.imageSearch);
        btnLogout = findViewById(R.id.btnLogout);
        tvDisableFilter = findViewById(R.id.tvDisableFilter);
        tvName = findViewById(R.id.tvName);

        rbBranch = findViewById(R.id.rbBranch);
        rbOnline = findViewById(R.id.rbOnline);


        cardViewCity = findViewById(R.id.cardViewCity);
        arrow = findViewById(R.id.arrow);
        expandLayout = findViewById(R.id.expandLayout);
        recViewCity = findViewById(R.id.recViewCity);
        progBarCity = findViewById(R.id.progBarCity);
        tvNoCityData = findViewById(R.id.tvNoCityData);
        progBarCity.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        llMakeOffer = findViewById(R.id.llMakeOffer);
        llFavorite = findViewById(R.id.llFavorite);


        recViewCity.setLayoutManager(new LinearLayoutManager(this));
        cityAdapter = new CityAdapter(cityModelList,this);
        recViewCity.setAdapter(cityAdapter);
        ah_bottom_nav = findViewById(R.id.ah_bottom_nav);

        if (userModel!=null)
        {
            tvName.setText(userModel.getName());
        }

        tab.addTab(tab.newTab().setText("عربي"));
        tab.addTab(tab.newTab().setText("English"));

        if (lang.equals("ar")) {
            tab.getTabAt(0).select();
        } else {
            tab.getTabAt(1).select();

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawer.setElevation(0.0f);
        }

        drawer.setScrimColor(Color.TRANSPARENT);

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {


                if (isFilter == 1) {

                    float slideX = drawerView.getWidth() * slideOffset;
                    if (lang.equals("ar")) {
                        slideX = slideX * -1;
                    }
                    llHomeContent.setTranslationX(slideX);
                }

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                llMainContent.setVisibility(View.VISIBLE);
                llContentSearch.setVisibility(View.GONE);
                isFilter = 0;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        rbBranch.setOnClickListener(view -> {
            rbBranch.setChecked(true);
            rbOnline.setChecked(false);
            drawer.closeDrawer(GravityCompat.START);
            merchant_type = 1;
            DisplayFragmentOffer();
        });

        rbOnline.setOnClickListener(view -> {
            rbBranch.setChecked(false);
            rbOnline.setChecked(true);
            drawer.closeDrawer(GravityCompat.START);
            merchant_type = 2;
            DisplayFragmentOffer();

        });


        imageFilter.setOnClickListener(view -> {
            llMainContent.setVisibility(View.GONE);
            llContentSearch.setVisibility(View.VISIBLE);
            isFilter = 1;

            drawer.openDrawer(GravityCompat.START);

        });
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                drawer.closeDrawer(GravityCompat.START);

                int pos = tab.getPosition();
                if (pos == 0) {
                    RefreshActivity("ar");
                } else {
                    RefreshActivity("en");

                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        cardViewCity.setOnClickListener(view -> {

            if (expandLayout.isExpanded()) {
                expandLayout.collapse(true);
                arrow.animate().setDuration(500).rotationBy(-180).start();

            } else {
                expandLayout.expand(true);
                arrow.animate().setDuration(500).rotationBy(180).start();

            }

        });

        imageSearch.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
            startActivity(intent);

        });

        llMakeOffer.setOnClickListener(view -> navigateToMakeOfferActivity());
        llFavorite.setOnClickListener(view -> {
            drawer.closeDrawer(GravityCompat.START);
            DisplayFragmentFavorite();
        });
        btnLogout.setOnClickListener(view ->{

            if (userModel!=null)
            {
                logout();
            }

        } );
        setUpBottomNavigation();

        tvDisableFilter.setOnClickListener(view -> {
            rbOnline.setChecked(false);
            rbBranch.setChecked(false);
            this.city_id =0;
            this.merchant_type =0;
            DisplayFragmentOffer();
            if (cityAdapter!=null&&cityModelList.size()>0)
            {
                cityAdapter.clearSelection();
            }
            drawer.closeDrawer(GravityCompat.START);



        });

        getCities();

    }

    private void getCities() {

        Api.getService(Tags.base_url).
                getCities("Bearer "+userModel.getToken(),lang).
                enqueue(new Callback<CityDataModel>() {
                    @Override
                    public void onResponse(Call<CityDataModel> call, Response<CityDataModel> response) {
                        progBarCity.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null&&response.body().getData()!=null) {

                            cityModelList.clear();
                            cityModelList.addAll(response.body().getData());

                            if (cityModelList.size()>0)
                            {
                                cityAdapter.notifyDataSetChanged();
                                tvNoCityData.setVisibility(View.GONE);
                            }else
                                {
                                    tvNoCityData.setVisibility(View.VISIBLE);

                                }

                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(HomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CityDataModel> call, Throwable t) {

                        try {
                            progBarCity.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(HomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }



                    }
                });
    }


    private void setUpBottomNavigation() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.offers), R.drawable.ic_tag);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.favorite), R.drawable.ic_heart);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.nearby), R.drawable.ic_pin);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.setting), R.drawable.ic_setting);

        ah_bottom_nav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ah_bottom_nav.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.gray2));
        ah_bottom_nav.setTitleTextSizeInSp(14, 12);
        ah_bottom_nav.setForceTint(true);
        ah_bottom_nav.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
        ah_bottom_nav.setInactiveColor(ContextCompat.getColor(this, R.color.black));
        ah_bottom_nav.addItem(item1);
        ah_bottom_nav.addItem(item2);
        ah_bottom_nav.addItem(item3);
        ah_bottom_nav.addItem(item4);


        ah_bottom_nav.setOnTabSelectedListener((position, wasSelected) -> {
            userModel = preferences.getUserData(this);
            switch (position) {
                case 0:
                    DisplayFragmentOffer();
                    break;
                case 1:
                    DisplayFragmentFavorite();
                    break;
                case 2:
                    DisplayFragmentNearBy();
                    break;
                case 3:
                    DisplayFragmentSetting();
                    break;


            }
            return false;
        });

        ah_bottom_nav.setCurrentItem(0, false);


    }


    private void DisplayFragmentOffer() {
        if (fragment_offers == null) {
            fragment_offers = Fragment_Offers.newInstance(city_id,merchant_type);
        }

        if (fragment_favorite != null && fragment_favorite.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_favorite).commit();
        }

        if (fragment_nearBy != null && fragment_nearBy.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_nearBy).commit();
        }

        if (fragment_setting != null && fragment_setting.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_setting).commit();
        }


        if (fragment_offers.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_offers).commit();
            if (city_id==0&&merchant_type==0)
            {
                fragment_offers.getProduct();

            }else
                {
                    if (merchant_type==1) {
                        fragment_offers.getProductFilter("local",city_id);
                    }else if (merchant_type==2)
                    {
                        fragment_offers.getProductFilter("online",city_id);

                    }else
                    {
                        fragment_offers.getProductFilter("disable",city_id);

                    }
                }


        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_offers, "fragment_offers").addToBackStack("fragment_offers").commit();

        }
        ah_bottom_nav.setCurrentItem(0, false);

    }

    private void DisplayFragmentFavorite() {

        if (fragment_favorite == null) {
            fragment_favorite = Fragment_Favorite.newInstance();

        }

        if (fragment_offers != null && fragment_offers.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_offers).commit();
        }
        if (fragment_nearBy != null && fragment_nearBy.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_nearBy).commit();
        }
        if (fragment_setting != null && fragment_setting.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_setting).commit();
        }


        if (fragment_favorite.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_favorite).commit();
            fragment_favorite.getProduct();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_favorite, "fragment_favorite").addToBackStack("fragment_favorite").commit();

        }
        ah_bottom_nav.setCurrentItem(1, false);
    }

    private void DisplayFragmentNearBy() {

        if (fragment_nearBy==null)
        {
            fragment_nearBy = Fragment_NearBy.newInstance();

        }

        if (fragment_offers != null && fragment_offers.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_offers).commit();
        }
        if (fragment_favorite != null && fragment_favorite.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_favorite).commit();
        }

        if (fragment_setting != null && fragment_setting.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_setting).commit();
        }


        if (fragment_nearBy.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_nearBy).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_nearBy, "fragment_nearBy").addToBackStack("fragment_nearBy").commit();

        }
        ah_bottom_nav.setCurrentItem(2, false);
    }

    private void DisplayFragmentSetting() {

        if (fragment_setting == null) {
            fragment_setting = Fragment_Setting.newInstance();
        }
        if (fragment_offers != null && fragment_offers.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_offers).commit();
        }
        if (fragment_favorite != null && fragment_favorite.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_favorite).commit();
        }

        if (fragment_nearBy != null && fragment_nearBy.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_nearBy).commit();
        }


        if (fragment_setting.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_setting).commit();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_setting, "fragment_setting").addToBackStack("fragment_setting").commit();

        }
        ah_bottom_nav.setCurrentItem(3, false);
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToMakeOfferActivity() {
        drawer.closeDrawer(GravityCompat.START);
        new Handler()
                .postDelayed(() -> {
                    Intent intent = new Intent(this, MakeOfferActivity.class);
                    startActivity(intent);

                }, 500);

    }


    public void RefreshActivity(String lang) {
        Paper.book().write("lang", lang);
        LanguageHelper.setNewLocale(this, lang);

        new Handler()
                .postDelayed(() -> {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 1050);


    }


    private void logout() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .logout("Bearer " + userModel.getToken(), userModel.getId())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                userModel = null;
                                preferences.clear(HomeActivity.this);
                                navigateToLoginActivity();

                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(HomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                                        Toast.makeText(HomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);


        } else {
            if (fragment_offers != null && fragment_offers.isAdded() && fragment_offers.isVisible()) {
                finish();

            } else {
                DisplayFragmentOffer();
            }


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }


    public void setItemCityData(CityDataModel.CityModel cityModel) {
        if (city_id!=cityModel.getId_city())
        {
            drawer.closeDrawer(GravityCompat.START);

            city_id = cityModel.getId_city();

            DisplayFragmentOffer();

        }
    }
}
