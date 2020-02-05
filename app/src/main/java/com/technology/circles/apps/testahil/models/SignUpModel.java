package com.technology.circles.apps.testahil.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.creative.share.apps.testahil.R;

public class SignUpModel extends BaseObservable {

    private String name;
    private String email;
    private String phone_code;
    private String phone;
    private String password;
    private String re_password;


    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_email = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();

    public ObservableField<String> error_password = new ObservableField<>();
    public ObservableField<String> error_re_password = new ObservableField<>();


    public boolean isDataValid(Context context) {
        if (!TextUtils.isEmpty(name) &&
                !TextUtils.isEmpty(email) &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                !TextUtils.isEmpty(phone)&&
                !TextUtils.isEmpty(password) &&
                password.equals(re_password)&&
                password.length() >= 6
        ) {
            error_name.set(null);
            error_email.set(null);
            error_phone.set(null);
            error_password.set(null);
            error_re_password.set(null);


            return true;
        } else {

            if (name.isEmpty()) {
                error_name.set(context.getString(R.string.field_req));
            }else
                {
                    error_name.set(null);

                }

            if (email.isEmpty()) {
                error_email.set(context.getString(R.string.field_req));
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                error_email.set(context.getString(R.string.inv_email));
            } else {
                error_email.set(null);

            }

            if (phone.isEmpty()) {
                error_phone.set(context.getString(R.string.field_req));
            }else
            {
                error_phone.set(null);

            }

            if (password.isEmpty()) {
                error_password.set(context.getString(R.string.field_req));
            } else if (password.length() < 6) {
                error_password.set(context.getString(R.string.pas_short));
            }else if (!password.equals(re_password))
            {
                error_re_password.set(context.getString(R.string.pas_not_match));
            }else
                {
                    error_password.set(null);
                    error_re_password.set(null);

                }


            return false;
        }
    }

    public SignUpModel() {
        this.name = "";
        notifyPropertyChanged(BR.name);
        this.email = "";
        notifyPropertyChanged(BR.email);

        this.phone_code = "";
        notifyPropertyChanged(BR.phone_code);

        this.phone = "";
        notifyPropertyChanged(BR.phone);


        this.password = "";
        notifyPropertyChanged(BR.password);

        this.re_password = "";
        notifyPropertyChanged(BR.re_password);

    }


    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
        notifyPropertyChanged(BR.phone_code);

    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);

    }

    @Bindable
    public String getRe_password() {
        return re_password;
    }

    public void setRe_password(String re_password) {
        this.re_password = re_password;
        notifyPropertyChanged(BR.re_password);

    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);

    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);

    }


}
