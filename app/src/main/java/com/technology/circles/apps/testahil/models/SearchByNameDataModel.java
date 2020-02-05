package com.technology.circles.apps.testahil.models;

import java.io.Serializable;
import java.util.List;

public class SearchByNameDataModel implements Serializable {

    private List<ProductModel> data;

    public List<ProductModel> getData() {
        return data;
    }
}
