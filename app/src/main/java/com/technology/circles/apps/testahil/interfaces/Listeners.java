package com.technology.circles.apps.testahil.interfaces;

import com.technology.circles.apps.testahil.models.ContactUsModel;
import com.technology.circles.apps.testahil.models.MakeOfferModel;
import com.technology.circles.apps.testahil.models.SignUpModel;

public interface Listeners {

    interface LoginListener {
        void checkDataLogin();
    }

    interface ShowCountryDialog{
        void showCountryDialog();

    }
    interface SkipListener
    {
        void skip();
    }
    interface CreateAccountListener
    {
        void createNewAccount();
    }

    interface ShowCountryDialogListener
    {
        void showDialog();
    }

    interface SignUpListener
    {
        void checkDataSignUp(SignUpModel signUpModel);

    }
    interface SettingActions {

        void terms();
        void aboutApp();
        void share();
        void notifications();
        void onLanguageSetting();
        void makeOffer();
        void logout();
        void settings();
        void contactUs();
        void onEditProfile();



    }
    interface BackListener
    {
        void back();
    }



    interface MoreActions
    {
        void aboutApp();
        void contactUs();
        void rateApp();
        void terms();
        void share();
        void editProfile();
    }

    interface ContactListener
    {
        void sendContact(ContactUsModel contactUsModel);
    }

    interface MakeOfferListener
    {
        void sendOffer(MakeOfferModel makeOfferModel);
    }

}
