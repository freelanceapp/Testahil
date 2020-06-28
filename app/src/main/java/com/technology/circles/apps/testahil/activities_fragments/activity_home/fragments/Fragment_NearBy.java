package com.technology.circles.apps.testahil.activities_fragments.activity_home.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.FragmentNearbyBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.technology.circles.apps.testahil.activities_fragments.activity_home.HomeActivity;
import com.technology.circles.apps.testahil.adapter.CategoryMapAdapter;
import com.technology.circles.apps.testahil.models.AdLocation;
import com.technology.circles.apps.testahil.models.CategoryDataModel;
import com.technology.circles.apps.testahil.models.ClusterRender;
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

public class Fragment_NearBy extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private FragmentNearbyBinding binding;
    private HomeActivity activity;
    private Preferences preferences;
    private String lang;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private GoogleMap mMap;
    private SupportMapFragment fragment;
    private final String fineLocPerm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int loc_req = 1225;
    private UserModel userModel;
    private CategoryMapAdapter categoryAdapter;
    private List<CategoryDataModel.CategoryModel> categoryModelList;
    private List<ProductModel> productModelList;
    private String category_id = "";
    private Call<ProductDataModel> callProduct, callLoadMore;
    private boolean isCategoryLoading = false;
    private int category_current_page = 1;
    private boolean isProductLoading = false;
    private int product_current_page = 1;
    private int totalPage = 1;
    private List<AdLocation> adLocationList;
    private ClusterManager clusterManager;
    private ClusterRender clusterRender;


    public static Fragment_NearBy newInstance() {
        return new Fragment_NearBy();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nearby, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        adLocationList = new ArrayList<>();
        productModelList = new ArrayList<>();
        categoryModelList = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        initMap();

        binding.recView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        categoryAdapter = new CategoryMapAdapter(categoryModelList, activity, this);
        binding.recView.setAdapter(categoryAdapter);


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
                    categoryAdapter.notifyItemInserted(categoryModelList.size() - 1);
                    loadMoreCategory(page);
                }
            }
        });

        binding.tvMore.setOnClickListener(view -> {

            if (product_current_page <= totalPage && !isProductLoading) {
                isProductLoading = true;
                int page = product_current_page + 1;
                productModelList.add(null);
                loadMoreProduct(page);
            }
        });

    }

    private void getCategories() {
        try {

            Api.getService(Tags.base_url)
                    .getCategories("Bearer " + userModel.getToken(), lang, 1)
                    .enqueue(new Callback<CategoryDataModel>() {
                        @Override
                        public void onResponse(Call<CategoryDataModel> call, Response<CategoryDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
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

    private void getProduct(String category_id) {
        this.category_id = category_id;
        productModelList.clear();
        binding.progBar2.setVisibility(View.VISIBLE);
        binding.tvMore.setVisibility(View.GONE);
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

                        totalPage = response.body().getTotal();
                        if (response.body().getData().size() > 0) {
                            productModelList.clear();
                            productModelList.addAll(response.body().getData());
                            binding.tvResult.setText(String.valueOf(productModelList.size()));

                            updateMapUI();


                            if (response.body().getCurrent_page() < response.body().getTotal()) {
                                binding.tvMore.setVisibility(View.VISIBLE);
                            } else {
                                binding.tvMore.setVisibility(View.GONE);

                            }
                        } else {
                            adLocationList.clear();
                            clusterManager.clearItems();
                            clusterManager.cluster();
                            binding.tvResult.setText("0");

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

    private void loadMoreCategory(int page) {
        try {

            Api.getService(Tags.base_url)
                    .getCategories("Bearer " + userModel.getToken(), lang, page)
                    .enqueue(new Callback<CategoryDataModel>() {
                        @Override
                        public void onResponse(Call<CategoryDataModel> call, Response<CategoryDataModel> response) {
                            isCategoryLoading = false;
                            categoryModelList.remove(categoryModelList.size() - 1);
                            categoryAdapter.notifyItemRemoved(categoryModelList.size() - 1);

                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                  int oldPos = categoryModelList.size() - 1;

                                if (response.body().getData().size() > 0) {
                                    categoryModelList.addAll(response.body().getData());
                                    categoryAdapter.notifyItemRangeChanged(oldPos, categoryModelList.size());
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

            binding.tvMore.setVisibility(View.GONE);
            binding.progBar2.setVisibility(View.VISIBLE);

            callLoadMore = Api.getService(Tags.base_url).getProductByCategory("Bearer " + userModel.getToken(), lang, category_id, page);

            callLoadMore.enqueue(new Callback<ProductDataModel>() {
                @Override
                public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                    binding.progBar2.setVisibility(View.GONE);
                    isProductLoading = false;

                    if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                        if (response.body().getData().size() > 0) {
                            productModelList.addAll(response.body().getData());
                            product_current_page = response.body().getCurrent_page();
                            if (product_current_page == response.body().getTotal()) {
                                binding.tvMore.setVisibility(View.GONE);
                            }
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

    private void updateMapUI() {
        adLocationList.clear();
        clusterManager.clearItems();
        clusterManager.cluster();

        for (ProductModel model : productModelList) {
            if (!model.getCompany().getLatitude().isEmpty() && model.getCompany().getLatitude() != null && !model.getCompany().getLongitude().isEmpty() && model.getCompany().getLongitude() != null) {
                Double lat = Double.parseDouble(model.getCompany().getLatitude());
                Double lng = Double.parseDouble(model.getCompany().getLongitude());

                LatLng latLng = new LatLng(lat, lng);
                adLocationList.add(new AdLocation(model.getOffer_title(), model.getPrice() + " " + getString(R.string.sar), latLng, model));
            }

        }

        clusterManager.addItems(adLocationList);
        clusterManager.setRenderer(clusterRender);
        clusterManager.cluster();
    }

    private void initMap() {

        fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (fragment != null) {
            fragment.getMapAsync(this);

        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap != null) {
            mMap = googleMap;
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.maps));
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.getUiSettings().setTiltGesturesEnabled(false);

            clusterManager = new ClusterManager(activity, mMap);
            mMap.setOnCameraIdleListener(clusterManager);
            mMap.setOnMarkerClickListener(clusterManager);
            mMap.setOnInfoWindowClickListener(clusterManager);


            clusterRender = new ClusterRender(activity, mMap, clusterManager);


            CheckPermission();

            getCategories();


        }

    }

    private void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(activity, fineLocPerm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{fineLocPerm}, loc_req);
        } else {

            initGoogleApi();
            mMap.setMyLocationEnabled(true);
        }
    }

    private void initGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initLocationRequest();
    }

    private void initLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(60000);
        LocationSettingsRequest.Builder request = new LocationSettingsRequest.Builder();
        request.addLocationRequest(locationRequest);
        request.setAlwaysShow(false);


        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, request.build());
        result.setResultCallback(locationSettingsResult -> {
            Status status = locationSettingsResult.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    startLocationUpdate();
                    break;

                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        status.startResolutionForResult(activity, 100);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        });

    }


    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(activity)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onLocationChanged(Location location) {


        if (googleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(activity).removeLocationUpdates(locationCallback);
            googleApiClient.disconnect();
            googleApiClient = null;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == loc_req) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initGoogleApi();
                mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {

            initGoogleApi();
            mMap.setMyLocationEnabled(true);
        }
    }

    public void setItemCategorySelected(String category_id) {

        if (!this.category_id.equals(category_id)) {
            if (callLoadMore != null && callLoadMore.isExecuted()) {
                callLoadMore.cancel();
            }
            getProduct(category_id);
        }
    }
}
