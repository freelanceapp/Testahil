package com.technology.circles.apps.testahil.models;

import android.content.Context;
import android.net.Uri;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.creative.share.apps.testahil.BR;
import com.creative.share.apps.testahil.R;

public class UpdateProfileModel extends BaseObservable {
    private String name;
    private String email;
    private String phone;


    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();


    public boolean isDataValid(Context context) {
        if (!name.trim().isEmpty() &&
                !phone.trim().isEmpty() )
                {
            error_name.set(null);
            error_phone.set(null);

            return true;
        } else {
            if (name.trim().isEmpty()) {
                error_name.set(context.getString(R.string.field_req));

            } else {
                error_name.set(null);

            }



            if (phone.trim().isEmpty()) {
                error_phone.set(context.getString(R.string.field_req));

            } else {
                error_phone.set(null);

            }

            return false;
        }
    }

    public UpdateProfileModel() {
        setName("");
        setPhone("");
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




    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}

