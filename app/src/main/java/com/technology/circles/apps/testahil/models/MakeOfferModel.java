package com.technology.circles.apps.testahil.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.creative.share.apps.testahil.R;

import java.io.Serializable;

public class MakeOfferModel extends BaseObservable implements Serializable {

    private String storeName;
    private String workId;
    private String phone;
    private String email;
    private String responsibleName;

    public ObservableField<String> error_storeName = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();
    public ObservableField<String> error_email = new ObservableField<>();



    public boolean isDataValid(Context context) {
        if (!TextUtils.isEmpty(storeName) &&
                !TextUtils.isEmpty(email) &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                !TextUtils.isEmpty(workId) &&
                !TextUtils.isEmpty(phone)&&
                phone.length()==9

        ) {
            error_storeName.set(null);
            error_email.set(null);
            error_phone.set(null);

            return true;
        } else {
            if (storeName.isEmpty()) {
                error_storeName.set(context.getString(R.string.field_req));
            } else {
                error_storeName.set(null);
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
            } else if (phone.length()!=9){
                error_phone.set(context.getString(R.string.inv_phone));
            }else
                {
                    error_phone.set(null);

                }

            if (workId.isEmpty()) {
                Toast.makeText(context, R.string.ch_cat_type, Toast.LENGTH_SHORT).show();
            }





            return false;
        }
    }
    public MakeOfferModel() {
        this.storeName="";
        this.workId = "";
        this.phone = "";
        this.email = "";
        this.responsibleName = "";
    }

    @Bindable
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
        notifyPropertyChanged(BR.storeName);
    }

    @Bindable
    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
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
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);

    }

    @Bindable
    public String getResponsibleName() {
        return responsibleName;
    }

    public void setResponsibleName(String responsibleName) {
        this.responsibleName = responsibleName;
        notifyPropertyChanged(BR.responsibleName);

    }
}
