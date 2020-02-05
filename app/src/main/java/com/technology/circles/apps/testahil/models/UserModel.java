package com.technology.circles.apps.testahil.models;

import java.io.Serializable;

public class UserModel implements Serializable {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String latitude;
    private String longitude;
    private String logo;
    private String block;
    private String is_login;
    private String is_confirmed;
    private String confirmation_code;
    private String city_id;
    private String token;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public String getLogo() {
        return logo;
    }

    public String getBlock() {
        return block;
    }

    public String getIs_login() {
        return is_login;
    }

    public String getIs_confirmed() {
        return is_confirmed;
    }

    public String getConfirmation_code() {
        return confirmation_code;
    }

    public String getCity_id() {
        return city_id;
    }

    public String getToken() {
        return token;
    }
}
