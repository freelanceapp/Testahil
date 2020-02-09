package com.technology.circles.apps.testahil.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class AdLocation implements ClusterItem {

    private String price;
    private String address;
    private LatLng latLng;
    private ProductModel productModel;

    public AdLocation(String price, String address, LatLng latLng,ProductModel productModel) {
        this.price = price;
        this.address = address;
        this.latLng = latLng;
        this.productModel = productModel;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getTitle() {
        return price;
    }

    @Override
    public String getSnippet() {
        return address;
    }


}
