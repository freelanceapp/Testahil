package com.technology.circles.apps.testahil.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.CountriesRowBinding;
import com.technology.circles.apps.testahil.activities_fragments.activity_make_offer.MakeOfferActivity;

import com.technology.circles.apps.testahil.models.CountryModel;

import java.util.List;

public class CountriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CountryModel> list;
    private Context context;
    private LayoutInflater inflater;
    private MakeOfferActivity activity;

    public CountriesAdapter(List<CountryModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (MakeOfferActivity) context;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CountriesRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.countries_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));

        myHolder.itemView.setOnClickListener(view -> {
            activity.setItemData(list.get(myHolder.getAdapterPosition()));

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public CountriesRowBinding binding;

        public MyHolder(@NonNull CountriesRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
