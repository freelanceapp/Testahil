package com.technology.circles.apps.testahil.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.CityRowBinding;
import com.technology.circles.apps.testahil.activities_fragments.activity_home.HomeActivity;
import com.technology.circles.apps.testahil.models.CityDataModel;

import java.util.List;

import io.paperdb.Paper;

public class CityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<CityDataModel.CityModel> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private int selectedPos = -1;
    private HomeActivity activity;

    public CityAdapter(List<CityDataModel.CityModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", "ar");
        activity = (HomeActivity) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CityRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.city_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;


        if (selectedPos == position) {
            myHolder.binding.rb.setChecked(true);
        } else {
            myHolder.binding.rb.setChecked(false);

        }

        myHolder.itemView.setOnClickListener(view -> {

            selectedPos = myHolder.getAdapterPosition();
            activity.setItemCityData(list.get(selectedPos));
            notifyDataSetChanged();

        });

        CityDataModel.CityModel model = list.get(position);
        myHolder.binding.setLang(lang);
        myHolder.binding.setModel(model);

    }

    public void clearSelection()
    {
        selectedPos = -1;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size() > 0 ? list.size() : 0;
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        private CityRowBinding binding;

        public MyHolder(@NonNull CityRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
