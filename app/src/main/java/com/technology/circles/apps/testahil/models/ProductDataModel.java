package com.technology.circles.apps.testahil.models;

import java.io.Serializable;
import java.util.List;

public class ProductDataModel implements Serializable {
    private int current_page;
    private int total;
    private List<ProductModel> data;


    public static class CompanyModel implements Serializable
    {
        private int id;

        private String logo;
        private String email;
        private String phone;
        private String address;
        private String latitude;
        private String longitude;
        private String location;
        private String city_id;
        private String followers_count;
        private CityModel city;





        public static class CityModel implements Serializable
        {
            private int id_city;
            private String ar_city_title;
            private String en_city_title;
            private String country_id;

            public int getId_city() {
                return id_city;
            }

            public String getAr_city_title() {
                return ar_city_title;
            }

            public String getEn_city_title() {
                return en_city_title;
            }

            public String getCountry_id() {
                return country_id;
            }
        }

        public int getId() {
            return id;
        }

        public String getLogo() {
            return logo;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getAddress() {
            return address;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getLocation() {
            return location;
        }

        public String getCity_id() {
            return city_id;
        }

        public String getFollowers_count() {
            return followers_count;
        }

        public CityModel getCity() {
            return city;
        }
    }

    public static class ProductImage implements Serializable
    {
        private int id;
        private String product_id;
        private String image;

        public int getId() {
            return id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public String getImage() {
            return image;
        }
    }
    public int getCurrent_page() {
        return current_page;
    }

    public int getTotal() {
        return total;
    }



}
