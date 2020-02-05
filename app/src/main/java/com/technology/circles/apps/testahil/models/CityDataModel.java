package com.technology.circles.apps.testahil.models;

import java.io.Serializable;
import java.util.List;

public class CityDataModel implements Serializable {

    private List<CityModel> data;

    public List<CityModel> getData() {
        return data;
    }

    public static class CityModel implements Serializable
    {
        private int id_city;
        private String ar_city_title;
        private String en_city_title;

        public int getId_city() {
            return id_city;
        }

        public String getAr_city_title() {
            return ar_city_title;
        }

        public String getEn_city_title() {
            return en_city_title;
        }
    }
}
