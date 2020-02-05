package com.technology.circles.apps.testahil.models;

import java.io.Serializable;

public class AppDataModel implements Serializable {

    private String titleOfApp;
    private String about_us;
    private String terms;

    public String getTitleOfApp() {
        return titleOfApp;
    }

    public String getAbout_us() {
        return about_us;
    }

    public String getTerms() {
        return terms;
    }
}
