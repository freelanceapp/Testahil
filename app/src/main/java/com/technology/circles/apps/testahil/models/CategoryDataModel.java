package com.technology.circles.apps.testahil.models;

import java.io.Serializable;
import java.util.List;

public class CategoryDataModel implements Serializable {
    private int current_page;
    private int total;
    private List<CategoryModel> data;


    public static class CategoryModel implements Serializable
    {
        private int id;
        private String image;
        private String level;
        private String parent_id;
        private String custom_order;
        private String products_count;
        private String companies_count;
        private String title;

        public int getId() {
            return id;
        }

        public String getImage() {
            return image;
        }

        public String getLevel() {
            return level;
        }

        public String getParent_id() {
            return parent_id;
        }

        public String getCustom_order() {
            return custom_order;
        }

        public String getProducts_count() {
            return products_count;
        }

        public String getCompanies_count() {
            return companies_count;
        }

        public String getTitle() {
            return title;
        }
    }

    public int getCurrent_page() {
        return current_page;
    }

    public int getTotal() {
        return total;
    }

    public List<CategoryModel> getData() {
        return data;
    }


}
