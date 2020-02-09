package com.technology.circles.apps.testahil.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.creative.share.apps.testahil.R;

import java.io.Serializable;

public class ContactUsModel extends BaseObservable implements Serializable {

    private String name;
    private String email;
    private String phone;
    private String message;
    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_email = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();
    public ObservableField<String> error_message = new ObservableField<>();


    public ContactUsModel() {
        this.name = "";
        this.email = "";
        this.phone = "";
        this.message = "";
    }

    public ContactUsModel(String name, String email, String phone, String message) {
        this.name = name;
        notifyPropertyChanged(BR.name);
        this.email = email;
        notifyPropertyChanged(BR.email);

        this.phone = phone;
        notifyPropertyChanged(BR.phone);

        this.message = message;
        notifyPropertyChanged(BR.message);

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
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Bindable
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isDataValid(Context context) {
        if (!TextUtils.isEmpty(name) &&
                !TextUtils.isEmpty(email) &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                !TextUtils.isEmpty(phone) &&
                phone.length()==9&&
                !TextUtils.isEmpty(message)

        ) {
            error_name.set(null);
            error_email.set(null);
            error_phone.set(null);
            error_message.set(null);

            return true;
        } else {
            if (name.isEmpty()) {
                error_name.set(context.getString(R.string.field_req));
            } else {
                error_name.set(null);
            }


            if (email.isEmpty()) {
                error_email.set(context.getString(R.string.field_req));
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                error_email.set(context.getString(R.string.inv_email));
            } else {
                error_email.set(null);
            }

            if (phone.isEmpty()) {
                error_phone.set(context.getString(R.string.field_req));
            }else if (phone.length()!=9) {
                error_phone.set(context.getString(R.string.inv_phone));
            } else {
                error_phone.set(null);
            }


            if (message.isEmpty()) {
                error_message.set(context.getString(R.string.field_req));
            }  else {
                error_message.set(null);

            }


            return false;
        }
    }
}
