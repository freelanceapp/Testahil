package com.technology.circles.apps.testahil.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.CategoryAllRowBinding;
import com.creative.share.apps.testahil.databinding.CategoryRowBinding;
import com.creative.share.apps.testahil.databinding.LoadMoreBinding;
import com.technology.circles.apps.testahil.activities_fragments.activity_home.fragments.Fragment_Offers;
import com.technology.circles.apps.testahil.models.CategoryDataModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int LOAD_DATA = 1;
    private final int LOAD_MORE = 2;
    private final int LOAD_ALL_DATA = 3;


    private List<CategoryDataModel.CategoryModel> list;
    private Context context;
    private Fragment fragment;
    private LayoutInflater inflater;

    private int selectedPos = 0;


    public CategoryAdapter(List<CategoryDataModel.CategoryModel> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LOAD_ALL_DATA)
        {
            CategoryAllRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.category_all_row,parent,false);
            return new MyAllHolder(binding);
        }else if (viewType == LOAD_DATA)
        {
            CategoryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.category_row,parent,false);
            return new MyHolder(binding);
        }else
            {
                LoadMoreBinding binding = DataBindingUtil.inflate(inflater, R.layout.load_more,parent,false);
                return new LoadMoreHolder(binding);
            }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (selectedPos == position)
        {
            if (holder instanceof MyHolder)
            {
                MyHolder myHolder = (MyHolder) holder;
                myHolder.binding.image.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary));
                myHolder.binding.tvTitle.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));



            }else if (holder instanceof MyAllHolder)
            {
                Log.e("1",position+"__");
                MyAllHolder myAllHolder = (MyAllHolder) holder;
                myAllHolder.binding.image.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary));
                myAllHolder.binding.tvTitle.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));

            }
        }else
            {
                if (holder instanceof MyHolder)
                {
                    MyHolder myHolder = (MyHolder) holder;
                    myHolder.binding.image.setColorFilter(ContextCompat.getColor(context,R.color.black));
                    myHolder.binding.tvTitle.setTextColor(ContextCompat.getColor(context,R.color.black));
                }else if (holder instanceof MyAllHolder)
                {
                    Log.e("2","2");

                    MyAllHolder myAllHolder = (MyAllHolder) holder;

                    myAllHolder.binding.image.setColorFilter(ContextCompat.getColor(context,R.color.black));
                    myAllHolder.binding.tvTitle.setTextColor(ContextCompat.getColor(context,R.color.black));

                }
            }


        if (holder instanceof MyHolder)
        {
            MyHolder myHolder = (MyHolder) holder;

            CategoryDataModel.CategoryModel model = list.get(myHolder.getAdapterPosition());
            myHolder.binding.setModel(model);

            myHolder.itemView.setOnClickListener(view -> {

                selectedPos = myHolder.getAdapterPosition();
                CategoryDataModel.CategoryModel model2 = list.get(selectedPos);

                if (fragment instanceof Fragment_Offers)
                {

                    Fragment_Offers fragment_offers = (Fragment_Offers) fragment;
                    fragment_offers.setItemCategorySelected(String.valueOf(model2.getId()));
                }
                notifyDataSetChanged();
            });


        }else if (holder instanceof MyAllHolder)
        {
            MyAllHolder myAllHolder = (MyAllHolder) holder;

            myAllHolder.itemView.setOnClickListener(view -> {

                selectedPos = myAllHolder.getAdapterPosition();
                if (fragment instanceof Fragment_Offers)
                {

                    Fragment_Offers fragment_offers = (Fragment_Offers) fragment;
                    fragment_offers.setItemCategorySelected("All");
                }
                notifyDataSetChanged();
            });

        }else if (holder instanceof LoadMoreHolder)
        {
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            loadMoreHolder.binding.progBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return list.size()>0?list.size():0;
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        private CategoryRowBinding binding;
        public MyHolder(@NonNull CategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class MyAllHolder extends RecyclerView.ViewHolder {
        private CategoryAllRowBinding binding;
        public MyAllHolder(@NonNull CategoryAllRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class LoadMoreHolder extends RecyclerView.ViewHolder {
        private LoadMoreBinding binding;
        public LoadMoreHolder(@NonNull LoadMoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position==0)
        {
            return LOAD_ALL_DATA;
        }else
            {
                if (list.get(position)==null)
                {
                    return LOAD_MORE;
                }else
                    {
                        return LOAD_DATA;
                    }
            }


    }
}
