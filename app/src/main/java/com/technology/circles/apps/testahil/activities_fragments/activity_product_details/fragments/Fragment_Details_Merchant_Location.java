package com.technology.circles.apps.testahil.activities_fragments.activity_product_details.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.FragmentDetailsMerchantLocationBinding;
import com.technology.circles.apps.testahil.activities_fragments.activity_product_details.ProductDetailsActivity;
import com.technology.circles.apps.testahil.models.ProductModel;
import com.technology.circles.apps.testahil.models.UserModel;
import com.technology.circles.apps.testahil.preferences.Preferences;

import io.paperdb.Paper;

public class Fragment_Details_Merchant_Location extends Fragment {
    private static final String TAG ="DATA";
    private FragmentDetailsMerchantLocationBinding binding;
    private ProductDetailsActivity activity;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private ProductModel productModel;



    public static Fragment_Details_Merchant_Location newInstance(ProductModel productModel) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,productModel);
        Fragment_Details_Merchant_Location fragment_details_offers = new Fragment_Details_Merchant_Location();
        fragment_details_offers.setArguments(bundle);
        return fragment_details_offers;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details_merchant_location, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (ProductDetailsActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");

        Bundle bundle = getArguments();
        if (bundle!=null)
        {

            productModel = (ProductModel) getArguments().getSerializable(TAG);
        }
        binding.setModel(productModel);

    }


}
